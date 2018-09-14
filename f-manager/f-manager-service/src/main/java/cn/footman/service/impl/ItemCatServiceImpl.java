package cn.footman.service.impl;

import cn.footman.common.pojo.EasyUITreeNode;
import cn.footman.mapper.TbItemCatMapper;
import cn.footman.pojo.TbItemCat;
import cn.footman.pojo.TbItemCatExample;
import cn.footman.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<EasyUITreeNode> getItemCatList(long parentId) {

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = tbItemCatMapper.selectByExample(example);

        List<EasyUITreeNode> list = new ArrayList<>();

        for(TbItemCat tbItemCat : tbItemCats){
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(tbItemCat.getId());
            easyUITreeNode.setText(tbItemCat.getName());
            easyUITreeNode.setState(tbItemCat.getIsParent()?"closed":"open");
            list.add(easyUITreeNode);
        }
        return list;
    }
}
