package cn.footman.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestQueueCustomer {

    @Test
    public void testQueueCustomer() throws Exception{
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        System.in.read();
    }
}
