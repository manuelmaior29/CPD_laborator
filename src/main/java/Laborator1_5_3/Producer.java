package Laborator1_5_3;

import java.util.Random;

public class Producer extends Thread {
    public int id;
    public Random random;
    public Boolean printerOccupied;

    public Producer() {}

    public Producer(int id, Boolean printerOccupied) {
        this.id = id;
        this.random = new Random();
        this.printerOccupied = printerOccupied;
    }

    @Override
    public void run() {
        int counter = 0;
        while(true) {
            counter++;
            this.print(counter);
        }
    }

    public synchronized void print(int counter) {
        System.out.println("Thread: " + this.id + " Counter: " + counter);
        while (this.printerOccupied) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.printerOccupied = true;
        int time = Math.abs(random.nextInt()) % 2000 + 5000;
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Thread " + id + " printed in " + (float) time / 1000 + " seconds. Counter: " + counter);
        this.printerOccupied = false;
        notifyAll();
    }
}
