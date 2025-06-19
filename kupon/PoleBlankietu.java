package totolotek.kupon;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * PoleBlankietu reprezentuje pojedyncze pole zakładu na blankiecie.
 * Pole może być anulowane (zaznaczone "anuluj") lub nie.
 * Tylko pole nieanulowane z dokładnie 6 liczbami jest uznawane za poprawny zakład.
 */
public class PoleBlankietu {
    private final Set<Integer> liczby;   // Zaznaczone liczby (1–49), zawsze posortowane
    private final boolean anulowane;     // Czy pole zostało anulowane

    /**
     * Tworzy pole z podanym zbiorem liczb i informacją o anulowaniu.
     * @param liczby liczby zaznaczone (1–49)
     * @param anulowane true jeśli pole anulowane
     */
    public PoleBlankietu(Set<Integer> liczby, boolean anulowane) {
        if (liczby == null) {
            this.liczby = Collections.emptySet();
        } else {
            Set<Integer> poprawne = new TreeSet<>();
            for (int n : liczby) {
                if (n >= 1 && n <= 49) poprawne.add(n);
            }
            this.liczby = Collections.unmodifiableSet(poprawne);
        }
        this.anulowane = anulowane;
    }

    /**
     * Zwraca niezmienny zbiór zaznaczonych liczb.
     */
    public Set<Integer> getLiczby() {
        return liczby;
    }

    /**
     * Czy pole zostało anulowane przez użytkownika.
     */
    public boolean isAnulowane() {
        return anulowane;
    }

    /**
     * Sprawdza, czy pole jest poprawnym zakładem (nieanulowane, dokładnie 6 liczb).
     */
    public boolean czyPoprawnyZaklad() {
        return !anulowane && liczby.size() == 6;
    }

    /**
     * Generuje zakład na podstawie tego pola (jeśli jest poprawne).
     * Jeśli pole nie jest poprawne, zwraca null.
     */
    public Zaklad generujZaklad() {
        if (czyPoprawnyZaklad()) {
            return new Zaklad(liczby);
        }
        return null;
    }
}
