package conn;

import ch.qos.logback.classic.db.SQLBuilder;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by fishiung on 2017-11-24
 */
public class JDBCConn {

    private static final Logger logger = Logger.getLogger(JDBCConn.class);
    private static final JDBCConn instance = new JDBCConn();

    private JDBCConn() {
    }

    public static JDBCConn conn() {
        return instance;
    }

    public Connection MYSQL(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


}














