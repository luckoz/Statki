package com.example.battleships;


import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.Random;

public class TwoPlayerGameStartingContract {

    /**
     *  Map representing amount of ships for every ship size
     * Key represents ship length
     * Value represents amount of ships with the given size (from key)
     */
    @NonNull
    private HashMap<Integer, Integer> map;

    TwoPlayerGameStartingContract(int maxShipsNum, int minShipsNum, int maxLength, int minLength){
        do {
            Random rand = new Random();
            map = new HashMap<>();
            int shipCount = rand.nextInt(maxShipsNum - minShipsNum) + minShipsNum;
            for (int i = 0; i < shipCount; i++) {
                Log.d("CONTRACT", "ILOSC STATKOW: " + shipCount);
                //długość statku
                Integer length = rand.nextInt(maxLength - minLength) + minLength;

                //dodajemy statek do mapy
                if (map.containsKey(length)) {
                    int num = map.getOrDefault(length, 0);
                    map.put(length, num + 1);
                } else {
                    map.put(length, 1);
                }
            }
        }while(noPlaceForShipStateReachable(map));
        Log.d("CONTRACT", "MAPA: " + map.toString());
    }


    private boolean noPlaceForShipStateReachable(HashMap<Integer, Integer> shipsMap){
        int worstCaseBusyCount = 0;
        for (int shipLength : shipsMap.keySet()) {
            int shipCount = shipsMap.get((shipLength));
            worstCaseBusyCount = worstCaseBusyCount + (3 * (shipLength + 2)) * shipCount;
        }
        if(worstCaseBusyCount > Constants.BOARD_SIZE)
            return true;
        else
            return false;

    }

    @NonNull
    public HashMap<Integer, Integer> getMap() {
        return map;
    }
}