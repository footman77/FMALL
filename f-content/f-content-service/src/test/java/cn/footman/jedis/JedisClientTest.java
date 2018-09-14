package cn.footman.jedis;

import cn.footman.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JedisClientTest {

    @Test
    public void testJedisClient() throws Exception{
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
        JedisClient jedisClient = ac.getBean(JedisClient.class);
        jedisClient.set("gogo1","hanhao");
        System.out.println(jedisClient.get("gogo1"));

    }
}
