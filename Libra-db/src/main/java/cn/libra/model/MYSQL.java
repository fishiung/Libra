package cn.libra.model;

/**
 * Created by fishiung on 2017-11-27
 */
public interface MYSQL {
//    tinyint(m)	1个字节  范围(-128~127)
//    smallint(m)	2个字节  范围(-32768~32767)
//    mediumint(m)	3个字节  范围(-8388608~8388607)
//    int(m)	4个字节  范围(-2147483648~2147483647)
//    bigint(m)	8个字节  范围(+-9.22*10的18次方)
    final String TINYINT = "tinyint";
    final String SMALLINT = "smallint";
    final String MEDIUMINT = "mediumint";
    final String INT = "int";
    final String BIGINT = "bigint";
    final String FLOAT = "float";
    final String DOUBLE = "double";

//    char(n)	固定长度，最多255个字符
//    varchar(n)	固定长度，最多65535个字符
//    tinytext	可变长度，最多255个字符
//    text	可变长度，最多65535个字符
//    mediumtext	可变长度，最多2的24次方-1个字符
//    longtext	可变长度，最多2的32次方-1个字符


    final String CHAR = "char";
    final String VARCHAR = "varchar";
    final String TINYTEXT = "tinytext";
    final String TEXT = "text";
    final String MEDIUMTEXT = "mediumtext";
    final String LONGTEXT = "longtext";

//    date	日期 '2008-12-2'
//    time	时间 '12:25:36'
//    datetime	日期时间 '2008-12-2 22:06:44'
//    timestamp	自动存储记录修改时间

    final String DATE = "date";
    final String TIME = "time";
    final String DATETIME = "datetime";
    final String TIMESTAMP = "timestamp";
}
