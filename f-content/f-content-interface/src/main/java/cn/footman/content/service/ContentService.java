package cn.footman.content.service;

import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbContent;

import java.util.List;

public interface ContentService {
    FResult addContent(TbContent tbContent);

    List<TbContent> getContentByCID(long categoryId);
}
