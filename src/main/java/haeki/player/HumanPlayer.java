package haeki.player;

import haeki.board.Board;
import haeki.board.BoardField;
import haeki.ui.PlayerUI;
import haeki.ui.UI;

import java.util.ArrayList;
import java.util.Collections;

public class HumanPlayer extends Player {



    public HumanPlayer(int num) {
        System.out.println("Create HumanPlayer number: " + num);
        this.playerNum = num;
        ui = new PlayerUI();
    }

    private boolean useColorJoker = false;

    /*
    @Override
    public void selectFields(int col, int num) {
        System.out.println("haeki.player.Player " + playerNum + " selecting Field for: ");
        System.out.print("Colors: ");
        haeki.Game.printColor(col);
        System.out.println();
        System.out.print("Numbers: ");
        haeki.Game.printNumber(num);
        System.out.println();

        haeki.board.BoardField bf = getFistField(col, num);
        if(bf == null) {return;}

        ArrayList<haeki.board.BoardField> checkedFields = new ArrayList<>(5);
        int selectedColor = bf.getFieldColor().getIndex();

        ui.confirmField(bf);
        checkedFields.add(bf);

        boolean canFinish;
        if(useColorJoker) {
            canFinish = (num == (checkedFields.size() - 1) || (num == 5 && remainingJokers > 1));
            System.out.println(remainingJokers + " - 1 for Color");
        } else {
            canFinish = (num == (checkedFields.size() - 1) || (num == 5 && remainingJokers > 0));
        }

        System.out.println(checkedFields.size() + " | " + num);
        System.out.println("confirm Field: " + bf.getX() + "|" + bf.getY() + " canFinish=" + canFinish);

        while(true) {
            int index = ((haeki.ui.PlayerUI) (ui)).chooseField();

            if(index == ui.CANCEL_MOVE) {
                ui.uncheckFields(checkedFields.iterator());
                System.out.println("Cancel haeki.Move");
                return;
            } else if(index == ui.CONFIRM_MOVE) {
                if(canFinish) {
                    System.out.println("Confirmed Fields: " + checkedFields);
                    if (num == 5) {
                        remainingJokers--;
                        System.out.println("Used Joker for Num (remaining: " + remainingJokers + ")");
                    }
                    if (useColorJoker) {
                        remainingJokers--;
                        System.out.println("Used Joker for Col (remaining: " + remainingJokers + ")");
                    }
                    board.setChecked(checkedFields);
                    board.updateAccessible(checkedFields);
                    return;
                } else {
                    System.out.println("Cant Finish");
                    continue;
                }
            }

            bf = board.getField(index);

            if(checkedFields.contains(bf)) {
                //int indexToUncheck = checkedFields.indexOf(bf);
                checkedFields.remove(bf);
                ui.uncheckField(bf);
                if(!hasAccessible(checkedFields)) {
                    ui.uncheckFields(checkedFields.iterator());
                    selectFields(col, num);
                    return;
                }
            } else {
                if (checkedFields.size() < (num + 1) && checkedFields.size() < 5) {
                    if (!bf.isChecked() && bf.getFieldColor().getIndex() == selectedColor) {
                        if (bf.nextToFields(checkedFields)) {
                            ui.confirmField(bf);
                            checkedFields.add(bf);
                            if(useColorJoker) {
                                canFinish = (num == (checkedFields.size() - 1) || (num == 5 && remainingJokers > 1));
                                System.out.println(remainingJokers + " - 1 for Color");
                            } else {
                                canFinish = (num == (checkedFields.size() - 1) || (num == 5 && remainingJokers > 0));
                            }
                        }
                    }
                }
            }
        }
    }*/

    private BoardField getFistField(ArrayList<Integer> col) {
        while(true) {
            useColorJoker = false;
            int index = ((PlayerUI) (ui)).chooseField();
            if(index == UI.CANCEL_MOVE) {
                return null;
            } else if(index >= 0) {
                BoardField bf = board.getField(index);
                if (bf.isAccessible() && !bf.isChecked()) {
                    if(col.contains(bf.getFieldColor().getIndex())) {
                        return bf;
                    } else if (col.contains(5) && remainingJokers > 0){
                        useColorJoker = true;
                        return bf;
                    }
                }
            }
        }
    }

    private boolean hasAccessible(ArrayList<BoardField> checked) {
        for(BoardField bf : checked) {
            if(bf.isAccessible()) {
                return true;
            }
        }
        return false;
    }


