package com.example.demo.Service;

import com.example.demo.entity.User;
import com.example.demo.mapper.CacheMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CacheService {

    @Autowired
    CacheMapper cacheMapper;

    /**
     *  分布式缓存的使用:
     *  1: 导入依赖: spring-boot-starter-cache, spring-boot-starter-data-redis
     *  2: 编写配置文件: spring.cache.type: redis
     *  3: 开启注解, @EnableCaching 简单使用 @Cacheable(value = "user") 缓存分区为 user
     *     主要注解: @Cacheable --  触发, 将数据保存到缓存的操作, 该数据没有则调用方法, 没有缓存则, 不调用方法
     *              @CacheEvict  -- 触发, 将数据从缓存删除的操作
     *              @CachePut  --  不影响方法执行更新缓存
     *              @Caching  --  组合以上多个, 注解的操作
     *              @CacheConfig  --  在类级别, 共享缓存的相同配置
     *
     *  简单使用的默认配置分析:
     *      1) 缓存有, 则不调用
     *      2) key自动生成, 缓存的名字::SimpleKey [] 自动生成的key值
     *      3) value的值, 默认使用的是jdk序列化机制, 将序列化后的值, 存入redis
     *      4) 默认的 ttl, 过期时间: 为 -1, 就是没有过期时间
     *
     *  自定义配置:
     *      1) 指定缓存使用的 key: key属性指定, 接受一个SpEL -- "'userList'", 必须加上单引号 ''
     *          key = "#root.method.name"  也可, 获取当前方法名, 作为 key
     *      2) 指定缓存的数据的缓存时间: 配置文件中指定, spring.cache.redis.time-to-live= ms
     *         可以选择配置:
     *         spring.cache.redis.key-prefix= 指定前缀
     *         spring.cache.redis.use-key-prefix= 开启使用前缀
     *         spring.cache.redis.cache-null-value= true 缓存控制, 放在缓存穿透
     *      3) 将数据保存为 json 格式
     *      4)
     *
     *     原理:
     *      CacheAutoConfiguration --> RedisCacheConfiguration -->
     *      自动配置了: RedisCacheManager --> 初始化所有的缓存 -->
     *      每个缓存决定使用什么配置 --> 如果 RedisCacheConfiguration有就用已有的,
     *      没有就用默认配置 --> 要自定义缓存的配置, 只需要给容器中放一个 RedisConfiguration 即可, -->
     *      就会应用到当前 RedisCacheManager管理的所有缓存分区中
     *      自定义: MyCacheConfig , 参考 defaultCacheConfig
     *
     *   缓存一致性:
     *   双写模式: @CachePut, 本地不使用fail方法
     *   失效模式:  -- @CacheEvict, 删除缓存的使用, 数据库更新则删除缓存
     *       一张表的数据改变, 可以需要更新多张表的数据, 即是有可能多个方法
     *       则可以使用:   @Caching(evict = {
     *                        @CacheEvict(value = "user", key = "'selectUserInfo'"),
     *                        @CacheEvict(value = "user", key = "'selectUserSource'")
     *                   })
     *       也可以使用:   @CacheEvict(value = "user"), 将整个命名空间删除
     *
     *   spring-cache 的不足:
     *      读模式:
     *        1) 缓存击穿, 查询一个 null数据, 解决: 缓存 null值
     *        2) 缓存穿透, 大并发进来同时查询一个正好获取的数据, 解决: 加锁 ?, spring-cache是不枷锁的
     *                    当 @Cacheable(sync = true), 加上的是本地锁, 不是分布式锁
     *        3) 缓存雪崩, 大量的 key同时过期, 解决: 加随机值, 加上过期时间
     *
     *      写模式: (缓存与数据库一致)
     *        1) 读写加锁
     *        2) 引入 Canal, 感知到 mysql的更新去更新数据库
     *        3) 读多写多, 直接去数据库查询就行
     *
     *    总结:
     *      常规数据: (读多写少, 即时性, 一致性要求不高的数据), 完全可以使用 spring-cache
     *               写模式: 只要缓存的数据有过期时间, 就足够了
     *      特殊数据: 特殊处理
     */


    /**
     * 查询数据, 并存入缓存
     */

    @Cacheable(value = "userInfo", key = "#root.method.name", sync = true) // userInfo为缓存分区, sync加上本地锁
    public List<User> selectUserInfo(){

        List<User> users = cacheMapper.selectAllUserInfo();
        return users;
    }

    /**
     * 该处使用, @CacheEvict, 代表, 数据库更新则清除缓存
     */
    @CacheEvict(value = "userInfo", key = "'selectUserInfo'")
    public boolean updateUserInfo(String userName){
        Integer ret = cacheMapper.updateUserInfo(userName);
        return ret >0 ? true:false;
    }
}
