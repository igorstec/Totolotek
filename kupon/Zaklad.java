package totolotek.kupon;

import java.util.*;

/**
 * Klasa Zaklad reprezentuje pojedynczy zakład w grze Totolotek.
 * Zakład to zestaw 6 unikalnych liczb z zakresu 1-49.
 */
public class Zaklad {
    public static final int LICZBA_TYPÓW = 6;
    public static final int MIN_LICZBA = 1;
    public static final int MAX_LICZBA = 49;

    private final Set<Integer> liczby;

    /**
     * Tworzy zakład na podstawie przekazanego zbioru liczb.
     * @param liczby zbiór 6 unikalnych liczb z zakresu 1-49
     * @throws IllegalArgumentException jeśli zbiór nie zawiera dokładnie 6 liczb lub liczby są spoza zakresu
     */
    public Zaklad(Set<Integer> liczby) {
        if (liczby == null || liczby.size() != LICZBA_TYPÓW)
            throw new IllegalArgumentException("Zakład musi zawierać dokładnie 6 liczb.");
        for (int liczba : liczby) {
            if (liczba < MIN_LICZBA || liczba > MAX_LICZBA)
                throw new IllegalArgumentException("Liczby muszą być z zakresu 1-49.");
        }
        // TreeSet automatycznie sortuje i eliminuje duplikaty
        this.liczby = Collections.unmodifiableSet(new TreeSet<>(liczby));
    }

    //statyczna funkcja do tworzenia zakladow chybil-trafil
    public static Zaklad chybilTraf() {
        Set<Integer> liczby = new TreeSet<>();
        Random rand = new Random();

        while (liczby.size() < LICZBA_TYPÓW) {
            int liczba = rand.nextInt(MAX_LICZBA - MIN_LICZBA + 1) + MIN_LICZBA;
            liczby.add(liczba);
        }

        return new Zaklad(liczby);
    }

    /**
     * Zwraca niezmienny zbiór liczb obstawionych w tym zakładzie.
     */
    public Set<Integer> getLiczby() {
        return liczby;
    }

    /**
     * Oblicza liczbę trafień tego zakładu względem podanego zbioru wylosowanych liczb.
     * @param wylosowaneLiczby zbiór 6 liczb wylosowanych w danym losowaniu
     * @return liczba trafionych liczb (od 0 do 6)
     */
    public int liczbaTrafien(Set<Integer> wylosowaneLiczby) {
        int trafienia = 0;
        for (int liczba : liczby) {
            if (wylosowaneLiczby.contains(liczba)) {
                trafienia++;
            }
        }
        return trafienia;
    }

    /**
     * Zwraca tekstową reprezentację zakładu (posortowane liczby, wyrównane do prawej).
     */
    @Override
    public String toString() {
        return liczby.stream()
                .sorted()
                .map(l -> String.format("%2d", l))
                .reduce((a, b) -> a + " " + b)
                .orElse("");
    }

    /**
     * Porównuje zakłady po zestawie liczb.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Zaklad other)) return false;
        return liczby.equals(other.liczby);
    }

    @Override
    public int hashCode() {
        return liczby.hashCode();
    }
}
