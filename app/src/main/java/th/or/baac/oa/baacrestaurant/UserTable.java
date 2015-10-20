package th.or.baac.oa.baacrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by BAAC on 10/19/2015.
 */
public class UserTable {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String TABLE_USER = "userTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER = "User";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_NAME = "Name";
    //End of Explicit

    public UserTable(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = objMyOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMyOpenHelper.getReadableDatabase();

    } // End of Constructor

    public long addNewUser(String strUser, String strPassword, String strName) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_USER, strUser);
        objContentValues.put(COLUMN_PASSWORD, strPassword);
        objContentValues.put(COLUMN_NAME, strName);

        return writeSqLiteDatabase.insert(TABLE_USER, null, objContentValues);
    }

//    public int updateUser(int strId, String strUser, String strPassword, String strName) {
//
//        ContentValues objContentValues = new ContentValues();
//        objContentValues.put(COLUMN_ID, strId);
//        objContentValues.put(COLUMN_USER, strUser);
//        objContentValues.put(COLUMN_PASSWORD, strPassword);
//        objContentValues.put(COLUMN_NAME, strName);
//
//        return writeSqLiteDatabase.update(USER_TABLE, objContentValues, );
//    }

//    public int deleteUser() {
//
//    }

} // End of Main Class
