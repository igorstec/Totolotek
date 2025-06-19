package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.kupon.Zaklad;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ZakladTest {

    @Test
    void testPoprawnyZaklad() {
        Set<Integer> liczby = Set.of(1, 2, 3, 4, 5, 6);
        Zaklad zaklad = new Zaklad(liczby);
        assertEquals(6, zaklad.getLiczby().size());
    }

    @Test
    void testZakladZaMaloLiczb() {
        Set<Integer> liczby = Set.of(1, 2, 3, 4, 5);
        assertThrows(IllegalArgumentException.class, () -> new Zaklad(liczby));
    }

    @Test
    void testZakladLiczbaPozaZakresem() {
        Set<Integer> liczby = Set.of(1, 2, 3, 4, 5, 50);
        assertThrows(IllegalArgumentException.class, () -> new Zaklad(liczby));
    }

    @Test
    void testLiczbaTrafien() {
        Zaklad zaklad = new Zaklad(Set.of(1, 2, 3, 4, 5, 6));
        Set<Integer> wylosowane = Set.of(1, 2, 3, 7, 8, 9);
        assertEquals(3, zaklad.liczbaTrafien(wylosowane));
    }
}
