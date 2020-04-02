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
        Random rand = new Random();
        map = new HashMap<>();
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

    //TODO ZADANIE Napisz logikę poniższej metody, aby uniknąć sytuacji, w której istnieje możliwość, że nie będzie się dało ustawić wsyztskich statków na planszy.
    //Zastanów się też i spróbuj użyć tej metody w kodzie we właściwym miejscu.
    //Dla jasności - chcemy uniknąć sytuacji, gdy np wylosuje się ilosć statków 6 i wszystkie 4 masztowe.
    //Apka może się niebezpiecznie zapętlić, więc z góry nie możemy dopuszczać do takiej sytuacji
    private boolean noPlaceForShipStateReachable(int rowLength, HashMap<Integer, Integer> shipsMap){
        return false;
    }

    @NonNull
    public HashMap<Integer, Integer> getMap() {
        return map;
    }
}