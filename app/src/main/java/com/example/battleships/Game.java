package com.example.battleships;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.telephony.CellSignalStrength;
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
    private Ship selectedShip = null;

    Game(GameActivity context, TwoPlayerGameStartingContract contract, String currentPlayer) {
        this.context = context;
        this.currentPlayer = currentPlayer;
        initBoardByMap(contract.getMap());
        shipsMapForListView = contract.getMap();
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
        Ship shipOnClickedCell = board.getShipById(cellIndex);
        if(shipOnClickedCell != null){
            board.updateSingleCell(cellIndex, HIT);
            handleShipHit(shipOnClickedCell);
        } else {
            board.updateSingleCell(cellIndex, MISS);
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


    public void setShipSelected(Ship shipSelected){
        selectedShip = shipSelected;
    }
    public void rotateShip(){
        Log.d("ROTATE", "roatting ship nullable");

        if(selectedShip != null){
            Log.d("ROTATE", "roatting ship is not null");

            int selectedShipFirstId = selectedShip.getFirstId();
            if(selectedShip.isHorizontal){
                for(int i = 0; i < selectedShip.IDs.size(); i++){
                    selectedShip.IDs.set(i, selectedShip.getFirstId() + (i * Constants.BOARD_ROW));
                }
            }else{
                for(int i = 0; i < selectedShip.IDs.size(); i++){
                    selectedShip.IDs.set(i, selectedShip.getFirstId() + i);
                }
            }
            selectedShip.isHorizontal = !selectedShip.isHorizontal;

            int shipIndex = board.ships.indexOf(board.getShipById(selectedShipFirstId));
            board.ships.set(shipIndex, selectedShip);
            onShipMoved();
        }
    }

    public ArrayList<Integer> getSelectedShipIds(){
        if(selectedShip == null)
            return null;
        return selectedShip.IDs;
    }

    public void onShipMoved(){
        board.updateCells(board.cellArray, UNCOVERED);
        for(Ship ship : board.ships){
            board.setBusyCellsForShip(ship);
        }
    }

}

