package com.example.battleships;

import java.util.ArrayList;

//Skoro już ta klasa istnieje, to należy ją wykorzystać i zaimplementować

public class Board {

    ArrayList<Cell> cellArray;

    //dodaję tu nowe pole: aby to plansza zawierała listę statków
    private ArrayList<Ship> ships;

    public Board(ArrayList cellArray){
        this.cellArray = cellArray;
        ships = new ArrayList<>();
    }


    //ZADANIE 1: spróbuj zaimplementować poniższe dwie metody (najlepiej tak, aby pierwsza wykorzystywała tę drugą.
    public void updateCells(ArrayList <Cell> cellsToUpdate, Cell.Status newStatus){
    }

    public void updateSingleCell(int id, Cell.Status newStatus){
    }


    //ZADANIE: spróbuj napisać metodę, która zwróci wszystkie dostępne id, na których można postawićnowy statek



    public void addShip(Ship newShip){
        ships.add(newShip);
    }
}
