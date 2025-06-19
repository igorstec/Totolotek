package totolotek.finanse;

/**
 * Klasa BudzetPanstwa reprezentuje uproszczony model budżetu państwa
 * na potrzeby systemu Totolotka.
 * Przechowuje łączną kwotę pobranych podatków od gier oraz sumę przekazanych subwencji.
 * Może wypisywać swój aktualny stan.
 */
public class BudzetPanstwa {
    /** Łączna kwota pobranych podatków (w groszach) */
    private long pobranePodatki;
    /** Łączna kwota przekazanych subwencji (w groszach) */
    private long przekazaneSubwencjie;

    /**
     * Tworzy nowy budżet państwa z zerowymi stanami podatków i subwencji.
     */
    public BudzetPanstwa() {
        pobranePodatki = 0;
        przekazaneSubwencjie = 0;
    }

    /**
     * Powiększa sumę pobranych podatków o podaną kwotę.
     * @param kwota kwota podatku do pobrania (w groszach)
     */
    public void pobierzPodatek(long kwota) {
        this.pobranePodatki += kwota;
    }

    /**
     * Powiększa sumę przekazanych subwencji o podaną kwotę.
     * @param kwota kwota subwencji do przekazania (w groszach)
     */
    public void udzielSubwencji(long kwota) {
        this.przekazaneSubwencjie += kwota;
    }

    /**
     * Wypisuje aktualny stan budżetu państwa:
     * łączną kwotę pobranych podatków i przekazanych subwencji (w groszach).
     */
    public void wypiszStan() {
        System.out.println("Pobrane podatki: " + pobranePodatki);
        System.out.println("Przekazane subwencje: " + przekazaneSubwencjie);
    }
}
