package cn.footman.sso.service.impl;

import cn.footman.common.jedis.JedisClient;
import cn.footman.common.utils.FResult;
import cn.footman.common.utils.JsonUtils;
import cn.footman.pojo.TbUser;
import cn.footman.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 根据ajax传递进来的token来查询用户信息
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JedisClient jedisClient;


    @Value("${TOKEN_EXPIRE}")
    private Integer TOKEN_EXPIRE;

    @Override
    public FResult getUserByToken(String token) {
        String userJson = jedisClient.get("SESSION:" + token);
        //如果没有user,表示token已经过期，需要重新登陆
        if(StringUtils.isBlank(userJson)){
            return FResult.build(201,"用户信息已过期，请重新登陆");
        }
        TbUser user = JsonUtils.jsonToPojo(userJson, TbUser.class);

        //重新设置过期时间
        jedisClient.expire("SESSION:" + token,TOKEN_EXPIRE);

        return FResult.ok(user);
    }
}
