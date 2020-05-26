package com.example.battleships;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.battleships.Cell.Status.*;

class Game {

    private static final String TAG = "GAME CLASS";

    private GameActivity context;
    public Board board;
    String currentPlayer;
    Map<Integer, Integer> shipsMapForListView;
    public boolean setupDone = false;

    Game(GameActivity context, TwoPlayerGameStartingContract contract, String currentPlayer) {
        this.context = context;
        this.currentPlayer = currentPlayer;
        initBoardByMap(contract.getMap());
        shipsMapForListView = contract.getMap();
    }

    Game(GameActivity context, String currentPlayer){
        this.context = context;
        this.currentPlayer = currentPlayer;
    }

    private void initBoardByMap(@NonNull HashMap<Integer, Integer> mapFromContract){
        board = new Board();
        for (Map.Entry<Integer, Integer> entry : mapFromContract.entrySet()) {
            int shipsAmount = entry.getValue();
            for (int i = 0; i < shipsAmount; i++) {
                try{
                    board.addRandomShip(entry.getKey());
                } catch (IllegalStateException e){
                    //try again - reached a state with no available positions for new ship
                    initBoardByMap(mapFromContract);
                }
            }
        }
        Log.d("GAME", "Created Ships IDs are: " + board.getBusyCellsIds());
    }

    public void updateCellStatusOnClicked(Integer cellIndex) {
        switch (board.getCellStatusById(cellIndex)) {
            case BUSY:
                Ship shipOnClickedCell = board.getShipById(cellIndex);
                if (shipOnClickedCell == null) {
                    board.updateSingleCell(cellIndex, MISS);
                } else {
                    board.updateSingleCell(cellIndex, HIT);
                   handleShipHit(shipOnClickedCell);
                }
                break;
            case UNCOVERED:
                board.updateSingleCell(cellIndex, MISS);
                break;
            case DROWNED:
            case MISS:
            case HIT:
                return;
        }
    }

    private void handleShipHit(Ship shipHit){
        for(int i : shipHit.IDs){
            if(board.getCellStatusById(i) == BUSY){
                return;
            }
        }
        drownShip(shipHit);

    }

    private void drownShip(Ship shipToDrown){
        board.drownShip(shipToDrown);
        updateContractMapOnDrown(shipToDrown.getSize());

        context.updateListView(currentPlayer, shipsMapForListView);
        if(board.areAllShipsDrowned()){
            endGame();
        }
    }

    private void updateContractMapOnDrown(int drownedShipSize){
        int currValue = shipsMapForListView.get(drownedShipSize) - 1;
        shipsMapForListView.put(drownedShipSize, currValue);
    }

    private void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("GAME ENDED!")
            .setMessage("Game was finished with result: player " + currentPlayer + " wins")
            .setCancelable(false)
            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.finish();
                }
            });
        builder.create();
        builder.show();
    }

    public Cell.Status getCellStatusById(int id){
        return board.getCellStatusById(id);
    }

    enum BoardSize  {
        SMALL,
        MEDIUM,
        BIG
    }

//TODO: listview
    public ArrayList<String> setUpShipListView(Map<Integer, Integer> map){

        ArrayList<String> stringsToReturn = new ArrayList<>();
        for(Integer key : map.keySet()){
            stringsToReturn.add(map.get(key) + " x " + key);
        }
        return stringsToReturn;
    }

    public void rotateShip(){

    }

}

