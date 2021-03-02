package Laborator1_5_3;

import java.util.ArrayList;
import java.util.List;

public class Laborator1_5_3 {
    public static void main(String[] args) {
        Printer printer = new Printer(1);
        for (int i = 0; i < 8; i++) {
            Producer producer = new Producer(i, printer);
            producer.start();
        }
    }
}
