package totolotek.testy;

import org.junit.Test;
import totolotek.core.Centrala;
import totolotek.core.Kolektura;
import totolotek.finanse.BudzetPanstwa;
import totolotek.gracz.*;
import totolotek.kupon.Blankiet;
import totolotek.kupon.PoleBlankietu;

import java.util.*;

public class GraTest {
    @Test
    public void test() {
        // given: przygotowanie środowiska: kolektury, budżet, centrala i gracze
        Kolektura[] kolektury = new Kolektura[10];
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(300000000L, budzet);

        for (int i = 0; i < 10; i++) {
            kolektury[i] = new Kolektura(i + 1, centrala);
            centrala.dodajKolekture(kolektury[i]);
        }
        Gracz[] gracze = new Gracz[1000000];
        for (int i = 0; i < 1000000; i++) {
            gracze[i] = new GraczMinimalista("Igor", "Stec", "mojpesel910" + i, 100000L, kolektury[i % 10]);
        }

        // when: wykonanie akcji: gracze kupują kupony, przeprowadzane są losowania, odbierają wygrane (3 razy)
        for (int i = 0; i < 3; i++) {
            for (Gracz gracz : gracze) {
                gracz.kupKupon();
            }
            centrala.przeprowadzLosowanie();
            for (Gracz gracz : gracze) {
                gracz.odbierzWygrane();
            }
        }

        // then: walidacja: wypisanie wyników losowań
        centrala.wypiszWynikiLosowan();
    }

    @Test
    public void test2() {
        // given: inicjalizacja systemu z budżetem, centralą i kolekturami oraz tworzenie różnych typów graczy
        BudzetPanstwa budzet = new BudzetPanstwa();
        Centrala centrala = new Centrala(1_000_000_000L, budzet);

        Kolektura[] kolektury = new Kolektura[10];
        for (int i = 0; i < 10; i++) {
            kolektury[i] = new Kolektura(i + 1, centrala);
            centrala.dodajKolekture(kolektury[i]);
        }

        List<Gracz> wszyscyGracze = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            Kolektura k = kolektury[i % 10];
            wszyscyGracze.add(new GraczMinimalista("Minimalista", "Gracz", "MIN" + i, 100_000L, k));
        }
        for (int i = 0; i < 200; i++) {
            wszyscyGracze.add(new GraczLosowy("Losowy", "Gracz", "LOS" + i, Arrays.asList(kolektury)));
        }
        Set<Integer> ulubioneLiczby = Set.of(1, 2, 3, 4, 5, 6);
        for (int i = 0; i < 200; i++) {
            List<Kolektura> ulubione = List.of(kolektury[i % 5], kolektury[(i % 5) + 5]);
            wszyscyGracze.add(new GraczStaloliczbowy("Staloliczbowy", "Gracz", "SL" + i, 500_000L, ulubione, ulubioneLiczby));
        }
        Blankiet blankiet = new Blankiet();
        blankiet.ustawPole(0, new PoleBlankietu(Set.of(7, 14, 21, 28, 35, 42), false));
        for (int i = 1; i < 8; i++) blankiet.ustawPole(i, null);
        blankiet.zaznaczLiczbeLosowan(5);
        for (int i = 0; i < 200; i++) {
            List<Kolektura> ulubione = List.of(kolektury[i % 3], kolektury[(i % 3) + 3], kolektury[(i % 3) + 6]);
            wszyscyGracze.add(new GraczStaloblankietowy("Staloblankietowy", "Gracz", "SB" + i, 1_000_000L, ulubione, blankiet, 2));
        }

        // when: gracze kupują kupony, przeprowadzane jest 20 losowań, odbierają wygrane
        for (int nrLosowania = 1; nrLosowania <= 20; nrLosowania++) {
            wszyscyGracze.parallelStream().forEach(Gracz::kupKupon);
            centrala.przeprowadzLosowanie();
            wszyscyGracze.parallelStream().forEach(Gracz::odbierzWygrane);
        }

        // then: wypisanie końcowych wyników, stanu finansowego i budżetu państwa
        System.out.println("\n=== WYNIKI KOŃCOWE ===");
        centrala.wypiszWynikiLosowan();
        centrala.wypiszStanFinansowy();

        System.out.println("\n=== BUDŻET PAŃSTWA ===");
        budzet.wypiszStan();
    }

}
