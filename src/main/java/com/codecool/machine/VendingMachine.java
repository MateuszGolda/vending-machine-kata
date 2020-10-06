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
        products = initializeMapWith0s(Product.values());
        machineCoins = initializeMapWith0s(Coin.values());
        insertedCoins = initializeMapWith0s(Coin.values());
        changeTray = initializeMapWith0s(Coin.values());
    }

    private <E extends Enum<E>> Map<E, Integer> initializeMapWith0s(E[] keys) {
        Map<E, Integer> map = new HashMap<>();
        for (E key : keys) {
            map.put(key, 0);
        }
        return map;
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
            Optional<Map<Coin, Integer>> change = makeChange(rest);
            change.ifPresent((c) -> buyProduct(product, c));
        }
    }

    private void buyProduct(Product product, Map<Coin, Integer> change) {
        products.merge(product, -1, Integer::sum);

        machineCoins.merge(Coin.QUARTER, -change.get(Coin.QUARTER), Integer::sum);
        machineCoins.merge(Coin.DIME, -change.get(Coin.DIME), Integer::sum);
        machineCoins.merge(Coin.NICKEL, -change.get(Coin.NICKEL), Integer::sum);

        machineCoins.merge(Coin.QUARTER, insertedCoins.get(Coin.QUARTER), Integer::sum);
        machineCoins.merge(Coin.DIME, insertedCoins.get(Coin.DIME), Integer::sum);
        machineCoins.merge(Coin.NICKEL, insertedCoins.get(Coin.NICKEL), Integer::sum);
    }

    private Optional<Map<Coin, Integer>> makeChange(int rest) {
        Map<Coin, Integer> changeToReturn = initializeMapWith0s(Coin.values());

        if (rest == 0) return Optional.of(changeToReturn);

        int maxQuartersNeeded =
                Math.min(machineCoins.get(Coin.QUARTER), rest / Coin.QUARTER.value);

        int maxDimesNeeded =
                Math.min(machineCoins.get(Coin.DIME), rest / Coin.DIME.value);

        int maxNickelsNeeded =
                Math.min(machineCoins.get(Coin.NICKEL), rest / Coin.NICKEL.value);

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

    public int getInsertedValue() {
        return insertedValue;
    }

    public int getQuarters() {
        return machineCoins.computeIfAbsent(Coin.QUARTER, this::zero);
    }

    private Integer zero(Coin coin) {
        return 0;
    }

    public int getDimes() {
        return machineCoins.get(Coin.DIME);
    }

    public int getNickels() {
        return machineCoins.get(Coin.NICKEL);
    }

    public Map<Coin, Integer> getMachineCoins() {
        return machineCoins;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void returnCoins() {
        insertedCoins.replace(Coin.QUARTER, 0);
        insertedCoins.replace(Coin.DIME, 0);
        insertedCoins.replace(Coin.NICKEL, 0);
        insertedValue = 0;
    }
}
