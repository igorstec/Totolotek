package totolotek.core;

import totolotek.finanse.BudzetPanstwa;
import totolotek.gracz.Gracz;
import totolotek.kupon.Blankiet;
import totolotek.kupon.Kupon;
import totolotek.kupon.Zaklad;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Klasa Kolektura reprezentuje punkt sprzedaży kuponów Totolotka.
 * Odpowiada za:
 *  - sprzedaż kuponów na podstawie blankietów lub "chybił-trafił",
 *  - weryfikację i rejestrację sprzedanych kuponów,
 *  - przekazywanie środków do centrali i podatków do budżetu państwa,
 *  - wypłatę wygranych graczom na podstawie kuponów.
 *
 * Każda kolektura ma unikalny numer i przechowuje sprzedane przez siebie kupony.
 */
public class Kolektura {
    /** Unikalny numer kolektury */
    private final int numerKoloktury;
    /** Mapa sprzedanych kuponów: identyfikator kuponu → kupon */
    private final Map<String, Kupon> sprzedaneKupony;
    /** Licznik do generowania kolejnych numerów kuponów */
    private int nastepnyNrKuponu = 1;
    /** Referencja do centrali Totolotka */
    private final Centrala centrala;

    /**
     * Tworzy kolekturę o podanym numerze i przypisuje ją do centrali.
     * @param numerKoloktury unikalny numer kolektury
     * @param centrala referencja do centrali Totolotka
     */
    public Kolektura(int numerKoloktury, Centrala centrala) {
        this.numerKoloktury = numerKoloktury;
        this.sprzedaneKupony = new HashMap<>();
        this.centrala = centrala;
    }

    /**
     * Sprzedaje kupon na podstawie przekazanego blankietu.
     * Waliduje blankiet, generuje zakłady i tworzy kupon na odpowiednią liczbę losowań.
     * Rejestruje kupon, pobiera środki od gracza i przekazuje podatek do budżetu państwa.
     *
     * @param blankiet blankiet wypełniony przez gracza
     * @param gracz gracz kupujący kupon
     * @return wygenerowany kupon lub null jeśli gracz nie ma wystarczających środków
     * @throws IllegalArgumentException jeśli blankiet nie zawiera poprawnych zakładów lub gracz jest null
     */
    public Kupon sprzedajKupon(Blankiet blankiet, Gracz gracz) {
        // 1. Walidacja blankietu i generacja zakładów
        List<Zaklad> zaklady = blankiet.generujZaklady()
                .stream()
                .filter(z -> z != null && z.getLiczby().size() == 6) // Tylko poprawne zakłady
                .collect(Collectors.toList());

        if (zaklady.isEmpty()) {
            throw new IllegalArgumentException("Blankiet nie zawiera prawidłowych zakładów");
        }
        if (gracz == null) {
            throw new IllegalArgumentException("Nie podano gracza");
        }
        // 2. Pobierz liczbę losowań z blankietu
        int liczbaLosowan = blankiet.getLiczbaLosowan();

        // 3. Oblicz cenę i podatek
        long cenaBrutto = zaklady.size() * liczbaLosowan * 300L; // 3 zł = 300 gr
        long podatek = zaklady.size() * liczbaLosowan * 60L;     // 0.60 zł = 60 gr

        // 4. Sprawdź środki gracza
        if (gracz.getSrodki() < cenaBrutto) {
            return null;
        }
        // 5. Utwórz kupon z unikalnym ID
        String id = generujIdKuponu();
        Kupon kupon = new Kupon(id, zaklady, liczbaLosowan, centrala, this);

        // 6. Pobierz środki i zarejestruj kupon
        gracz.odejmijSrodki(cenaBrutto);
        gracz.dodajKupon(kupon);
        this.sprzedaneKupony.put(id, kupon);

        // 7. Przekaż podatek do budżetu państwa
        BudzetPanstwa budzetPanstwa = centrala.getBudzetPanstwa();
        budzetPanstwa.pobierzPodatek(podatek);

        return kupon;
    }

    /**
     * Pomocnicza metoda do generowania unikalnych identyfikatorów kuponów.
     * Identyfikator składa się z numeru kuponu, numeru kolektury, losowego znacznika i sumy kontrolnej.
     * @return wygenerowany identyfikator kuponu
     */
    private String generujIdKuponu() {
        String randomTag = String.format("%09d", new Random().nextInt(1_000_000_000));
        String czesc = this.numerKoloktury + "-" + randomTag;
        int suma = czesc.chars().map(Character::getNumericValue).sum();
        return this.nastepnyNrKuponu++ + "-" + czesc + "-" + String.format("%02d", suma % 100);
    }

