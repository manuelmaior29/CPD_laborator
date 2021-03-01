package Laborator1_5_4;

public class Laborator1_5_4 {
    public static void main(String[] args) {
        String string = new String("Exemplu");

        StringParser stringParser1 = new StringParser(1, string, true);
        StringParser stringParser2 = new StringParser(2, string, false);
        stringParser1.start();
        stringParser2.start();
    }
}
