package org.wso2.db.miragor;

import java.util.concurrent.ConcurrentHashMap;

public class Constants {

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/am_db?reWriteBatchedInserts=true";
    public static final String DB_USER = "postgres";
    public static final String DB_PASS = "mysecretpassword";
    public static final String Query = "insert into temp_token ("
            + "TOKEN_ID,"
            + "ACCESS_TOKEN,"
            + "REFRESH_TOKEN, "
            + "CONSUMER_KEY_ID,"
            + "AUTHZ_USER,"
            + "TENANT_ID,"
            + "USER_DOMAIN,"
            + "USER_TYPE,"
            + "GRANT_TYPE,"
            + "TIME_CREATED, "
            + "REFRESH_TOKEN_TIME_CREATED,"
            + "VALIDITY_PERIOD,"
            + "REFRESH_TOKEN_VALIDITY_PERIOD,"
            + "TOKEN_SCOPE_HASH, "
            + "TOKEN_STATE,"
            + "TOKEN_STATE_ID,"
            + "SUBJECT_IDENTIFIER,"
            + "ACCESS_TOKEN_HASH,"
            + "REFRESH_TOKEN_HASH,"
            + "IDP_ID, "
            + "TOKEN_BINDING_REF) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) on conflict do nothing;";

    public static ConcurrentHashMap<Integer, Integer> recordMap = new ConcurrentHashMap<Integer, Integer>();
}
