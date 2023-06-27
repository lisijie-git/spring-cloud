package com.lisijietech.service.module.rabbitmq.consumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.lisijietech.service.module.rabbitmq.constant.RabbitMQConstant;
import com.lisijietech.service.module.rabbitmq.util.RabbitMQClose;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 消费端手动确认，单个消息确认。
 * 原生方法的话，推荐单个消息确认，批量确认会有很多问题。批量确认会确认小于当前消息的tag值的所有消息。
 * 如果多个消费者订阅一个队列，消息tag值是如何生成，一个消费者是否会确认到同一个列队的其他消费者消息？
 * 如果消息消费异常，还是需要单个确认消费失败，否则批量确认消费失败，已经消费的消息并不会回滚，即使自己开发了回滚功能，
 * 性能消耗很大，而且这种设计本省就不合理。
 * 如果消息消费异常，单个确认失败，之后的消息成功，然后批量确认，是否会影响之前的消费失败消息让其误以为确认成功，从而
 * 让重回队列的消息被删除。（猜测不会，消费失败，消息重回队列，可能原来的tag不同了，或者消息在队列的位置不一样了。）
 * @author LiSiJie
 * @date 2023年6月17日 上午3:29:52
 */
public class ManualAckConsumer {
	public static void receive(String msg) {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQConstant.HOST);
		connectionFactory.setPort(RabbitMQConstant.PORT);
		connectionFactory.setVirtualHost("/");
		//学习用，如果默认不写用户名和密码，ConnectionFactory的包中默认就是guest，是RabbitMQ默认账户。
		//所以下两行代码是guest用户情况下可以不写。自定义创建用户就必须设置。
//		connectionFactory.setUsername("guest");
//		connectionFactory.setPassword("guest");
		
		//这里仅仅是引用try块里的对象，用来finally块里关闭连接用的。
		//这种做法感觉可以优化，不太合理。但是有原因的，lambda表达式中语句块使用了局部变量channel.basicAck，
		//connection和channel不能在try块之外声明并赋值null，再在try块内重新赋值，lambda中的channel会报错。
		//并且我不想向方法上层声明抛出异常，所以暂时这样写。
		Connection connectionClose = null;
		Channel channelClose = null;
		//记录运行时间，第一个元素是开始时间戳，第二个元素是结束时间戳，第三个元素是缓存新的时间，减少变量创建时间。
		//假设消费者是同步消费消息的，没有线程安全问题。
		long[] time = {0,0,0};
		try {
//			connection = connectionFactory.newConnection();
//			channel = connection.createChannel();
			
			Connection connection = connectionFactory.newConnection();
			//成员变量channel在deliverCallback引用的lambda函数对象中被使用，成员变量必须为final修饰或者实际上不可变的。
			//final修饰好理解。实际上不可变，个人理解就是变量声明好赋值就不变了的意思，就不用显示final修饰。
			//否则报错Local variable channel defined in an enclosing scope must be final or effectively final
			//如，写了局部变量Channel channel = null;赋值null后又进行赋值channel = connection.createChannel();
			//这样在lambda中的channel.basicAck就会报错。
			Channel channel = connection.createChannel();
			
			connectionClose = connection;
			channelClose = channel;
			
			//消费消息时的回调接口。待研究，消息消费是否是异步的，即同时来了几条消息，多线程进行消费，
			//还是消息在消费者放入队列缓存，一个一个消费。
			DeliverCallback deliverCallback = (consumerTag,delivery) -> {
				System.out.println(new String(delivery.getBody()));
				//参数一、deliveryTag消息应答标记
				//参数二、multiple。
				//false单个应答，只应答接受到的这个消息。true，批量应答，包括小于等于当前消息tag值的消息。
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
				//结束时间。假设消费是同步的，time没有线程安全问题。
				time[2] = System.currentTimeMillis();
				if(time[2] > time[1]) {
					time[1] = time[2];
				}
			};
			//消费取消时的回调接口，如队列被删除了，取消消费
			CancelCallback cancelCallback = consumerTag -> {
				System.out.println(consumerTag + "消费被取消");
			};
			
			//重复调用相同配置的channel.basicConsume，是错误的。
			//个人理解，RabbitMQ的消息处理模式有推和拉，channel.basicConsume是推模式，由broker主动推送消息，
			//消费者进行订阅，消息队列推送消息，有消息就会推送，消费者异步非阻塞监听处理消息。
			//消费者拉取消息，需要用channel.basicGet方法，消息需要消费者主动去获取。
			//如下for循环channel.basicConsume，是异步非阻塞监听处理的。可能每次调用channel.basicConsume都是新创建
			//消费监听，或者新创建的消费监听覆盖原来的监听，或者是原来有就会重新配置原来的消费监听。
			//所以导致for循环还没到10，1000条信息已经被消费完了。
//			for(int i = 0;i < 1000;i++) {
//				//参数autoAck，false是手动应答，true是自动应答。
//				//重载方法，没有autoAck参数的，默认是自动应答。
//				channel.basicConsume(RabbitMQConstant.QUEUE_CONFIRM_ASYNC, false, deliverCallback, cancelCallback);
//			}
			//开始时间
			time[0] = System.currentTimeMillis();
			channel.basicConsume(RabbitMQConstant.QUEUE_CONFIRM_ASYNC, false, deliverCallback, cancelCallback);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
			System.out.println("消息消费异常");
		} finally {
			try {
				//由于channel.basicConsume方法是异步非阻塞的，不能马上关闭连接，防止消息未消费完。
				Thread.sleep(5000);
				System.out.println("消费1000消息，单个消费手动应答耗时：" + (time[2] - time[0]) + "ms");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			RabbitMQClose.close(channelClose, connectionClose);
		}
	}
}
