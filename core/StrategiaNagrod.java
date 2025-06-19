package totolotek.core;

import java.util.Map;

/**
 * Interfejs StrategiaNagrod definiuje kontrakt dla strategii podziału i rozliczania nagród
 * w systemie Totolotka. Pozwala na implementację różnych algorytmów wyznaczania baz wygranych,
 * np. standardowych, promocyjnych lub świątecznych, bez konieczności modyfikowania logiki centrali.
 *
 * Wzorzec strategii umożliwia dynamiczną podmianę sposobu wyliczania pul nagród
 * w zależności od potrzeb biznesowych lub okresu działania systemu.
 *
 * @see StandardowaStrategiaNagrod
 */
public interface StrategiaNagrod {
    /**
     * Wyznacza bazę wygranych (pule nagród i liczbę wygranych każdego stopnia)
     * na podstawie sumy wpłat, sumy podatku, ewentualnej kumulacji oraz liczby wygranych
     * w danym losowaniu.
     *
     * @param sumaWplat         suma wpłat za zakłady w danym losowaniu (w groszach)
     * @param sumaPodatku       suma podatków odprowadzonych do budżetu państwa (w groszach)
     * @param kumulacjaIstopnia kwota kumulacji I stopnia z poprzednich losowań (w groszach)
     * @param liczbaWygranych   mapa: stopień wygranej (3, 4, 5, 6) → liczba wygranych
     * @return BazaWygranych    obiekt zawierający pule i liczby wygranych każdego stopnia
     */
    BazaWygranych ustalBazeWygranych(
            long sumaWplat,
            long sumaPodatku,
            long kumulacjaIstopnia,
            Map<Integer, Integer> liczbaWygranych
    );
}
