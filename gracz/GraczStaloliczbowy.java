package totolotek.gracz;

import totolotek.core.Kolektura;
import totolotek.kupon.Blankiet;
import totolotek.kupon.Kupon;
import totolotek.kupon.PoleBlankietu;

import java.util.List;
import java.util.Set;

/**
 * GraczStaloliczbowy reprezentuje gracza, który zawsze gra tym samym zestawem 6 ulubionych liczb,
 * na 10 kolejnych losowań, kupując kupon w kolejnej ulubionej kolekturze z listy.
 * Nowy kupon kupuje dopiero wtedy, gdy wszystkie losowania poprzedniego kuponu się zakończą.
 */
public class GraczStaloliczbowy extends Gracz {
    /** Lista ulubionych kolektur, w których gracz kupuje kupony (cyklicznie) */
    private final List<Kolektura> ulubioneKolektury;
    /** Zestaw 6 ulubionych liczb gracza */
    private final Set<Integer> ulubioneLiczby;
    /** Indeks aktualnie wybieranej kolektury z listy */
    private int kolekturaIdx = 0;

    /**
     * Tworzy gracza stałoliczbowego.
     * @param imie imię gracza
     * @param nazwisko nazwisko gracza
     * @param pesel numer PESEL gracza
     * @param srodki ilość środków początkowych (w groszach)
     * @param ulubioneKolektury lista kolektur, z których korzysta gracz
     * @param ulubioneLiczby zestaw 6 ulubionych liczb (1–49)
     */
    public GraczStaloliczbowy(String imie, String nazwisko, String pesel, long srodki,
                              List<Kolektura> ulubioneKolektury, Set<Integer> ulubioneLiczby) {
        super(imie, nazwisko, pesel, srodki);
        this.ulubioneKolektury = ulubioneKolektury;
        this.ulubioneLiczby = ulubioneLiczby;
    }

    /**
     * Kupuje nowy kupon na 10 kolejnych losowań z ulubionymi liczbami,
     * w kolejnej ulubionej kolekturze z listy.
     * Nowy kupon kupowany jest tylko, jeśli nie ma aktywnego (niezrealizowanego) kuponu.
     */
    @Override
    public void kupKupon() {
        // Sprawdź, czy gracz ma już aktywny kupon (niezrealizowany)
        boolean aktywny = kupony.stream().anyMatch(k -> !k.czyZrealizowany());
        if (aktywny) return;

        // Wybierz kolekturę cyklicznie z listy
        Kolektura kolektura = ulubioneKolektury.get(kolekturaIdx);
        kolekturaIdx = (kolekturaIdx + 1) % ulubioneKolektury.size();

        // Przygotuj blankiet z ulubionymi liczbami na 10 losowań
        Blankiet blankiet = new Blankiet();
        blankiet.ustawPole(0, new PoleBlankietu(ulubioneLiczby, false));
        for (int i = 1; i < 8; i++) blankiet.ustawPole(i, null);
        blankiet.zaznaczLiczbeLosowan(10);

        // Kup kupon w wybranej kolekturze
        Kupon kupon = kolektura.sprzedajKupon(blankiet, this);
        if (kupon != null) {
            this.dodajKupon(kupon);
        }
    }
}
