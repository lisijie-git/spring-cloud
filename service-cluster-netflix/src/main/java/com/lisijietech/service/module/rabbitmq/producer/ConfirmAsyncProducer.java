package com.lisijietech.service.module.rabbitmq.producer;

import java.io.IOException;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

import com.lisijietech.service.module.rabbitmq.constant.RabbitMQConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者，使用发布确认机制，异步确认。
 * 如果需要对发送的消息进行回调处理，需要有容器记录他们，如失败消息重发。
 * 效率高性价比最好的。
 * 参考：
 * https://blog.csdn.net/cuierdan/article/details/124013373
 * https://blog.csdn.net/linwu_2006_2006/article/details/94171841
 * https://blog.csdn.net/dingd1234/article/details/124935443
 * https://blog.csdn.net/agonie201218/article/details/106922450/
 * @author LiSiJie
 * @date 2023年5月7日 上午7:07:47
 */
public class ConfirmAsyncProducer {
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
					RabbitMQConstant.EXCHANGE_CONFIRM_ASYNC,
					RabbitMQConstant.EXCHANGE_TYPE_TOPIC,
					false,false,false,null);
			
			channel.queueDeclare(RabbitMQConstant.QUEUE_CONFIRM_ASYNC,false,false,false,null);
			
			channel.queueBind(
					RabbitMQConstant.QUEUE_CONFIRM_ASYNC,
					RabbitMQConstant.EXCHANGE_CONFIRM_ASYNC,
					RabbitMQConstant.ROUTING_KEY_EXCHANGE);
			
			//未完成确认的消息缓存。key为消息序号，value为消息内容。ConcurrentSkipListMap网络了解学习。
			//可能是批量确认，可能是单个确认。
			//批量确认，清除 <= 返回的消息序号的消息缓存。单个确认，只清除返回的消息序号的消息缓存。
			ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();
			//发布确认成功回调
			ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
				if(multiple) {
					//获取小于等于sequenceNumber也就是deliveryTag的消息
					ConcurrentNavigableMap<Long, String> confirmPartMap = outstandingConfirms.headMap(deliveryTag);
					//删除已经批量确认的消息map
					confirmPartMap.clear();
				} else {
					//删除单个确认的消息。
					outstandingConfirms.remove(deliveryTag);
				}
				System.out.println("消息发布确认成功，消息序号为：" + deliveryTag);
			};
			//发布确认失败回调。失败后可以重发或者其他处理。
			ConfirmCallback nackCallback = (deliveryTag,multiple) -> {
				System.out.println("消息发布确认失败，消息序号为：" + deliveryTag);
			};
			
			//记录运行时间，运行时间开始。
			long start = System.currentTimeMillis();
			
			//开启发布确认模式
			channel.confirmSelect();
			//添加发布确认监视器。异步监听确认，进行成功或者失败回调。
			channel.addConfirmListener(ackCallback, nackCallback);
			
			//发送1000次消息，并进行单个发布确认。用于测试速度。
			long sequenceNo = 0;//消息序列号
			for(int i = 0;i <1000;i++) {
				//消息序列号
				sequenceNo = channel.getNextPublishSeqNo();
				//有序缓存消息序列和消息内容
				outstandingConfirms.put(sequenceNo,msg);
				//推送消息
				channel.basicPublish(
						RabbitMQConstant.EXCHANGE_CONFIRM_ASYNC,
						RabbitMQConstant.ROUTING_KEY_PRODUCER,
						null,msg.getBytes());
			}
			//记录运行时间，运行时间开始。
			long end = System.currentTimeMillis();
			System.out.println("发送1000消息，异步发布确认耗时：" + (end - start) + "ms");
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
			System.out.println("消息发送异常");
		} finally {
			//不关闭连接，保证异步发布确认的监听
//			RabbitMQClose.close(channel, connection);
		}
	}
}
