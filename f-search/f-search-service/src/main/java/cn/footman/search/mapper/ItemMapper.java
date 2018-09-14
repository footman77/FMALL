package cn.footman.search.mapper;

import cn.footman.common.pojo.SearchItem;

import java.util.List;

public interface ItemMapper {

    List<SearchItem> getSearchItem();

    SearchItem searchItemById(long itemId);
}
