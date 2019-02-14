package cn.henu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.henu.common.pojo.EasyUITreeNode;
import cn.henu.mapper.TbItemCatMapper;
import cn.henu.pojo.TbItemCat;
import cn.henu.pojo.TbItemCatExample;
import cn.henu.pojo.TbItemCatExample.Criteria;
import cn.henu.service.ItemCatService;

/**
 * 商品分类的service
 * */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Override
	public List<EasyUITreeNode> getItemCatlist(long parentId) {
		// 根据商品的parentid来查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//创建返回结果的List
		List<EasyUITreeNode> resultList = new <EasyUITreeNode>ArrayList();
		//把列表转换成easyuitreenode列表
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			//判断
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
			
		}
		//返回结果
		return resultList;
	}

}
