package com.example.coding_challange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn_newCompetition;
    private Button btn_leaderboard;
    private boolean openLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_newCompetition = findViewById(R.id.btn_newCompetition);
        btn_leaderboard = findViewById(R.id.btn_leaderboard);
        openLeaderboard = false;

        btn_newCompetition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openCompetitionActivity();
            }
        });

        btn_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(openLeaderboard){
                    openLeaderboardActivity();
                }else{
                    Toast.makeText(getApplicationContext(), "You have to finish one competition to look at the leaderboard!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void openLeaderboardActivity() {
        Intent intent = new Intent(this, LeaderboardActivity.class);
        startActivity(intent);
    }

    private void openCompetitionActivity(){
        Intent intent = new Intent(this, CompetitionActivity.class);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 2)
        {
            openLeaderboard = true;
        }
    }
}
