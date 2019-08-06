import java.util.Iterator;
import java.util.TreeSet;

class Move {
    int usedFields;
    int color;
    boolean useNumJoker;
    boolean useColJoker;
    TreeSet<BoardField> fields;
    Board moveBoard;
    int moveScore;

    public Move(TreeSet<BoardField> visitedFields, boolean useNumJoker, boolean useColJoker, Board mainBoard) {
        this.useNumJoker = useNumJoker;
        this.useColJoker = useColJoker;
        this.moveBoard = new Board(mainBoard);
        this.color = visitedFields.first().getFieldColor().getIndex();
        fields = visitedFields;
        usedFields = visitedFields.size();
        this.moveBoard.setChecked(visitedFields);
        this.moveBoard.updateAccessible(visitedFields);
    }

    public TreeSet<BoardField> getFields() {
        return fields;
    }

    public int getMoveScore() {
        return moveScore;
    }

    public int getColor() {
        return color;
    }

    public int getUsedFields() {
        return usedFields;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Move) {
            /*
            System.out.println(obj);
            if(((Move) obj).fields.equals(fields)) {
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
        String str = "";
        for(BoardField bf : fields) {
            str += String.valueOf(bf.getIndex());
        }
        //System.out.println("Hash = " + str);
        double temp = Double.parseDouble(str);
        if(temp > Integer.MAX_VALUE) {
            return (int) (0 - (temp % Integer.MAX_VALUE));
        }
        return (int) temp;
    }


    @Override
    public String toString() {
        String str = "---------Move--------\n";
        str += "Col Joker: " + useColJoker + ", Num Joker: " + useNumJoker + ", Num: " + usedFields  + "\n";
        Iterator<BoardField> bfIt = fields.iterator();
        BoardField firstField = bfIt.next();
        str += "Col: " + firstField.getFieldColor() + " {" + firstField;
        while(bfIt.hasNext()) {
            str += ", ";
            str += bfIt.next();
        }
        str += "} \n";
        str += "---------"+ super.toString() + "-----------";
        return str;
    }

    public void calcScore(int jokers) {
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
                if(!moveBoard.finishedColumnsFirst.contains(x) && !moveBoard.finishedColumnsLast.contains(x)) {
                    if (moveBoard.completeColumns.contains(x)) {
                        moveBoard.finishedColumnsLast.add(x);
                    } else {
                        moveBoard.finishedColumnsFirst.add(x);
                    }
                }
            }
        }

        for (int i = 0; i < colorFinished.length; i++) {
            if(colorFinished[i]) {
                BoardField.FieldColor col = BoardField.FieldColor.get(i);
                if(!moveBoard.finishedColorsFirst.contains(col) && !moveBoard.finishedColorsLast.contains(col)) {
                    if (moveBoard.completedColors.contains(col)) {
                        moveBoard.finishedColorsLast.add(col);
                    } else {
                        moveBoard.finishedColorsFirst.add(col);
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

        moveScore += (moveBoard.finishedColorsFirst.size() * 5);
        moveScore += (moveBoard.finishedColorsLast.size() * 3);
        for (int i: moveBoard.finishedColumnsFirst) {
            moveScore += Game.pointsFirst[i];
        }
        for (int i: moveBoard.finishedColumnsLast) {
            moveScore += Game.pointsLast[i];
        }

    }
}
