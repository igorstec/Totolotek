package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.losowanie.Losowanie;

import static org.junit.jupiter.api.Assertions.*;

class LosowanieTest {

    @Test
    void testWynikMa6Liczb() {
        // given: nowe losowanie z numerem 1
        Losowanie losowanie = new Losowanie(1);

        // when: pobierany jest wynik losowania

        // then: wynik powinien zawierać 6 liczb
        assertEquals(6, losowanie.getWynik().size());
    }

    @Test
    void testLiczbyWZakresie() {
        // given: nowe losowanie z numerem 2
        Losowanie losowanie = new Losowanie(2);

        // when: pobierane są wylosowane liczby

        // then: każda liczba powinna być w zakresie 1-49
        for (int liczba : losowanie.getWynik()) {
            assertTrue(liczba >= 1 && liczba <= 49);
        }
    }

    @Test
    void testNumerLosowania() {
        // given: nowe losowanie z numerem 5
        Losowanie losowanie = new Losowanie(5);

        // when: pobierany jest numer losowania

        // then: numer powinien się zgadzać
        assertEquals(5, losowanie.getNumerLosowania());
    }
}
