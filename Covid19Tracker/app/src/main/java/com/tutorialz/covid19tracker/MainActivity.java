package com.tutorialz.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<CoronaItem> coronaItemsArrayList;
    private RequestQueue requestQueue;
    private TextView dailyDeaths,dailyConfirm,dailyReco,dateHeaders,totalDeath,totalConfirm,totalRecovered;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dailyConfirm=findViewById(R.id.dailyConfirmed);
        dailyDeaths=findViewById(R.id.dailyDeath);
        dailyReco=findViewById(R.id.dailyRecovered);
        dateHeaders=findViewById(R.id.dateHeader);
        totalRecovered=findViewById(R.id.todayRecovered);
        totalConfirm=findViewById(R.id.totalConfirm);
        totalDeath=findViewById(R.id.totalDeath);

        recyclerView=findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coronaItemsArrayList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        jsonParse();

    }

    private void jsonParse() {
        String url="https://api.covid19india.org/raw_data.json";
        final JsonObjectRequest request=new
                JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>(){


            @Override
            public void onResponse(JSONObject response) {
                try {
                    //first fetch header and display it
                    //Header consists total and TODAY'S statistics
                    // for Today's details

                    //statewise hold data of Today's cases at index 0
                    JSONArray todayAndTotalDataArray = response.getJSONArray("statewise");
                    JSONObject todayAndTotalDataJsonObject = todayAndTotalDataArray.getJSONObject(0);

                    String dailyConfirmed = todayAndTotalDataJsonObject.getString("deltaconfirmed");
                    String dailyDeath = todayAndTotalDataJsonObject.getString("deltadeaths");
                    String dailyRec = todayAndTotalDataJsonObject.getString("deltarecovered");
                    String dataHeader = todayAndTotalDataJsonObject.getString("Lastupdatedtime").substring(0, 5);
                    dataHeader = getFormattedDate(dataHeader);
                    //Method implemented Successfully

                    dailyConfirm.setText(dailyConfirmed);
                    dailyReco.setText(dailyRec);
                    dailyDeaths.setText(dailyDeath);
                    dateHeaders.setText(dataHeader);
                    //for total details
                    //todayAndTotalDataArray holds data of all the states
                    //At the index 0 of the todayAndTotalDateArray"total Details" is stored
                    String totalDeathFetched = todayAndTotalDataJsonObject.getString("deaths");
                    String totalRecoveredFetched = todayAndTotalDataJsonObject.getString("recovered");
                    String totalConfirmedFetched = todayAndTotalDataJsonObject.getString("confirmed");

                    totalDeath.setText(totalDeathFetched);
                    totalRecovered.setText(totalRecoveredFetched);
                    totalConfirm.setText(totalConfirmedFetched);

                    //now fetch and display the data for all the states
                    //this data is also present in the todayAndTotalDataArray from index 1
                    for (int i = 1; i < todayAndTotalDataArray.length(); i++) {
                        JSONObject statewiseArrayJSONObject = todayAndTotalDataArray.getJSONObject(i);
                        String active = statewiseArrayJSONObject.getString("active");
                        String death = statewiseArrayJSONObject.getString("death");
                        String recovered = statewiseArrayJSONObject.getString("recovered");
                        String state = statewiseArrayJSONObject.getString("state");
                        String confirmed = statewiseArrayJSONObject.getString("confirmed");
                        String lastUpdate = statewiseArrayJSONObject.getString("lastupdatetime");

                        //Now the details of the today cases for each individual

                        String todayActive = statewiseArrayJSONObject.getString("deltaconfirmed");
                        String todayDeath = statewiseArrayJSONObject.getString("deltadeath");
                        String todayRecovered = statewiseArrayJSONObject.getString("deltarecovered");
                        // Now pass all the details to CoronaItem class
                        CoronaItem coronaItem = new CoronaItem(state, death, active, recovered, confirmed, lastUpdate,
                                todayDeath, todayRecovered, todayActive);
                        coronaItemsArrayList.add(coronaItem);
                    }
                    //Now we  just have to set up the recyclerview to display the content or data
                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, coronaItemsArrayList);


                }
                // try statement must have the catch statement so we'll add the catch statement here
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } , new  Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

    }
    private String getFormattedDate(String dateHeader){
        switch (dateHeader.substring(3,4)){
            case "01":
                return dateHeader.substring(0,2)+ "Jan";
            case "02":
                return dateHeader.substring(0,2)+ "Fed";
            case "03":
                return dateHeader.substring(0,2)+ "Mar";
            case "04":
                return dateHeader.substring(0,2)+ "Apr";
            case "05":
                return dateHeader.substring(0,2)+ "May";
            case "06":
                return dateHeader.substring(0,2)+ "Jun";
            case "07":
                return dateHeader.substring(0,2)+ "Jul";
            case "08":
                return dateHeader.substring(0,2)+ "Aug";
            case "09":
                return dateHeader.substring(0,2)+ "Sep";
            case "10":
                return dateHeader.substring(0,2)+ "Oct";
            case "11":
                return dateHeader.substring(0,2)+ "Nov";
            case "12":
                return dateHeader.substring(0,2)+ "Dec";
            default:
                return null;

        }

    }
}