package org.wso2.db.miragor;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DataSourceConnectionFactory;
import org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS;
import org.apache.commons.dbcp2.datasources.SharedPoolDataSource;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.dc.pr.PRError;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Migrator {

    private static ConPool conPool;
    private static DataSource dataSource = null;
    private static int recordsPerConnection = 0;
    /**
     *
     * Get the count
     * Connection pool : 50
     * Chunk per connection: count/50
     * for loop 50 times.
     * read 130000 records.
     * get the final created time.
     * create a thread.
     * Create 50 threads
     *
     * */

    public static int getREcordsPerThread(Connection connection) throws SQLException {
//        Connection connection = dataSource.getConnection();
        double totalCount = 0.0;
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT count(*) from "
                + "idn_oauth2_access_token");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            totalCount = resultSet.getDouble("count");
        }
        resultSet.close();
        connection.close();
        return  (int)Math.ceil(totalCount/100); //6000 * 1000
    }

    public static void main(String[] args) {
        try {
            long start = System.currentTimeMillis();
            conPool = new ConPool();
            Connection connection = conPool.getConnection();
            int recordsPerThread = getREcordsPerThread(connection);
            conPool.releaseConnection(connection);
            ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(10);

            for (int i = 0; i<100; i++) {
                Thread t = new Thread(new Worker(i, recordsPerThread, conPool));
                executor.schedule(t, i*1000, TimeUnit.MILLISECONDS);
            }
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.HOURS);
            long end = System.currentTimeMillis();
            System.out.println((end - start));
            //System.out.println(Constants.recordMap);
            conPool.cleanUp();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
