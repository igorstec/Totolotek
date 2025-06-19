package totolotek.losowanie;

import java.util.*;

/**
 * Reprezentuje pojedyncze oficjalne losowanie Totolotka.
 * Przechowuje unikalny numer losowania oraz wynik (6 różnych liczb 1-49, rosnąco).
 */
public class Losowanie {
    private final int numerLosowania;
    private final SortedSet<Integer> wynik; // TreeSet - automatycznie rosnąco
    public static final int LICZBA_LOSOWANYCH_LICZB = 6;

    /**
     * Tworzy losowanie z automatycznie wylosowanym wynikiem.
     *
     * @param numerLosowania unikalny numer porządkowy (od 1)
     */
    public Losowanie(int numerLosowania) {
        this.numerLosowania = numerLosowania;
        this.wynik = wylosujWynik();
    }


    /**
     * Zwraca numer tego losowania.
     */
    public int getNumerLosowania() {
        return numerLosowania;
    }

    /**
     * Zwraca niezmienny zbiór 6 wylosowanych liczb w porządku rosnącym.
     */
    public SortedSet<Integer> getWynik() {
        return Collections.unmodifiableSortedSet(wynik);
    }

    /**
     * Wypisuje losowanie w wymaganym formacie.
     */
    public void wypiszLosowanie() {
        System.out.println(this);
    }

    /**
     * Zwraca tekstową reprezentację losowania zgodną z wymaganiami.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Losowanie nr ").append(numerLosowania).append("\n");
        sb.append("Wyniki: ");
        for (int liczba : wynik) {
            // Wyrównanie do prawej: jednocyfrowe z dodatkową spacją
            if (liczba < 10) {
                sb.append(" ");
            }
            sb.append(liczba).append(" ");
        }
        return sb.toString();
    }

    /**
     * Losuje 6 różnych liczb z zakresu 1-49 i zwraca je jako posortowany zbiór.
     */
    private SortedSet<Integer> wylosujWynik() {
        Random rand = new Random();
        Set<Integer> liczby = new HashSet<>();
        while (liczby.size() < LICZBA_LOSOWANYCH_LICZB) {
            int liczba = rand.nextInt(49) + 1; // 1..49
            liczby.add(liczba);
        }
        return new TreeSet<>(liczby); // automatycznie rosnąco
    }
}
