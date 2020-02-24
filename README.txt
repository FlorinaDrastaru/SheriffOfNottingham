DRASTARU FLORINA CRISTINA 325CA

Sheriff of Nothingam is a java project that implements a minimalist version 
of the boardgame Sheriff of Nothingam, using OOP concepts. The game is organised
in rounds and subrounds and the players can have two roles:Sheriff or Merchant. 
The merchants apply different strategies(Basic, Bribed, Greedy) in order to 
accumulate as many coins as they can by playing fair or by tricking the sheriff.  

Implementation
 The project is organised in 5 packages:
- package common:Class Constants
- game: Class Game
- goods: Class Goods, Class GoodsFactory, Class GoodsType, 
	 Class IllegalGoods, Class LegalGoods
- main: Class GameInput, Class GameInputLoader, Class Main
- players: Class Basic, Class Bribed, Class Greedy, 
	   Class PlayerComparator, Class ProfitComparator

In Class Main I use the input to create the players. At every subround the Sheriff
plays his game and the other players take cards and play the merchant game. 
At the end of the subround, the merchants put the goods that passed the inspection 
on the table, remove the remaining goods from hand and empty the bag. At the end
of the game the players receive the bonuses and profits and the leaderboard is printed.

In Class Game I gather and call the specific methods for each type of player and role. 
It also contains methods related to the game('addKingQueenBonus', 'printLeaderBoard').

Package players contains classes with the types of players(strategies).
Classes Bribed and Greedy extend Basic because all players use at a certain 
moment the strategy of the Basic player. 
Each of these classes has the methods 'playSheriff' and 'playMerchant'.

In Class Basic, in method 'playSheriff', the current player(the sheriff) 
checks all the other players. If they lie about their goods, he takes their 
cards and receives penalty. Otherwise, he pays penalty. 
In method 'playMerchant', the merchant plays legal if can, or illegal if he has
just illegal goods in hand.

In Class Greedy, in method 'playSheriff' there are two cases: the sheriff 
checks every player that doesn't bribe him or takes the bribe. 
In method 'playMerchant' I check the parity of the round. If the number of round 
is even, after calling the 'playMerchant' method from Basic, the player adds 
in bag the most profitable illegal good, if he has one.

In Class Bribe, in method 'playSheriff',  I approach two cases: if there are 
2 players in the game or more than two players. The current player takes bribe
from all players that don't sit next to him. If he has enough money, he checks
his neighbours.
In 'playMerchant' method, the merchant plays as a Basic if he hasn't enough money
or if he has no illegal goods in hand. Otherwise, the player puts in bag illegal
goods and completes with legal goods until he has at least one coin. 

In classes PlayerComparator and ProfitComparator, I sort different 
lists(of players or of goods) by different criterias(amount of money or profit).

In Class Constants I declare constants that I use frequently in the project.







