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

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        /*RequestQueue requestQueue = Volley.newRequestQueue(this);



        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                getResources().getString(R.string.api_url),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        TextView view = findViewById(R.id.text_home);
                        view.setText(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Snackbar.make(findViewById(R.id.nav_view),error.toString(),Snackbar.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(objectRequest);*/
    }
}
