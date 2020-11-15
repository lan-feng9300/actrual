package com.example.exception.test.service;

import com.example.test.domain.ErrorResp;
import com.example.test.util.HttpUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class TimeOutService {

    static ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);

    public String getTest () {
       Future<String> future = executorService.submit(new Callable<String>() {

            @Override
            public String call() {
                try {
                    //TimeUnit.SECONDS.sleep(5);
                    String ret = HttpUtil.httpGet("http://192.168.101.15:8088/getTest");
                    return new ErrorResp(200,"请求成功").toString();
                } catch (InterruptedException e) {
                    return "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return  "OK";
            }
        });
        try {
            String result = future.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException |ExecutionException | TimeoutException e) {
            future.cancel(true);
            System.out.println("任务超时。");
        }finally {
            System.out.println("清理资源。");
        }*/

    }

}
