package com.lisijietech.service.module.rabbitmq.producer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lisijietech.service.module.rabbitmq.constant.RabbitMQConstant;
import com.lisijietech.service.module.rabbitmq.util.RabbitMQClose;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者，使用发布确认机制，单个确认。
 * 学习用，效率比事务机制高，但效率还是很低。
 * 参考：
 * https://blog.csdn.net/cuierdan/article/details/124013373
 * https://blog.csdn.net/linwu_2006_2006/article/details/94171841
 * https://blog.csdn.net/dingd1234/article/details/124935443
 * @author LiSiJie
 * @date 2023年5月7日 上午3:18:00
 */
public class ConfirmSingleProducer {
	public static void send(String msg) {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQConstant.HOST);
		connectionFactory.setPort(RabbitMQConstant.PORT);
		connectionFactory.setVirtualHost("/");
		//学习用，如果默认不写用户名和密码，ConnectionFactory的包中默认就是guest，是RabbitMQ默认账户。
		//所以下两行代码是guest用户情况下可以不写。自定义创建用户就必须设置。
//		connectionFactory.setUsername("guest");
//		connectionFactory.setPassword("guest");
		
		Connection connection = null;
		Channel channel = null;
		try {
			connection = connectionFactory.newConnection();
			channel = connection.createChannel();
			
			channel.exchangeDeclare(
					RabbitMQConstant.EXCHANGE_CONFIRM_SINGLE,
					RabbitMQConstant.EXCHANGE_TYPE_TOPIC,
					false,false,false,null);
			
			channel.queueDeclare(RabbitMQConstant.QUEUE_CONFIRM_SINGLE,false,false,false,null);
			
			channel.queueBind(
					RabbitMQConstant.QUEUE_CONFIRM_SINGLE,
					RabbitMQConstant.EXCHANGE_CONFIRM_SINGLE,
					RabbitMQConstant.ROUTING_KEY_EXCHANGE);
			//记录运行时间，运行时间开始。
			long start = System.currentTimeMillis();
			
			//开启发布确认模式
			channel.confirmSelect();
			
			//发送1000次消息，并进行单个发布确认。用于测试速度。
			long sequenceNo = 0;//消息序列号
			boolean flag = true;//生产者向RabbitMQ发送确认发布请求。
			for(int i = 0;i <1000;i++) {
				
				//消息序列号
				sequenceNo = channel.getNextPublishSeqNo();
				//推送消息
				channel.basicPublish(
						RabbitMQConstant.EXCHANGE_CONFIRM_SINGLE,
						RabbitMQConstant.ROUTING_KEY_PRODUCER,
						null,msg.getBytes());
				//生产者向RabbitMQ发送确认发布请求。
				flag = channel.waitForConfirms();
				if(flag) {
					System.out.println("消息发布确认成功，消息序号为：" + sequenceNo);
				}else {
					System.out.println("消息发布确认失败，消息序号为：" + sequenceNo);
				}
			}
			//记录运行时间，运行时间开始。
			long end = System.currentTimeMillis();
			System.out.println("发送1000消息，单个发布确认耗时：" + (end - start) + "ms");
		} catch (IOException | InterruptedException | TimeoutException e) {
			e.printStackTrace();
			System.out.println("消息发送异常");
		} finally {
			RabbitMQClose.close(channel, connection);
		}
	}
}
