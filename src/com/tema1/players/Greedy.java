package com.tema1.players;

import com.tema1.game.Game;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Greedy extends Basic {

    // In method 'playSheriff', the sheriff checks the player if he doesn't receive bribe
    public void playSheriff(final LinkedList<Basic> players, final List<Integer> mAssetOrder) {
        for (Basic p:players) {
            if (p.getBribe() == 0) {
                checkPlayer(p, mAssetOrder);
            } else {
                addCoins(p.getBribe());
                p.addCoins(p.getBribe() * (-1));
            }
        }
    }


    public void  playMerchant() {
        // plays as a Basic player in odd rounds or when he has no illegals
        if ((Game.getRound() % 2 == 1) || (countsIllegal() == 0)) {
            super.playMerchant();
            // applies the Basic strategy and adds one more illegal good
        } else if (Game.getRound() % 2 == 0) {
            super.playMerchant();
            if (getBag().size() < Constants.MAX_IN_BAG && countsIllegal() != 0) {
                // get the most profitable illegal good from the goods in hand
                Goods illegalGood = getGoodsInHand().get(0);
                addInBag(illegalGood);
                removeGood(illegalGood);
            }
        }
    }

    // Method 'countsIllegal' counts the number of illegal goods from hand
    public int countsIllegal() {
        int counter = 0;
        ArrayList<Goods> goodsInHand = super.getGoodsInHand();
        for (Goods g: goodsInHand) {
            if (g.getType() == GoodsType.Illegal) {
                counter++;
            }
        }
        return counter;
    }
}

