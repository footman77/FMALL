package cn.footman.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class ActiveSpringProducer {

    @Test
    public void testActiveSpring() throws Exception{

        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        JmsTemplate jmsTemplate = ac.getBean(JmsTemplate.class);

        Destination destination = (Destination) ac.getBean("queueDestination");
        //使用JmsTemplate对象发送消息。
        jmsTemplate.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                //创建一个消息对象并返回
                TextMessage textMessage = session.createTextMessage("spring activemq !!!");
                return textMessage;
            }
        });



    }
}
