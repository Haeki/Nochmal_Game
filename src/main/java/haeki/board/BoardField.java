package haeki.board;

import java.util.ArrayList;

public class BoardField implements Comparable<BoardField>{

    public enum FieldColor {
        GREEN(0),
        YELLOW(1),
        BLUE(2),
        RED(3),
        ORANGE(4);

        private final int index;

        public static FieldColor get(int index) {
            switch (index) {
                case 0: return GREEN;
                case 1: return YELLOW;
                case 2: return BLUE;
                case 3: return RED;
                case 4: return ORANGE;
            }
            throw new IndexOutOfBoundsException();
        }

        public int getIndex() {
            return index;
        }

        FieldColor(int index) {
            this.index = index;
        }
    }

    private final boolean star;
    private boolean checked;
    private boolean accessible;
    private final FieldColor fieldColor;
    private final int x;
    private final int y;

    public BoardField(boolean star, FieldColor fieldColor, int x, int y) {
        this.star = star;
        checked = false;
        this.fieldColor = fieldColor;
        this.x = x;
        this.y = y;
    }

    public BoardField(BoardField bf) {
        this.star = bf.isStar();
        this.checked = bf.isChecked();
        this.accessible = bf.isAccessible();
        this.fieldColor = bf.getFieldColor();
        this.x = bf.getX();
        this.y = bf.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getIndex() {
        return x + (15*y);
    }

    public boolean nextToFields(ArrayList<BoardField> fields) {
        for (BoardField f : fields) {
            //check for x
            if(((x > 0 && (x - f.getX()) == 1) || (x < 14 && (f.getX() - x) == 1)) && y == f.getY()) {
                return true;
            }
            if(((y > 0 && (y - f.getY()) == 1) || (y < 7 && (f.getY() - y) == 1)) && x == f.getX()) {
                return true;
            }
        }
        return  false;
    }


    public boolean isStar() {
        return star;
    }

    public boolean isChecked() {
        return checked;
    }

    public FieldColor getFieldColor() {
        return fieldColor;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }

    @Override
    public String toString() {
        return x + "|" + y;
    }

    @Override
    public int compareTo(BoardField boardField) {
        return this.getIndex() - boardField.getIndex();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BoardField) {
            return  ((((BoardField) obj).x == x) && (((BoardField) obj).y == y));
        }
        return false;
    }
}
