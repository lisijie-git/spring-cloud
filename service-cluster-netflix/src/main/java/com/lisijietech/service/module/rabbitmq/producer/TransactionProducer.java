package com.lisijietech.service.module.rabbitmq.producer;

import java.io.IOException;

import com.lisijietech.service.module.rabbitmq.constant.RabbitMQConstant;
import com.lisijietech.service.module.rabbitmq.util.RabbitMQClose;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 消息生产者，使用事务机制。
 * 学习用，效率低，事务是阻塞的，实际生产中不考虑。
 * 参考：
 * https://blog.csdn.net/cuierdan/article/details/124013373
 * https://blog.csdn.net/linwu_2006_2006/article/details/94171841
 * https://blog.csdn.net/dingd1234/article/details/124935443
 * @author LiSiJie
 * @date 2023年5月5日 上午7:09:30
 */
public class TransactionProducer {
	
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
					RabbitMQConstant.EXCHANGE_TRANSACTION,
					RabbitMQConstant.EXCHANGE_TYPE_TOPIC,
					false,false,false,null);
			
			channel.queueDeclare(RabbitMQConstant.QUEUE_TRANSACTION,false,false,false,null);
			
			channel.queueBind(
					RabbitMQConstant.QUEUE_TRANSACTION,
					RabbitMQConstant.EXCHANGE_TRANSACTION,
					RabbitMQConstant.ROUTING_KEY_EXCHANGE);
			
			//开启事务机制
			channel.txSelect();
			//发布消息
			channel.basicPublish(
					RabbitMQConstant.EXCHANGE_TRANSACTION,
					RabbitMQConstant.ROUTING_KEY_PRODUCER,
					null,msg.getBytes());
			//提交事务
			channel.txCommit();
			
			//开启事务机制，发送第二条消息。在生产者这里产生异常或者MQ方产生异常，测试事务回滚。
			channel.txSelect();
			channel.basicPublish(
					RabbitMQConstant.EXCHANGE_TRANSACTION,
					RabbitMQConstant.ROUTING_KEY_PRODUCER,
					null,msg.getBytes());
			//设置异常。当然，也需要测设MQ出现问题，事务回滚，可以睡眠生产者一段时间，主动关闭RabbitMQ来实现。
//			Thread.sleep(10000);
			float f = 1/0;
			channel.txCommit();
		} catch (Exception e) {
			e.printStackTrace();
			//事务回滚其实作用在 开启事务 - 提交事务 这个区间的代码执行时。
			//但try catch 异常捕获了其他代码的异常，容易混淆异常，以后优化
			//如connectionFactory.newConnection()的IOException | TimeoutException。
			//并且回滚异常捕获需要Exception异常，在事务代码区间，如果加入其他代码，可能有其他异常，如运行时异常等。
			try {
				if(channel != null) {
					//回滚事务
					channel.txRollback();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("消息发送异常，回滚异常");
			}
			
		} finally {
			RabbitMQClose.close(channel, connection);
		}
	}
}
