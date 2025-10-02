package totolotek.testy;

import org.junit.Test;
import totolotek.core.Centrala;
import totolotek.core.Kolektura;
import totolotek.finanse.BudzetPanstwa;
import totolotek.gracz.GraczMinimalista;
import totolotek.kupon.Kupon;

import static org.junit.jupiter.api.Assertions.*;

public class KolekturaTest {

    @Test
    public void testSprzedajKuponChybilTraf_Poprawny() {
        // given - Budżet, centrala, kolektura, gracz z wystarczającą liczbą środków
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000L, budzet);
        Kolektura kolektura = new Kolektura(1, centrala);
        GraczMinimalista gracz = new GraczMinimalista("Jan", "Kowalski", "12345678901", 10_000L, kolektura);

        // when: gracz kupuje poprawny kupon chybił trafił
        Kupon kupon = kolektura.sprzedajKuponChybilTraf(1, 1, gracz);

        // then: kupon został wystawiony, ilości są poprawne, środki zostały odjęte
        assertNotNull(kupon);
        assertEquals(1, kupon.getIleZakladow());
        assertEquals(1, kupon.getLiczbaLosowan());
        assertTrue(gracz.getSrodki() < 10_000L); // środki zmniejszone
    }

    @Test
    public void testSprzedajKuponChybilTraf_ZaMaloSrodkow() {
        // given: Budżet, centrala, kolektura, gracz z niewystarczającą liczbą środków
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000L, budzet);
        Kolektura kolektura = new Kolektura(1, centrala);
        GraczMinimalista gracz = new GraczMinimalista("Jan", "Kowalski", "12345678901", 100L, kolektura);

        // when: gracz próbuje kupić kupon chybił trafił
        Kupon kupon = kolektura.sprzedajKuponChybilTraf(1, 1, gracz);

        // then: kupon nie został wystawiony (brak środków)
        assertNull(kupon);
    }

    @Test
    public void testWyplacKupon_WrongKolektura_ThrowsException() {
        // given: Budżet, centrala, dwie kolektury i gracz z kuponem w jednej z nich
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000_000L, budzet);

        Kolektura kolektura1 = new Kolektura(1, centrala);
        Kolektura kolektura2 = new Kolektura(2, centrala);
        centrala.dodajKolekture(kolektura1);
        centrala.dodajKolekture(kolektura2);

        GraczMinimalista gracz = new GraczMinimalista("Jan", "Kowalski", "12345678901", 10_000L, kolektura1);

        // when: gracz kupuje kupon, a potem próbuje wypłacić go w niewłaściwej kolekturze
        Kupon kupon = kolektura1.sprzedajKuponChybilTraf(1, 1, gracz);
        assertNotNull(kupon);

        // then: rzucany jest odpowiedni wyjątek z komunikatem
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gracz.wyplacKupon(0, kolektura2));

        String expectedMessage = "Błędna próba wypłaty wygranej kuponu";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
