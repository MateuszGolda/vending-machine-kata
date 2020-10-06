package com.codecool.machine;

public enum Coin {
    NICKEL(5, 500, 50, 5, 2),
    DIME(10, 1000, 100, 10, 4),
    QUARTER(25, 2500, 250, 25, 10),
    INVALID(-1,-1,-1,0,0);

    final int value;
    final int weight;
    final int size;
    final int weightError;
    final int sizeError;

    Coin(int value, int weight, int size, int weightError, int sizeError) {
        this.value = value;
        this.weight = weight;
        this.size = size;
        this.weightError = weightError;
        this.sizeError = sizeError;
    }
}
