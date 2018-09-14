package cn.footman.sso.service.impl;

import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.mapper.TbUserMapper;
import cn.footman.pojo.TbUser;
import cn.footman.pojo.TbUserExample;
import cn.footman.sso.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${TOKEN_EXPIRE}")
    private Integer TOKEN_EXPIRE;

    @Override
    public FResult login(String username, String password) {
        //验证账号是否在数据库 中存在
        TbUserExample tbUserExample = new TbUserExample();
        TbUserExample.Criteria criteria = tbUserExample.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> userList = tbUserMapper.selectByExample(tbUserExample);

        if(userList == null || userList.size() == 0 ){
            //表示没有查到数据
            return FResult.build(400,"账号或者密码输入错误");
        }
        //根据账号查看密码
        TbUser user = userList.get(0);
        if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
            //密码不符合
            return FResult.build(400,"账号或者密码输入错误");
        }

        //密码正确生成token放入redis
        String token = UUID.randomUUID().toString();

        user.setPassword(null);
        jedisClient.set("SESSION:" + token,JsonUtils.objectToJson(user));
        //过期时间
        jedisClient.expire("SESSION:" + token,TOKEN_EXPIRE);

        //返回token
        return FResult.ok(token);

    }
}
