package com.tema1.main;


import com.tema1.game.Game;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.players.Basic;
import com.tema1.players.Bribed;
import com.tema1.players.Greedy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic

        Map<Integer, Goods> allGoods;
        allGoods = new HashMap<Integer, Goods>(GoodsFactory.getInstance().getAllGoods());
        LinkedList<Basic> players = new LinkedList<Basic>();
        List<Integer> mAssetOrder = gameInput.getAssetIds();
        List<String> playerNames = gameInput.getPlayerNames();
        // create the players by searching their strategy in the input list of names
        for (String playerName : playerNames) {
            if (playerName.equals("basic")) {
                Basic basic = new Basic();
                players.add(basic);
            }
            if (playerName.equals("bribed")) {
                Bribed bribed = new Bribed();
                players.add(bribed);
            }
            if (playerName.equals("greedy")) {
                Greedy greedy = new Greedy();
                players.add(greedy);
            }
        }
        Game.giveId(players);
        int nrSubrounds = gameInput.getRounds() * players.size();
        int round = 1;
        // I increase the number of rounds
        for (int i = 0; i < nrSubrounds; i++) {
            if (i == round * players.size()) {
               Game.setRound();
               round++;
            }
            // I find the Sheriff of the current subround
            Basic currSheriff = players.get(i % players.size());
            // I give cards to every player that is not sheriff
            for (Basic p : players) {
                if (!p.equals(currSheriff)) {
                    p.takeCards(gameInput.getAssetIds(), allGoods);
                    Game.merchantGame(p);
                }
            }
            // the sheriff plays his game(check or not the players)
            Game.sheriffGame(currSheriff, players, mAssetOrder);
            // at the end of the subround, every player puts the cards on the table,
            // puts down the remaining cards in hand
            // empties his bag
            for (Basic p : players) {
                p.putCardsOnTable();
                p.removeGoodsFromHand();
                p.emptyTheBag();
            }
        }

        // I add the king and queen bonus at the end of the game
            Game.addKingQueenBonus(players, allGoods);
            // I give the profit to every player
            for (Basic p : players) {
                int profit = p.countProfit();
                p.addCoins(profit);
            }
            // I print the leaderboard
            Game.printLeaderBoard(players);
        }
    }



