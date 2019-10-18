package com.hy.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BaseEntity implements Serializable {
    @JsonIgnore
    int delFlag;

    public boolean hasDeleted() {
        return delFlag == 1;
    }
}
