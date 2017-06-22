package com.app.uniqesofttech.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.uniqesofttech.model.CustomerModel;
import com.app.uniqesofttech.model.PaymentModel;
import com.app.uniqesofttech.model.SyncModel;
import com.app.uniqesofttech.util.WriteLog;

import java.util.ArrayList;


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
        db.execSQL(DBUtils.DB_CREATE_SYNC_TABLE);
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
                values.put(DBUtils.COLUMN_CUSTOMER_CASHMEMONO, smsModel.getCashMemoNo());
                long val = database.insert(DBUtils.CUSTOMER_TABLE, null, values);
                WriteLog.E("VAL", "" + val);
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
                long val = database.insert(DBUtils.PAYMENT_TABLE, null, values);
                WriteLog.E("VAL1", "" + val);
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

    public long insertSync(SyncModel syncModel) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            // database.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(DBUtils.COLUMN_SYNC_CASHMEMO, syncModel.getCashmemono());
            values.put(DBUtils.COLUMN_SYNC_DEALERCODE, syncModel.getDealercode());
            values.put(DBUtils.COLUMN_SYNC_AMOUNT, syncModel.getAmount());
            values.put(DBUtils.COLUMN_SYNC_PAYMENTMODE, syncModel.getPaymentmode());
            long i = database.insert(DBUtils.SYNC_TABLE, null, values);
            return i;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;

        } finally {
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
    public CustomerModel getCustomer(String cusmemono) {
        CustomerModel customerModel = null;
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.CUSTOMER_TABLE + " where " + DBUtils.COLUMN_CUSTOMER_CASHMEMONO + "=" + cusmemono;
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getCount(); i++) {
                    customerModel = new CustomerModel();
                    customerModel.setCusid(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_ID)));
                    customerModel.setName(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_NAME)));
                    customerModel.setAddress(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_ADDRESS)));
                    customerModel.setCashMemoNo(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_CUSTOMER_CASHMEMONO)));
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
        ArrayList<PaymentModel> paymentlist = new ArrayList<>();
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


    public ArrayList<SyncModel> getSyncList() {
        ArrayList<SyncModel> syncList = new ArrayList<>();
        if (!database.isOpen()) {
            openDataBase();
        }
        Cursor cursor = null;
        try {
            String query = "Select * from " + DBUtils.SYNC_TABLE;
            cursor = database.rawQuery(query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                SyncModel syncModel = null;
                for (int i = 0; i < cursor.getCount(); i++) {
                    syncModel = new SyncModel();
                    syncModel.setId(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SYNC_ID)));
                    syncModel.setDealercode(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SYNC_DEALERCODE)));
                    syncModel.setAmount(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SYNC_AMOUNT)));
                    syncModel.setPaymentmode(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SYNC_PAYMENTMODE)));
                    syncModel.setCashmemono(cursor.getString(cursor.getColumnIndex(DBUtils.COLUMN_SYNC_CASHMEMO)));
                    cursor.moveToNext();
                    syncList.add(syncModel);
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
        return syncList;
    }


    /**
     * Delete SMS
     */


    /**
     * Delete SMS by id
     */

    public void deleteCustomer(String cashmemono) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.CUSTOMER_TABLE, DBUtils.COLUMN_CUSTOMER_CASHMEMONO + "=" + cashmemono, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }


    public void deleteSyncData(String syncid) {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.SYNC_TABLE, DBUtils.COLUMN_SYNC_ID + "=" + syncid, null);
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

    public void deleteAllSyncData() {
        if (!database.isOpen()) {
            openDataBase();
        }
        try {
            database.delete(DBUtils.SYNC_TABLE, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    //--------------------------------SMS END------------------------------------

}