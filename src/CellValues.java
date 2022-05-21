import javax.swing.*;
import java.awt.*;

public final class CellValues {
    public static final int BLANK = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final int FIVE = 5;
    public static final int SIX = 6;
    public static final int SEVEN = 7;
    public static final int EIGHT = 8;
    public static final int MINE = 9;
    public static final int FACED_DOWN = 10;
    public static final int FLAG = 11;
    public static final int WRONG_FLAG = 12;


    public static Image getImage(int code) {
        return new ImageIcon("resources/blocks/" + code + ".png").getImage();
    }
}
