package com.example.battleships;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MenuActivity extends AppCompatActivity {

    Button newGameBtn;
    Button statsBtn;
    Button optionsBtn;

    CheckBox isShipPositionRandomChck;
    CheckBox isGameMultiPlayerChck;

    SharedPreferences sharedPreferences;

    public static final String GAME_PLAYER_KEY = "GAME_PLAYER_KEY";
    boolean isGameMP;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initLayouts();
    }

    private void initLayouts() {
        initViews();
        initButtons();
        initOptions();
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
                isShipPositionRandomChck.setVisibility(View.GONE);
                isGameMultiPlayerChck.setVisibility(View.GONE);
                optionsBtn.setVisibility(View.VISIBLE);
                statsBtn.setVisibility(View.GONE);
            }
        });
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShipPositionRandomChck.setVisibility(View.VISIBLE);
                isGameMultiPlayerChck.setVisibility(View.VISIBLE);
                optionsBtn.setVisibility(View.GONE);
                statsBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initMainActivity() {

        sharedPreferences = getSharedPreferences("menuSharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("isGameMP", isGameMultiPlayerChck.isChecked());
        editor.putBoolean("areShipsPosRandom", isShipPositionRandomChck.isChecked());
        editor.apply();


        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        startActivity(intent);
    }



    private void initViews() {
        newGameBtn = findViewById(R.id.newGameBtn);
        statsBtn = findViewById(R.id.statsBtn);
        optionsBtn = findViewById(R.id.optionsBtn);
    }
    private void initOptions(){
        isGameMultiPlayerChck = findViewById(R.id.multiPlayerCheckB);
        isShipPositionRandomChck = findViewById(R.id.randomShipPosBtn);

        isShipPositionRandomChck.setVisibility(View.GONE);
        isGameMultiPlayerChck.setVisibility(View.GONE);
    }
}
