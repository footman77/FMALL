package cn.footman.content.service;

import cn.footman.common.pojo.EasyUITreeNode;
import cn.footman.common.utils.FResult;

import java.util.List;

public interface ContentCategoryService {
    List<EasyUITreeNode> getContentCatListByPID(long parentId);

    FResult addContentCategory(long parentId,String name);
}
