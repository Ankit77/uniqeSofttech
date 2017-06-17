package com.app.uniqesofttech.database;

public class DBUtils {

    /**
     * Integer Constants
     */
    public static final int DATABASE_VERSION = 3;

    /**
     * Database Name
     */
    public static final String DATABASE_NAME = "deltrack.db";

    /**
     * Tables Constant Names
     */
    public static final String CUSTOMER_TABLE = "customer";
    public static final String PAYMENT_TABLE = "payment";


//


    /**
     * Columns Names
     */
    // CUS
    public static final String COLUMN_CUSTOMER_ID = "cusid";
    public static final String COLUMN_CUSTOMER_NAME = "cusname";
    public static final String COLUMN_CUSTOMER_ADDRESS = "address";

    public static final String COLUMN_PAYMENT_ID = "payid";
    public static final String COLUMN_PAYMENT_MODE = "paymentmode";


    //    /**

    //SMS TABLE
    static final String DB_CREATE_CUS_TABLE = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE + " (" + COLUMN_CUSTOMER_ID
            + " TEXT, " + COLUMN_CUSTOMER_NAME + " TEXT," + COLUMN_CUSTOMER_ADDRESS + " TEXT)";

    static final String DB_CREATE_PAYMENT_TABLE = "CREATE TABLE IF NOT EXISTS " + PAYMENT_TABLE + " (" + COLUMN_PAYMENT_ID
            + " TEXT, " + COLUMN_PAYMENT_MODE + " TEXT)";


}