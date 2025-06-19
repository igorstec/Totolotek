package totolotek.gracz;

import totolotek.core.Kolektura;
import totolotek.kupon.Kupon;

import java.util.List;

/**
 * Klasa abstrakcyjna reprezentująca gracza w systemie Totolotka.
 * Każdy gracz posiada imię, nazwisko, PESEL, środki pieniężne oraz listę zakupionych kuponów.
 * Gracz potrafi kupować kupony, wypisywać informacje o sobie oraz odbierać wygrane.
 */
public abstract class Gracz {
    /** Imię gracza */
    protected final String imie;
    /** Nazwisko gracza */
    protected final String nazwisko;
    /** Numer PESEL gracza */
    protected final String pesel;
    /** Aktualna ilość środków pieniężnych gracza (w groszach) */
    protected long srodki;
    /** Lista posiadanych przez gracza kuponów */
    protected final List<Kupon> kupony = new java.util.ArrayList<>();

    /**
     * Konstruktor gracza.
     * @param imie imię gracza
     * @param nazwisko nazwisko gracza
     * @param pesel numer PESEL gracza
     * @param srodki początkowa ilość środków (w groszach)
     */
    public Gracz(String imie, String nazwisko, String pesel, long srodki) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.pesel = pesel;
        this.srodki = srodki;
    }

    /**
     * Wypisuje informacje o graczu: nazwisko, imię, PESEL, środki oraz identyfikatory posiadanych kuponów.
     */
    public void wypiszInformacje() {
        System.out.println(nazwisko + " " + imie + ", PESEL: " + pesel);
        System.out.printf("Środki: %d zł %02d gr\n", srodki / 100, srodki % 100);
        if (kupony.isEmpty()) {
            System.out.println("Brak kuponów.");
        } else {
            System.out.println("Identyfikatory posiadanych kuponów:");
            kupony.forEach(k -> System.out.println("  " + k.getIdentyfikator()));
        }
    }

    /**
     * Abstrakcyjna metoda kupowania kuponu – implementowana przez konkretne klasy graczy.
     */
    public abstract void kupKupon();

    /**
     * Odbiera wygrane z kuponów, które brały już udział we wszystkich swoich losowaniach.
     * Po odbiorze wygranej kupon jest usuwany z listy posiadanych kuponów.
     */
    public void odbierzWygrane() {
        kupony.removeIf(kupon -> {
            // Jeśli kupon brał udział we wszystkich losowaniach
            if (kupon.getOstatnieLosowanieKuponu() < kupon.getKolektura().getCentrala().getNajblizszeLosowanie()) {
                kupon.getKolektura().wyplacWygrane(this, kupon); // Odbierz wygraną i usuń kupon
                return true;
            }
            return false;
        });
    }

    /**
     * Próbuje wypłacić wygraną z kuponu o podanym indeksie w danej kolekturze.
     * Jeśli kupon nie został zakupiony w tej kolekturze lub nic nie wygrał, nie dzieje się nic.
     * @param index indeks kuponu na liście posiadanych kuponów
     * @param kolektura kolektura, w której próbujemy wypłacić wygraną
     */
    public void wyplacKupon(int index, Kolektura kolektura) {
        if (index < 0 || index >= kupony.size())
            throw new IllegalArgumentException("Nie posiadasz tylu kuponów: " + imie);
        if (!kolektura.wyplacWygrane(this, kupony.get(index))) {
            throw new IllegalArgumentException("Błędna próba wypłaty wygranej kuponu");
        }
    }

    /**
     * Zwraca niezmienną listę posiadanych przez gracza kuponów.
     * @return lista kuponów
     */
    public List<Kupon> getKupony() {
        return java.util.Collections.unmodifiableList(kupony);
    }

    /**
     * Dodaje nowy kupon do listy posiadanych kuponów.
     * @param nowyKupon kupon do dodania
     */
    public void dodajKupon(Kupon nowyKupon) {
        kupony.add(nowyKupon);
    }

    /**
     * Odejmuje podaną kwotę od środków gracza.
     * @param kwota kwota do odjęcia (w groszach)
     */
    public void odejmijSrodki(long kwota) {
        srodki -= kwota;
    }

    /**
     * Dodaje podaną kwotę do środków gracza.
     * @param kwota kwota do dodania (w groszach)
     */
    public void dodajSrodki(long kwota) {
        srodki += kwota;
    }

    /**
     * Zwraca aktualną ilość środków gracza (w groszach).
     * @return ilość środków
     */
    public long getSrodki() {
        return srodki;
    }
}
