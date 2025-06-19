package totolotek.testy;

import org.junit.Test;
import totolotek.core.Centrala;
import totolotek.core.Kolektura;
import totolotek.finanse.BudzetPanstwa;
import totolotek.kupon.Kupon;
import totolotek.kupon.Zaklad;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KuponTest {
    BudzetPanstwa budzet = new BudzetPanstwa();
    Centrala centrala = new Centrala(300000000L, budzet);
    Kolektura kolektura1 = new Kolektura(1, centrala);

    @Test
    public void testKupon() {
        // Zakład 1: 11 12 19 23 33 43
        Zaklad zaklad1 = new Zaklad(new HashSet<>(Arrays.asList(11, 12, 19, 23, 33, 43)));
        // Zakład 2: 4 15 24 33 35 44
        Zaklad zaklad2 = new Zaklad(new HashSet<>(Arrays.asList(4, 15, 24, 33, 35, 44)));

        List<Zaklad> zaklady = Arrays.asList(zaklad1, zaklad2);
        List<Integer> numeryLosowan = Arrays.asList(8, 9, 10, 11);

        Kupon kupon = new Kupon("5-125-611233269-46", zaklady, numeryLosowan);

        assertEquals("KUPON NR 5-125-611233269-46\n" +
                "1: 11 12 19 23 33 43\n" +
                "2:  4 15 24 33 35 44\n" +
                "LICZBA LOSOWAŃ: 4\n" +
                "NUMERY LOSOWAŃ:\n" +
                " 8 9 10 11\n" +
                "CENA: 24 zł 00 gr\n", kupon.toString());

    }

    @Test
    public void testPoprawnyKupon() {
        Zaklad zaklad = new Zaklad(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6)));
        Kupon kupon = new Kupon("1-1-123456789-01", List.of(zaklad), List.of(1));
        assertEquals(1, kupon.getIleZakladow());
        assertEquals(1, kupon.getLiczbaLosowan());
        assertEquals("1-1-123456789-01", kupon.getIdentyfikator());
    }

    @Test
    public void testKuponNiepoprawnyBrakZakladow() {
        assertThrows(IllegalArgumentException.class,
                () -> new Kupon("id", null, List.of(1)));
    }

    @Test
    public void testKuponNiepoprawnyBrakLosowan() {
        Zaklad zaklad = new Zaklad(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6)));
        assertThrows(IllegalArgumentException.class,
                () -> new Kupon("id", List.of(zaklad), null));
        assertThrows(IllegalArgumentException.class,
                () -> new Kupon("id", List.of(zaklad), List.of()));
    }

}
