package totolotek.kupon;

import totolotek.core.Centrala;
import totolotek.core.Kolektura;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Klasa reprezentująca kupon Totolotka.
 * Kupon zawiera zestaw zakładów, identyfikator, listę numerów losowań,
 * cenę brutto, podatek oraz informację o kolekturze, w której został zakupiony.
 * Jest niezbędny do odbioru ewentualnych wygranych.
 */
public class Kupon {
    /** Unikalny identyfikator kuponu */
    private final String identyfikator;
    /** Lista zakładów przypisanych do kuponu */
    private final List<Zaklad> zaklady;
    /** Liczba losowań, na które obowiązuje kupon */
    private final int liczbaLosowan;
    /** Lista numerów losowań, w których bierze udział kupon */
    private final List<Integer> numeryLosowan;
    /** Cena brutto kuponu (w groszach) */
    private final long cenaBrutto;
    /** Podatek od kuponu (w groszach) */
    private final long podatek;
    /** Czy kupon został już zrealizowany (odebrano wygraną) */
    private boolean zrealizowany;
    /** Kolektura, w której kupon został zakupiony */
    private Kolektura kolektura;

    /**
     * Tworzy kupon na podstawie listy zakładów, liczby losowań, centrali i kolektury.
     * Generuje listę numerów losowań od najbliższego.
     *
     * @param id unikalny identyfikator kuponu
     * @param zaklady lista zakładów
     * @param liczbaLosowan liczba losowań (musi być > 0)
     * @param centrala centrala Totolotka (do pobrania numeru najbliższego losowania)
     * @param kolektura kolektura, w której kupon został zakupiony
     * @throws IllegalArgumentException jeśli id, zaklady są null lub liczbaLosowan <= 0
     */
    public Kupon(String id, List<Zaklad> zaklady, int liczbaLosowan, Centrala centrala, Kolektura kolektura) {
        if (id == null || zaklady == null || liczbaLosowan <= 0) {
            throw new IllegalArgumentException();
        }
        this.zaklady = zaklady;
        this.liczbaLosowan = liczbaLosowan;
        this.zrealizowany = false;
        this.identyfikator = id;
        this.cenaBrutto = liczbaLosowan * 300L * zaklady.size();
        this.podatek = cenaBrutto / 5;
        this.kolektura = kolektura;
        int start = centrala.getNajblizszeLosowanie();
        this.numeryLosowan = IntStream.range(start, start + liczbaLosowan)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Alternatywny konstruktor kuponu – przydatny do testów.
     * Pozwala określić bezpośrednio listę numerów losowań.
     *
     * @param id unikalny identyfikator kuponu
     * @param zaklady lista zakładów
     * @param numeryLosowan lista numerów losowań (nie może być pusta)
     * @throws IllegalArgumentException jeśli id, zaklady lub numeryLosowan są null lub puste
     */
    public Kupon(String id, List<Zaklad> zaklady, List<Integer> numeryLosowan) {
        if (id == null || zaklady == null || numeryLosowan == null || numeryLosowan.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.zaklady = zaklady;
        this.liczbaLosowan = numeryLosowan.size();
        this.zrealizowany = false;
        this.identyfikator = id;
        this.cenaBrutto = liczbaLosowan * 300L * zaklady.size();
        this.podatek = cenaBrutto / 5;
        this.numeryLosowan = numeryLosowan;
    }

    /**
     * Zwraca unikalny identyfikator kuponu.
     * @return identyfikator kuponu
     */
    public String getIdentyfikator() {
        return identyfikator;
    }

    /**
     * Zwraca cenę brutto kuponu (w groszach).
     * @return cena brutto
     */
    public long getCenaBrutto() {
        return cenaBrutto;
    }

    /**
     * Zwraca liczbę zakładów na kuponie.
     * @return liczba zakładów
     */
    public int getIleZakladow() {
        return zaklady.size();
    }

    /**
     * Zwraca liczbę losowań, na które obowiązuje kupon.
     * @return liczba losowań
     */
    public int getLiczbaLosowan() {
        return liczbaLosowan;
    }

    /**
     * Zwraca kolekturę, w której kupon został zakupiony.
     * @return kolektura
     */
    public Kolektura getKolektura() {
        return this.kolektura;
    }

    /**
     * Zwraca numer ostatniego losowania, na które obowiązuje kupon.
     * @return numer ostatniego losowania
     */
    public int getOstatnieLosowanieKuponu() {
        return numeryLosowan.getLast();
    }

    /**
     * Zwraca wartość podatku od kuponu (w groszach).
     * @return podatek
     */
    public long getPodatek() {
        return podatek;
    }

    /**
     * Sprawdza, czy kupon został już zrealizowany.
     * @return true jeśli zrealizowany, false w przeciwnym razie
     */
    public boolean czyZrealizowany() {
        return zrealizowany;
    }

    /**
     * Oznacza kupon jako zrealizowany (odebrano wygraną).
     */
    public void oznaczJakoZrealizowany() {
        this.zrealizowany = true;
    }

    /**
     * Zwraca tekstową reprezentację kuponu zgodną z wymaganiami zadania.
     * Zawiera identyfikator, zakłady, liczbę losowań, numery losowań i cenę.
     * @return napis opisujący kupon
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // 1. Identyfikator kuponu
        sb.append("KUPON NR ").append(identyfikator).append('\n');

        // 2. Ponumerowana lista zakładów (każdy zestaw liczb wyrównany do prawej)
        int numerZakladu = 1;
        for (Zaklad zaklad : zaklady) {
            sb.append(numerZakladu).append(":");
            for (Integer liczba : zaklad.getLiczby()) {
                sb.append(String.format(" %2d", liczba));
            }
            sb.append('\n');
            numerZakladu++;
        }

        // 3. Liczba losowań
        sb.append("LICZBA LOSOWAŃ: ").append(liczbaLosowan).append('\n');

        // 4. Lista numerów losowań w jednym wierszu
        sb.append("NUMERY LOSOWAŃ:\n");
        for (int numerLosowania : numeryLosowan) {
            sb.append(" ").append(numerLosowania);
        }
        sb.append('\n');

        // 5. Cena brutto kuponu
        sb.append(String.format("CENA: %d zł %02d gr", cenaBrutto / 100, cenaBrutto % 100)).append('\n');

        return sb.toString();
    }

    /**
     * Zwraca niezmienną listę zakładów przypisanych do kuponu.
     * @return lista zakładów
     */
    public List<Zaklad> getZaklady(){
        return Collections.unmodifiableList(zaklady);
    }

    /**
     * Zwraca niezmienną listę numerów losowań, na które obowiązuje kupon.
     * @return lista numerów losowań
     */
    public List<Integer> getNumeryLosowan() {
        return Collections.unmodifiableList(numeryLosowan);
    }
}