    private BoardField getFistField(int col) {
        while(true) {
            useColorJoker = false;
            int index = ((PlayerUI) (ui)).chooseField();
            if(index == UI.CANCEL_MOVE) {
                return null;
            }
            BoardField bf = board.getField(index);
            if(bf.isAccessible() && !bf.isChecked()) {
                if (col == bf.getFieldColor().getIndex()) {
                    return bf;
                } else if(col == 5 && remainingJokers > 0) {
                    useColorJoker = true;
                    return bf;
                }
            }
        }
    }

    @Override
    public int[] selectFields(ArrayList<Integer> col, ArrayList<Integer> num) {
        //System.out.println("haeki.player.Player " + playerNum + " via Arrays selecting Field for: ");
        //haeki.Game.printDice(col, num);
        ui.updateDices(col, num);

        BoardField bf = getFistField(col);
        if(bf == null) {return null;}

        ArrayList<BoardField> checkedFields = new ArrayList<>(5);
        int selectedColor = bf.getFieldColor().getIndex();

        ui.checkField(bf);
        checkedFields.add(bf);

        boolean canFinish;
        if(useColorJoker) {
            //System.out.println(remainingJokers + " - 1 for Color");
            canFinish = (num.contains(checkedFields.size() - 1) || (num.contains(5) && remainingJokers > 1));
        } else {
            canFinish = (num.contains(checkedFields.size() - 1) || (num.contains(5) && remainingJokers > 0));
        }

        //System.out.println(checkedFields.size() + " | " + num);
        //System.out.println("confirm Field: " + bf.getX() + "|" + bf.getY() + " canFinish=" + canFinish);

        while(true) {
            int index = ((PlayerUI) (ui)).chooseField();

            if(index == UI.CANCEL_MOVE) {
                ui.uncheckFields(checkedFields.iterator());
                System.out.println("Cancel haeki.Move");
                return null;
            } else if(index == UI.CONFIRM_MOVE) {
                if(canFinish) {
                    System.out.println("Confirmed Fields: " + checkedFields);
                    int colIndex;
                    int numIndex;
                    if (!num.contains(checkedFields.size() - 1)) {
                        remainingJokers--;
                        numIndex = num.indexOf(5);
                        //System.out.println("Used Joker for Num (remaining: " + remainingJokers + ")");
                    } else {
                        numIndex = num.indexOf(checkedFields.size()-1);
                    }
                    if (useColorJoker) {
                        remainingJokers--;
                        colIndex = col.indexOf(5);
                        //System.out.println("Used Joker for Col (remaining: " + remainingJokers + ")");
                    } else {
                        colIndex = col.indexOf(bf.getFieldColor().getIndex());
                    }
                    board.setChecked(checkedFields);
                    board.updateAccessible(checkedFields);
                    ui.updateJokers(8 - remainingJokers);
                    return new int[]{colIndex, numIndex};
                } else {
                    System.out.println("Cant Finish");
                    continue;
                }
            }

            bf = board.getField(index);

            if(checkedFields.contains(bf)) {
                //int indexToUncheck = checkedFields.indexOf(bf);
                checkedFields.remove(bf);
                ui.uncheckField(bf);
                if(!hasAccessible(checkedFields)) {
                    ui.uncheckFields(checkedFields.iterator());
                    return selectFields(col, num);
                }
            } else {
                //System.out.println("Max: " + Collections.max(num) + "  size: " + checkedFields.size());

                if ((checkedFields.size() < (Collections.max(num) + 1)) && (checkedFields.size() < 5)) {

                    if (!bf.isChecked() && bf.getFieldColor().getIndex() == selectedColor) {
                        if (bf.nextToFields(checkedFields)) {
                            ui.checkField(bf);
                            checkedFields.add(bf);
                            if(useColorJoker) {
                                //System.out.println(remainingJokers + " - 1 for Color");
                                canFinish = (num.contains(checkedFields.size() - 1) || (num.contains(5) && remainingJokers > 1));
                            } else {
                                canFinish = (num.contains(checkedFields.size() - 1) || (num.contains(5) && remainingJokers > 0));
                            }
                        }
                    }
                }
            }
        }
    }


    @Override
    public void init(Board board) {
        super.init(board);
        ui.initUI("Human Player " + playerNum, board);
    }
}
