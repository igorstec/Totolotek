package totolotek.kupon;

import java.util.*;

/**
 * Blankiet jest formularzem na podstawie którego kolektura generuje kupon.
 * Zawiera do 8 pól zakładów oraz informację o liczbie losowań.
 */
public class Blankiet {
    private final List<PoleBlankietu> pola; // zawsze 8 pól (indeksy 0..7)
    private final Set<Integer> zaznaczoneLosowania; // liczby 1..10

    /**
     * Tworzy pusty blankiet (wszystkie pola puste, brak zaznaczeń liczby losowań).
     */
    public Blankiet() {
        this.pola = new ArrayList<>(Collections.nCopies(8, null));
        this.zaznaczoneLosowania = new HashSet<>();
    }

    /**
     * Ustawia pole zakładu (indeks od 0 do 7).
     */
    public void ustawPole(int indeks, PoleBlankietu pole) {
        if (indeks < 0 || indeks >= 8) throw new IllegalArgumentException("Indeks pola: 0..7");
        pola.set(indeks, pole);
    }

    /**
     * Zaznacza liczbę losowań (od 1 do 10).
     */
    public void zaznaczLiczbeLosowan(int liczba) {
        if (liczba < 1 || liczba > 10) throw new IllegalArgumentException("Liczba losowań: 1..10");
        zaznaczoneLosowania.add(liczba);
    }

    /**
     * Zwraca listę poprawnych zakładów wygenerowanych z blankietu.
     * Pomija anulowane pola i pola z inną niż 6 liczbą.
     */
    public List<Zaklad> generujZaklady() {
        List<Zaklad> zaklady = new ArrayList<>();
        for (PoleBlankietu pole : pola) {
            if (pole == null) continue;
            if (pole.czyPoprawnyZaklad()) {
                zaklady.add(pole.generujZaklad());
            }
        }
        return zaklady;
    }

    /**
     * Zwraca liczbę losowań do których ma trafić kupon.
     * Jeśli zaznaczono kilka, wybiera największą; jeśli żadnej, domyślnie 1.
     */
    public int getLiczbaLosowan() {
        return zaznaczoneLosowania.stream().max(Integer::compareTo).orElse(1);
    }

    /**
     * Zwraca niezmienną listę pól blankietu.
     */
    public List<PoleBlankietu> getPola() {
        return Collections.unmodifiableList(pola);
    }

    /**
     * Zwraca niezmienny zbiór zaznaczonych liczb losowań.
     */
    public Set<Integer> getZaznaczoneLosowania() {
        return Collections.unmodifiableSet(zaznaczoneLosowania);
    }
}
