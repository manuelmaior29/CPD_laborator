package Laborator1_5_3;

import java.util.Random;

public class Producer extends Thread {
    public int id;
    public Random random;
    public Printer printer;

    public Producer() {}

    public Producer(int id, Printer printer) {
        this.id = id;
        this.random = new Random();
        this.printer = printer;
    }

    @Override
    public void run() {
        while(true) {
            this.usePrinter(Math.abs(random.nextInt()) % 2000 + 1000);
        }
    }

    public synchronized void usePrinter(int time) {
        this.printer.executePrint("Producer " + this.id + " printed in " + time + " ms.", time);
    }
}
