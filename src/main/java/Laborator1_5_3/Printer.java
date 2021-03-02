package Laborator1_5_3;

public class Printer {
    private Integer id;
    private Boolean occupied;

    public Printer(int id) {
        this.id = id;
        this.occupied = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public synchronized void executePrint(String string, Integer time) {
        while (this.isOccupied()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.setOccupied(true);
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.setOccupied(false);
        System.out.println(string);
        notifyAll();
    }
}
