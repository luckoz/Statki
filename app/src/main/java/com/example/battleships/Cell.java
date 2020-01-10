package com.example.battleships;

public class Cell {
    private Integer index;
    public Status status;

    Cell (int index){
        this.status = Status.UNCOVERED;
        this.index = index;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public Status getStatus() {
        return status;
    }



    public enum Status{
        UNCOVERED,
        MISS,
        HIT,
        DROWNED,
        BUSY
    }
}
