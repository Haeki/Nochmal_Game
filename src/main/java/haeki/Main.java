package haeki;

import haeki.player.*;
import java.awt.*;
import java.util.ArrayList;

class Main extends Component{

    public static void main(String[] args) {
        System.out.println("Started Game");
        ArrayList<Player> players = new ArrayList<>();
        players.add(new HumanPlayer(1));
        //players.add(new AlgoPlayer(1));
        players.add(new AlgoPlayer(2));
        //players.add(new AlgoPlayer(3));
        //players.add(new haeki.player.AlgoPlayer(4));
        //players.add(new haeki.player.HumanPlayer(2));
        Game game = new Game("res/boards/Black.board");
        game.initGame(players);
        game.run();
        System.out.println("Ended Game");
    }


}
