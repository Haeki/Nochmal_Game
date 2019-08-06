import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.TreeSet;

public class AlgoPlayer extends Player {
    final int uiDelay = 200;

    public AlgoPlayer(int num) {
        this.playerNum = num;
        ui = new AlgoUI();
    }

    @Override
    void init(Board board) {
        super.init(board);
        ui.initUI("Player " + playerNum, board);
    }

    /*
    @Override
    public void selectFields(int col, int num) {
        HashSet<Move> possibleMoves = getPossibleMoves(col, num);
        selectMove(possibleMoves);
    }*/

    @Override
    public int[] selectFields(ArrayList<Integer> col, ArrayList<Integer> num) {
        //Game.printDice(col, num);
        ui.updateDices(col, num);
        try {
            Thread.sleep(uiDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashSet<Move> possibleMoves = getPossibleMoves(col, num);
        Move move = selectMove(possibleMoves);
        if(move == null) {
            return null;
        } else {
            int colIndex;
            int numIndex;
            if(move.useColJoker) {
                remainingJokers--;
                colIndex = col.indexOf(5);
            } else {
                colIndex = col.indexOf(move.getColor());
            }
            if(move.useNumJoker) {
                remainingJokers--;
                numIndex = num.indexOf(5);
            } else {
                numIndex = num.indexOf(move.getUsedFields()-1);
            }
            ui.updateJokers(8 - remainingJokers);
            return new int[]{colIndex, numIndex};
        }
    }

    Move selectMove(HashSet<Move> possibleMoves) {
        System.out.println("Found " + possibleMoves.size() + " possible moves");
        int maxScore = Integer.MIN_VALUE;
        Move bestMove =  null;
        for (Move m: possibleMoves) {
            m.calcScore(remainingJokers);
            if(m.getMoveScore() > maxScore) {
                maxScore = m.getMoveScore();
                bestMove = m;
            }
            /*
            ui.checkFields(m.getFields().iterator());
            //ui.showMessage(m.toString());
            try {
                Thread.sleep(1000);
            } catch (Exception e) {}
            ui.uncheckFields(m.getFields().iterator());*/
        }
        if(bestMove != null) {
            System.out.println("Found Best Move with score: " + maxScore);
            System.out.println(bestMove);
            board.setChecked(bestMove.getFields());
            board.updateAccessible(bestMove.getFields());
            ui.checkFields(bestMove.getFields().iterator());
        }
        return bestMove;
    }

    /*
    HashSet<Move> getPossibleMoves(int col, int num) {
        HashSet<Move> possibleMoves = new HashSet<>();
        for (int i = 0; i < board.getBoardSize(); i++) {
            BoardField bf = board.getField(i);
            if (bf.isAccessible() && !bf.isChecked()) {
                if (col == bf.getFieldColor().getIndex()) {
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if (num == 5 && remainingJokers > 0) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, true, false));
                        }
                    } else {
                        possibleMoves.addAll(getMovesWithField(visited, bf, bf, num, false, false));
                    }
                } else if (col == 5 && remainingJokers > 0) {
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if (num == 5 && remainingJokers > 1) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, true, true));
                        }
                    } else {
                        possibleMoves.addAll(getMovesWithField(visited, bf, bf, num, false, true));
                    }
                }
            }
        }
        return possibleMoves;
    }*/

    HashSet<Move> getPossibleMoves(ArrayList<Integer> col, ArrayList<Integer> num) {
        HashSet<Move> possibleMoves = new HashSet<>();
        for (int i = 0; i < board.getBoardSize(); i++) {
            BoardField bf = board.getField(i);
            if (bf.isAccessible() && !bf.isChecked()) {
                if(col.contains(bf.getFieldColor().getIndex())) {
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if(num.contains(5) && remainingJokers > 0) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf , n, (!num.contains(n)), false));
                        }
                    } else {
                        for (int n : num) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf , n, false, false));
                        }
                    }
                } else if (col.contains(5) && remainingJokers > 0){
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if(num.contains(5) && remainingJokers > 1) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf , n, (!num.contains(n)), true));
                        }
                    } else {
                        for (int n : num) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf , n, false, true));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }


    HashSet<Move> getMovesWithField(TreeSet<BoardField> visitedFields, BoardField lastField, BoardField firstField, int fieldsLeft, boolean useNumJoker, boolean useColJoker) {
        HashSet<Move> moves = new HashSet<>();
        if(fieldsLeft == 0) {
            moves.add(new Move(visitedFields, useNumJoker, useColJoker, board));
            return moves;
        } else {
            ArrayList<BoardField> neighbours = board.getNeighboursWithSameColor(lastField);
            boolean foundNeighbour = false;
            for(BoardField bf : neighbours) {
                if (!visitedFields.contains(bf)) {
                    foundNeighbour = true;
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.addAll(visitedFields);
                    visited.add(bf);
                    moves.addAll(getMovesWithField(visited, bf, firstField,fieldsLeft - 1, useNumJoker, useColJoker));
                }
            }
            if(!foundNeighbour) {
                neighbours = board.getNeighboursWithSameColor(firstField);
                for(BoardField bf : neighbours) {
                    if (!visitedFields.contains(bf)) {
                        TreeSet<BoardField> visited = new TreeSet<>();
                        visited.addAll(visitedFields);
                        visited.add(bf);
                        moves.addAll(getMovesWithField(visited, bf, firstField,fieldsLeft - 1, useNumJoker, useColJoker));
                    }
                }
            }
        }
        return moves;
    }


}
