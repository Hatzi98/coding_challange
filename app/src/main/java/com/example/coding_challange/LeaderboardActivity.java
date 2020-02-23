package com.example.coding_challange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;

public class LeaderboardActivity extends AppCompatActivity {

    private TextView txtView_Leaderboard;
    private HashSet<JSONObject> robots;
    private boolean finishedDownloading;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        finishedDownloading = false;
        _getLeaderboard();

        txtView_Leaderboard = findViewById(R.id.txtView_Leaderboard);
        txtView_Leaderboard.setText("Results are downloading, please wait! ... ");

      /*  while(!finishedDownloading){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

    }

    private void _getLeaderboard() {
        requestQueue = Volley.newRequestQueue(this);
        robots = new HashSet<>();

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, getResources().getString(R.string.api_url) + "/robots", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                robots.add(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        finishedDownloading = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(objectRequest);
        requestQueue.start();
    }
}
