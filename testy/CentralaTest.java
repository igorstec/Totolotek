package totolotek.testy;

import org.junit.jupiter.api.Test;
import totolotek.core.Centrala;
import totolotek.core.Kolektura;
import totolotek.finanse.BudzetPanstwa;

import static org.junit.jupiter.api.Assertions.*;

public class CentralaTest {

    @Test
    void testPrzeprowadzLosowanie_ZwiekszaLiczbeLosowan() {
        // given: przygotowanie budżetu, centrali, kolektury i początkowego stanu liczby losowań
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000L, budzet);
        Kolektura kolektura = new Kolektura(1, centrala);
        centrala.dodajKolekture(kolektura);

        int przed = centrala.getLosowania().size();

        // when: przeprowadzenie losowania
        centrala.przeprowadzLosowanie();
        int po = centrala.getLosowania().size();

        // then: liczba losowań powinna wzrosnąć o 1
        assertEquals(przed + 1, po);
    }

}
