package com.example.coding_challange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_newCompetition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_newCompetition = findViewById(R.id.btn_newCompetition);
        btn_newCompetition.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openCompetitionActivity();
            }
        });

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
            findViewById(R.id.btn_leaderboard).setClickable(true);
        }
    }
}
