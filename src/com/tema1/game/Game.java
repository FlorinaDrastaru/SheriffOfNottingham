package com.tema1.game;

import com.tema1.goods.Goods;
import com.tema1.goods.LegalGoods;
import com.tema1.players.Basic;
import com.tema1.players.Bribed;
import com.tema1.players.Greedy;
import com.tema1.players.PlayerComparator;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Game {
    private Game() { }
    private static int round = 1;

    // Method 'addKingQueen' adds the king and queen bonus
    public static void addKingQueenBonus(final List<Basic> players,
                                         final Map<Integer, Goods> allGoods) {
        LinkedList<Integer> freq;
        Basic king;
        Basic queen;
        for (Map.Entry<Integer, Goods> good : allGoods.entrySet()) {
            if (good.getKey() < Constants.ID_LEGALS) {
                // I create a list of frequency for each legal good
                freq = new LinkedList<Integer>();
                // I find the frequency of every good in the sold goods of every player
                for (Basic p : players) {
                    int frec = Collections.frequency(p.getSoldGoods(), good.getValue());
                    freq.add(players.indexOf(p), frec);
                }
                // I find the maximum frequency
                int max = Collections.max(freq);
                // I find the id of the player with the maximum frequency of the current  good
                int idMax = freq.indexOf(Collections.max(freq));
                // add the bonus
                if (max != 0) {
                    king = players.get(idMax);
                    Goods g = good.getValue();
                    int bonus = ((LegalGoods) g).getKingBonus();
                    king.addCoins(bonus);
                    // I make the maximum frequency null in order to find the second max frequency
                    freq.set(idMax, 0);
                }

                // I find the queen player and add the bonus the same way I added the king bonus
                int secmax = Collections.max(freq);
                int secIdMax = freq.indexOf(Collections.max(freq));
                if (secmax != 0) {
                    queen = players.get(secIdMax);
                    Goods g = good.getValue();
                    int bonus2 = ((LegalGoods) g).getQueenBonus();
                    queen.addCoins(bonus2);
                    freq.set(idMax, 0);
                }
            }
        }
    }

    // Method 'sheriffGame' calls the playSheriff() method for every type of player,
    // using downcasting to the Basic class
    public static void sheriffGame(final Basic player, final LinkedList<Basic> players,
                                   final List<Integer> mAssetOrder) {
        if (player.getTotalCoins() >= Constants.MIN_FOR_SHERIFF) {
            if (player.getClass().getSimpleName().equals("Basic")) {
                player.playSheriff(players, mAssetOrder);
            }
            if (player.getClass().getSimpleName().equals("Greedy")) {
                ((Greedy) player).playSheriff(players, mAssetOrder);
            }
        }
        if (player.getClass().getSimpleName().equals("Bribed")) {
            ((Bribed) player).playSheriff(players, mAssetOrder);
        }
    }

    // Method 'merchantGame' calls the playMerchant() method for every type of player,
    // using downcasting to the Basic class
    public static void merchantGame(final Basic player) {
        if (player.getClass().getSimpleName().equals("Basic")) {
            player.playMerchant();
        }
        if (player.getClass().getSimpleName().equals("Greedy")) {
            ((Greedy) player).playMerchant();
        }
        if (player.getClass().getSimpleName().equals("Bribed")) {
            ((Bribed) player).playMerchant();
        }
    }

    // In method 'giveId', every player gets as id his index in the list of players
    public static void giveId(final List<Basic> players) {
        for (Basic p : players) {
            p.setId(players.indexOf(p));
        }
    }

    // Method 'printLeaderBoard' sorts the list of players by the number of total coins
    // and prints the final leaderboard
    public static void printLeaderBoard(final List<Basic> players) {
        PlayerComparator playerComparator = new PlayerComparator();
        Collections.sort(players, playerComparator);

        for (Basic player : players) {
            System.out.println(player.getId() + " "
                    + player.getClass().getSimpleName().toUpperCase()
                    + " " + player.getTotalCoins());
        }
    }

    // Method 'setRound' increases the number of rounds with one
    public static void setRound() {
        round++;
    }

    public static int getRound() {
        return round;
    }
}
