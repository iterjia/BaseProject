package com.hy.develop;

import lombok.Data;

@Data
public class FieldInfo {
    /**
     * 是否是自增列
     */
    private boolean autoInc;
    /**
     * 是否需要从entity类中排除
     */
    private boolean entityExclude;
    /**
     * 列名
     */
    private String colName;
    /**
     * 属性名
     */
    private String propName;
    /**
     * 数据类型
     */
    private String propDataType;
}
