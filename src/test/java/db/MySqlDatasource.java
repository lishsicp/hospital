package db;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MySqlDatasource {
    private static final BasicDataSource ds = new BasicDataSource();

    private static final ResourceBundle rs = ResourceBundle.getBundle("db");

    static {
        ds.setUrl(rs.getString("url"));
        ds.setUsername(rs.getString("username"));
        ds.setPassword(rs.getString("password"));
    }

    public static DataSource getDataSource() throws SQLException {
        return ds;
    }
}
