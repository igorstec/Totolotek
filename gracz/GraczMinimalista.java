package totolotek.gracz;

import totolotek.kupon.Kupon;
import totolotek.core.Kolektura;

/**
 * GraczMinimalista – zawsze kupuje jeden zakład "chybił-trafił"
 * w swojej ulubionej kolekturze i tylko na najbliższe losowanie.
 */
public class GraczMinimalista extends Gracz {
    private final Kolektura ulubionaKolektura;

    public GraczMinimalista(String imie, String nazwisko, String pesel, long srodkiPoczatkowe, Kolektura ulubionaKolektura) {
        super(imie, nazwisko, pesel, srodkiPoczatkowe);
        this.ulubionaKolektura = ulubionaKolektura;
    }

    /**
     * Minimalista kupuje zawsze jeden zakład chybił-trafił na najbliższe losowanie
     * w swojej ulubionej kolekturze, jeśli ma wystarczająco środków.
     */
    @Override
    public void kupKupon() {
        // Sprawdź, czy gracz ma już aktywny kupon na przyszłe losowanie
        if(!kupony.isEmpty()){
            return;
        }
        // Kup jeden zakład, jedno losowanie, chybił-trafił
        Kupon nowyKupon = ulubionaKolektura.sprzedajKuponChybilTraf(1, 1, this);
        // Jeśli nie udało się kupić (np. brak środków), nic nie robimy
    }

    /**
     * Zwraca ulubioną kolekturę gracza.
     */
    public Kolektura getUlubionaKolektura() {
        return ulubionaKolektura;
    }
}
