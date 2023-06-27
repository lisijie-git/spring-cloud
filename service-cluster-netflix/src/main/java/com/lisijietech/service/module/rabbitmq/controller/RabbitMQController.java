package com.lisijietech.service.module.rabbitmq.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.lisijietech.service.module.rabbitmq.consumer.ManualAckConsumer;
import com.lisijietech.service.module.rabbitmq.consumer.ManualBatchAckConsumer;
import com.lisijietech.service.module.rabbitmq.consumer.MyConsumer;
import com.lisijietech.service.module.rabbitmq.producer.ConfirmAsyncProducer;
import com.lisijietech.service.module.rabbitmq.producer.ConfirmBatchProducer;
import com.lisijietech.service.module.rabbitmq.producer.ConfirmSingleProducer;
import com.lisijietech.service.module.rabbitmq.producer.MyProducer;
import com.lisijietech.service.module.rabbitmq.producer.PersistenceProducer;
import com.lisijietech.service.module.rabbitmq.producer.TransactionProducer;

/**
 * RabbitMQ测试Controller层。
 * 原生RabbitMQ客户端测试，
 * 当然也可以与Spring的AMQP集成，使用比较简单，或者Spring的Stream集成，以前项目用过，现在网上搜索出来的用的比较少。
 * @author LiSiJie
 * @date 2021年4月20日 上午2:20:09
 */
@RestController
public class RabbitMQController {
	@Value("${spring.application.name}")
	private String appName;
	
	@Value("${server.port}")
	private String port;
	
	public String getServiceInfo() {
		return new StringBuilder(appName).append(",").append(port).append("    ").toString();
	}
	
//	------生产者接口------
	
	@GetMapping(value = "/rabbitmq/producer/{msg}")
	public String sendByProducer(@PathVariable(name = "msg") String msg) {
		MyProducer.send(msg);
		return getServiceInfo() + "生产者发送的消息：" + msg;
	}
	
	@GetMapping(value = "/rabbitmq/producer-transaction/{msg}")
	public String sendByTransactionProducer(@PathVariable(name = "msg") String msg) {
		TransactionProducer.send(msg);
		return getServiceInfo() + "生产者发送的消息：" + msg;
	}
	
	@GetMapping(value = "/rabbitmq/producer-confirm-single/{msg}")
	public String sendByConfirmSingleProducer(@PathVariable(name = "msg") String msg) {
		ConfirmSingleProducer.send(msg);
		return getServiceInfo() + "生产者发送的消息：" + msg;
	}
	
	@GetMapping(value = "/rabbitmq/producer-confirm-batch/{msg}")
	public String sendByConfirmBatchProducer(@PathVariable(name = "msg") String msg) {
		ConfirmBatchProducer.send(msg);
		return getServiceInfo() + "生产者发送的消息：" + msg;
	}
	
	@GetMapping(value = "/rabbitmq/producer-confirm-async/{msg}")
	public String sendByConfirmAsyncProducer(@PathVariable(name = "msg") String msg) {
		ConfirmAsyncProducer.send(msg);
		return getServiceInfo() + "生产者发送的消息：" + msg;
	}
	
	@GetMapping(value = "/rabbitmq/producer-persistence/{msg}")
	public String sendByPersistenceProducer(@PathVariable(name = "msg") String msg) {
		PersistenceProducer.send(msg);
		return getServiceInfo() + "生产者发送的消息：" + msg;
	}
	
//	------消费者接口------
	
	@GetMapping(value = "/rabbitmq/consumer")
	public String receiveByConsumer() {
		String info = getServiceInfo() + "消费者收到的消息：";
		MyConsumer.receive(info);
		return "消费者收到了消息";
	}
	
	@GetMapping(value = "/rabbitmq/consumer-manual-ack")
	public String receiveByManualAckConsumer() {
		ManualAckConsumer.receive(null);
		return getServiceInfo() + "消费者收到了消息";
	}
	
	@GetMapping(value = "/rabbitmq/consumer-manual-batch-ack")
	public String receiveByManualBatchAckConsumer() {
		ManualBatchAckConsumer.receive(null);
		return getServiceInfo() + "消费者收到了消息";
	}

}
