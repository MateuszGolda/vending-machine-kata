package com.codecool.machine;

public class SquareDigits {
    public static void main(String[] args) {
        int n = 3212;
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            sb.insert(0, (n % 10) * (n % 10));
            n /= 10;
        }
        System.out.println(Integer.parseInt(sb.toString()));
    }
}
