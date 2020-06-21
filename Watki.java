package main;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

class Generuj {
    final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    int n = 8;
    int licznik_metoda1=0;
    int licznik_metoda2=0;
    int licznik_metoda3=0;
    HashMap<String, Integer> map_kolekcja = new HashMap<String, Integer>(); //Key, Value

    // ------------------------generator_ 1-----------------------------------------------------------------------------
    void generuj_metoda1() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = this.n;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
//        System.out.println(generatedString);
        licz_ile(1);
        wstaw_do_kolekcji(generatedString);
    }


    // --------------------------generator_2----------------------------------------------------------------------------
    SecureRandom random = new SecureRandom();

    void generuj_metoda2() {
        StringBuilder sb = new StringBuilder(this.n);
        for (int i = 0; i < this.n; i++) {
            // 0-62 (exclusive), random returns 0-61
            int rndCharAt = random.nextInt(CHAR_LOWER.length());
            char rndChar = CHAR_LOWER.charAt(rndCharAt);
//            System.out.println(rndChar);
//            System.out.println(rndChar);
            // debug
//            System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);
            sb.append(rndChar);
        }
//        System.out.println(sb);
        licz_ile(2);
        wstaw_do_kolekcji(sb.toString());

    }


    // -----------------------generator_3-------------------------------------------------------------------------------
    void generuj_metoda3() {
        // create StringBuffer size of AlphaNumericString
        StringBuilder sbc = new StringBuilder(this.n);

        for (int i = 0; i < this.n; i++) {
            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(CHAR_LOWER.length()
                    * Math.random());
            // add Character one by one in end of sb
            sbc.append(CHAR_LOWER
                    .charAt(index));
        }
//        System.out.println(sbc);
        licz_ile(3);
        wstaw_do_kolekcji(sbc.toString());
    }

    // metoda wstawiajaca wygenerowane słowo do kolekcji
    void wstaw_do_kolekcji(String slowo){
        System.out.println(" To ja - wątek: " + Thread.currentThread());
        System.out.println(slowo);
        System.out.println("Czy się powtarza? "+ map_kolekcja.containsKey(slowo));
        if (map_kolekcja.containsKey(slowo)) {
            System.out.println("UPS! TO JUZ BYLOOOOO!!!!");
            for (Map.Entry<String, Integer> entry : map_kolekcja.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                if (slowo.equals(key)) {
                    value += 1;
                    map_kolekcja.put(key, value + 1);
                    System.out.println("Słowo: " + key + " | Ilosc powtorzen: " + value);
                }
            }
        } else {
            map_kolekcja.put(slowo, 1);
        }
    }


    // metoda zwracajaca ilosc wygenerowanych słów przez daną metodę
    void licz_ile(int ktora){
        if(ktora == 1)  this.licznik_metoda1=licznik_metoda1+1;
        if(ktora == 2)  this.licznik_metoda2=licznik_metoda2+1;
        if(ktora == 3)  this.licznik_metoda3=licznik_metoda3+1;
    }

}


class Generator implements Runnable {
    Generuj g;
    Thread t;

    public Generator(Generuj g) {
        this.g = g;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        synchronized (g) {
            for(int i=0; i<15000;i++){
                g.generuj_metoda1();
                g.generuj_metoda2();
                g.generuj_metoda3();
            }
        }
    }
}


public class Watki {
    public static <policzono> void main(String[] args){
        Generuj g = new Generuj();

        Generator j1 = new Generator(g);
        Generator j2 = new Generator(g);
        Generator j3 = new Generator(g);
        Generator j4 = new Generator(g);
        Generator j5 = new Generator(g);
        Generator j6 = new Generator(g);

        try {
            j1.t.join();
            j2.t.join();
            j3.t.join();
            j4.t.join();
            j5.t.join();
            j6.t.join();


            AtomicInteger policzono= new AtomicInteger();
            g.map_kolekcja.entrySet().forEach(entry->{
                System.out.println(entry.getKey() + " " + entry.getValue());
                int tmp = entry.getValue();
                policzono.set(policzono.get() + tmp);
            });
            System.out.println("Policzono liczniki w kolekcji. Jest ich: " + policzono);

            System.out.println("Metoda 1 wygenerowała słów: " + g.licznik_metoda1);
            System.out.println("Metoda 2 wygenerowała słów: " + g.licznik_metoda2);
            System.out.println("Metoda 3 wygenerowała słów: " + g.licznik_metoda3);
        } catch (InterruptedException e) {
            System.out.println("Przerwano!");
        }





    }
}



