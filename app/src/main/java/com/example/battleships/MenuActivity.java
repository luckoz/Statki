package com.example.battleships;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button newGameBtn;
    Button statsBtn;
    Button optionsBtn;

    SharedPreferences sharedPreferences;

    public static final String GAME_PLAYER_KEY = "GAME_PLAYER_KEY";
    boolean isGameMP = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initLayouts();
    }

    private void initLayouts() {
        initViews();
        initButtons();
    }

    private void initButtons() {
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMainActivity();
            }
        });
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Stats
            }
        });
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: options
            }
        });
    }

    private void initMainActivity() {
        sharedPreferences = getSharedPreferences("menuSharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(GAME_PLAYER_KEY, isGameMP);
        editor.apply();


        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    private void initViews() {
        newGameBtn = findViewById(R.id.newGameBtn);
        statsBtn = findViewById(R.id.statsBtn);
        optionsBtn = findViewById(R.id.optionsBtn);
    }
}