    /**
     * Sprzedaje kupon "chybił-trafił" na wskazaną liczbę zakładów i losowań.
     * Generuje losowe zakłady, sprawdza środki gracza i rejestruje kupon.
     * Przekazuje podatek do budżetu państwa.
     *
     * @param liczbaZakladow liczba zakładów (1–8)
     * @param liczbaLosowan liczba losowań (1–10)
     * @param gracz gracz kupujący kupon
     * @return wygenerowany kupon lub null jeśli gracz nie ma wystarczających środków
     * @throws IllegalArgumentException jeśli liczba zakładów lub losowań jest spoza dozwolonego zakresu
     */
    public Kupon sprzedajKuponChybilTraf(int liczbaZakladow, int liczbaLosowan, Gracz gracz) {
        // 1. Walidacja
        if (liczbaZakladow < 1 || liczbaZakladow > 8 || liczbaLosowan < 1 || liczbaLosowan > 10) {
            throw new IllegalArgumentException("Nieprawidłowa liczba zakładów lub losowań");
        }
        // 2. Sprawdź środki gracza
        long cenaBrutto = liczbaZakladow * 300L * liczbaLosowan;
        long podatek = cenaBrutto / 5;
        if (gracz.getSrodki() < cenaBrutto) {
            return null;
        }
        // 3. Wygeneruj zakłady
        List<Zaklad> zaklady = new ArrayList<>();
        for (int i = 0; i < liczbaZakladow; i++) {
            zaklady.add(Zaklad.chybilTraf());
        }
        // 4. Utwórz kupon
        String id = generujIdKuponu();
        Kupon kupon = new Kupon(id, zaklady, liczbaLosowan, centrala, this);

        // 5. Zarejestruj kupon
        sprzedaneKupony.put(kupon.getIdentyfikator(), kupon);
        // 6. Pobierz środki od gracza
        gracz.odejmijSrodki(cenaBrutto);
        gracz.dodajKupon(kupon);
        BudzetPanstwa budzetPanstwa = centrala.getBudzetPanstwa();
        budzetPanstwa.pobierzPodatek(podatek);
        // 7. Zwróć kupon
        return kupon;
    }

    /**
     * Zwraca referencję do centrali Totolotka.
     * @return centrala
     */
    public Centrala getCentrala() {
        return centrala;
    }

    /**
     * Zwraca numer tej kolektury.
     * @return numer kolektury
     */
    public int getNumerKoloktury() {
        return numerKoloktury;
    }

    /**
     * Weryfikuje, czy dany kupon był sprzedany w tej kolekturze i nie został jeszcze zrealizowany.
     * @param kupon kupon do weryfikacji
     * @return true jeśli kupon jest autentyczny i niezrealizowany, false w przeciwnym razie
     */
    public boolean zweryfikujKupon(Kupon kupon) {
        if (!sprzedaneKupony.containsKey(kupon.getIdentyfikator())) {
            return false;
        }
        if (sprzedaneKupony.get(kupon.getIdentyfikator()) != kupon) {
            return false;
        }
        return !kupon.czyZrealizowany();
    }

    /**
     * Zwraca listę kuponów ważnych na podane losowanie.
     * @param numerLosowania numer losowania
     * @return lista kuponów na to losowanie
     */
    public List<Kupon> kuponyNaLosowanie(int numerLosowania) {
        List<Kupon> wynik = new ArrayList<>();
        for (Kupon kupon : sprzedaneKupony.values()) {
            if (kupon.getNumeryLosowan().contains(numerLosowania)) {
                wynik.add(kupon);
            }
        }
        return wynik;
    }

    /**
     * Wypłaca wygrane z danego kuponu dla gracza, jeśli kupon jest autentyczny i niezrealizowany.
     * Oznacza kupon jako zrealizowany, wypłaca wygrane za każdy zakład z każdego losowania,
     * pobiera podatek od wysokich wygranych (≥ 2280 zł), resztę przekazuje graczowi.
     *
     * @param gracz gracz odbierający wygraną
     * @param kupon kupon, z którego odbierana jest wygrana
     * @return true jeśli wypłacono jakąkolwiek wygraną, false w przeciwnym razie
     */
    public boolean wyplacWygrane(Gracz gracz, Kupon kupon) {
        boolean wynik = false;
        if (zweryfikujKupon(kupon)) {
            /** Oznaczamy kupon jako zrealizowany nawet jeżeli marnuje to jego przyszłe szanse na wygraną.*/
            kupon.oznaczJakoZrealizowany();
            for (Integer losowanieNR : kupon.getNumeryLosowan()) {
                if (losowanieNR < centrala.getNajblizszeLosowanie() && losowanieNR > 0) {
                    for (Zaklad zaklad : kupon.getZaklady()) {
                        long wygrana = centrala.getWygrana(zaklad, losowanieNR);
                        if (wygrana != 0) {
                            centrala.wyplacPieniadze(wygrana);
                            if (wygrana >= 228000) { // 2280 zł w groszach
                                centrala.getBudzetPanstwa().pobierzPodatek(wygrana / 10);
                                wygrana = wygrana * 9 / 10;
                            }
                            gracz.dodajSrodki(wygrana);
                            wynik = true;
                        }
                    }
                } else {
                    return wynik;
                }
            }
            return wynik;
        }
        return false;
    }
}
