# JavaMiniProject21
Command line 21 game as a Java mini project.

The game has two command line arguments [no. of players] [no. of lines for blank space]. The second is optional.

1 player game: The player will be dealt 2 cards then will repeatedly be given the option to stick or twist until the player
has either stuck or gone bust (ie has a handvalue > 21). Aces are counted as either 1 or 11, the value is chosen to maximise
the players hand without going bust. If the player goes bust the game is over with the dealer winning. Once the player has
stuck the dealer takes his turn. He deals himself a hand, then keeps dealing cards until he has a winning hand, goes bust or 
possibly draws with the player at 21. In the latter case the game is declared a draw. If the dealer goes bust the player wins!

multiple players: Each player is dealt a hand then consecutively take their go, given the option to stick or twist. After a player 
has taken their go a number of blank lines will be created before the player is prompted to change to the next player 
(ie pass laptop\device to other player), more blank lines are created then the next player will take their go. Hopefully enough 
space will be created to prevent the previous players cards from being visible. The exact number of lines is set by default but
can be adjusted by giving the second command line argument. Once a player has stuck or gone bust they will still be given
a go if other players are active, they'll be required to press c to skip to the next player. The reasoning is that whether a 
player has gone bust or has stuck should remain secret until all players have finished. Otherwise if it is known all other
players are bust then regardless of my hand I can stick and win. Once all players have stuck or are bust the results are 
displayed.

rules: 
Aces are valued 1 or 11.
Kings, Queens and Jacks are worth 10.
All other cards are worth face value.
Closest to 21 wins, composition of hands is unimportant.

TODO comment code.
