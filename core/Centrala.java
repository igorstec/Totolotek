package totolotek.core;

import totolotek.losowanie.Losowanie;
import totolotek.kupon.Kupon;
import totolotek.kupon.Zaklad;
import totolotek.finanse.BudzetPanstwa;

import java.util.*;

/**
 * Klasa Centrala zarządza główną logiką systemu Totolotka.
 * Odpowiada za:
 * - przeprowadzanie losowań i zarządzanie ich wynikami
 * - rozliczanie finansów i podatków
 * - przechowywanie informacji o kolekturach
 * - stosowanie strategii podziału nagród
 * - obsługę kumulacji nagród I stopnia
 */

public class Centrala {
    private long srodkiFinansowe;
    private final BudzetPanstwa budzetPanstwa;
    private final List<Kolektura> kolektury;
    private final TreeMap<Integer, Losowanie> losowania; // numer losowania -> losowanie
    private final TreeMap<Integer, BazaWygranych> bazeWygranych;// numer losowania -> pula nagród
    private long kumulacjaIstopnia;
    private StrategiaNagrod strategiaNagrod;

    public Centrala(long srodkiPoczatkowe, BudzetPanstwa budzetPanstwa) {
        this.srodkiFinansowe = srodkiPoczatkowe;
        this.budzetPanstwa = budzetPanstwa;
        this.kolektury = new ArrayList<>();
        this.losowania = new TreeMap<>();
        this.bazeWygranych = new TreeMap<>();
        this.kumulacjaIstopnia = 0L;
        this.strategiaNagrod = new StandardowaStrategiaNagrod();
    }

    public int getNajblizszeLosowanie() {
        return losowania.size() + 1;
    }

    public BudzetPanstwa getBudzetPanstwa() {
        return budzetPanstwa;
    }

    /** Pozwala na podmianę strategii rozliczania nagród*/
    public void setStrategiaNagrod(StrategiaNagrod strategia) {
        this.strategiaNagrod = strategia;
    }

    public void dodajKolekture(Kolektura kolektura) {
        kolektury.add(kolektura);
    }

    /**
     * Przeprowadza nowe losowanie:
     * - Generuje nowy numer losowania
     * - Zbiera kupony ze wszystkich kolektur
     * - Oblicza wpływy i podatki
     * - Określa liczbę wygranych każdego stopnia
     * - Oblicza pule nagród poprzez strategię
     * - Aktualizuje stan kumulacji
     * @throws IllegalStateException jeśli losowanie o tym numerze już istnieje
     */

    public void przeprowadzLosowanie() {
        int numerLosowania = losowania.size() + 1;
        if (losowania.containsKey(numerLosowania)) {
            throw new IllegalStateException("Losowanie o numerze " + numerLosowania + " już istnieje!");
        }
        Losowanie losowanie = new Losowanie(numerLosowania);
        losowania.put(numerLosowania, losowanie);

        // Zbierz kupony na to losowanie
        List<Kupon> kupony = new ArrayList<>();
        for (Kolektura kolektura : kolektury) {
            kupony.addAll(kolektura.kuponyNaLosowanie(numerLosowania));
        }

        long sumaWplat = kupony.stream().mapToLong(Kupon::getIleZakladow).sum() * 300L;
        long sumaPodatku = sumaWplat / 5;

        srodkiFinansowe += (sumaWplat - sumaPodatku);
        budzetPanstwa.pobierzPodatek(sumaPodatku);

        // Liczba trafień: stopień -> liczba
        Map<Integer, Integer> liczbaWygranych = new HashMap<>();
        for (int stopien = 3; stopien <= 6; stopien++) liczbaWygranych.put(stopien, 0);

        Set<Integer> wylosowane = losowanie.getWynik();


        for (Kupon kupon : kupony) {
            for (Zaklad zaklad : kupon.getZaklady()) {
                int trafienia = liczbaTrafien(zaklad, wylosowane);
                if (trafienia >= 3 && trafienia <= 6) {
                    liczbaWygranych.put(trafienia, liczbaWygranych.get(trafienia) + 1);
                }
            }
        }

        // Oblicz pule nagród przez strategię
        BazaWygranych bazaWygranych = strategiaNagrod.ustalBazeWygranych(
                sumaWplat, sumaPodatku, kumulacjaIstopnia, liczbaWygranych
        );

        bazeWygranych.put(numerLosowania, bazaWygranych);

        // Zarządzanie kumulacją
        if (liczbaWygranych.get(6) == 0) {
            kumulacjaIstopnia = bazaWygranych.getPulaI();
        } else {
            kumulacjaIstopnia = 0;
        }
    }

