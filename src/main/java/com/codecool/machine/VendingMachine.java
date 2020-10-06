package com.codecool.machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class VendingMachine {
    private final Map<Product, Integer> products;
    private final Map<Coin, Integer> machineCoins;
    private final Map<Coin, Integer> insertedCoins;
    private final Map<Coin, Integer> changeTray;
    private final CoinFactory coinFactory = new CoinFactory();
    private int insertedValue = 0;

    VendingMachine() {
        products = new HashMap<>();
        machineCoins = new HashMap<>();
        insertedCoins = new HashMap<>();
        changeTray = new HashMap<>();
    }

    public void insertCoin(int weight, int size) {
        Optional<Coin> optionalCoin = coinFactory.getCoin(weight, size);
        optionalCoin.ifPresent(this::addInsertedCoin);
        // TODO when coin is not recognized return it to the buyer
    }

    private void addInsertedCoin(Coin c) {
        insertedCoins.merge(c, 1, Integer::sum);
        insertedValue += c.value;
    }

    public void selectProduct(Product product) {
        if (product.cost <= insertedValue) {
            int rest = insertedValue - product.cost;
            Optional<Map<Coin, Integer>> change = computeChange(rest);
            change.ifPresent((c) -> buyProduct(product, c));
        }
    }

    private void buyProduct(Product product, Map<Coin, Integer> change) {
        products.merge(product, -1, Integer::sum);

        withdrawChange(change);

        mergeCoins(machineCoins, insertedCoins);

        clearInsertedCoins();
    }

    private void clearInsertedCoins() {
        insertedCoins.put(Coin.QUARTER, 0);
        insertedCoins.put(Coin.DIME, 0);
        insertedCoins.put(Coin.NICKEL, 0);
        insertedValue = 0;
    }

    private void mergeCoins(Map<Coin, Integer> machineCoins, Map<Coin, Integer> insertedCoins) {
        machineCoins.merge(Coin.QUARTER, insertedCoins.computeIfAbsent(Coin.QUARTER, k -> 0), Integer::sum);
        machineCoins.merge(Coin.DIME, insertedCoins.computeIfAbsent(Coin.DIME, k -> 0), Integer::sum);
        machineCoins.merge(Coin.NICKEL, insertedCoins.computeIfAbsent(Coin.NICKEL, k -> 0), Integer::sum);
    }

    private void withdrawChange(Map<Coin, Integer> change) {
        machineCoins.merge(Coin.QUARTER, -change.computeIfAbsent(Coin.QUARTER, k -> 0), Integer::sum);
        machineCoins.merge(Coin.DIME, -change.computeIfAbsent(Coin.DIME, k -> 0), Integer::sum);
        machineCoins.merge(Coin.NICKEL, -change.computeIfAbsent(Coin.NICKEL, k -> 0), Integer::sum);

        mergeCoins(changeTray, change);
    }

    private Optional<Map<Coin, Integer>> computeChange(int rest) {
        Map<Coin, Integer> changeToReturn = new HashMap<>();

        if (rest == 0) return Optional.of(changeToReturn);

        int maxQuartersNeeded =
                Math.min(machineCoins.computeIfAbsent(Coin.QUARTER, k -> 0), rest / Coin.QUARTER.value);

        int maxDimesNeeded =
                Math.min(machineCoins.computeIfAbsent(Coin.DIME, k -> 0), rest / Coin.DIME.value);

        int maxNickelsNeeded =
                Math.min(machineCoins.computeIfAbsent(Coin.NICKEL, k -> 0), rest / Coin.NICKEL.value);

        if (maxQuartersNeeded * Coin.QUARTER.value
                + maxDimesNeeded * Coin.DIME.value
                + maxNickelsNeeded * Coin.NICKEL.value
                < rest) {
            return Optional.empty();
        }
        return findChange(maxQuartersNeeded, maxDimesNeeded, maxNickelsNeeded, rest, changeToReturn);
    }

    private Optional<Map<Coin, Integer>> findChange(int quarters, int dimes, int nickels, int rest, Map<Coin, Integer> changeToReturn) {
        int currentRestValue;
        for (int qua = quarters; qua >= 0; qua--) {
            for (int dim = dimes; dim >= 0; dim--) {
                for (int nic = 0; nic <= nickels; nic++) {
                    currentRestValue = qua * 25 + dim * 10 + nic * 5;
                    if (currentRestValue > rest) {
                        break;
                    }
                    if (currentRestValue == rest) {
                        changeToReturn.put(Coin.QUARTER, qua);
                        changeToReturn.put(Coin.DIME, dim);
                        changeToReturn.put(Coin.NICKEL, nic);
                        return Optional.of(changeToReturn);
                    }
                }
            }
        }
        return Optional.empty();
    }

    public void returnCoins() {
        changeTray.putAll(insertedCoins);
        clearInsertedCoins();
    }

    public int getInsertedValue() {
        return insertedValue;
    }

    public int getQuarters() {
        machineCoins.putIfAbsent(Coin.QUARTER, 0);
        return machineCoins.get(Coin.QUARTER);
    }

    public int getDimes() {
        machineCoins.putIfAbsent(Coin.DIME, 0);
        return machineCoins.get(Coin.DIME);
    }

    public int getNickels() {
        machineCoins.putIfAbsent(Coin.NICKEL, 0);
        return machineCoins.get(Coin.NICKEL);
    }

    public Map<Coin, Integer> getMachineCoins() {
        return machineCoins;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public Map<Coin, Integer> getInsertedCoins() {
        return insertedCoins;
    }

    public Map<Coin, Integer> getChangeTray() {
        return changeTray;
    }
}
