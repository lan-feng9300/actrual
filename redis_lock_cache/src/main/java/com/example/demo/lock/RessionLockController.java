package com.example.demo.lock;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RessionLockController {

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/read")
    @ResponseBody
    public String read(){
        RReadWriteLock readAndWrite = redisson.getReadWriteLock("readAndWrite");
        // 上读锁
        RLock rLock = readAndWrite.readLock();
        String end = null;
        try {
            rLock.lock();
            String json = redisTemplate.opsForValue().get("RWend");
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return end;
    }

    /**
     *  信号锁, 停车例子
     */
    @GetMapping("/write")
    @ResponseBody
    public void write(){
        RReadWriteLock readAndWrite = redisson.getReadWriteLock("readAndWrite");
        RLock wLock = readAndWrite.writeLock();
        try {
            wLock.lock();
            String end = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set("RWend", end);
            Thread.sleep(6000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            wLock.unlock();
        }
    }

    /**
     *  信号锁, 开车走例子
     */
    @GetMapping("/park")
    @ResponseBody
    public String park(){
        RSemaphore park = redisson.getSemaphore("park");
        String end = null;
        try {
            park.acquire();
            end = redisTemplate.opsForValue().get("park");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return end;
    }

    @GetMapping("/go")
    @ResponseBody
    public String go(){
        RSemaphore park = redisson.getSemaphore("park");
        park.release();
        String end = redisTemplate.opsForValue().get("park");
        return end;
    }

    /**
     * 场景: 放假了, 一共 7个学生都回去了, 班长必须最后一个锁门了才能走
     * 闭锁测试
     */
    @GetMapping("/lockDoor")
    @ResponseBody
    public String lockDoor(){
        RCountDownLatch lockDoor = redisson.getCountDownLatch("lockDoor");
        lockDoor.trySetCount(7);
        try {
            lockDoor.await();
            return "锁门成功";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "锁门异常";
        }
    }

    @GetMapping("/gogo/{id}")
    @ResponseBody
    public String gogo(@PathVariable int id){
        RCountDownLatch lockDoor = redisson.getCountDownLatch("lockDoor");
        lockDoor.countDown();
        return id+"同学走了";
    }
}
