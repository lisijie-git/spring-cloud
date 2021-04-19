package com.lisijietech.service.module.rabbitmq.constant;

public interface RabbitMQConstant {
	String HOST = "localhost";
	int PORT = 5672;
	String USERNAME = "guest";
	String PASSWORD = "guest";
	
	String EXCHANGE_TYPE = "topic";
	String EXCHANGE_NAME = "my-exchange";
	
	String ROUTING_KEY_EXCHANGE = "my.routing-key.#";
	String ROUTING_KEY_PRODUCER = "my.routing-key.one";
	
	String QUEUE_NAME = "my-queue";
}
