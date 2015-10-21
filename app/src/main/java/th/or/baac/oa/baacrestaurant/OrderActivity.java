package th.or.baac.oa.baacrestaurant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {

    // Explicit
    private TextView officerTextView;
    private Spinner deskSpinner;
    private ListView foodListView;
    private String strOfficer, strDesk, strFood, strItem;
    // End of Explicit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        bindWidget();

        showOfficer();

        createSpinner();

        createListView();
    }

    private void createListView() {
        FoodTable objFoodTable = new FoodTable(this);
        final String[] foodStrings = objFoodTable.readAllData(0);
        String[] priceStrings = objFoodTable.readAllData(1);
        String[] sourceStrings = objFoodTable.readAllData(2);

        MyFoodAdapter objMyFoodAdapter = new MyFoodAdapter(OrderActivity.this, sourceStrings, foodStrings, priceStrings);
        foodListView.setAdapter(objMyFoodAdapter);

        foodListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strFood = foodStrings[i];
                chooseItem(strFood);
            }
        });
    }

    private void chooseItem(String strFood) {
        CharSequence[] objCharSequences = {"1 Set", "2 Set", "3 Set", "4 Set", "5 Set"};
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setTitle(strFood);
        objBuilder.setSingleChoiceItems(objCharSequences, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                strItem = Integer.toString(i + 1);
                // Upload to mySQL
                uploadToMySQL();
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();
    }

    private void uploadToMySQL() {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(this);
        objBuilder.setTitle("Officer ==> " + strOfficer);
        objBuilder.setMessage("Food =" + strFood + "\n" +
                "Item = " + strItem + "\n" +
                "Desk = " + strDesk);
        objBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        objBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postNewOrder();
                dialogInterface.dismiss();
            }
        });
        objBuilder.show();
    }

    private void postNewOrder() {
        StrictMode.ThreadPolicy myThreadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(myThreadPolicy);

        try {
            ArrayList<NameValuePair> objNameValuePairs = new ArrayList<NameValuePair>();
            objNameValuePairs.add(new BasicNameValuePair("isAdd", "true"));
            objNameValuePairs.add(new BasicNameValuePair("Officer", strOfficer));
            objNameValuePairs.add(new BasicNameValuePair("Desk", strDesk));
            objNameValuePairs.add(new BasicNameValuePair("Food", strFood));
            objNameValuePairs.add(new BasicNameValuePair("Item", strItem));


            HttpClient objHttpClient = new DefaultHttpClient();
            HttpPost objHttpPost=new HttpPost("http://swiftcodingthai.com/baac/php_add_data_restaurant.php");
            objHttpPost.setEntity(new UrlEncodedFormEntity(objNameValuePairs, "UTF-8"));
            objHttpClient.execute(objHttpPost);

            Toast.makeText(OrderActivity.this, "Update order successfull", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(OrderActivity.this, "Cannot update order to mySQL", Toast.LENGTH_LONG).show();
        }
    }

    private void createSpinner() {
        final String[] deskStrings = new String[10];
        deskStrings[0] = "A1";
        deskStrings[1] = "A2";
        deskStrings[2] = "A3";
        deskStrings[3] = "A4";
        deskStrings[4] = "A5";
        deskStrings[5] = "A6";
        deskStrings[6] = "A7";
        deskStrings[7] = "A8";
        deskStrings[8] = "A9";
        deskStrings[9] = "A10";

        ArrayAdapter<String> deskArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deskStrings);
        deskSpinner.setAdapter(deskArrayAdapter);
        // Active when Choose
        deskSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strDesk = deskStrings[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                strDesk = deskStrings[0];
            }
        });
    }

    private void showOfficer() {
        strOfficer = getIntent().getStringExtra("Name");
        officerTextView.setText(strOfficer);
    }

    private void bindWidget() {
        officerTextView = (TextView) findViewById(R.id.txtShowName);
        deskSpinner = (Spinner) findViewById(R.id.deskSpin);
        foodListView = (ListView) findViewById(R.id.foodListView);
    }
}
