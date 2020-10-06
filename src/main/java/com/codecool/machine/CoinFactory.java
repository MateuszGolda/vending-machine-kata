package com.codecool.machine;

import java.util.Optional;

public class CoinFactory {
    public Optional<Coin> getCoin(int weight, int size) {
        Optional<Coin> optionalCoin = Optional.empty();
        for (Coin coin : Coin.values()) {
            if (weight >= coin.weight - coin.weightError
                    && weight <= coin.weight + coin.weightError
                    && size >= coin.size - coin.sizeError
                    && size <= coin.size + coin.sizeError) {
                optionalCoin = Optional.of(coin);
            }
        }
        return optionalCoin;
    }
}
