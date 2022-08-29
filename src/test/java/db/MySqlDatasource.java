package db;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
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

    public static DataSource getDataSource() {
        return ds;
    }

    public static void resetDatabase() throws SQLException, FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(ds.getConnection());
        Reader reader = new BufferedReader(new FileReader("sql/hospital_test.sql"));
        sr.runScript(reader);
    }

}
