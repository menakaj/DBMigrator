package org.wso2.db.miragor;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {

    int iteration;
    int count;
    ConPool conPool;
    CountDownLatch latch;
    static int recordCount = 0;
    public Worker(int iteration, int count, ConPool pool) {
        this.iteration = iteration;
        this.count = count;
        this.conPool = pool;
    }


    /**
     *
     * + "TOKEN_ID,"
     *             + "ACCESS_TOKEN,"
     *             + "REFRESH_TOKEN, "
     *             + "CONSUMER_KEY_ID,"
     *             + "AUTHZ_USER,"
     *             + "TENANT_ID,"
     *             + "USER_DOMAIN,"
     *             + "USER_TYPE,"
     *             + "GRANT_TYPE,"
     *             + "TIME_CREATED, "
     *             + "REFRESH_TOKEN_TIME_CREATED,"
     *             + "VALIDITY_PERIOD,"
     *             + "REFRESH_TOKEN_VALIDITY_PERIOD,"
     *             + "TOKEN_SCOPE_HASH, "
     *             + "TOKEN_STATE,"
     *             + "TOKEN_STATE_ID,"
     *             + "SUBJECT_IDENTIFIER,"
     *             + "ACCESS_TOKEN_HASH,"
     *             + "REFRESH_TOKEN_HASH,"
     *             + "IDP_ID, "
     *             + "TOKEN_BINDING_REF
     * */

    public void run() {
        System.out.println("Executing : " + iteration + ", Current time : " + new Date().toString());
        long start = System.currentTimeMillis();

        try {
            Connection connection = conPool.getConnection();
            if (connection.isClosed()) {
                connection = conPool.getConnection();
            }
            PreparedStatement statement1 = connection.prepareStatement("insert into temp_idn_access_token (SELECT * from "
                    + "idn_oauth2_access_token order by "
                    + "TIME_CREATED limit ? offset ?) on conflict do nothing");
            statement1.setInt(1, count);
            statement1.setInt(2, count * iteration);
            System.out.println("TID: " + iteration+ " | Select " + count + " with offset " + iteration * count);
//            long startSelect = System.currentTimeMillis();
            // System.out.println("TID " + iteration + " catalog " + statement1.getConnection().getCatalog());
            int result = statement1.executeUpdate();
//            System.out.println("TID " + iteration + " Result " + result);
//            //conPool.releaseConnection(connection);
//            //Connection connection2 = conPool.getConnection();
//            System.out.println("TID: " + iteration+ " | Time for select = " + (System.currentTimeMillis() - startSelect));
//            PreparedStatement statement = connection.prepareStatement(Constants.Query);
//            long startQueryCreation = System.currentTimeMillis();
//            while (resultSet.next()) {
//                statement.setString(1, resultSet.getString("TOKEN_ID"));
//                statement.setString(2, resultSet.getString("ACCESS_TOKEN"));
//                statement.setString(3, resultSet.getString("REFRESH_TOKEN"));
//                statement.setInt(4, resultSet.getInt("CONSUMER_KEY_ID"));
//                statement.setString(5, resultSet.getString("AUTHZ_USER"));
//                statement.setInt(6, resultSet.getInt("TENANT_ID"));
//                statement.setString(7, resultSet.getString("USER_DOMAIN"));
//                statement.setString(8, resultSet.getString("USER_TYPE"));
//                statement.setString(9, resultSet.getString("GRANT_TYPE"));
//                statement.setTimestamp(10, resultSet.getTimestamp("TIME_CREATED"));
//                statement.setTimestamp(11, resultSet.getTimestamp("REFRESH_TOKEN_TIME_CREATED"));
//                statement.setLong(12, resultSet.getLong("VALIDITY_PERIOD"));
//                statement.setLong(13, resultSet.getLong("REFRESH_TOKEN_VALIDITY_PERIOD"));
//                statement.setString(14, resultSet.getString("TOKEN_SCOPE_HASH"));
//                statement.setString(15, resultSet.getString("TOKEN_STATE"));
//                statement.setString(16, resultSet.getString("TOKEN_STATE_ID"));
//                statement.setString(17, resultSet.getString("SUBJECT_IDENTIFIER"));
//                statement.setString(18, resultSet.getString("ACCESS_TOKEN_HASH"));
//                statement.setString(19, resultSet.getString("REFRESH_TOKEN_HASH"));
//                statement.setInt(20, resultSet.getInt("IDP_ID"));
//                statement.setString(21, resultSet.getString("TOKEN_BINDING_REF"));
//                statement.addBatch();
//            }
//            System.out.println("TID: " + iteration+ " | For query creation = " + (System.currentTimeMillis() - startQueryCreation));
//            long startInsert = System.currentTimeMillis();
//            int[] result = statement.executeBatch();
//            Constants.recordMap.put(iteration, result.length);
            connection.commit();
//            System.out.println("TID: " + iteration+ " | Time for insert : " + (System.currentTimeMillis() - startInsert));
//            statement.close();
//            resultSet.close();
//            connection2.close();
//            connection.close();
            conPool.releaseConnection(connection);
            System.out.println("========== TID: " + iteration+ " | Total time for thread : " + iteration + " : " + (System.currentTimeMillis() - start));
            //System.out.println(Constants.recordMap);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
