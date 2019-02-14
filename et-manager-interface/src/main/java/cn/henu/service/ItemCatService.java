package cn.henu.service;

import java.util.List;

import cn.henu.common.pojo.EasyUITreeNode;

//商品分类
public interface ItemCatService {
	List<EasyUITreeNode> getItemCatlist(long parentId);
}
