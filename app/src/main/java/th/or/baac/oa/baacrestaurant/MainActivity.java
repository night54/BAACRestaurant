package th.or.baac.oa.baacrestaurant;

import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        //Test add data
//        testerAdd();

        //Delete all data
        deleteAllSQLite();

        synJSONtoSQLite();


    } // End of Main Method

    private void synJSONtoSQLite() {

        //Change Policy
        StrictMode.ThreadPolicy myThreadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myThreadPolicy);

        int intTimes = 0; // check number of tables
        while (intTimes <= 1) {

            //Create InputStream
            InputStream objInputStream = null;
            String strJson = null;
            String strUserUrl = "http://swiftcodingthai.com/baac/php_get_data_master.php";
            String strFoodUrl = "http://swiftcodingthai.com/baac/php_get_food.php";
            HttpPost objHttpPost;

            try {
                HttpClient objHttpClient = new DefaultHttpClient();

                switch (intTimes) {
                    case 0:
                        objHttpPost = new HttpPost(strUserUrl);
                        break;
                    case 1:
                        objHttpPost = new HttpPost(strFoodUrl);
                        break;
                    default:
                        objHttpPost = new HttpPost(strUserUrl);
                        break;
                } // End of switch

                HttpResponse objHttpResponse = objHttpClient.execute(objHttpPost);
                HttpEntity objHttpEntity = objHttpResponse.getEntity();
                objInputStream = objHttpEntity.getContent();

            } catch (Exception e) {

                Log.d("baac", "InputStream ==>" + e.toString());

            }

            //Create JSON String
            try {

                BufferedReader objBufferedReader = new BufferedReader(new InputStreamReader(objInputStream, "UTF-8"));
                StringBuilder objStringBuilder = new StringBuilder();
                String strLine = null;

                while ((strLine = objBufferedReader.readLine()) != null) {

                    objStringBuilder.append(strLine);

                } // End of While

                objInputStream.close();
                strJson = objStringBuilder.toString();

            } catch (Exception e) {

                Log.d("baac", "strJSON ==>" + e.toString());

            }

            //Update SQLite
            try {

                JSONArray objJsonArray = new JSONArray(strJson);

                for (int index = 0; index < objJsonArray.length(); index++) {

                    JSONObject jsonObject = objJsonArray.getJSONObject(index);

                    switch (intTimes) {
                        case 0:
                            String strUser = jsonObject.getString("User");
                            String strPassword = jsonObject.getString("Password");
                            String strName = jsonObject.getString("Name");
                            objUserTable.addNewUser(strUser, strPassword, strName);
                            break;
                        case 1:
                            String strFood = jsonObject.getString("Food");
                            String strSource = jsonObject.getString("Source");
                            String strPrice = jsonObject.getString("Price");
                            objFoodTable.addNewFood(strFood, strSource, strPrice);
                            break;
                    }

                } // End of for

            } catch (Exception e) {

                Log.d("baac", "Update ==>" + e.toString());

            }

            intTimes++;

        } // End of While

    } // End of SynJSONtoSQLite

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