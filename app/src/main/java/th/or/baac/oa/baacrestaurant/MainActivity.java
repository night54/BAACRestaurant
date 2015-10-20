package th.or.baac.oa.baacrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private EditText userEditText, passwordEditText;
    private String userString, passwordString;
    //End of Explicit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        bindWidget();

        //Create and Connect Database
        createAndConnected();

        //Test add data
//        testerAdd();

        //Delete all data
        deleteAllSQLite();

        //sync JSON to SQLite
        synJSONtoSQLite();

    } // End of Main Method

    private void bindWidget() {
        userEditText = (EditText) findViewById(R.id.editText);
        passwordEditText = (EditText) findViewById(R.id.editText2);
    }


    public void clickLogin(View view) {
        userString = userEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        if (userString.equals("") || passwordString.equals("")) {
            //Have space
            errorDialog("Have space", "Please fill all every blank");
        } else {
            //No space
            checkUser();
        }
    } // End of clickLogin

    private void checkUser() {
        try {
            String[] strMyResult = objUserTable.searchUser(userString);

            if (passwordString.equals(strMyResult[2])) {
                Toast.makeText(MainActivity.this, "Welcome " + strMyResult[3], Toast.LENGTH_LONG).show();
            } else {
                errorDialog("Password false", "Please try again. Password false");
            }
        } catch (Exception e) {
            errorDialog("No this user", "No " + userString + " on my Database");
        }
    }

    private void errorDialog(String strTitle, String strMessage) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setIcon(R.drawable.danger);
        objBuilder.setTitle(strTitle);
        objBuilder.setMessage(strMessage);
        objBuilder.setCancelable(false);
        objBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();

    }

    private void synJSONtoSQLite() {

        //Change Policy
        StrictMode.ThreadPolicy myThreadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myThreadPolicy);

        int intTimes = 0;
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
                            objUserTable.addNewUser(strFood, strSource, strPrice);
                            break;
                        default:
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