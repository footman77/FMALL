package cn.footman.content.service.impl;

import cn.footman.common.pojo.EasyUITreeNode;
import cn.footman.common.utils.FResult;
import cn.footman.content.service.ContentCategoryService;
import cn.footman.mapper.TbContentCategoryMapper;
import cn.footman.pojo.TbContentCategory;
import cn.footman.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getContentCatListByPID(long parentId) {

        TbContentCategoryExample categoryExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andParentIdEqualTo(parentId);

        List<TbContentCategory> categoryList = tbContentCategoryMapper.selectByExample(categoryExample);
        ArrayList<EasyUITreeNode> list = new ArrayList<>();

        for(TbContentCategory contentCategory : categoryList){
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setId(contentCategory.getId());
            easyUITreeNode.setText(contentCategory.getName());
            easyUITreeNode.setState(contentCategory.getIsParent()?"closed":"open");
            list.add(easyUITreeNode);
        }
        return list;
    }

    @Override
    public FResult addContentCategory(long parentId, String name) {

        //创建一个contentCategory
        TbContentCategory contentCategory = new TbContentCategory();
        contentCategory.setName(name);
        contentCategory.setParentId(parentId);
        contentCategory.setIsParent(false);
        contentCategory.setSortOrder(1);
//        可选值:1(正常),2(删除)'
        contentCategory.setStatus(1);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入
        tbContentCategoryMapper.insert(contentCategory);

        //判断父节点是不是叶子节点，如果是，就将他变成父节点
        TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!parent.getIsParent()){
            parent.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(parent);
        }

        return FResult.ok(contentCategory);


    }
}
