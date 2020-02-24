package com.tema1.players;


import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Bribed extends Basic {

    public void playSheriff(final LinkedList<Basic> players, final List<Integer> mAssetOrder) {

        int curIdx = players.indexOf(this);
        // if the sheriff has the minimum amount of coins, he checks the neighbours
        // otherwise, he just takes the bribe
        if (this.getTotalCoins() >= Constants.MIN_FOR_SHERIFF) {
            if (players.size() == Constants.TWO_PLAYERS) {
                if (curIdx == 0) {
                    super.checkPlayer(players.get(getRightIdx(players)), mAssetOrder);
                } else {
                    super.checkPlayer(players.get(getLeftIdx(players)), mAssetOrder);
                }
            } else {
                super.checkPlayer(players.get(getLeftIdx(players)), mAssetOrder);
                super.checkPlayer(players.get(getRightIdx(players)), mAssetOrder);
            }
        }
        takeBribe(players);
    }

    // Method 'getLeftIdx' returns the index of the left player
    public int getLeftIdx(final LinkedList<Basic> players) {
        int leftIdx;
        int curIdx = players.indexOf(this);
        if (curIdx == 0) {
            leftIdx = players.size() - 1;
        } else if (curIdx == players.size() - 1) {
            leftIdx = curIdx - 1;
        } else {
            leftIdx = curIdx - 1;
        }
        return leftIdx;
    }

    // Method 'getRightIdx' returns the index of the right player
    public int getRightIdx(final LinkedList<Basic> players) {
        int rightIdx;
        int curIdx = players.indexOf(this);
        if (curIdx == 0) {
            rightIdx = curIdx + 1;
        } else if (curIdx == players.size() - 1) {
            rightIdx = 0;
        } else {
            rightIdx = curIdx + 1;
        }
        return rightIdx;
    }

    // In method 'takeBribe', the current player takes bribe
    // from the players who don't sit next to him
    public void takeBribe(final LinkedList<Basic> players) {
        if (players.size() > Constants.THREE_PLAYERS) {
            for (Basic p : players) {
                if (!p.equals(this) && players.indexOf(p) != getRightIdx(players)
                        && players.indexOf(p) != getLeftIdx(players)) {
                    this.addCoins(p.getBribe());
                    p.payCoins(p.getBribe());
                }
            }
        }
    }

    public void playMerchant() {
        int nrIllegals;
        int maxLegals, maxIllegals;
        ArrayList<Goods> goodsInHand = super.getGoodsInHand();
        ArrayList<Goods> illegalGoodsInHand = new ArrayList<Goods>();
        // I add in a list the illegal goods from hand
        for (Goods g : goodsInHand) {
            if (g.getType() == GoodsType.Illegal) {
                illegalGoodsInHand.add(g);
            }
        }
        int countIllegals = illegalGoodsInHand.size();
        // the player plays as a Basic when he doesn't have illegals
        // or when he doesn't have enough money to pay a bribe
        if (countIllegals == 0 || (getTotalCoins() <= Constants.MIN_FOR_BRIBE)) {
            setBribe(0);
            super.playMerchant();
        }  else {
            // I sort the illegals in hand by profit
            ProfitComparator profitComparator = new ProfitComparator();
            illegalGoodsInHand.sort(profitComparator);
            Goods apple = new Goods(0, GoodsType.Legal, 2, 2);
            setDeclaredGood(apple);
            // I find out the maximum number of illegals a player could put in bag
            // so he would't remain with no coins
            if (getTotalCoins() % Constants.ILLEGAL_PENALTY == 0) {
                maxIllegals = getTotalCoins() / Constants.ILLEGAL_PENALTY - 1;
            } else {
                maxIllegals = getTotalCoins() / Constants.ILLEGAL_PENALTY;
            }
            if (illegalGoodsInHand.size() < maxIllegals) {
                maxIllegals = illegalGoodsInHand.size();
            }
            nrIllegals = 0;
            // I put in bag illegals until the bag is full
            for (int i = 0; i < maxIllegals; i++) {
                Goods g = illegalGoodsInHand.get(i);
                if (getBag().size() < Constants.MAX_IN_BAG) {
                    addInBag(g);
                    removeGood(g);
                    nrIllegals++;
                }
            }
            // I set the bribe depending on the number of illegals
            if (0 < nrIllegals && nrIllegals <= 2) {
                this.setBribe(Constants.SMALL_BRIBE);
            } else if (nrIllegals > 2) {
                this.setBribe(Constants.BIG_BRIBE);
            }
            // I remove the illegals from hand
            goodsInHand.removeAll(illegalGoodsInHand);
            int legalsHand = goodsInHand.size();
            // I calculate the cumulative penalty of the illegal goods
            int illegalPenalty =  nrIllegals * Constants.ILLEGAL_PENALTY;
            // I find out the max number of legals to complete the bag
            if ((getTotalCoins() - illegalPenalty) % Constants.LEGAL_PENALTY == 0) {
                maxLegals = (getTotalCoins() - illegalPenalty) / Constants.LEGAL_PENALTY - 1;
            } else {
                maxLegals = (getTotalCoins() - illegalPenalty) / Constants.LEGAL_PENALTY;
            }
            if (legalsHand < maxLegals) {
                maxLegals = legalsHand;
            }
            // I put the legals in bag
            for (int i = 0; i < maxLegals; i++) {
                if (getBag().size() < Constants.MAX_IN_BAG) {
                    addInBag(goodsInHand.get(i));
                }
            }
        }
    }
}
