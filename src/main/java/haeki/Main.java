package haeki;

import haeki.ui.MenuUI;

public class Main {

    public static void main(String[] args) {
        MenuUI menu = new MenuUI();
        Game game = new Game();
        while(true) {
            if(menu.startGame()) {
                game.initGame(menu.getPlayers(), menu.getBoard());
                game.run();
                break;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println("fdaut");
        }
    }

}
