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
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000L, budzet);
        Kolektura kolektura = new Kolektura(1, centrala);
        GraczMinimalista gracz = new GraczMinimalista("Jan", "Kowalski", "12345678901", 10_000L, kolektura);

        Kupon kupon = kolektura.sprzedajKuponChybilTraf(1, 1, gracz);

        assertNotNull(kupon);
        assertEquals(1, kupon.getIleZakladow());
        assertEquals(1, kupon.getLiczbaLosowan());
        assertTrue(gracz.getSrodki() < 10_000L); // środki zmniejszone
    }

    @Test
    public void testSprzedajKuponChybilTraf_ZaMaloSrodkow() {
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000L, budzet);
        Kolektura kolektura = new Kolektura(1, centrala);
        GraczMinimalista gracz = new GraczMinimalista("Jan", "Kowalski", "12345678901", 100L, kolektura);

        Kupon kupon = kolektura.sprzedajKuponChybilTraf(1, 1, gracz);

        assertNull(kupon);
    }
    @Test
    public void testWyplacKupon_WrongKolektura_ThrowsException() {
        // Przygotowanie środowiska testowego
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000_000L, budzet);

        Kolektura kolektura1 = new Kolektura(1, centrala);
        Kolektura kolektura2 = new Kolektura(2, centrala);
        centrala.dodajKolekture(kolektura1);
        centrala.dodajKolekture(kolektura2);

        GraczMinimalista gracz = new GraczMinimalista("Jan", "Kowalski", "12345678901", 10_000L, kolektura1);

        // Gracz kupuje kupon w kolekturze 1
        Kupon kupon = kolektura1.sprzedajKuponChybilTraf(1, 1, gracz);
        assertNotNull(kupon);

        // Próba wypłaty tego kuponu w niewłaściwej kolekturze powinna rzucić wyjątek
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            gracz.wyplacKupon(0, kolektura2);
        });

        String expectedMessage = "Błędna próba wypłaty wygranej kuponu";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }



}
