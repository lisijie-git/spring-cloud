package com.lisijietech.service.module.rabbitmq.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * 关闭RabbitMQ
 * @author LiSiJie
 * @date 2023年5月7日 上午3:28:38
 */
public class RabbitMQClose {
	/**
	 * 关闭RabbitMQ的Channel和Connection
	 * @param channel
	 * @param connection
	 */
	public static void close(Channel channel,Connection connection) {
		if(channel != null && channel.isOpen()) {
			try {
				channel.close();
			} catch (IOException | TimeoutException e) {
				e.printStackTrace();
			}
		}
		if(connection != null) {
			try {
				connection.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
