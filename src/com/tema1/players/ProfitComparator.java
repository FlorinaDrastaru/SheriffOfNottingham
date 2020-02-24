package com.tema1.players;

import com.tema1.goods.Goods;

import java.util.Comparator;

// compare two goods by profit
public final class ProfitComparator implements Comparator<Goods> {
    public int compare(final Goods g1, final Goods g2) {
        if (g1.getProfit() != g2.getProfit()) {
            return g2.getProfit() - g1.getProfit();
        } else {
            return g2.getId() - g1.getId();
        }
    }
}

