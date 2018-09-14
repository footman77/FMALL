package cn.footman.sso.service.impl;

import cn.footman.common.utils.FResult;
import cn.footman.mapper.TbUserMapper;
import cn.footman.pojo.TbUser;
import cn.footman.pojo.TbUserExample;
import cn.footman.sso.service.RegisterService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public FResult checkData(String param, int type) {
        //根据类型生成不同的sql语句
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        if(type == 1){
            criteria.andUsernameEqualTo(param);
        }else if(type == 2){
            criteria.andPhoneEqualTo(param);
        }else if(type == 3){
            criteria.andEmailEqualTo(param);
        }else {
            return FResult.build(400,"参数错误");
        }
        //进行查询
        List<TbUser> userList = tbUserMapper.selectByExample(example);
        //判断查询结果，如果有内容，则返回false，没有就返回true

        if(userList != null && userList.size() > 0){
            return FResult.ok(false);
        }else {
            return FResult.ok(true);
        }


    }

    //注册,保存用户数据
    @Override
    public FResult saveUser(TbUser tbUser) {
        //判断数据的完整性
        if(StringUtils.isBlank(tbUser.getUsername()) || StringUtils.isBlank(tbUser.getPhone()) || StringUtils.isBlank(tbUser.getPassword())){
            return FResult.build(400,"用户数据不完整");
        }
        //判断是否重复
        FResult result = checkData(tbUser.getUsername(), 1);
        if(!(boolean)result.getData()){
            return FResult.build(400,"用户名重复");
        }
        result = checkData(tbUser.getPhone(), 2);
        if(!(boolean)result.getData()){
            return FResult.build(400,"手机号重复");
        }

        //补全数据
        tbUser.setCreated(new Date());
        tbUser.setUpdated(new Date());

        //将密码进行md5加密
        String s = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
        tbUser.setPassword(s);

        tbUserMapper.insert(tbUser);

        return FResult.ok();
    }
}
