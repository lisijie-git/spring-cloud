package com.lisijietech.service.module.rabbitmq.consumer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.lisijietech.service.module.rabbitmq.constant.RabbitMQConstant;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/**
 * rabbitmq的消费者。
 * 原生，未集成spring。
 * 参考：
 * https://www.cnblogs.com/chy18883701161/p/12501428.html
 * https://www.jianshu.com/p/df231c152754
 * 
 * @author LiSiJie
 * @date 2021年4月19日 上午5:45:29
 */
public class MyConsumer {
	
	public static void receive(String info) {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQConstant.HOST);
		connectionFactory.setPort(RabbitMQConstant.PORT);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = null;
		Channel channel = null;
		try {
			connection = connectionFactory.newConnection();
			connection.createChannel();
			
			channel = connection.createChannel();
			
			DefaultConsumer consumer = new DefaultConsumer(channel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					System.out.println(info + new String(body));
				}
			};
			
			channel.basicConsume(RabbitMQConstant.QUEUE_NAME,true,consumer);
			
			//休眠是防止测试时，还没收到消息就断开连接了
			TimeUnit.SECONDS.sleep(5);
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
