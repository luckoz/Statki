package com.example.battleships;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

    String player1Name = "1";
    String player2Name = "2";


    ArrayList<String> shipsListForListView;
    ArrayList<String> shipsListForListView2;

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
        listViewAdapter = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, shipsListForListView);
        listViewAdapter2 = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, shipsListForListView2);
        gridView.setAdapter(adapter);
        nextPlayerBtn.setOnClickListener(new NextPlayerOnClick());
        shipListView.setAdapter(listViewAdapter);
    }
    public void setUpGame(){
        sharedPrefs = getSharedPreferences("menuSharedPrefs", MODE_PRIVATE);
        isGame2P = sharedPrefs.getBoolean("isGameMP",false);
        Log.d("GAME_ACTIVITY", String.valueOf(isGame2P));
        if(isGame2P){
            gamePlayer1 = new Game(this, contract, player1Name);
            gamePlayer2 = new Game(this, contract, player2Name);
        } else {
            gamePlayer1= new Game(this, contract, player1Name);
        }
        shipsListForListView = setUpList(contract.getMap());
        shipsListForListView2 = setUpList(contract.getMap());
    }

    private void switchGridView(){
        switch (currentPlayer){
            case 1:
                currentPlayer++;
                adapter.setGame(gamePlayer1);
                adapter.notifyDataSetChanged();
                shipListView.setAdapter(listViewAdapter);
                shipsListForListView = setUpList(contract.getMap() );
                listViewAdapter.notifyDataSetChanged();
//                shipsListForListView.removeAll(gamePlayer1.shipIdsToRemove);
                break;
            case 2:
                currentPlayer--;
                adapter.setGame(gamePlayer2);
                adapter.notifyDataSetChanged();
                shipListView.setAdapter(listViewAdapter2);
                shipsListForListView2 = setUpList(contract.getMap());
                listViewAdapter2.notifyDataSetChanged();
//                shipsListForListView2.removeAll(gamePlayer2.shipIdsToRemove);
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
//        stringsToReturn.add("Pozostało do zatopienia: ");
        for(Integer key : map.keySet()){
            //TODO domowe ZADANIE 1
            //Napisac logikę, najlepiej metodę, która obsłuży polską odmianę słowa "statek" zależnie od ilości pozostałych statkó do zbicia
            stringsToReturn.add(map.get(key) + " statki " + key + " - masztowe");
        }
        return stringsToReturn;
    }

    public void updateListView(String player, Map<Integer, Integer> updatedMap){
        if(player.equals(player1Name)){
            shipsListForListView.clear();
            shipsListForListView.addAll(setUpList(updatedMap));
            listViewAdapter.notifyDataSetChanged();
        }else {
            shipsListForListView2.clear();
            shipsListForListView2.addAll(setUpList(updatedMap));
            listViewAdapter2.notifyDataSetChanged();
        }

    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder backPressedAlertDialog = new AlertDialog.Builder(this);
        backPressedAlertDialog.setTitle(""); //TODO ZADANIE UZUEPLNIJ DIALOG TESKTAMI
        backPressedAlertDialog.setMessage("");
        backPressedAlertDialog.setNegativeButton("NEY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        backPressedAlertDialog.setPositiveButton("YEY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        backPressedAlertDialog.create();
        backPressedAlertDialog.show();
    }
}
