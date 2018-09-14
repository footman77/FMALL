package cn.footman.service;

import cn.footman.common.pojo.EasyUIDataGridResult;
import cn.footman.common.utils.FResult;
import cn.footman.pojo.TbItem;
import cn.footman.pojo.TbItemDesc;


public interface ItemService {
    TbItem getItemById(long itemId);

    EasyUIDataGridResult getItemList(int page,int rows);

    FResult addItem(TbItem item,String desc);

    TbItemDesc getItemDescById(long itemId);
}
