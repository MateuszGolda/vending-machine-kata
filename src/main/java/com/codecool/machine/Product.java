package com.codecool.machine;

public enum Product {
    COLA(100), CHIPS(50), CANDY(65);

    final int cost;

    Product(int cost) {
        this.cost = cost;
    }
}
