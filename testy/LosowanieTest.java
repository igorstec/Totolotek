package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.losowanie.Losowanie;

import static org.junit.jupiter.api.Assertions.*;

class LosowanieTest {

    @Test
    void testWynikMa6Liczb() {
        Losowanie losowanie = new Losowanie(1);
        assertEquals(6, losowanie.getWynik().size());
    }

    @Test
    void testLiczbyWZakresie() {
        Losowanie losowanie = new Losowanie(2);
        for (int liczba : losowanie.getWynik()) {
            assertTrue(liczba >= 1 && liczba <= 49);
        }
    }

    @Test
    void testNumerLosowania() {
        Losowanie losowanie = new Losowanie(5);
        assertEquals(5, losowanie.getNumerLosowania());
    }
}
