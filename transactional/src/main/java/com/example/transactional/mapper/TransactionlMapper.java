package com.example.transactional.mapper;

import com.example.transactional.entity.SCEntity;
import com.example.transactional.entity.Student;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionlMapper {
    int saveStus(List<Student> stus);

    int saveSC(List<SCEntity> scs);

}
