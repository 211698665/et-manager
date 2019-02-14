package cn.henu.service;

import cn.henu.common.pojo.EasyUIDataGridResult;
import cn.henu.common.utils.EtResult;
import cn.henu.pojo.TbItem;
import cn.henu.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(long itemId);
	EasyUIDataGridResult getItemList(int page,int rows);
	EtResult addItem(TbItem item ,String desc);
	public TbItemDesc selectTbItemDesc(long id);
	
	void updateItem(TbItem tbItem);
	
	void updateItemDesc(TbItemDesc itemDesc);
	
	void batchDeleteItem(long [] ids);
	
	void batchDeleteItemDesc(long [] ids);
}
