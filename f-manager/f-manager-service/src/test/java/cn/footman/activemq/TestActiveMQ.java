package cn.footman.activemq;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.junit.Test;

import javax.jms.*;

public class TestActiveMQ {

    @Test
    public void testQueueProducer() throws Exception{

//        1、创建一个连接工厂对象，需要指定服务器的ip及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");
        //2、使用工厂对象创建一个Connection对象
        Connection connection = connectionFactory.createConnection();
        //3、开启连接，调用Connection对象的start方法
        connection.start();
        //4、创建一个session对象
        //第一个参数：是否开启事务，true开启，第二个参数无意义；一般不开启事务
        //第二个参数：应答模式，自动应答或者手动应答，一般自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5、使用session创建一个Destination对象，两种形式queue\topic
        Queue queue = session.createQueue("test-queue");
        //6、使用session对象创建一个producer对象
        MessageProducer producer = session.createProducer(queue);
        //创建一个message对象，可以使用textmessage
        TextMessage textMessage = session.createTextMessage("hello world activemq");

        //发送消息
        producer.send(textMessage);
        //关闭资源
        producer.close();
        session.close();
        connection.close();

    }

    @Test
    public void testQueueCustomer() throws Exception{

        //创建一个ConnectionFactory对象连接MQ服务器
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.130:61616");

        //创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用connnection对象创建一个session对象
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //创建一个Destination对象，queue对象
        Queue queue = session.createQueue("spring-queue");
        //使用session对象创建一个customer对象
        MessageConsumer consumer = session.createConsumer(queue);
        //接受消息
        consumer.setMessageListener(new MessageListener() {
            //打印结果

            @Override
            public void onMessage(Message message) {
                TextMessage textMessage = (TextMessage) message;
                try {
                    String text = textMessage.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //等待接受消息
        System.in.read();
        //关闭资源
        consumer.close();
        session.close();
        connection.close();



    }

}
