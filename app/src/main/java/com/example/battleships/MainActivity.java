package com.example.battleships;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

import static com.example.battleships.Game.BOARD_ROW;

public class MainActivity extends AppCompatActivity {

    ListView shipListView;
    GridView gridView;
    Button nextPlayerBtn;

    CAdapter adapter;
    CAdapter adapter2P;
    ArrayAdapter listViewAdapter;
    ArrayAdapter listViewAdapter2;

    SharedPreferences sharedPrefs;

    String difficulty;
    ArrayList ships;
    int shipsNum;
    int shipMaxSize;
    int shipMinSize;
    int shipLength;
    Boolean isGame2P;
    int currentPlayer = 1;

    Game gamePlayer1;
    Game gamePlayer2;

    //TODO:
    //Create ArrayList<String> instances for listViewAdapters
    //Add activity context to Game class
    //popraw ten brzydki ListView

    TwoPlayerGameStartingContract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contract = new TwoPlayerGameStartingContract(Constants.MAX_SHIP_NUM, Constants.MIN_SHIP_NUM, Constants.MAX_LENGTH, Constants.MIN_LENGTH);
        setUpGame();
        initLayouts();
    }
    public void initLayouts(){

        nextPlayerBtn = findViewById(R.id.nextPBtn);
        shipListView = findViewById(R.id.listView);
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(BOARD_ROW);
        adapter = new CAdapter(MainActivity.this, gamePlayer1);
        listViewAdapter = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, gamePlayer1.setUpShipListView(contract.getMap()));
        listViewAdapter2 = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, gamePlayer2.setUpShipListView(contract.getMap()));
        gridView.setAdapter(adapter);
        nextPlayerBtn.setOnClickListener(new NextPlayerOnClick());
        shipListView.setAdapter(listViewAdapter);
    }
    public void setUpGame(){
        sharedPrefs = getSharedPreferences("menuSharedPrefs", MODE_PRIVATE);
        isGame2P = sharedPrefs.getBoolean(MenuActivity.GAME_PLAYER_KEY,false);
        if(isGame2P){
            gamePlayer1 = new Game(contract);
            gamePlayer2 = new Game(contract);
        }else {
            gamePlayer1= new Game(contract);
        }
    }
    private void switchGridView(){
        switch (currentPlayer){
            case 1:
                currentPlayer++;
                adapter.setGame(gamePlayer1);
                adapter.notifyDataSetChanged();
                shipListView.setAdapter(listViewAdapter);
                listViewAdapter.notifyDataSetChanged();
                break;
            case 2:
                currentPlayer--;
                adapter.setGame(gamePlayer2);
                adapter.notifyDataSetChanged();
                shipListView.setAdapter(listViewAdapter2);
                listViewAdapter2.notifyDataSetChanged();
                break;
        }
    }



    public void setDifficulty(){


        switch (difficulty){
            case "EASY":
                shipsNum = 3;
                shipMaxSize = 5;
                shipMinSize = 3;
                break;
            case "NORMAL":
                shipsNum = 5;
                shipMaxSize = 4;
                shipMinSize = 2;
                break;
            case "HARD":
                shipsNum = 7;
                shipMaxSize = 2;
                shipMinSize = 1;
                break;
            default:
                shipsNum = 5;
                shipMaxSize = 4;
                shipMinSize = 2;
                break;

        }

    }


    private class NextPlayerOnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switchGridView();
        }
    }
}
