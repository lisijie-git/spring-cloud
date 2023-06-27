package com.lisijietech.service.module.rabbitmq.constant;

public interface RabbitMQConstant {
	String HOST = "localhost";
	int PORT = 5672;
	String USERNAME = "guest";
	String PASSWORD = "guest";
	
	String EXCHANGE_TYPE_TOPIC = "topic";
	String EXCHANGE_NAME = "my-exchange";
	String EXCHANGE_TRANSACTION = "exchange-transaction";
	String EXCHANGE_CONFIRM_SINGLE = "exchange-confirm-single";
	String EXCHANGE_CONFIRM_BATCH = "exchange-confirm-batch";
	String EXCHANGE_CONFIRM_ASYNC = "exchange-confirm-async";
	String EXCHANGE_PERSISTENCE = "exchange-persistence";
	
	String ROUTING_KEY_EXCHANGE = "my.routing-key.#";
	String ROUTING_KEY_PRODUCER = "my.routing-key.one";
	
	String QUEUE_NAME = "my-queue";
	String QUEUE_TRANSACTION = "queue-transaction";
	String QUEUE_CONFIRM_SINGLE = "queue-confirm-single";
	String QUEUE_CONFIRM_BATCH = "queue-confirm-batch";
	String QUEUE_CONFIRM_ASYNC = "queue-confirm-async";
	String QUEUE_PERSISTENCE = "queue-persistence";
}
