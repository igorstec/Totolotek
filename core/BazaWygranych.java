package totolotek.core;

/**
 * Klasa BazaWygranych przechowuje informacje o pulach nagród oraz liczbie zwycięzców
 * w czterech kategoriach wygranych w systemie loteryjnym.
 * Umożliwia obliczanie wysokości wygranych dla każdej kategorii.
 */
public class BazaWygranych {
    // Pula nagród dla poszczególnych stopni wygranych (I-IV)
    private final long pulaI, pulaII, pulaIII, pulaIV;
    // Liczba zwycięzców w poszczególnych stopniach wygranych (I-IV)
    private final int liczbaI, liczbaII, liczbaIII, liczbaIV;

    /**
     * Konstruktor inicjalizujący wszystkie pule nagród i liczby zwycięzców.
     *
     * @param pulaI   pula nagród I stopnia
     * @param pulaII  pula nagród II stopnia
     * @param pulaIII pula nagród III stopnia
     * @param pulaIV  pula nagród IV stopnia
     * @param liczbaI   liczba zwycięzców I stopnia
     * @param liczbaII  liczba zwycięzców II stopnia
     * @param liczbaIII liczba zwycięzców III stopnia
     * @param liczbaIV  liczba zwycięzców IV stopnia
     */
    public BazaWygranych(long pulaI, long pulaII, long pulaIII, long pulaIV,
                         int liczbaI, int liczbaII, int liczbaIII, int liczbaIV) {
        this.pulaI = pulaI;
        this.pulaII = pulaII;
        this.pulaIII = pulaIII;
        this.pulaIV = pulaIV;
        this.liczbaI = liczbaI;
        this.liczbaII = liczbaII;
        this.liczbaIII = liczbaIII;
        this.liczbaIV = liczbaIV;
    }

    /**
     * Zwraca wysokość wygranej I stopnia.
     * Jeśli pula przekracza 200 000 000, dzieli ją między zwycięzców,
     * w przeciwnym razie zwraca minimalną gwarantowaną wygraną podzieloną przez liczbę zwycięzców.
     *
     * @return wysokość wygranej I stopnia
     * @throws IllegalStateException jeśli nie ma zwycięzców I stopnia
     */
    public long getWygranaI() {
        if (liczbaI == 0) throw new IllegalStateException("Nikt nie wygral nagrody 1 stopnia");
        if (pulaI > 200_000_000) {
            return pulaI / liczbaI;
        }
        return 200_000_000 / liczbaI;
    }

    /**
     * Zwraca wysokość wygranej II stopnia.
     *
     * @return wysokość wygranej II stopnia
     * @throws IllegalStateException jeśli nie ma zwycięzców II stopnia
     */
    public long getWygranaII() {
        if (liczbaII == 0) throw new IllegalStateException("Nikt nie wygral nagrody 2 stopnia");
        return pulaII / liczbaII;
    }

    /**
     * Zwraca wysokość wygranej III stopnia.
     *
     * @return wysokość wygranej III stopnia
     * @throws IllegalStateException jeśli nie ma zwycięzców III stopnia
     */
    public long getWygranaIII() {
        if (liczbaIII == 0) throw new IllegalStateException("Nikt nie wygral nagrody 3 stopnia");
        return pulaIII / liczbaIII;
    }

    /**
     * Zwraca stałą wysokość wygranej IV stopnia.
     *
     * @return wysokość wygranej IV stopnia (zawsze 2400)
     */
    public long getWygranaIV() {
        return 2400;
    }

    // Gettery do odczytu wartości pól

    /** @return pula nagród I stopnia */
    public long getPulaI() {
        return pulaI;
    }

    /** @return pula nagród II stopnia */
    public long getPulaII() {
        return pulaII;
    }

    /** @return pula nagród III stopnia */
    public long getPulaIII() {
        return pulaIII;
    }

    /** @return pula nagród IV stopnia */
    public long getPulaIV() {
        return pulaIV;
    }

    /** @return liczba zwycięzców I stopnia */
    public int getLiczbaI() {
        return liczbaI;
    }

    /** @return liczba zwycięzców II stopnia */
    public int getLiczbaII() {
        return liczbaII;
    }

    /** @return liczba zwycięzców III stopnia */
    public int getLiczbaIII() {
        return liczbaIII;
    }

    /** @return liczba zwycięzców IV stopnia */
    public int getLiczbaIV() {
        return liczbaIV;
    }
}
