package com.codecool.machine;

import com.codecool.machine.vending_machine.Coin;
import com.codecool.machine.vending_machine.Display;
import com.codecool.machine.vending_machine.Product;
import com.codecool.machine.vending_machine.VendingMachine;

import java.util.Map;
import java.util.Scanner;

public class App {
    static Display display;
    static VendingMachine machine;
    static boolean isRunning = true;

    public static void main(String[] args) {
        initializeMachine();

        display = new Display(machine);

        display.startScreen();
        Scanner sc = new Scanner(System.in);

        while (isRunning) {
            display.machineDisplay();
            machine.resetState();
            String[] request = sc.nextLine().toLowerCase().split(" ");
            try {
                processRequest(request);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void initializeMachine() {
        machine = new VendingMachine();
        Map<Coin, Integer> coins = machine.getMachineCoins();
        coins.put(Coin.QUARTER, 1);
        coins.put(Coin.DIME, 0);
        coins.put(Coin.NICKEL, 1);

        Map<Product, Integer> products = machine.getProducts();
        products.put(Product.COLA, 0);
        products.put(Product.CANDY, 2);
        products.put(Product.CHIPS, 2);
    }

    private static void processRequest(String[] request) {
        if (request.length == 0) throw new IllegalArgumentException("Empty input");
        switch (request[0]) {
            case "exit" -> isRunning = false;
            case "select" -> {
                if (request.length < 2) throw new IllegalArgumentException("To buy cola type 'select cola'");
                Product product = Product.valueOf(request[1].toUpperCase());
                machine.selectProduct(product);
            }
            case "take" -> {
                if (request.length < 2) throw new IllegalArgumentException("Type 'take product' or 'take change'");
                if (request[1].equals("product")) {
                    display.displayProductTray();
                    machine.takeProduct();
                } else if (request[1].equals("change")) {
                    display.displayChangeTray();
                    machine.takeChange();
                } else {
                    throw new IllegalArgumentException("Type 'take product' or 'take change'");
                }
            }
            case "return" -> machine.returnCoins();
            case "insert" -> {
                if (request.length < 3) throw new IllegalArgumentException("To insert quarter type 'insert 2500 250'");
                machine.insertCoin(Integer.parseInt(request[1]), Integer.parseInt(request[2]));
            }
            default -> throw new IllegalArgumentException("Incorrect command");
        }
    }
}
