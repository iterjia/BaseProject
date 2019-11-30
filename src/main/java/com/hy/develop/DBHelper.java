package com.hy.develop;

import com.hy.common.tool.Utils;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DBHelper {
    private final static String URL = "jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true&serverTimezone=GMT%2B8";
    private final static String DRIVER = "com.mysql.jdbc.Driver";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";
    private DBProperties dbProperties;
    private Connection connection;
    private final static int[] INT_TYPES = new int[]{Types.INTEGER, Types.BIGINT, Types.SMALLINT, Types.TINYINT};

    public DBHelper() {
//        dbProperties = SpringUtil.getBean(DBProperties.class);
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<FieldInfo> getTableFields(String tableName) {
        List<FieldInfo> fields = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " limit 0, 1");
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int colCount = metaData.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                FieldInfo info = new FieldInfo();
                int colType = metaData.getColumnType(i);
                if (colType == Types.INTEGER || colType == Types.TINYINT
                        || colType == Types.SMALLINT || colType == Types.BIGINT) {
                    info.setPropDataType("int");
                } else {
                    info.setPropDataType("String");
                }
                String colName = metaData.getColumnName(i);
                if (colName.equals("del_flag")) {
                    info.setEntityExclude(true);
                }
                info.setAutoInc(metaData.isAutoIncrement(i));
                info.setColName(colName);
                info.setPropName(Utils.underLine2Camel(info.getColName()));
                log.info("FieldInfo={}", info);
                fields.add(info);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fields;
    }

    public void release() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
//        DBHelper helper = new DBHelper();
//        helper.getTableFields("t_user");
//        helper.release();

        System.out.println(Utils.underLine2Camel("_A_b_c_"));
        System.out.println(Boolean.parseBoolean("true"));
    }
}
