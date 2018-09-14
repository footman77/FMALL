package cn.footman.service;

import cn.footman.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {

    public List<EasyUITreeNode> getItemCatList(long parentId);
}
