package totolotek.core;

import java.util.Map;

/**
 * Klasa StandardowaStrategiaNagrod implementuje domyślną strategię podziału puli nagród
 * zgodnie z wymaganiami zadania. Oblicza wysokości wygranych dla każdego stopnia na podstawie
 * sumy wpłat, podatków, kumulacji oraz liczby wygranych w danym losowaniu.
 */
class StandardowaStrategiaNagrod implements StrategiaNagrod {
    /** Minimalna gwarantowana pula nagród I stopnia (2 mln zł w groszach) */
    private static final long MIN_PULA_I_STOPNIA = 2_000_000_00L;

    /**
     * Oblicza bazę wygranych dla danego losowania zgodnie z zasadami:
     * - 51% wpłat (po odjęciu podatku) przeznacza na nagrody
     * - 44% na I stopień, 8% na II stopień, reszta na III stopień
     * - Stała kwota 24 zł za każdą wygraną IV stopnia
     * - Gwarancja minimalnej puli I stopnia i minimalnej wygranej III stopnia
     * - Uwzględnienie kumulacji z poprzednich losowań
     *
     * @param sumaWplat         suma wpłat za zakłady (w groszach)
     * @param sumaPodatku       suma odprowadzonego podatku (w groszach)
     * @param kumulacjaIstopnia kumulacja z poprzednich losowań (w groszach)
     * @param liczbaWygranych   mapa: stopień wygranej (3-6) → liczba wygranych
     * @return BazaWygranych z obliczonymi pulami i liczbami wygranych
     */
    @Override
    public BazaWygranych ustalBazeWygranych(
            long sumaWplat, long sumaPodatku, long kumulacjaIstopnia,
            Map<Integer, Integer> liczbaWygranych
    ) {
        // Krok 1: Oblicz całkowitą pulę nagród (51% wpłat po odjęciu podatku)
        long sumaNaNagrody = (long) Math.floor((sumaWplat - sumaPodatku) * 0.51);

        // Krok 2: Podział na poszczególne stopnie wygranych
        long pulaI = (long) Math.floor(sumaNaNagrody * 0.44);
        long pulaII = (long) Math.floor(sumaNaNagrody * 0.08);
        long sumaIV = liczbaWygranych.get(3) * 2_400; // 24 zł za każde trafienie 3 liczb
        long pulaIII = sumaNaNagrody - pulaI - pulaII - sumaIV;

        // Krok 3: Gwarancja minimalnej wygranej III stopnia (36 zł)
        long minNagrodaIII = 36_00;
        if (liczbaWygranych.get(4) > 0) {
            long pojedynczaIII = pulaIII / liczbaWygranych.get(4);
            if (pojedynczaIII < minNagrodaIII) {
                pulaIII = minNagrodaIII * liczbaWygranych.get(4); // Nadpisujemy pulę III
            }
        } else {
            pulaIII = 0; // Brak wygranych III stopnia
        }

        // Krok 4: Gwarancja minimalnej puli I stopnia + kumulacja
        if (pulaI < MIN_PULA_I_STOPNIA) {
            pulaI = MIN_PULA_I_STOPNIA;
        }
        pulaI += kumulacjaIstopnia; // Dodajemy kumulację

        // Krok 5: Tworzenie i zwracanie obiektu z wynikami
        return new BazaWygranych(
                pulaI, pulaII, pulaIII, sumaIV,
                liczbaWygranych.get(6), liczbaWygranych.get(5), liczbaWygranych.get(4), liczbaWygranych.get(3)
        );
    }
}
