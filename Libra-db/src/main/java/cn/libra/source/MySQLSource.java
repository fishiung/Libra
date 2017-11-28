package cn.libra.source;

import cn.libra.conn.JDBCConn;
import cn.libra.model.TableDescBean;
import cn.libra.util.TransformType;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by fishiung on 2017-11-24
 */
public class MySQLSource implements Source {
    private String databaseName;
    private String url;
    private String username;
    private String password;
    private volatile boolean INITIALIZED = false;
    private List<String> tables = new ArrayList<>();
    private Map<String, List<String>> tablesDesc = new HashMap<>();

    public MySQLSource(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private void init(String url, String username, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            conn = JDBCConn.conn().MYSQL(url, username, password);
            databaseName = conn.getCatalog();
            String getTableNameSQL = "select table_name from information_schema.tables where table_schema= ? and table_type='base table'";
            String getFieldNameAndTypeSQL = "select column_name,column_type from information_schema.columns where table_schema=? and table_name=?";

            //添加当前database的所有表名
            ps = conn.prepareStatement(getTableNameSQL);
            ps.setString(1, databaseName);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                tables.add(resultSet.getString(1));
            }

            //添加每个表的字段名和字段类型
            ps = conn.prepareStatement(getFieldNameAndTypeSQL);
            for (String tableName : tables) {
                ps.clearParameters();
                ps.setString(1, databaseName);
                ps.setString(2, tableName);
                resultSet = ps.executeQuery();
                List<String> tmp = new ArrayList<>();
                while (resultSet.next()) {
                    tmp.add(resultSet.getString(1).toLowerCase() + "\t" + resultSet.getString(2).toLowerCase());
                }
                tablesDesc.put(tableName, tmp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        INITIALIZED = true;
    }

    public String[] getTables() {
        if (!INITIALIZED) init(url, username, password);
        return tables.toArray(new String[]{});
    }

    @Override
    public String[] getFields(String tableName) {
        if (!INITIALIZED) init(url, username, password);
        List<String> tmp = tablesDesc.get(tableName);
        if (tmp == null || tmp.isEmpty()) return null;
        return (String[]) tmp.stream().map(s -> s.split("\t")[0]).toArray();
    }

    public TableDescBean getTableDesc(String tableName) {
        if (!INITIALIZED) init(url, username, password);
        List<String> tmp = tablesDesc.get(tableName);
        if (tmp == null || tmp.isEmpty()) return null;
        String[] fields = (String[]) tmp.stream().map(s -> s.split("\t")[0]).collect(Collectors.toList()).toArray(new String[]{});
        String[] types = (String[]) tmp.stream().map(s -> s.split("\t")[1]).map(TransformType::MYSQL2ES).collect(Collectors.toList()).toArray(new String[]{});
        return new TableDescBean(tableName, fields, types);
    }

    @Override
    public String[] getType(String tableName) {
        if (!INITIALIZED) init(url, username, password);
        List<String> tmp = tablesDesc.get(tableName);
        if (tmp == null || tmp.isEmpty()) return null;
        return (String[]) tmp.stream().map(s -> s.split("\t")[1]).toArray();
    }

    @Override
    public String type() {
        return "MySQL";
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }


    public static void main(String[] args) throws SQLException {
        MySQLSource source=new MySQLSource("jdbc:mysql://localhost:3306/hippo?useSSL=false", "root", "root");

        String[] all = source.getAll();
        for (String s : all) {
            System.out.println(s);
        }


    }


    public  String[] getAll() throws SQLException{
        ResultSet resultSet = null;
        Connection conn = JDBCConn.conn().MYSQL(url, username, password);
        String sql = "select * from data_import_record";
        PreparedStatement ps = conn.prepareStatement(sql);
        resultSet = ps.executeQuery();
        //{ "price" : 1000, "color" : "红色", "brand" : "长虹", "sold_date" : "2016-10-28" }
        String fields[] = getTableDesc("data_import_record").getFields();
        StringBuilder sb = new StringBuilder();
        int count=53;
        String res[] = new String[count];
        int num =0;
        while (resultSet.next()) {
            for(int i=1;i<fields.length;i++){
                Object value = resultSet.getObject(i);
                try{
                    Timestamp a = (Timestamp)value;
                    value = a.getTime();
                }catch (Exception e){
                }
                sb.append("\""+fields[i-1]+"\":" +"\""+value+"\",");
            }
            res[num++] = "{"+sb.toString().substring(0,sb.length()-1)+"}";
            sb.delete(0,sb.length());
        }
        return res;
    }

}
