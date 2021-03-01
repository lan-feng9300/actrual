package com.example.rabbitmq.controller;

import com.example.rabbitmq.entity.User;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RabbitListener(queues = {"hello-java-queue"})
public class RabbitController {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     *  queues = {}, 队列数字, 可以同时监听多个队列
     *  同理, 一个队列可以被多线程监听, 但是最终, 消息只会被一个, 消费者即线程 拿到
     *
     *  此处是在, test包下完成, 消息的发送
     */

    /**
     *  listenMsg(Object message)
     *  message 的组成:
     *      body: (Body:'{"name":"李四","userId":"9527","age":22}'
     *      headers: headers={__TypeId__=com.example.rabbitmq.entity.User},
     *               contentType=application/json, contentEncoding=UTF-8 等
     *
     *  也可以指定泛型接收消息:
     *   listenMsg(Object message, User user, AMQP.Channel channel)
     */

//    方法一:
//    @RabbitListener(queues = {"hello-java-queue"})
//    public void listenMsg(Object message){
//
//        log.info("监听成功, 已拿到消息:"+ message + "消息类型:" + message.getClass());
//    }


//    方法二:
//    @RabbitListener(queues = {"hello-java-queue"})
//    public void listenMsg2(Message message, User user)
//        打印出 body 实体
//        log.info("消息监听成功:" + message);
//        log.info("消息体:" + user);
//    }


//    @RabbitListener(queues = {"hello-java-queue"})
//    public void  listenMsg3(Message message, User user, Channel channel){
//        log.info("实体:" + user);
//        log.info("信道:" + channel);
//    }

    @RabbitHandler
    public void  receveMsg(Message message, User user, Channel channel){
        log.info("接收到消息, 实体:" + user+ "信道:" + channel);
        // 消息头属性
        MessageProperties pro = message.getMessageProperties();
        //log.info("消息头属性: " + pro);
        // 获取信道内, 消息的 tag, 信道内按顺序自增的
        long deliveryTag = pro.getDeliveryTag();
        //log.info("信道内tag: " + deliveryTag);

        try {
            // 信道内签收消息
            channel.basicAck(deliveryTag, false);
            log.info("签收了货物 tag: " + deliveryTag);
        } catch (IOException e) {
            log.error("网络中断, 消息签收失败 tag: "+ deliveryTag);
        }
    }

    @GetMapping("/sendMag")
    public String sendMg(@RequestParam(value = "num", defaultValue = "5") Integer num){
//        for (int i=0; i<num; i++){
//            if (i % 2 == 0) {
//                User user = new User("name["+i+"]", i+"", 20+i);
//                rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", user, new CorrelationData(UUID.randomUUID().toString()));
//            }else {
//                User user = new User("name["+i+"]", UUID.randomUUID().toString(), 10+i);
//                rabbitTemplate.convertAndSend("hello-java-exchange", "hello2-java", user, new CorrelationData(UUID.randomUUID().toString()));
//            }
//        }

        for(int i=0; i<=num; i++){
            User user = new User("name["+i+"]", i+"", 20+i);
            rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", user, new CorrelationData(UUID.randomUUID().toString()));
        }
        return "ok";
    }
}
