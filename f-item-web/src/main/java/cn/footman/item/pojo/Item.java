package cn.footman.item.pojo;

import cn.footman.pojo.TbItem;

public class Item extends TbItem {


    public Item(TbItem tbItem){
        this.setId(tbItem.getId());
        this.setCreated(tbItem.getCreated());
        this.setStatus(tbItem.getStatus());
        this.setUpdated(tbItem.getUpdated());
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setTitle(tbItem.getTitle());
    }



    public String[] getImages(){
        String image = this.getImage();
        if(image != null && !"".equals(image)){
            return image.split(",");
        }
        return null;
    }
}
