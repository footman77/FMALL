package cn.footman.jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

public class JedisTest {

    @Test
    public void testJedis() throws Exception{
        Jedis jedis = new Jedis("192.168.25.129", 6379);
        jedis.set("baba","henlihai");
        String baba = jedis.get("baba");
        System.out.println(baba);
        jedis.close();
    }
    @Test
    public void testJedisPool() throws Exception{
        JedisPool jedisPool = new JedisPool("192.168.25.129", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("mama","shuai a !");
        String mama = jedis.get("mama");
        System.out.println(mama);

        jedis.close();
        jedisPool.close();
    }
    @Test
    public void testJedisCluster() throws Exception{

        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.25.129",7001));
        nodes.add(new HostAndPort("192.168.25.129",7002));
        nodes.add(new HostAndPort("192.168.25.129",7003));
        nodes.add(new HostAndPort("192.168.25.129",7004));
        nodes.add(new HostAndPort("192.168.25.129",7005));
        nodes.add(new HostAndPort("192.168.25.129",7006));


        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("test1232","test12133");
        System.out.println(jedisCluster.get("test1232"));

        jedisCluster.close();
    }
}
