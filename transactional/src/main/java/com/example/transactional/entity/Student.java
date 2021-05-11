package com.example.transactional.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Student {

    private int id;
    private String name;
    private Date age;
    private String sex;

    public Student(int id, String name, Date age, String sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
}
