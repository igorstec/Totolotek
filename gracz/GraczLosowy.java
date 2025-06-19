package totolotek.gracz;

import totolotek.core.Kolektura;
import totolotek.kupon.Kupon;

import java.util.List;
import java.util.Random;

/**
 * GraczLosowy reprezentuje gracza, który:
 * - posiada losową ilość środków (poniżej miliona złotych),
 * - kupuje losową liczbę kuponów "chybił-trafił" (od 1 do 100) w losowo wybranych kolekturach,
 * - każdy kupon zawiera losową liczbę zakładów (1–8) i losową liczbę losowań (1–10).
 */
public class GraczLosowy extends Gracz {
    /** Lista dostępnych kolektur, w których gracz może kupować kupony */
    private final List<Kolektura> kolektury;
    /** Generator liczb losowych używany przez gracza */
    private final Random rand = new Random();

    /**
     * Tworzy gracza losowego o losowych środkach i dostępnych kolekturach.
     * @param imie imię gracza
     * @param nazwisko nazwisko gracza
     * @param pesel numer PESEL gracza
     * @param kolektury lista kolektur, w których gracz może kupować kupony
     */
    public GraczLosowy(String imie, String nazwisko, String pesel, List<Kolektura> kolektury) {
        super(imie, nazwisko, pesel, 0);
        wylosujSrodki();
        this.kolektury = kolektury;
    }

    /**
     * Losuje początkową ilość środków gracza (maksymalnie 1 000 000 zł, czyli 100 000 000 gr).
     */
    private void wylosujSrodki() {
        this.srodki = rand.nextLong(100_000_000);
    }

    /**
     * Kupuje losową liczbę kuponów (1–100), każdy w losowej kolekturze,
     * z losową liczbą zakładów (1–8) i losowań (1–10).
     * Kupony są dodawane do listy gracza tylko jeśli zakup zakończył się sukcesem.
     */
    @Override
    public void kupKupon() {
        int ileKuponow = rand.nextInt(100) + 1;
        for (int i = 0; i < ileKuponow; i++) {
            Kolektura kolektura = kolektury.get(rand.nextInt(kolektury.size()));
            int ileZakladow = rand.nextInt(8) + 1;
            int ileLosowan = rand.nextInt(10) + 1;
            Kupon kupon = kolektura.sprzedajKuponChybilTraf(ileZakladow, ileLosowan, this);
            if (kupon != null) {
                this.dodajKupon(kupon);
            }
        }
    }
}
