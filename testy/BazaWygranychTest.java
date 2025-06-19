package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.core.BazaWygranych;

import static org.junit.jupiter.api.Assertions.*;

class BazaWygranychTest {

    @Test
    void testWygranaI_KiedyJestWygranyIPulaPonad2mln() {
        BazaWygranych baza = new BazaWygranych(400_000_000L, 0, 0, 0, 2, 0, 0, 0);
        assertEquals(200_000_000L, baza.getWygranaI());
    }

    @Test
    void testWygranaI_BrakWygranych() {
        BazaWygranych baza = new BazaWygranych(2_000_000L, 0, 0, 0, 0, 0, 0, 0);
        assertThrows(IllegalStateException.class, baza::getWygranaI);
    }

    @Test
    void testWygranaIV_Zawsze2400() {
        BazaWygranych baza = new BazaWygranych(0, 0, 0, 0, 0, 0, 0, 0);
        assertEquals(2400L, baza.getWygranaIV());
    }

    @Test
    void testWygranaI_KiedyJestWygranyIPulaPonizej2mln() {
        BazaWygranych baza = new BazaWygranych(1_500_000L, 0, 0, 0, 3, 0, 0, 0);
        // Minimalna wygrana I stopnia to 2 mln zł (200_000_000 groszy)
        assertEquals(66_666_666L, baza.getWygranaI());
    }

    @Test
    void testWygranaII_BrakWygranych() {
        BazaWygranych baza = new BazaWygranych(0, 1_000_000L, 0, 0, 0, 0, 0, 0);
        assertThrows(IllegalStateException.class, baza::getWygranaII);
    }
}
