package com.tema1.players;

import com.tema1.common.Constants;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsType;
import com.tema1.goods.IllegalGoods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class of the Basic player.
 * @return a basic player
 */

public class Basic {
    private int bribe;
    private ArrayList<Goods> bag;
    private Goods declaredGood;
    private ArrayList<Goods> goodsInHand;
    private int totalCoins;
    private ArrayList<Goods> soldGoods;
    private int id = -1;

    public Basic() {
        bribe = 0;
        totalCoins = Constants.START_MONEY;
        bag = new ArrayList<Goods>();
        soldGoods = new ArrayList<Goods>();
        declaredGood = null;
        goodsInHand = new ArrayList<Goods>();
    }

    public final void setId(final int id) {
        this.id = id;
    }

    public final int getId() {
        return id;
    }

    public final ArrayList<Goods> getGoodsInHand() {
        return goodsInHand;
    }

    // throws all the cards from hand
    public final void removeGoodsFromHand() {
        goodsInHand.clear();
    }

    final void setDeclaredGood(final Goods good) {
        declaredGood = good;
    }

    public final int getIdDeclaredGood() {
        return declaredGood.getId();
    }

    // returns the total coins of a player
    public final int getTotalCoins() {
        return totalCoins;
    }

    public final ArrayList<Goods> getBag() {
        return bag; // returnez cartile din sac
    }

    // throws the cards from the bag
    public final void emptyTheBag() {
        bag.clear();
    }

    // adds a certain sum to the total number of coins of a player
    public final void addCoins(final int sum) {
        totalCoins = totalCoins + sum;
    }

    // removes a certain sum from the total number of coins of a player
    public final void payCoins(final int sum) {
        totalCoins = totalCoins - sum;
    }

    // adds a card in bag
    public final void addInBag(final Goods good) {
        bag.add(good);
    }

    // removes a card from hand
    public final void removeGood(final Goods good) {
        goodsInHand.remove(good);
    }

    // returns the declared good
    public final Goods getDeclaredGood() {
        return declaredGood;
    }

    // returns the sold goods by a player
    public final ArrayList<Goods> getSoldGoods() {
        return soldGoods;
    }

    public final void setBribe(final int sum) {
        this.bribe = sum;
    }

    // returns the bribe that a player is willing to give
    public final int getBribe() {
        return bribe;
    }

    // Method 'takeCards' gives cards to a player and removes them from the package
    public final ArrayList<Goods> takeCards(final List<Integer> mAssetOrder,
                                            final Map<Integer, Goods> allGoods) {
        for (int i = 0; i < Constants.CARDS_IN_HAND; i++) {
            for (Map.Entry<Integer, Goods> good: allGoods.entrySet()) {
                if (mAssetOrder.get(i).equals(good.getKey())) {
                    this.goodsInHand.add(good.getValue());
                }
            }
        }
        for (int i = 0; i < Constants.CARDS_IN_HAND; i++) {
            mAssetOrder.remove(0);
        }
        // I sort the goods in hand by profit
        ProfitComparator profitComp = new ProfitComparator();
        goodsInHand.sort(profitComp);
        return goodsInHand;
    }

    // I find the final profit of a player by adding the profit of every sold good
    public final int countProfit() {
        int profit = 0;
        for (Goods g:getSoldGoods()) {
            profit += g.getProfit();
        }
        return profit;
    }

    // I put cards on table in order to sell them
    public final void putCardsOnTable() {
        // I add all goods from bag on the table
        soldGoods.addAll(this.getBag());
        // for every illegal good, I put on the table the corresponding legal goods given as bonus
        for (Goods g : this.getBag()) {
            if (g.getType() == GoodsType.Illegal) {
                IllegalGoods good = (IllegalGoods) g;
                for (Map.Entry<Goods, Integer> bonus : good.getIllegalBonus().entrySet()) {
                    for (int i = 0; i < bonus.getValue(); i++) {
                        soldGoods.add(bonus.getKey());
                    }
                }
            }
        }
    }

    // Method 'searchLegals' searches for legal goods in the hand of a player
    public final boolean searchLegals() {
        for (Goods g : goodsInHand) {
            if (g.getType() == GoodsType.Legal) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method of Basic player.
     */

    // play the sheriff game
    public void playSheriff(final LinkedList<Basic> players, final List<Integer> mAssetOrder) {
        for (Basic p : players) {
            if (!this.equals(p)) {
                checkPlayer(p, mAssetOrder);
            }
        }
    }

    // Method 'checkPlayer' checks the bag of a player and finds out if he is lying
    public final void checkPlayer(final Basic player, final List<Integer> mAssetOrder) {
        boolean liar = false;
        int penalty = 0;
        ArrayList<Goods> goodsInBag = player.getBag();
        ArrayList<Goods> toRemove = new ArrayList<Goods>();
        for (Goods g: goodsInBag) {
            // if the player lied, he pays the penalty to the sheriff
            if (g.getId() != player.getIdDeclaredGood()) {
                liar = true;
                this.addCoins(g.getPenalty());
                player.payCoins(g.getPenalty());
                toRemove.add(g);
            }
        }
        // I remove the confiscated goods from the bag
        player.getBag().removeAll(toRemove);

        // I put the consficated Goods back in the package
        for (Goods g : toRemove) {
            mAssetOrder.add(g.getId());
        }

        // if the player didn't lie, the sheriff pays the penalty
        if (!liar) {
            int nrDeclaredGoods = goodsInBag.size();
            penalty = nrDeclaredGoods * Constants.LEGAL_PENALTY;
            this.payCoins(penalty);
            player.addCoins(penalty);
        }
    }

    /**
     * Method of Basic player.
     */
    public void playMerchant() {
        // if the player has legal goods in hand he plays a legal game
        if (searchLegals()) {
            legalGame();
            // if the player has no legal good, he plays illegal
        } else {
            Goods illegalGood = getGoodsInHand().get(0);
            addInBag(illegalGood);
            removeGood(illegalGood);
            Goods apple = new Goods(0, GoodsType.Legal, 2, 2);
            setDeclaredGood(apple);
        }
    }

    public final void legalGame() {
        int maxFreq = 0;
        Goods searchedGood = new Goods(0, GoodsType.Legal, 0, 0);
        ArrayList<Goods> freqGoods = new ArrayList<Goods>();
        // I search for the most frequent good in hand
        for (Goods g: getGoodsInHand()) {
            if (g.getType() == GoodsType.Legal) {
                int frec = Collections.frequency(getGoodsInHand(), g);
                if (frec > maxFreq) {
                    maxFreq = frec;
                    searchedGood = g;
                }
            }
        }
        // I add the most frequent goods in a list
        for (Goods g: getGoodsInHand()) {
            if (g.equals(searchedGood)) {
                freqGoods.add(g);
            }
        }

        // I sort the list by profit and in case of equality, by id
        ProfitComparator profitComparator = new ProfitComparator();
        freqGoods.sort(profitComparator);
        setDeclaredGood(freqGoods.get(0));

        // I add in bag the found good and remove it from hand
        for (int i = 0; i < maxFreq; i++) {
            addInBag(getDeclaredGood());
            removeGood(getDeclaredGood());
        }
    }

}

