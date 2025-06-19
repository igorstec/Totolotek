package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.kupon.Blankiet;
import totolotek.kupon.PoleBlankietu;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BlankietTest {

    @Test
    void testGenerujZaklady_TylkoPoprawne() {
        Blankiet blankiet = new Blankiet();
        // Poprawne pole
        blankiet.ustawPole(0, new PoleBlankietu(Set.of(1,2,3,4,5,6), false));
        // Anulowane pole
        blankiet.ustawPole(1, new PoleBlankietu(Set.of(7,8,9,10,11,12), true));
        // Za mało liczb
        blankiet.ustawPole(2, new PoleBlankietu(Set.of(13,14,15), false));
        assertEquals(1, blankiet.generujZaklady().size());
    }

    @Test
    void testGetLiczbaLosowan_Domyślnie1() {
        Blankiet blankiet = new Blankiet();
        assertEquals(1, blankiet.getLiczbaLosowan());
    }

    @Test
    void testGetLiczbaLosowan_WybieraNajwiększą() {
        Blankiet blankiet = new Blankiet();
        blankiet.zaznaczLiczbeLosowan(3);
        blankiet.zaznaczLiczbeLosowan(7);
        assertEquals(7, blankiet.getLiczbaLosowan());
    }
}
