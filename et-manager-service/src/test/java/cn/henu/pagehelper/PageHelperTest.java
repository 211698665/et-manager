package cn.henu.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.henu.mapper.TbItemMapper;
import cn.henu.pojo.TbItem;
import cn.henu.pojo.TbItemExample;

public class PageHelperTest {
	
	/*
	@Test
	public void testpagehelper() {
		//初始化spring容器
	    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得Mapper代理对象
	    TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		//执行sql语句之前设置分页信息,使用pagehelper的静态方法进行设置
	    PageHelper.startPage(1,10);
	    
		//执行查询
	    TbItemExample example = new TbItemExample();
	    List<TbItem> list = mapper.selectByExample(example);
		//取出分页信息，使用pageInfo,取出总记录数，总页数，当前页码，每页显示的记录数
		PageInfo<TbItem> pageInfo=new PageInfo<TbItem>(list);
		System.out.println("总记录数"+pageInfo.getTotal());
		System.out.println("总页数"+pageInfo.getPages());
		System.out.println("当前页码"+pageInfo.getPageNum());
		System.out.println(list.size());
	}
	*/
}
