package source;

import conn.JDBCConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public MySQLSource(String url, String username, String password){
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
                tablesDesc.put(tableName,tmp);
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

    public String[] getTables(){
        if (!INITIALIZED) init(url, username, password);
        return tables.toArray(new String[]{});
    }

    @Override
    public String[] getFields(String tableName) {
        if (!INITIALIZED) init(url, username, password);
        List<String> tmp = tablesDesc.get(tableName);
        if(tmp == null || tmp.isEmpty()) return null;
        return (String[])tmp.stream().map(s->s.split("\t")[0]).toArray();
    }

    public List[String[]] getTableDesc(String tableName){
        if (!INITIALIZED) init(url, username, password);
        List<String> tmp = tablesDesc.get(tableName);
        if(tmp == null || tmp.isEmpty()) return null;
        String [] fields = (String[]) tmp.stream().map(s->s.split("\t")[0]).toArray();
        String [] types = (String[]) tmp.stream().map(s->s.split("\t")[1]).toArray();
        return ;
    }

    @Override
    public String[] getType(String tableName) {
        if (!INITIALIZED) init(url, username, password);
        List<String> tmp = tablesDesc.get(tableName);
        if(tmp == null || tmp.isEmpty()) return null;
        return (String[])tmp.stream().map(s->s.split("\t")[1]).toArray();
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



//    public static void main(String[] args) throws SQLException {
//        MySQLSource source = new MySQLSource();
//        source.init("jdbc:mysql://localhost:3306/hippo", "root", "root");
//
//        source.tables.forEach(e -> System.out.println(e));
//        source.tablesDesc.forEach((a, b) -> System.out.println(a + ":" + b));
//    }
}
