package com.example.transactional.service;

import com.example.transactional.entity.SCEntity;
import com.example.transactional.entity.Student;
import com.example.transactional.exception.UpdateException;
import com.example.transactional.mapper.TransactionlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *  本地事务验证类:
 *      方法1: saveStu() 开启事务
 *      方法2: saveSC  开启事务
 *      模拟抛出异常: 在 sql中, 将参数匹配不上
 *
 *      测试 -1:
 *          在 saveStu() 方法中调用 saveSC()
 *          结果: saveSC() 抛出异常, 则 mapper.saveStu(stus), 也会失败, 数据库两张表都未更新
 *               数据库事务一致, 数据更新一致
 *
 *      测试 -2:
 *          在 saveStu() 方法中直接调用 mapper.saveSC(scs)
 *          结果: saveSC() 抛出异常, 则 mapper.saveStu(stus), 也会失败, 数据库两张表都未更新
 *               数据库事务一致, 数据更新一致
 *
 *      测试 -3:
 *          在 测试 -2: 的代码上, 去掉 @Transactional, 即不开启事务
 *          结果: saveSC() 抛出异常, 则 mapper.saveStu(stus) 已经更新, 数据不能达到一致
 *
 *      测试 -4:
 *          在实际业务中, 可以通过抛出异常的形式, 达到事务一致性
 *          如本例子: 在分数表中 抛出异常, 则学生表也会回滚
 */

@Service
public class TransactionlService {

    @Autowired
    TransactionlMapper mapper;

    @Transactional
    public void saveStu () throws UpdateException {
        Student lisi = new Student(7, "lisi", new Date(), "男");
        Student wawu = new Student(8, "wawu", new Date(), "女");
        List<Student> stus = Arrays.asList(lisi, wawu);

        // 测试 -1:
        int end = mapper.saveStus(stus);
        // saveSC();

        // 测试 -2:
//        SCEntity s1 = new SCEntity(7, 02, 77.0);
//        SCEntity s2 = new SCEntity(8, 03, 99.0);
//        List<SCEntity> scs = Arrays.asList(s1, s2);
//        int ret = mapper.saveSC(scs);

        // 测试 -4:
        SCEntity s1 = new SCEntity(7, 02, 77.0);
        SCEntity s2 = new SCEntity(8, 03, 99.0);
        List<SCEntity> scs = Arrays.asList(s1, s2);
        if (end == stus.size()) {
            int ret = mapper.saveSC(scs);
            if (ret != scs.size()) {
                throw new UpdateException("分数表更新失败, 整个更新已经回滚, 即学生表也没有更新");
            }
        }

    }

    @Transactional
    public void saveSC() {
        SCEntity s1 = new SCEntity(7, 02, 77.0);
        SCEntity s2 = new SCEntity(8, 03, 99.0);
        List<SCEntity> scs = Arrays.asList(s1, s2);

        int ent = mapper.saveSC(scs);

    }
}
