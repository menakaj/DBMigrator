package org.wso2.db.miragor;

import org.postgresql.ds.PGConnectionPoolDataSource;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ConPool {

    private List<Connection> created;
    private List<Connection> used = new ArrayList<Connection>();
    private int initCount = 10;

    public ConPool() throws SQLException {
        created = new ArrayList<Connection>(initCount);
        for (int i = 0; i < initCount; i++) {
            Connection connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASS);
            if (connection != null) {
                System.out.println("Connection created " + i);
                connection.setAutoCommit(false);
//                connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                created.add(connection);
            }
            //            System.out.println("Connection created");
        }
    }

    public Connection getConnection() {
        Connection connection = created.remove(created.size() - 1);
        used.add(connection);
        return connection;
    }

    public void releaseConnection(Connection connection) {
        created.add(connection);
        used.remove(connection);
    }

    public void cleanUp() throws SQLException {
        for (Connection c : created) {
//            System.out.println("Connection closed....");
            c.close();
        }
    }
}