    public int liczbaTrafien(Zaklad zaklad, Set<Integer> wylosowane) {
        return (int) zaklad.getLiczby().stream().filter(wylosowane::contains).count();
    }

    // Publiczny dostęp do losowań i pul nagród
    public SortedMap<Integer, Losowanie> getLosowania() {
        return Collections.unmodifiableSortedMap(losowania);
    }

    public void wypiszWynikiLosowan() {
        for (Map.Entry<Integer, Losowanie> wpis : losowania.entrySet()) {
            int numerLosowania = wpis.getKey();
            Losowanie daneLosowanie = wpis.getValue();
            BazaWygranych nagrody = bazeWygranych.get(numerLosowania);

            System.out.println("Losowanie nr " + numerLosowania);
            System.out.print("Wyniki: ");
            daneLosowanie.getWynik().forEach(liczba -> System.out.printf("%2d ", liczba));
            System.out.println();

            if (nagrody != null) {
                System.out.println("PULA NAGRÓD:");
                System.out.printf("I stopień:   %d x %s (łączna pula: %s)\n",
                        nagrody.getLiczbaI(),
                        nagrody.getLiczbaI() > 0 ? formatKwota(nagrody.getWygranaI()) : "---",
                        formatKwota(nagrody.getPulaI()));
                System.out.printf("II stopień:  %d x %s (łączna pula: %s)\n",
                        nagrody.getLiczbaII(),
                        nagrody.getLiczbaII() > 0 ? formatKwota(nagrody.getWygranaII()) : "---",
                        formatKwota(nagrody.getPulaII()));
                System.out.printf("III stopień: %d x %s (łączna pula: %s)\n",
                        nagrody.getLiczbaIII(),
                        nagrody.getLiczbaIII() > 0 ? formatKwota(nagrody.getWygranaIII()) : "---",
                        formatKwota(nagrody.getPulaIII()));
                System.out.printf("IV stopień:  %d x %s (łączna pula: %s)\n",
                        nagrody.getLiczbaIV(),
                        nagrody.getLiczbaIV() > 0 ? formatKwota(nagrody.getWygranaIV()) : "---",
                        formatKwota(nagrody.getPulaIV()));
            }
            System.out.println();
        }
    }


    public void wypiszStanFinansowy() {
        System.out.println("Stan środków finansowych centrali: " + formatKwota(srodkiFinansowe));
    }

    public void wyplacPieniadze(long kwota) {
        if (srodkiFinansowe < kwota) {
            pobierzSubwencje(kwota-srodkiFinansowe);
        }
        srodkiFinansowe -= kwota;
    }

    public void pobierzSubwencje(long kwota) {
        budzetPanstwa.udzielSubwencji(kwota);
        srodkiFinansowe += kwota;
    }

    private String formatKwota(long grosze) {
        return String.format("%d zł %02d gr", grosze / 100, grosze % 100);
    }

    public long getWygrana(Zaklad zaklad, int numerLosowania) {
        if (!losowania.containsKey(numerLosowania)) return 0;

        BazaWygranych nagrody = bazeWygranych.get(numerLosowania);
        if (nagrody == null) return 0;

        int trafienia = liczbaTrafien(zaklad, losowania.get(numerLosowania).getWynik());

        return switch (trafienia) {
            case 6 -> nagrody.getWygranaI();
            case 5 -> nagrody.getWygranaII();
            case 4 -> nagrody.getWygranaIII();
            case 3 -> nagrody.getWygranaIV();
            default -> 0;
        };
    }

}
