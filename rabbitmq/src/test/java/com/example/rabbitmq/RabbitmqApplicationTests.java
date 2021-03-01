package com.example.rabbitmq;

import com.example.rabbitmq.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

	@Autowired
	AmqpAdmin amqpAdmin;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Test
	public void createExchange(){
		/**
		 *  创建 直接交换机 hello-java-exchange
		 *  DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
		 */
		DirectExchange exchange = new DirectExchange("hello-java-exchange", true, false);
		amqpAdmin.declareExchange(exchange);
		log.info("hello-java-exchange 创建成功");
	}

	@Test
	public void createQueue(){
		/**
		 *  创建 hello-java-queue 队列
		 *  Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
		 */
		Queue queue = new Queue("hello-java-queue", true, false, false);
		amqpAdmin.declareQueue(queue);
		log.info("hello-java-queue 队列创建成功");
	}

	@Test
	public void createBinding(){
		/**
		 * 创建绑定
		 *    Binding(String destination, Binding.DestinationType destinationType,
		 *    String exchange, String routingKey, Map<String, Object> arguments)
		 */
		Binding binding = new Binding(
				"hello-java-queue",
				Binding.DestinationType.QUEUE,
				"hello-java-exchange",
				"hello-java",
				null
		);
		amqpAdmin.declareBinding(binding);
	}

	@Test
	public void sendMessage(){
		/**
		 *  send(Message msg)  只发送消息
		 *  convertAndSend() :  发送并转换
		 *  (exchange var1, routing-key var2, Object var3)
		 *
		 *  此处若是, 发送一个实体,会默认使用 jdk 序列化,
		 *  所有需要自定义序列化机制, 发送 json 到 queue中
		 *
		 *  自定义 序列化机制  RabbitConfig
		 */
//		String msg = "hello-word";
//		rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", msg);

		User lisi = new User("李四", "9527", 22);
		rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", lisi, new CorrelationData(UUID.randomUUID().toString()));
		log.info("消息发送完成");
	}

}
