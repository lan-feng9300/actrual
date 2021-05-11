package com.example.transactional.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SCEntity {

    private Integer sId;
    private Integer cId;
    private Double score;

    public SCEntity(Integer sId, Integer cId, Double score) {
        this.sId = sId;
        this.cId = cId;
        this.score = score;
    }
}
