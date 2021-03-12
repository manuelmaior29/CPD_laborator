package Laborator1_5_4;

public class StringParser extends Thread {
    private Integer id;
    private String string;
    private Boolean fromStart;
    private int index;

    public StringParser(Integer id, String string, Boolean fromStart) {
        this.id = id;
        this.string = string;
        this.fromStart = fromStart;
        if (fromStart)
            index = 0;
        else
            index = string.length() - 1;
    }

    @Override
    public void run() {
        if (fromStart)
            while(index < this.string.length()) {
                System.out.println("Thread " + this.id + ":" + this.string.charAt(index++));
            }
        else
            while(index >= 0) {
                System.out.println("Thread " + this.id + ":" + this.string.charAt(index--));
            }

    }
}
