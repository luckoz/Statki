package com.example.battleships;


import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TwoPlayerGameStartingContract {

    private Map<Integer, Integer> map;

    TwoPlayerGameStartingContract(int maxShipsNum, int minShipsNum, int maxLength, int minLength){
        Random rand = new Random();
        map = new HashMap<Integer, Integer>();
        int shipCount = rand.nextInt(maxShipsNum - minShipsNum) + minShipsNum;
        for(int i = 0; i < shipCount; i++){
            Log.d("CONTRACT", "ILOSC STATKOW: " + shipCount);
           //długość statku
           Integer length = rand.nextInt(maxLength - minLength) + minLength;

           //dodajemy statek do mapy
           if(map.containsKey(length)) {
               int num = map.getOrDefault(length, 0);
               map.put(length, num + 1);
           } else {
               map.put(length, 1);
           }
       }
        Log.d("CONTRACT", "MAPA: " + map.toString());
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }
}