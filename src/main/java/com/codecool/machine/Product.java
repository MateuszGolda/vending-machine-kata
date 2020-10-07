package com.codecool.machine;

public enum Product {
    COLA(100), CANDY(65), CHIPS(50);

    final int cost;

    Product(int cost) {
        this.cost = cost;
    }
}
