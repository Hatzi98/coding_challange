package com.example.coding_challange;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import static com.example.coding_challange.R.*;

public class CompetitionActivity extends AppCompatActivity {
    private Button btn_createTeams;
    private Button btn_startDancing;
    private EditText txt_teamOne;
    private EditText txt_teamTwo;
    private TextView txtView_teamOne;
    private TextView txtView_teamTwo;
    private String nameTeamOne, nameTeamTwo;
    private RequestQueue requestQueue;
    private HashSet<JSONObject> robots, robotsTeamOne, robotsTeamTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_competition);

        btn_createTeams = findViewById(id.btn_createTeams);
        btn_startDancing = findViewById(id.btn_startDancing);
        txt_teamOne = findViewById(id.txt_teamOne);
        txt_teamTwo = findViewById(id.txt_teamTwo);
        txtView_teamOne = findViewById(id.txtView_lineupTeamOne);
        txtView_teamTwo = findViewById(id.txtView_lineupTeamTwo);
        nameTeamOne = "";
        nameTeamTwo = "";
        _getRobots();

        btn_createTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    _createTeams();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_startDancing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _startDancing();
            }
        });
    }

    private void _startDancing() {
        Random rand = new Random();
        JSONArray winner_array = new JSONArray();
        String builder_message = "The competition is over. Here are the results: \n\n";
        int counter = 1;

        if(robotsTeamOne.size() == robotsTeamTwo.size()){
            Iterator<JSONObject> first_iterator = robotsTeamOne.iterator();
            Iterator<JSONObject> second_iterator = robotsTeamTwo.iterator();

            try {
                while(first_iterator.hasNext() && second_iterator.hasNext()) {
                    JSONObject temp = new JSONObject();
                    JSONObject robot_team_one = first_iterator.next();
                    JSONObject robot_team_two = second_iterator.next();

                    //get some useful information
                    int first_id = robot_team_one.getInt("id");
                    int second_id = robot_team_two.getInt("id");

                    String first_name = robot_team_one.getString("name");
                    String second_name = robot_team_two.getString("name");

                    String first_move = robot_team_one.getString("powermove");
                    String second_move = robot_team_two.getString("powermove");

                    int[] opponents = {first_id, second_id};

                    builder_message += counter + ". dance of:\n";
                    temp.put("opponents", opponents);

                    if (rand.nextInt(10) >= 5) {
                        temp.put("winner", first_id);
                        builder_message += first_name + " won against " + second_name + " with the move " + first_move;
                    } else {
                        temp.put("winner", second_id);
                        builder_message += second_name + " won against " + first_name + " with the move " + second_move;
                    }
                    builder_message += "\n\n";
                    counter++;
                    //add the match to the winner_array
                    winner_array.put(temp);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Display results
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(builder_message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "Finish",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        setResult(2);
                        finish();
                    }
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    /*private void _sendResults(JSONArray results_to_send){

        JsonArrayRequest sendResult = new JsonArrayRequest(
                Request.Method.POST,
                getResources().getString(R.string.api_url) + "/danceoffs",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected JSONArray getParams(){

            }
        };
        requestQueue.add(sendResult);

    }*/

    private void _createTeams() {
        String first_name = txt_teamOne.getText().toString();
        String second_name = txt_teamTwo.getText().toString();
        if (first_name.isEmpty() || second_name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter a name for both Teams!", Toast.LENGTH_LONG).show();
            _getRobots();
        } else {
            nameTeamOne = "Team " + first_name + ":\n";
            nameTeamTwo = "Team " + second_name + ":\n";
            
            if (robots.size() == 0) {
                Toast.makeText(getApplicationContext(), "There are no robots to choose from! Check your internet connection!", Toast.LENGTH_LONG).show();
            } else {

                //assign 5 robots to every team
                robotsTeamOne = _fillTeam();
                robotsTeamTwo = _fillTeam();

                //make some buttons and textviews visible and display the lineup
                findViewById(id.txtView_lineup).setVisibility(View.VISIBLE);

                txtView_teamOne.setText(nameTeamOne + _printTeam(robotsTeamOne));
                txtView_teamOne.setVisibility(View.VISIBLE);

                txtView_teamTwo.setText(nameTeamTwo + _printTeam(robotsTeamTwo));
                txtView_teamTwo.setVisibility(View.VISIBLE);

                btn_startDancing.setVisibility(View.VISIBLE);

                //disable the textboxes and the button for creating teams
                txt_teamOne.setEnabled(false);
                txt_teamTwo.setEnabled(false);
                btn_createTeams.setClickable(false);
            }
        }
    }

    private void _getRobots() {
        requestQueue = Volley.newRequestQueue(this);
        robots = new HashSet<>();

        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, getResources().getString(string.api_url) + "/robots", null,
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

    private HashSet<JSONObject> _fillTeam() {
        HashSet<JSONObject> team_to_fill = new HashSet<>();
        int experience = 0;
        int assigned_robots = 0;

        Iterator<JSONObject> robots_iterator = robots.iterator();

        try {
            while (assigned_robots < 5 && robots_iterator.hasNext()) {
                //get the current robot from the set
                JSONObject robot = robots_iterator.next();

                //get the experience and the outOfOrder data from the robot
                int robot_experience = robot.getInt("experience");
                boolean robot_out_of_order = robot.getBoolean("outOfOrder");

                //check if the robot can be added to the team
                if (experience + robot_experience <= 50 && !robot_out_of_order) {
                    team_to_fill.add(robot);
                    experience += robot_experience;
                    assigned_robots++;

                    //remove the added robot from the set
                    //robots.remove(robot);
                }
            }
            //remove assigend robots from set
            Iterator<JSONObject> robot_iterator = team_to_fill.iterator();
            while(robot_iterator.hasNext()){
                robots.remove(robot_iterator.next());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return team_to_fill;
    }

    private String _printTeam(HashSet<JSONObject> team_to_print){
        String output = "";

        Iterator<JSONObject> iterator = team_to_print.iterator();
        while(iterator.hasNext()){
            try {
                output += iterator.next().getString("name") + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return output;

    }

}
