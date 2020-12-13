package com.example.validation.entity;

import com.example.validation.validate.AddGroup;
import com.example.validation.validate.ListValue;
import com.example.validation.validate.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotEmpty(groups = AddGroup.class, message = "用户名不能为空")
    private String name;

    @NotEmpty(groups = UpdateGroup.class)
    private Integer userId;

    @NotEmpty(groups = AddGroup.class)
    @URL(groups = {AddGroup.class, UpdateGroup.class})
    private String logo;

    @ListValue(values={1,2,3}, message = "statue, 不正确", groups = {AddGroup.class, UpdateGroup.class})
    private Integer statue;
}
