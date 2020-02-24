package com.example.battleships;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TwoPlayerGameStartingContract {

    int shipCount;
//    int maxShipsNum, minShipsNum, maxLength, minLength;
//    int countTwoFiledShips;
//    int countThreeFiledShips;
//    int countFourFiledShips;
    Map<Integer, Integer> map;


    TwoPlayerGameStartingContract(int maxShipsNum, int minShipsNum, int maxLength, int minLength){

        map = new HashMap<Integer, Integer>();
        shipCount =   new Random().nextInt((maxShipsNum - minShipsNum) + 1) + minShipsNum;
        for(int i = 0; i <= shipCount; i++){
           //liczba statków
           Integer length = (int) ((Math.random() * ((maxLength - minLength) + 1)) + minLength);

           //długość statków
           if(map != null && map.containsKey(length)) {
               int num = map.get(length);
               map.put(length, num++);
           } else {
               map.put(length,1);
           }
       }
    }

    TwoPlayerGameStartingContract(int shipCount, Map<Integer, Integer> mapa){
        map = mapa;
        this.shipCount = shipCount;
    }


    public TwoPlayerGameStartingContract cloneContract(){
        return new TwoPlayerGameStartingContract(this.shipCount, this.map);
    }

    public boolean isMapEmpty(){
        return map.isEmpty();
    }

    public Map<Integer, Integer> getMap() {
        return map;
    }
}