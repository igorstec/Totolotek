package totolotek.gracz;

import totolotek.core.Kolektura;
import totolotek.kupon.Blankiet;
import totolotek.kupon.Kupon;

import java.util.List;

/**
 * GraczStaloblankietowy reprezentuje gracza, który zawsze kupuje kupon
 * według tego samego, zdefiniowanego blankietu i robi to cyklicznie,
 * co określoną liczbę losowań.
 *
 * Gracz posiada listę ulubionych kolektur, z których korzysta naprzemiennie.
 * Po zakupie kuponu w danej kolekturze, następnym razem wybiera kolejną z listy.
 *
 * Przykład zastosowania: gracz wypełnia zawsze ten sam blankiet i regularnie,
 * np. co 2 losowania, kupuje nowy kupon w kolejnej kolekturze z listy.
 */
public class GraczStaloblankietowy extends Gracz {
    /** Lista ulubionych kolektur, z których korzysta gracz (cyklicznie) */
    private final List<Kolektura> ulubioneKolektury;
    /** Blankiet, według którego gracz zawsze kupuje kupon */
    private final Blankiet ulubionyBlankiet;
    /** Liczba losowań, co którą gracz kupuje nowy kupon */
    private final int coIleLosowan;
    /** Indeks aktualnie wybieranej kolektury z listy */
    private int kolekturaIdx = 0;
    /** Licznik losowań do zakupu nowego kuponu */
    private int losowaniaDoNowegoKuponu = 0;

    /**
     * Tworzy gracza stałoblankietowego.
     *
     * @param imie imię gracza
     * @param nazwisko nazwisko gracza
     * @param pesel numer PESEL gracza
     * @param srodki ilość środków początkowych (w groszach)
     * @param ulubioneKolektury lista kolektur, z których korzysta gracz
     * @param ulubionyBlankiet blankiet, według którego zawsze kupuje kupon
     * @param coIleLosowan co ile losowań gracz kupuje nowy kupon
     */
    public GraczStaloblankietowy(String imie, String nazwisko, String pesel, long srodki,
                                 List<Kolektura> ulubioneKolektury, Blankiet ulubionyBlankiet, int coIleLosowan) {
        super(imie, nazwisko, pesel, srodki);
        this.ulubioneKolektury = ulubioneKolektury;
        this.ulubionyBlankiet = ulubionyBlankiet;
        this.coIleLosowan = coIleLosowan;
    }

    /**
     * Kupuje nowy kupon według ulubionego blankietu w kolejnej kolekturze z listy,
     * jeśli upłynęła odpowiednia liczba losowań od poprzedniego zakupu.
     *
     * Jeśli licznik losowań do nowego kuponu jest większy od zera, to zmniejsza licznik
     * i nie kupuje kuponu w tym wywołaniu.
     *
     * Po zakupie kuponu licznik jest ustawiany ponownie na coIleLosowan.
     */
    @Override
    public void kupKupon() {
        if (losowaniaDoNowegoKuponu > 0) {
            losowaniaDoNowegoKuponu--;
            return;
        }
        Kolektura kolektura = ulubioneKolektury.get(kolekturaIdx);
        kolekturaIdx = (kolekturaIdx + 1) % ulubioneKolektury.size();

        Kupon kupon = kolektura.sprzedajKupon(ulubionyBlankiet, this);
        if (kupon != null) {
            this.dodajKupon(kupon);
            losowaniaDoNowegoKuponu = coIleLosowan;
        }
    }
}
