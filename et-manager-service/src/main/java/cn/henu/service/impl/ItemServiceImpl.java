package cn.henu.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.henu.common.jedis.JedisClient;
import cn.henu.common.pojo.EasyUIDataGridResult;
import cn.henu.common.utils.EtResult;
import cn.henu.common.utils.IDUtils;
import cn.henu.common.utils.JsonUtils;
import cn.henu.mapper.TbItemDescMapper;
import cn.henu.mapper.TbItemMapper;
import cn.henu.pojo.TbItem;
import cn.henu.pojo.TbItemDesc;
import cn.henu.pojo.TbItemExample;
import cn.henu.pojo.TbItemExample.Criteria;
import cn.henu.service.ItemService;

/**
 * 商品管理的service
 * @author syw
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper; 
	@Autowired
	//用于向Tb-desc表添加商品的信息
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource //这个是首先是根据ID来注入，匹配有没有ID为topicDestination的,如果没有的话再根据类型，@Autowired是根据类型来注入
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存，缓存不能影响业务逻辑，所以最好try-catch
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			if(StringUtils.isNoneBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		
		//方式一: 根据主键进行查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//return tbItem;
		
		//方式二:根据查询对象进行查询
		//创建查询对象
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		
		if(list!=null&&list.size()>0) {
			//把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				//设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page,rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取出分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public EtResult addItem(TbItem item, String desc) {
		//生成商品的id
	    final long itemId = IDUtils.genItemId();//局部变量用final修饰
		//补全商品的属性
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品的描述表的pojo
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向描述表插入数据
		itemDescMapper.insert(itemDesc);
		
		//发送一个添加消息
		jmsTemplate.send(topicDestination,new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				// TODO Auto-generated method stub
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		//返回成功
		return EtResult.ok();
	}

	public TbItemDesc selectTbItemDesc(long itemId) {
		//查询缓存，缓存不能影响业务逻辑，所以最好try-catch
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if(StringUtils.isNoneBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
	    TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
	  //把结果添加到缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		}catch(Exception e) {
			e.printStackTrace();
		}
	    return itemDesc;
	}
	@Override
	public void updateItem(TbItem tbItem) {
		itemMapper.updateByPrimaryKey(tbItem);
		
	}

	@Override
	public void updateItemDesc(TbItemDesc itemDesc) {
		itemDescMapper.updateByPrimaryKey(itemDesc);
	}

	@Override
	public void batchDeleteItem(long[] ids) {
		itemMapper.batchDeleteItem(ids);
	}

	@Override
	public void batchDeleteItemDesc(long[] ids) {
		itemDescMapper.batchDeleteItemDesc(ids);
	}

}
