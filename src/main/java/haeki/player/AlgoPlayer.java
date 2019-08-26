package haeki.player;

import haeki.board.Board;
import haeki.board.BoardField;
import haeki.Game;
import haeki.ui.AlgoUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class AlgoPlayer extends Player {
    @SuppressWarnings("FieldCanBeLocal")
    private final int uiDelay = 400;

    public AlgoPlayer(int num) {
        System.out.println("Create AlgoPlayer number: " + num);
        this.playerNum = num;
        ui = new AlgoUI();
    }

    @Override
    public void init(Board board) {
        super.init(board);
        ui.initUI("Algo Player " + playerNum, board);
    }

    /*
    @Override
    public void selectFields(int col, int num) {
        HashSet<haeki.Move> possibleMoves = getPossibleMoves(col, num);
        selectMove(possibleMoves);
    }*/

    @Override
    public int[] selectFields(ArrayList<Integer> col, ArrayList<Integer> num) {
        //haeki.Game.printDice(col, num);
        ui.updateDices(col, num);
        try {
            Thread.sleep(uiDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashSet<Move> possibleMoves = getPossibleMoves(col, num);
        Move move = selectMove(possibleMoves);
        if (move == null) {
            return null;
        } else {
            int colIndex;
            int numIndex;
            if (move.useColJoker) {
                remainingJokers--;
                colIndex = col.indexOf(5);
            } else {
                colIndex = col.indexOf(move.getColor());
            }
            if (move.useNumJoker) {
                remainingJokers--;
                numIndex = num.indexOf(5);
            } else {
                numIndex = num.indexOf(move.getUsedFields() - 1);
            }
            ui.updateJokers(8 - remainingJokers);
            return new int[]{colIndex, numIndex};
        }
    }

    private Move selectMove(HashSet<Move> possibleMoves) {
        System.out.println("Found " + possibleMoves.size() + " possible moves");
        int maxScore = Integer.MIN_VALUE;
        Move bestMove = null;
        for (Move m : possibleMoves) {
            m.calcScore(remainingJokers);
            if (m.getMoveScore() > maxScore) {
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
        if (bestMove != null) {
            System.out.println("Found Best Move with score: " + maxScore);
            System.out.println(bestMove);
            board.setChecked(bestMove.getFields());
            board.updateAccessible(bestMove.getFields());
            ui.checkFields(bestMove.getFields().iterator());
        }
        return bestMove;
    }

    /*
    HashSet<haeki.Move> getPossibleMoves(int col, int num) {
        HashSet<haeki.Move> possibleMoves = new HashSet<>();
        for (int i = 0; i < board.getBoardSize(); i++) {
            haeki.board.BoardField bf = board.getField(i);
            if (bf.isAccessible() && !bf.isChecked()) {
                if (col == bf.getFieldColor().getIndex()) {
                    TreeSet<haeki.board.BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if (num == 5 && remainingJokers > 0) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, true, false));
                        }
                    } else {
                        possibleMoves.addAll(getMovesWithField(visited, bf, bf, num, false, false));
                    }
                } else if (col == 5 && remainingJokers > 0) {
                    TreeSet<haeki.board.BoardField> visited = new TreeSet<>();
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

    private HashSet<Move> getPossibleMoves(ArrayList<Integer> col, ArrayList<Integer> num) {
        HashSet<Move> possibleMoves = new HashSet<>();
        for (int i = 0; i < board.getBoardSize(); i++) {
            BoardField bf = board.getField(i);
            if (bf.isAccessible() && !bf.isChecked()) {
                if (col.contains(bf.getFieldColor().getIndex())) {
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if (num.contains(5) && remainingJokers > 0) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, (!num.contains(n)), false));
                        }
                    } else {
                        for (int n : num) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, false, false));
                        }
                    }
                } else if (col.contains(5) && remainingJokers > 0) {
                    TreeSet<BoardField> visited = new TreeSet<>();
                    visited.add(bf);
                    if (num.contains(5) && remainingJokers > 1) {
                        for (int n = 0; n < 5; n++) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, (!num.contains(n)), true));
                        }
                    } else {
                        for (int n : num) {
                            possibleMoves.addAll(getMovesWithField(visited, bf, bf, n, false, true));
                        }
                    }
                }
            }
        }
        return possibleMoves;
    }


    private HashSet<Move> getMovesWithField(TreeSet<BoardField> visitedFields, BoardField lastField, BoardField firstField, int fieldsLeft, boolean useNumJoker, boolean useColJoker) {
        HashSet<Move> moves = new HashSet<>();
        if (fieldsLeft == 0) {
            moves.add(new Move(visitedFields, useNumJoker, useColJoker, board));
            return moves;
        } else {
            ArrayList<BoardField> neighbours = board.getNeighboursWithSameColor(lastField);
            boolean foundNeighbour = false;
            for (BoardField bf : neighbours) {
                if (!visitedFields.contains(bf)) {
                    foundNeighbour = true;
                    TreeSet<BoardField> visited = new TreeSet<>(visitedFields);
                    visited.add(bf);
                    moves.addAll(getMovesWithField(visited, bf, firstField, fieldsLeft - 1, useNumJoker, useColJoker));
                }
            }
            if (!foundNeighbour) {
                neighbours = board.getNeighboursWithSameColor(firstField);
                for (BoardField bf : neighbours) {
                    if (!visitedFields.contains(bf)) {
                        TreeSet<BoardField> visited = new TreeSet<>(visitedFields);
                        visited.add(bf);
                        moves.addAll(getMovesWithField(visited, bf, firstField, fieldsLeft - 1, useNumJoker, useColJoker));
                    }
                }
            }
        }
        return moves;
    }

    class Move {
        private final int usedFields;
        private final int color;
        final boolean useNumJoker;
        final boolean useColJoker;
        private final TreeSet<BoardField> fields;
        private final Board moveBoard;
        private int moveScore;

        Move(TreeSet<BoardField> visitedFields, boolean useNumJoker, boolean useColJoker, Board mainBoard) {
            this.useNumJoker = useNumJoker;
            this.useColJoker = useColJoker;
            this.moveBoard = new Board(mainBoard);
            this.color = visitedFields.first().getFieldColor().getIndex();
            fields = visitedFields;
            usedFields = visitedFields.size();
            this.moveBoard.setChecked(visitedFields);
            this.moveBoard.updateAccessible(visitedFields);
        }

        TreeSet<BoardField> getFields() {
            return fields;
        }

        int getMoveScore() {
            return moveScore;
        }

        int getColor() {
            return color;
        }

        int getUsedFields() {
            return usedFields;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Move) {
            /*
            System.out.println(obj);
            if(((haeki.Move) obj).fields.equals(fields)) {
                System.out.println("equals");
            } else {
                System.out.println("NOT equals");
            }
            System.out.println(this);
            */
                return ((Move) obj).fields.equals(fields);
            }
            return false;
        }

        @Override
        public int hashCode() {
            StringBuilder str = new StringBuilder();
            for(BoardField bf : fields) {
                str.append(bf.getIndex());
            }
            //System.out.println("Hash = " + str);
            double temp = Double.parseDouble(str.toString());
            if(temp > Integer.MAX_VALUE) {
                return (int) (0 - (temp % Integer.MAX_VALUE));
            }
            return (int) temp;
        }


        @Override
        public String toString() {
            StringBuilder str = new StringBuilder("---------Move--------\n");
            str.append("Col Joker: ").append(useColJoker).append(", Num Joker: ").append(useNumJoker).append(", Num: ").append(usedFields).append("\n");
            Iterator<BoardField> bfIt = fields.iterator();
            BoardField firstField = bfIt.next();
            str.append("Col: ").append(firstField.getFieldColor()).append(" {").append(firstField);
            while(bfIt.hasNext()) {
                str.append(", ");
                str.append(bfIt.next());
            }
            str.append("} \n");
            str.append("---------").append(super.toString()).append("-----------");
            return str.toString();
        }

        void calcScore(int jokers) {
            int uncheckedStars = 0;
            int notAccessible = 0;
            int singleFields = 0;
            int checkedFields = 0;

            boolean[] colorFinished = new boolean[] {true, true, true, true, true};
            for (int x = 0; x < moveBoard.getWidth(); x++) {
                boolean columnFinish = true;
                for (int y = 0; y < moveBoard.getHeight(); y++) {
                    BoardField bf = moveBoard.getField(x, y);
                    if(!bf.isChecked()) {
                        columnFinish = false;
                        if(bf.isStar()) {
                            uncheckedStars++;
                        }
                        if(!bf.isAccessible()) {
                            notAccessible++;
                        }
                        boolean allNeighboursChecked = true;
                        for (BoardField nBf : moveBoard.getNeighboursWithSameColor(bf)) {
                            if(!nBf.isChecked()) {
                                allNeighboursChecked = false;
                                break;
                            }
                        }
                        if(allNeighboursChecked) {
                            singleFields++;
                        }
                        colorFinished[bf.getFieldColor().getIndex()] = false;
                    } else {
                        checkedFields++;
                    }
                }
                if(columnFinish) {
                    if(!moveBoard.getFinishedColumnsFirst().contains(x) && !moveBoard.getFinishedColumnsLast().contains(x)) {
                        if (moveBoard.getCompletedColumns().contains(x)) {
                            moveBoard.getFinishedColumnsLast().add(x);
                        } else {
                            moveBoard.getFinishedColumnsFirst().add(x);
                        }
                    }
                }
            }

            for (int i = 0; i < colorFinished.length; i++) {
                if(colorFinished[i]) {
                    BoardField.FieldColor col = BoardField.FieldColor.get(i);
                    if(!moveBoard.getFinishedColorsFirst().contains(col) && !moveBoard.getFinishedColorsLast().contains(col)) {
                        if (moveBoard.getCompletedColors().contains(col)) {
                            moveBoard.getFinishedColorsLast().add(col);
                        } else {
                            moveBoard.getFinishedColorsFirst().add(col);
                        }
                    }
                }
            }

            moveScore = 0;
            moveScore += checkedFields;
            moveScore -= notAccessible;
            moveScore -= (uncheckedStars * 2);
            moveScore += jokers;
            moveScore -= (singleFields * 2);
            if(useColJoker && useNumJoker) {
                moveScore -= 18;
            } else if(useColJoker) {
                moveScore -= 6;
            } else if(useNumJoker) {
                moveScore -= 6;
            }

            moveScore += (moveBoard.getFinishedColorsFirst().size() * 5);
            moveScore += (moveBoard.getFinishedColorsLast().size() * 3);
            for (int i: moveBoard.getFinishedColumnsFirst()) {
                moveScore += Game.pointsFirst[i];
            }
            for (int i: moveBoard.getFinishedColumnsLast()) {
                moveScore += Game.pointsLast[i];
            }

        }
    }

}
