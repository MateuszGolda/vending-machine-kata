package com.codecool.machine;

public class CoinFactory {
    public Coin getCoin(int weight, int size) {
        for (Coin coin : Coin.values()) {
            if (weight >= coin.weight - coin.weightError
                    && weight <= coin.weight + coin.weightError
                    && size >= coin.size - coin.sizeError
                    && size <= coin.size + coin.sizeError) {
                return coin;
            }
        }
        return Coin.INVALID;
    }
}
