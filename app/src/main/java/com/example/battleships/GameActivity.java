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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import static com.example.battleships.Constants.BOARD_ROW;

public class GameActivity extends AppCompatActivity {

    ListView shipListView;
    GridView gridView;
    Button nextPlayerBtn;
    Button finishSetupBtn;
    Button rotateBtn;
    TextView infoTxtView;

    CAdapter adapter;
    ArrayAdapter listViewAdapter;
    ArrayAdapter listViewAdapter2;

    SharedPreferences sharedPrefs;

    String difficulty;

    String player1Name = "1";
    String player2Name = "2";

    ArrayList<String> shipsListForListView;
    ArrayList<String> shipsListForListView2;

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
        infoTxtView = findViewById(R.id.currentPlayerTxt);
        nextPlayerBtn = findViewById(R.id.nextPBtn);
        rotateBtn = findViewById(R.id.rotateShipBtn);
        finishSetupBtn = findViewById(R.id.finishSetupBtn);


        shipListView = findViewById(R.id.listView);
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(BOARD_ROW);

        adapter = new CAdapter(GameActivity.this, gamePlayer1);
        listViewAdapter = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, shipsListForListView);
        listViewAdapter2 = new ArrayAdapter(this, R.layout.ship_list_item, R.id.text, shipsListForListView2);
        gridView.setAdapter(adapter);
        nextPlayerBtn.setOnClickListener(new NextPlayerOnClick());

        shipListView.setAdapter(listViewAdapter);
        updaterTopTextView();

        if(!isGame2P) {
            nextPlayerBtn.setVisibility(View.GONE);
            rotateBtn.setVisibility(View.GONE);
            finishSetupBtn.setVisibility(View.GONE);
            infoTxtView.setVisibility(View.GONE);
            adapter.shipsVisible = false;
        }

        finishSetupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentPlayer){
                    case 1:
                        gamePlayer1.setupDone = true;
                        gamePlayer1.setShipSelected(null);
                        switchGridView();
                        break;
                    case 2:
                        gamePlayer2.setupDone = true;
                        finishSetupBtn.setVisibility(View.GONE);
                        nextPlayerBtn.setVisibility(View.VISIBLE);
                        rotateBtn.setVisibility(View.GONE);
                        adapter.shipsVisible = false;
                        gamePlayer2.setShipSelected(null);
                        switchGridView();
                        break;
                }
            }
        });

        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ROTATE", "clicked");
                switch (currentPlayer){
                    case 1:
                        gamePlayer1.rotateShip();
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        gamePlayer2.rotateShip();
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }
    public void setUpGame(){
        sharedPrefs = getSharedPreferences("menuSharedPrefs", MODE_PRIVATE);
        isGame2P = sharedPrefs.getBoolean("isGameMP",false);
        boolean isRandomPositionOn = sharedPrefs.getBoolean("areShipsPosRandom",true);

        if (isGame2P) {
            gamePlayer1 = new Game(this, contract, player1Name);
            gamePlayer2 = new Game(this, contract, player2Name);
        }
        else {
            gamePlayer1= new Game(this, contract, player1Name);
            gamePlayer1.setupDone = true;
        }
        shipsListForListView = setUpList(contract.getMap());
        shipsListForListView2 = setUpList(contract.getMap());
    }

    private void switchGridView(){
        switch (currentPlayer){
            case 1:
                if(isGame2P && !gamePlayer2.setupDone){
                    adapter.setGame(gamePlayer2);
                    currentPlayer++;
                    shipListView.setAdapter(listViewAdapter2);
                    listViewAdapter2.notifyDataSetChanged();
                } else {
                    adapter.setGame(gamePlayer1);
                    currentPlayer++;
                    shipListView.setAdapter(listViewAdapter);
                    listViewAdapter.notifyDataSetChanged();
                }
//                shipsListForListView2 = setUpList(contract.getMap());
                adapter.notifyDataSetChanged();
                break;
            case 2:
                currentPlayer--;
                adapter.setGame(gamePlayer2);
                shipListView.setAdapter(listViewAdapter2);
//                shipsListForListView = setUpList(contract.getMap());
                listViewAdapter2.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                break;
        }
        updaterTopTextView();
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
            stringsToReturn.add(map.get(key) + " " + getRightShipsForm(map.get(key), key));
        }
        return stringsToReturn;
    }

    int getLastDigitOfNumberUpTo99(Integer number){
            return number % 10;
    }

    public void updateListView(String player, Map<Integer, Integer> updatedMap){
        if(!player.equals(player1Name)){
            shipsListForListView.clear();
            shipsListForListView.addAll(setUpList(updatedMap));
            listViewAdapter.notifyDataSetChanged();
        } else {
            shipsListForListView2.clear();
            shipsListForListView2.addAll(setUpList(updatedMap));
            listViewAdapter2.notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder backPressedAlertDialog = new AlertDialog.Builder(this);
        backPressedAlertDialog.setTitle("Do you want to quit?");
        backPressedAlertDialog.setMessage("Do you really, realy, really want to quit");
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

    String getRightShipsForm(Integer shipsNumber, int key){
        switch (getLastDigitOfNumberUpTo99(shipsNumber)){
            case 1:
                if(shipsNumber > 10)
                    return String.format("statków %s - masztowych", key);
                else
                    return String.format("statek %s - masztowy", key);
            case 2:
            case 3:
            case 4:
                if(shipsNumber < 20 && shipsNumber > 10)
                    return String.format("statków %s - masztowych", key);
                else
                    return String.format("statki %s - masztowe", key);
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 0:
                return String.format("statków %s - masztowych", key);
            default:
                return String.format("ships with %s poles", key);
        }
    }

    private void updaterTopTextView(){
        switch (currentPlayer){
            case 1:
                if(gamePlayer1.setupDone){
                    infoTxtView.setText("Player" + currentPlayer);
                } else {
                    infoTxtView.setText("SETUP SHIPS PLAYER " + currentPlayer );
                }
                break;
            case 2:
                if(gamePlayer2.setupDone){
                    infoTxtView.setText("Player" + currentPlayer);
                } else {
                    infoTxtView.setText("SETUP SHIPS PLAYER " + currentPlayer );
                }
                break;
        }
    }

}
