package th.or.baac.oa.baacrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public String[] searchUser(String strUser) {
        try {
            String[] strResult = null;
            Cursor objCursor = readSqLiteDatabase.query(
                    TABLE_USER,
                    new String[]{COLUMN_ID, COLUMN_USER, COLUMN_PASSWORD, COLUMN_NAME},
                    COLUMN_USER + "=?",
                    new String[]{String.valueOf(strUser)},
                    null,
                    null,
                    null,
                    null
            );
            if (objCursor != null) {
                if (objCursor.moveToFirst()) {
                    strResult = new String[objCursor.getColumnCount()];
                    strResult[0] = objCursor.getString(0);
                    strResult[1] = objCursor.getString(1);
                    strResult[2] = objCursor.getString(2);
                    strResult[3] = objCursor.getString(3);
                } // End of if2
            } // End of if1
            objCursor.close();
            return strResult;
        } catch (Exception e) {
            return null;
        }
    }

    public long addNewUser(String strUser, String strPassword, String strName) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_USER, strUser);
        objContentValues.put(COLUMN_PASSWORD, strPassword);
        objContentValues.put(COLUMN_NAME, strName);

        return writeSqLiteDatabase.insert(TABLE_USER, null, objContentValues);
    }

} // End of Main Class
