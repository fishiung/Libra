package cn.libra.util;

import cn.libra.model.ES;
import cn.libra.model.MYSQL;

/**
 * Created by fishiung on 2017-11-27
 */
public class TransformType {

    public static String MYSQL2ES(String sqlType) {
        if (sqlType.startsWith(MYSQL.TINYINT) || sqlType.startsWith(MYSQL.BIGINT)
                || sqlType.startsWith(MYSQL.INT) || sqlType.startsWith(MYSQL.SMALLINT)
                || sqlType.startsWith(MYSQL.MEDIUMINT)) return ES.LONG;
        else if (sqlType.startsWith(MYSQL.FLOAT) || sqlType.startsWith(MYSQL.DOUBLE)) return ES.DOUBLE;
        else if (sqlType.startsWith(MYSQL.TIMESTAMP) || sqlType.startsWith(MYSQL.DATETIME)
                || sqlType.startsWith(MYSQL.TIME) || sqlType.startsWith(MYSQL.DATE)) return ES.DATE;
        else if (sqlType.startsWith(MYSQL.MEDIUMTEXT)||sqlType.startsWith(MYSQL.LONGTEXT)
                ||sqlType.startsWith(MYSQL.VARCHAR)||sqlType.startsWith(MYSQL.TINYTEXT)
                ||sqlType.startsWith(MYSQL.CHAR)||sqlType.startsWith(MYSQL.TEXT)) return ES.TEXT;
        else throw new RuntimeException("no suppot format");
    }


}
