package th.or.baac.oa.baacrestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private UserTable objUserTable;
    private FoodTable objFoodTable;
    //End of Explicit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create and Connect Database
        createAndConnected();
//        testerAdd();
        deleteAllSQLite();

    } // End of Main Method

    private void deleteAllSQLite() {

        SQLiteDatabase objSqLiteDatabase = openOrCreateDatabase("BAAC.db", MODE_PRIVATE, null);
        objSqLiteDatabase.delete("userTable", null, null);
        objSqLiteDatabase.delete("foodTable", null, null);

    }

    private void testerAdd() {

        objUserTable.addNewUser("night54", "1234", "Krongrat Chanakok");
        objFoodTable.addNewFood("Noodle", "testSource", "40");

    }

    private void createAndConnected() {

        objUserTable = new UserTable(this);
        objFoodTable = new FoodTable(this);

    } // End of createAndConnected Method;

} // End of Main Class