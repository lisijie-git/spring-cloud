package com.lisijietech.service.module.rabbitmq.producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lisijietech.service.module.rabbitmq.constant.RabbitMQConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * rabbitmq的生产者。
 * 原生，未集成spring。
 * 参考：
 * https://www.cnblogs.com/chy18883701161/p/12501428.html
 * @author LiSiJie
 * @date 2021年4月19日 上午6:03:02
 */
public class MyProducer {
	
	public static void send(String msg) {
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
			
			channel.exchangeDeclare(
					RabbitMQConstant.EXCHANGE_NAME,RabbitMQConstant.EXCHANGE_TYPE,false,false,false,null);
			
			channel.queueDeclare(RabbitMQConstant.QUEUE_NAME,false,false,false,null);
			
			channel.queueBind(
					RabbitMQConstant.QUEUE_NAME,
					RabbitMQConstant.EXCHANGE_NAME,
					RabbitMQConstant.ROUTING_KEY_EXCHANGE);
			
			channel.basicPublish(
					RabbitMQConstant.EXCHANGE_NAME,
					RabbitMQConstant.ROUTING_KEY_PRODUCER,
					null,
					msg.getBytes());
			
			channel.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		
	}
}
