package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.kupon.Zaklad;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ZakladTest {

    @Test
    void testPoprawnyZaklad() {
        // given: poprawny zestaw 6 liczb
        Set<Integer> liczby = Set.of(1, 2, 3, 4, 5, 6);

        // when: tworzony jest zakład
        Zaklad zaklad = new Zaklad(liczby);

        // then: zakład zawiera 6 liczb
        assertEquals(6, zaklad.getLiczby().size());
    }

    @Test
    void testZakladZaMaloLiczb() {
        // given: zestaw z 5 liczbami (za mało)
        Set<Integer> liczby = Set.of(1, 2, 3, 4, 5);

        // when & then: próba utworzenia zakładu rzuca wyjątek
        assertThrows(IllegalArgumentException.class, () -> new Zaklad(liczby));
    }

    @Test
    void testZakladLiczbaPozaZakresem() {
        // given: zestaw z liczbą spoza zakresu (50)
        Set<Integer> liczby = Set.of(1, 2, 3, 4, 5, 50);

        // when & then: próba utworzenia zakładu rzuca wyjątek
        assertThrows(IllegalArgumentException.class, () -> new Zaklad(liczby));
    }

    @Test
    void testLiczbaTrafien() {
        // given: zakład oraz zbiór wylosowanych liczb
        Zaklad zaklad = new Zaklad(Set.of(1, 2, 3, 4, 5, 6));
        Set<Integer> wylosowane = Set.of(1, 2, 3, 7, 8, 9);

        // when: obliczana jest liczba trafień
        int trafienia = zaklad.liczbaTrafien(wylosowane);

        // then: wynik powinien być równy 3
        assertEquals(3, trafienia);
    }
}
