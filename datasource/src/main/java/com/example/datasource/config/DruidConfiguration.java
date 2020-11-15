package com.example.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.datasource.entity.DruidBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 *  数据源配置信息
 */
@Configuration
public class DruidConfiguration {

    private final static Logger log = LoggerFactory.getLogger(DruidConfiguration.class);
    @Primary
    @Bean(name = "dataSource")
    public DataSource configureDataSource(DruidBean bean) {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(bean.getUrl());
        ds.setUsername(bean.getUsername());
        ds.setPassword(bean.getPassword());
        ds.setInitialSize(bean.getInitialSize());
        ds.setMinIdle(bean.getMinIdle());
        ds.setMaxActive(bean.getMaxActive());
        ds.setMaxWait(bean.getMaxWait());
        ds.setTimeBetweenEvictionRunsMillis(bean.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(bean.getMinEvictableIdleTimeMillis());
        ds.setValidationQuery(bean.getValidationQuery());
        ds.setTestWhileIdle(bean.isTestWhileIdle());
        ds.setTestOnBorrow(bean.isTestOnBorrow());
        ds.setTestOnReturn(bean.isTestOnReturn());
        ds.setPoolPreparedStatements(false);
        try {
            ds.setFilters(bean.getFilters());
        }catch (SQLException e){
            log.error("datasource Initialization produce error..");
        }
        ds.setConnectionProperties("druid.stat.slowSqlMillis=3000");
        ds.setRemoveAbandoned(true);
        ds.setRemoveAbandonedTimeout(1800);
        ds.setLogAbandoned(true);

        System.out.println(bean.getDriver());
        System.out.println(bean.getUsername());
        System.out.println(bean.getUrl());
        System.out.println(bean.getInitialSize());
        System.out.println(bean.getMinIdle());
        System.out.println(bean.getMaxActive());
        System.out.println(bean.getMaxWait());

        return ds;
    }

}
