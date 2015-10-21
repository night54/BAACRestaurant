package th.or.baac.oa.baacrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by BAAC on 10/19/2015.
 */
public class FoodTable {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String TABLE_FOOD = "foodTable";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_FOOD = "Food";
    public static final String COLUMN_SOURCE = "Source";
    public static final String COLUMN_PRICE = "Price";
    //End of Explicit

    public FoodTable(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeSqLiteDatabase = objMyOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMyOpenHelper.getReadableDatabase();

    } // End of Constructor

    public String[] readAllData(int intType) {
        String[] strResult = null;
        Cursor objCursor = readSqLiteDatabase.query(
                TABLE_FOOD,
                new String[]{COLUMN_ID, COLUMN_FOOD, COLUMN_PRICE, COLUMN_SOURCE},
                null,
                null,
                null,
                null,
                null
        );
        Log.d("baac", "objCursor count = " + String.valueOf(objCursor.getCount()));
        if (objCursor != null) {
            objCursor.moveToFirst();
            strResult = new String[objCursor.getCount()];
            for (int index = 0; index < objCursor.getCount(); index++) {
                switch (intType) {
                    case 0:
                        strResult[index] = objCursor.getString(objCursor.getColumnIndex(COLUMN_FOOD));
                        break;
                    case 1:
                        strResult[index] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PRICE));
                        break;
                    case 2:
                        strResult[index] = objCursor.getString(objCursor.getColumnIndex(COLUMN_SOURCE));
                        break;
                    default:
                        // strResult[index] = objCursor.getString(objCursor.getColumnIndex(COLUMN_ID));
                        break;
                } // End of switch
                objCursor.moveToNext();
            } // End of for
        } // End of if
        objCursor.close();

        return strResult;
    }

    public long addNewFood(String strFood, String strSource, String strPrice) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_SOURCE, strSource);
        objContentValues.put(COLUMN_PRICE, strPrice);

        return writeSqLiteDatabase.insert(TABLE_FOOD, null, objContentValues);
    }

} // End of Main Class
