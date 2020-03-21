package com.example.battleships;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import static com.example.battleships.Constants.BOARD_ROW;

public class GameActivity extends AppCompatActivity {

    ListView shipListView;
    GridView gridView;
    Button nextPlayerBtn;

    CAdapter adapter;
    ArrayAdapter listViewAdapter;
    ArrayAdapter listViewAdapter2;

    SharedPreferences sharedPrefs;

    String difficulty;

    String winnerPlayerName;

    ArrayList<String> ships;
    ArrayList<String> ships2;

    int shipsNum;
    public int shipMaxSize;
    int shipMinSize;
    int shipLength;
    boolean isGame2P = false;
    int currentPlayer = 1;

    Game gamePlayer1;
    Game gamePlayer2;

    TwoPlayerGameStartingContract contract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contract = new TwoPlayerGameStartingContract(Constants.MAX_SHIP_NUM, Constants.MIN_SHIP_NUM, Constants.MAX_SHIP_SIZE, Constants.MIN_SHIP_SIZE);
        setUpGame();
        initLayouts();
    }
    public void initLayouts(){
        nextPlayerBtn = findViewById(R.id.nextPBtn);
        if(!isGame2P) {
            nextPlayerBtn.setVisibility(View.GONE);
        }
        shipListView = findViewById(R.id.listView);
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(BOARD_ROW);

        adapter = new CAdapter(GameActivity.this, gamePlayer1);
        listViewAdapter = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, ships);
        listViewAdapter2 = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, ships2);
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
        } else {
            gamePlayer1= new Game(contract);
        }
        ships = setUpList(contract.getMap());
        ships2 = setUpList(contract.getMap());
    }

    private void switchGridView(){
        switch (currentPlayer){
            case 1:
                currentPlayer++;
                adapter.setGame(gamePlayer1);
                adapter.notifyDataSetChanged();
                shipListView.setAdapter(listViewAdapter);
                ships = setUpList(contract.getMap() );
                listViewAdapter.notifyDataSetChanged();
//                ships.removeAll(gamePlayer1.shipIdsToRemove);
                break;
            case 2:
                currentPlayer--;
                adapter.setGame(gamePlayer2);
                adapter.notifyDataSetChanged();
                shipListView.setAdapter(listViewAdapter2);
                ships2 = setUpList(contract.getMap());
                listViewAdapter2.notifyDataSetChanged();
//                ships2.removeAll(gamePlayer2.shipIdsToRemove);
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
            case "HARD":
                shipsNum = 7;
                shipMaxSize = 2;
                shipMinSize = 1;
                break;
            case "NORMAL":
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

    private ArrayList<String> setUpList(Map<Integer, Integer> map){
        ArrayList<String> stringsToReturn = new ArrayList<>();
        for(Integer key : map.keySet()){
            stringsToReturn.add(map.get(key) + " x " + key);
        }
        return stringsToReturn;
    }
    public void end(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        dialogBuilder.setTitle("THE END");
        dialogBuilder.setMessage("The " + winnerPlayerName + " has won THE GAME");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("AGAIN!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gamePlayer1 = null;
                gamePlayer2 = null;
                setUpGame();
                dialog.dismiss();
            }
        });
        dialogBuilder.create();
        dialogBuilder.show();


    }
//    private Context getWinnerPlayerContext(){
//        if(currentPlayer == 1){
//            return gamePlayer1.getCo;
//        }
//        else return gamePlayer2;
//
//    }

}
