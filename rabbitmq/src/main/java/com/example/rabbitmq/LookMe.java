package com.example.rabbitmq;

public class LookMe {
    /**
     *  rabbitmq 的使用
     *  该部分, 在 test 包下完成
     *  消息  ---  收发
     *  1: 导入依赖包  依赖包中, 自动导入了, RabbitAutoConfiguration
     *      其中: AmqpAdmin, RabbitTemplate
     *  2: 配置连接信息
     *      spring.rabbitmq.host=192.168.101.19
     *      spring.rabbitmq.virtual-host=/
     *
     *      开启注解: @EnableRabbit
     *  3: 导入 AmqpAdmin  amqpAdmin , 使用其, 创建 exchange, queue, route-key
     *          RabbitTemplate, 收发消息
     *  4:  若是传送 实体类, 需要 implements Serializable
     *      自定义: json, 序列化机制, 而不是使用默认的 jdk 序列化机制
     *      RabbitConfig
     */

    /**
     *  监听 -- 消息
     * @RbbitListener  类 + 方法上 (监听哪些队列即可)
     * @RabbitHandler  方法上 (重载区分不同的消息)
     * 如:
     *      @RabbitHandler
     *      listener1(Message message, User user){}
     *
            @RabbitHandler
     *      listener1(Message message, Consumer consumer){}
     *
     *  队列监听原则:
     *      1: 一个业务, 监听的队列有多个消息, 只能是, 上一个 消息处理完了, 才能接受下一个方法
     *      2: 同一个消息, 只能被一个分收到
     */


    /**
     *  消息 确认机制, 此处测试, 只能在 controller 发消息, test, 发送不成功
     *
     *   publisher  confirmCallback  确认模式
     *   publisher  returnCallback   未投递到queue, 退回模式
     *   consumer   ack  机制
     *
     *   p --->  broker( exchange  -->  queue )  --->  c
     *   p--> broker: confirmCallback
     *   exchange --> queue: returnCallback
     *   queue--> c:  ack
     *
     *
     *   使用步骤:
     *   一: 服务器收到消息, 回调:
     *      1): 开启发送端确认: spring.rabbitmq.publisher-confirms=true
     *      2): 配置 RabbitConfig  --> 如: intiRabbitTemplate()-> rabbitTemplate.setConfirmCallback 的配置
     *
     *   二: 消息正确抵达队列, 进行回调
     *      1): 开启发送端, 消息正确抵达队列回调
     *          spring.rabbitmq.publisher-returns=true --> 抵达确认
     *          spring.rabbitmq.template.mandatory=true --> 只要抵达队列,以异步发送优先回调我们这个 returnconfirm
     *      2): 配置 RabbitConfig  --> 如: intiRabbitTemplate()-> rabbitTemplate.setReturnCallback 的配置
     *
     *   三: 消费端确认 (保证每个消息被正确消费, 此时才可以做在 broker 删除这个消息)
     *      1) : 默认是自动确认的, 只要消息接收到, 客户端会自动确认, 服务端就会移除这个消息
     *          问题:
     *              我们收到很多消息, 自动回复给服务器 ack, 只有一个消息处理成功, 宕机了, 发生了消息丢失
     *          解决:
     *              1.1) 关闭自动ack模式, 开启手动签收
     *                   spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *              效果: 消费者手动确认模式, 只要我们没有明确告诉mq, 货物被签收, 没有ack,
     *                    消息就一直都是 unacked状态, 即使consumer 宕机, 消息会重新被置为, Ready状态
     *     如何手动签收:
     *              消费者, 监听处, channel.basicAck(deliveryTag, multiple), 如: receveMsg() 方法
     *     如何手动拒收:
     *              参数: requeue, 是否重回队列
     *                   multiple: 是否批量
     *                   delivery: 消息 tag
     *              channel.basicNack(long delivery, boolean requeue, boolean multiple);
     *              channel.basicReject(long delivery, boolean requeue)
     *
     *
     */
}
