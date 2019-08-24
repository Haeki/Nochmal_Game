package haeki;

import haeki.player.AlgoPlayer;
import haeki.player.Player;

import java.awt.*;
import java.util.ArrayList;

class Main extends Component{

    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        //players.add(new haeki.player.HumanPlayer(1));
        players.add(new AlgoPlayer(1));
        players.add(new AlgoPlayer(2));
        players.add(new AlgoPlayer(3));
        //players.add(new haeki.player.AlgoPlayer(4));
        //players.add(new haeki.player.HumanPlayer(2));
        Game game = new Game("res/boards/Blue.board");
        game.initGame(players);
        game.run();

    }


}
