package Laborator1_5_3;

import java.util.ArrayList;
import java.util.List;

public class Laborator1_5_3 {
    public static void main(String[] args) {
        List<Producer> producers = new ArrayList<Producer>();
        Boolean printerStatus = false;
        for (int i = 0; i < 8; i++) {
            producers.add(new Producer(i, printerStatus));
            producers.get(i).start();
        }
    }
}
