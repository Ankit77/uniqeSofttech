package com.app.uniqesofttech.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;

import java.util.ArrayList;

import app.sosdemo.model.FileModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private Context context;
    private SQLiteDatabase database;

    //
//    /**
//     * Constructor
//     * *
//     */
    public DatabaseHelper(Context context) {
        super(context, DBUtils.DATABASE_NAME, null, DBUtils.DATABASE_VERSION);
        this.context = context;
    }

    //
//    /**
//     * Create database tables if it does not exists.
//     * *
//     */
//    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(DBUtils.DB_CREATE_CUS_TABLE);
        db.execSQL(DBUtils.DB_CREATE_PAYMENT_TABLE);
        this.database = db;
    }

    //
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //
//    /**
//     * Open Databases
//     * *
//     */
    public void openDataBase() throws SQLException {
        database = this.getWritableDatabase();
    }

    //
    @Override
    public synchronized void close() {
        // if (database != null && database.isOpen())
        // database.close();
        // super.close();
    }

    //--------------------------------SMS START------------------------------------

    /**
     * Insert SMS
     *
     * @param
     */

    public void insertCustomer(ArrayList<CustomerModel> list) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                CustomerModel smsModel = list.get(i);
                values.put(DBUtils.COLUMN_CUSTOMER_ID, smsModel.getCusid());
                values.put(DBUtils.COLUMN_CUSTOMER_NAME, smsModel.getName());
                values.put(DBUtils.COLUMN_CUSTOMER_ADDRESS, smsModel.getAddress());
                database.insert(DBUtils.CUSTOMER_TABLE, null, values);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            database.endTransaction();
            close();
            SQLiteDatabase.releaseMemory();
        }
    }

    public void insertPayment(ArrayList<PaymentModel> list) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.beginTransaction();
            for (int i = 0; i < list.size(); i++) {
                ContentValues values = new ContentValues();
                PaymentModel smsModel = list.get(i);
                values.put(DBUtils.COLUMN_PAYMENT_ID, smsModel.getPaymentId());
                values.put(DBUtils.COLUMN_PAYMENT_MODE, smsModel.getPaymentmode());
                database.insert(DBUtils.PAYMENT_TABLE, null, values);
            }
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            database.endTransaction();
            close();
            SQLiteDatabase.releaseMemory();
        }
    }


    /**
     * get SMS All SMSList
     *
     * @param
     * @return
     */
    public CustomerModel getCustomer(String cusID) {
        CustomerModel customerModel = null;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.CUSTOMER_TABLE + " where " + DBUtils.COLUMN_CUSTOMER_ID + "=" + cusID;
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    customerModel = new CustomerModel();
                    customerModel.setCusid(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_ID)));
                    customerModel.setName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_NAME)));
                    customerModel.setAddress(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_ADDRESS)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return customerModel;
    }


    public ArrayList<PaymentModel> getPaymentList() {
        ArrayList<PaymentModel> paymentlist = null;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.PAYMENT_TABLE;
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                PaymentModel paymentModel = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    paymentModel = new PaymentModel();
                    paymentModel.setPaymentId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_PAYMENT_ID)));
                    paymentModel.setPaymentmode(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_PAYMENT_MODE)));
                    cursor.moveToNext();
                    paymentlist.add(paymentModel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            close();
            if (cursor != null) {
                cursor.close();
                SQLiteDatabase.releaseMemory();
            }
        }
        return paymentlist;
    }


    /**
     * Delete SMS
     */


    /**
     * Delete SMS by id
     */

    public void deleteSMS(String cusid) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.CUSTOMER_TABLE, DBUtils.COLUMN_CUSTOMER_ID + "=" + cusid, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void deletePaymentMode() {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.PAYMENT_TABLE, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void deleteAllCustomerData() {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.CUSTOMER_TABLE, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    //--------------------------------SMS END------------------------------------

}