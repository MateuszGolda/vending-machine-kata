package com.codecool.machine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class VendingMachineTest {
    static VendingMachine machine;

    @BeforeEach
    void init() {
        machine = new VendingMachine();
        setMachineMoney(1, 2, 3);
        setMachineProducts(1, 2, 3);
    }

    @Test
    void should_haveAddedMoneyAndCoins() {
        assertAll(
                () -> assertEquals(1, machine.getQuarters()),
                () -> assertEquals(2, machine.getDimes()),
                () -> assertEquals(3, machine.getNickels()),

                () -> assertEquals(1, machine.getProducts().get(Product.COLA)),
                () -> assertEquals(2, machine.getProducts().get(Product.CANDY)),
                () -> assertEquals(3, machine.getProducts().get(Product.CHIPS))
        );
    }

    @Test
    void should_insertValidCoins() {
        machine.insertCoin(500, 50); // nickel
        machine.insertCoin(1000, 100); // dime
        machine.insertCoin(2525, 260); // quarter
        assertEquals(40, machine.getInsertedValue());
    }

    @Test
    void should_notInsertInvalidCoins() {
        machine.insertCoin(600, 40);
        machine.insertCoin(1200, 200);
        machine.insertCoin(1500, 90);
        assertEquals(0, machine.getInsertedValue());
    }

    @Test
    void should_sellProductWhenEnoughMoneyAndNoChange() {
        insert25Cents(2);
        machine.selectProduct(Product.CHIPS);

        assertAll(
                () -> assertEquals(2, machine.getProducts().get(Product.CHIPS)),
                () -> assertEquals(3, machine.getQuarters()),
                () -> assertEquals(2, machine.getDimes()),
                () -> assertEquals(3, machine.getNickels())
        );
    }

    @Test
    void should_useSmallerCoinsWhenNoQuarters() {
        setMachineMoney(0, 0, 10);
        insert25Cents(3);
        machine.selectProduct(Product.CANDY);

        assertAll(
                () -> assertEquals(1, machine.getProducts().get(Product.CANDY)),
                () -> assertEquals(3, machine.getQuarters()),
                () -> assertEquals(0, machine.getDimes()),
                () -> assertEquals(8, machine.getNickels())
        );
    }

    @Test
    void should_returnCoinsWhenReturnCoinsCalled() {
        insert25Cents(3);
        machine.returnCoins();
        assertAll(
                () -> assertEquals(1, machine.getQuarters()),
                () -> assertEquals(2, machine.getDimes()),
                () -> assertEquals(3, machine.getNickels())
        );
    }


    void setMachineMoney(int quarters, int dimes, int nickels) {
        Map<Coin, Integer> coins = machine.getMachineCoins();
        coins.put(Coin.QUARTER, quarters);
        coins.put(Coin.DIME, dimes);
        coins.put(Coin.NICKEL, nickels);
    }

    void setMachineProducts(int cola, int candy, int chips) {
        Map<Product, Integer> products = machine.getProducts();
        products.put(Product.COLA, cola);
        products.put(Product.CANDY, candy);
        products.put(Product.CHIPS, chips);
    }

    void insertCoins(int quantity, int weight, int size) {
        for (int i = 0; i < quantity; i++) {
            machine.insertCoin(weight, size);
        }
    }

    void insert5Cents(int quantity) {
        insertCoins(quantity, 500, 50); // nickel
    }

    void insert10Cents(int quantity) {
        insertCoins(quantity, 1000, 100); // dime
    }

    void insert25Cents(int quantity) {
        insertCoins(quantity, 2500, 250); // quarter
    }
}
