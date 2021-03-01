package com.example.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class RabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 自定义消息转换器
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct // RabbitConfig 对象创建完成以后, 执行这个方法
    public void intiRabbitTemplate(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            /**
             * 只有消息抵达服务器, ack, 就会为true, 不论有没有被监听, 都会执行
             * @param correlationData  当前消息的唯一关联数据, 即消息 唯一 id
             *                         可以在发消息的时候指定唯一id, 即执行, convertAndSend() 方法时
             *        rabbitTemplate.convertAndSend("hello-java-exchange", "hello-java", lisi, new CorrelationData(UUID.randomUUID().toString()));
             * @param b  消息是否成功收到
             * @param s  失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("confirm.. correlationData:" +correlationData+ "ack:"+b+ "cause:"+s);
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 触发机制: 只要消息没有投递给指定队列, 就触发这个失败回调
             * @param message  投递失败的详细信息
             * @param i   回复的状态码
             * @param s   回复的文本内容
             * @param s1  当时这个消息发给哪个交换机 exchange
             * @param s2  当时这个消息, 使用的哪个路邮键
             */
            @Override
            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
                System.out.println("returnConfirm.. message:" +message+ ", code:"+i+ ", text:"+s +
                        ", exchange:" +s1 + ", route:"+ s2);
            }
        });
    }


}
