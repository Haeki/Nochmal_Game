import java.awt.*;
import java.util.ArrayList;

public class Main extends Component{

    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        //players.add(new HumanPlayer(1));
        players.add(new AlgoPlayer(1));
        players.add(new AlgoPlayer(2));
        players.add(new AlgoPlayer(3));
        //players.add(new AlgoPlayer(4));
        //players.add(new HumanPlayer(2));
        Game game = new Game("res/boards/Blue.board");
        game.initGame(players);
        game.run();

    }


}
