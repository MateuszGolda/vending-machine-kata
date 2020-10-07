package com.codecool.machine.vending_machine;

import java.util.Scanner;

public class Display {
    private final VendingMachine machine;

    public Display(VendingMachine vendingMachine) {
        this.machine = vendingMachine;
    }

    public void startScreen() {
        System.out.println("Welcome to vending machine simulator!\n");
        displayCoins();
        displayProducts();
        displayInstruction();
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    private void displayCoins() {
        System.out.println("Accepted coins:");
        for (String column : new String[]{"name", "value", "weight", "size", "available"}) {
            System.out.printf("%-9s", column);
        }
        System.out.println();
        for (Coin coin : Coin.values()) {
            if (coin.equals(Coin.INVALID)) continue;
            System.out.println(String.format("%-9s", coin.toString())
                    + String.format("%-9s", coin.value)
                    + String.format("%-9s", coin.weight)
                    + String.format("%-9s", coin.size)
                    + String.format("%-9s", machine.getMachineCoins().getOrDefault(coin, 0))
            );
        }
        System.out.println();
    }

    private void displayProducts() {
        System.out.println("Available products:");
        for (String column : new String[]{"name", "cost", "available"}) {
            System.out.printf("%-9s", column);
        }
        System.out.println();
        for (Product product : Product.values()) {
            System.out.println(String.format("%-9s", product.toString())
                    + String.format("%-9s", product.cost)
                    + String.format("%-9s", machine.getProducts().getOrDefault(product, 0))
            );
        }
        System.out.println();
    }

    private void displayInstruction() {
        System.out.println("""
                To interact with the machine, type:
                insert $weight $size : to insert coin
                return               : to return inserted coins
                select $productName  : to buy product
                take product         : to take bought product
                take change          : to take change
                exit                 : to exit program
                """);
    }

    public void machineDisplay() {
        switch (machine.getState()) {
            case INSERT_COIN -> System.out.println("INSERT COIN");
            case SOLD_OUT -> System.out.println("SOLD OUT");
            case COIN_INSERTED -> System.out.println(machine.getInsertedValue());
            case PRODUCT_BOUGHT -> System.out.println("THANK YOU");
            case INSUFFICIENT_FUNDS -> System.out.println("INSUFFICIENT FUNDS");
            case CANT_MAKE_CHANGE -> System.out.println("CAN'T MAKE CHANGE, INSERT EXACT VALUE");
        }
    }

    public void displayChangeTray() {
        System.out.println("Change:");
        for (String column : new String[]{"name", "coins"}) {
            System.out.printf("%-9s", column);
        }
        System.out.println();
        for (Coin coin : Coin.values()) {
            if (coin.equals(Coin.INVALID)) continue;
            if (machine.getChangeTray().getOrDefault(coin, 0) == 0) continue;
            System.out.println(String.format("%-9s", coin.toString())
                    + String.format("%-9s", machine.getChangeTray().get(coin))
            );
        }
        System.out.println();
    }

    public void displayProductTray() {
        System.out.println("Bought products:");
        for (String column : new String[]{"name", "cost", "bought"}) {
            System.out.printf("%-9s", column);
        }
        System.out.println();
        for (Product product : Product.values()) {
            if (machine.getProductTray().getOrDefault(product, 0) == 0) continue;
            System.out.println(String.format("%-9s", product.toString())
                    + String.format("%-9s", product.cost)
                    + String.format("%-9s", machine.getProductTray().get(product))
            );
        }
        System.out.println();
    }
}
