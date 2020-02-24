package com.tema1.players;

import java.util.Comparator;

// compare two players by the number of total coins they have
public final class PlayerComparator implements Comparator<Basic> {
    public int compare(final Basic p1, final Basic p2) {
        if (p1.getTotalCoins() != p2.getTotalCoins()) {
            return p2.getTotalCoins() - p1.getTotalCoins();
        } else {
            return p1.getId() - p2.getId();
        }
    }
}
