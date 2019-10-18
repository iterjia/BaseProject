package com.hy.develop;

import com.hy.common.tool.Utils;
import lombok.Data;

import java.util.List;

@Data
public class ModuleInfo {
    private final static String OUT_ROOT_DIR = "src/main/java/com/hy/modules/";
    private final static String OUT_RES_ROOT_DIR = "src/main/resources/";

    /**
     * 模块名
     */
    private String name;
    /**
     * 首字母大写的驼峰名
     */
    private String capitalName;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表中包含的字段列表
     */
    private List<FieldInfo> tableFields;

    public ModuleInfo(String moduleName) {
        this.name = moduleName;
        this.tableName = "t_" + moduleName;
    }

    public ModuleInfo(String moduleName, String tableName) {
        this.name = moduleName;
        this.tableName = tableName;
    }

    public String getCapitalName() {
        return Utils.firstCharToUpper(name);
    }

    public String getMapperJavaDir() {
        return OUT_ROOT_DIR + name + "/mapper/";
    }
    public String getMapperXmlDir() {
        return OUT_RES_ROOT_DIR + "mapper/";
    }

    public String getEntityDir() {
        return OUT_ROOT_DIR + name + "/entity/";
    }

    public String getControllerDir() {
        return OUT_ROOT_DIR + name + "/controller/";
    }

    public String getServiceDir() {
        return OUT_ROOT_DIR + name + "/service/";
    }

    public String getServiceImplDir() {
        return OUT_ROOT_DIR + name + "/service/impl/";
    }
}
