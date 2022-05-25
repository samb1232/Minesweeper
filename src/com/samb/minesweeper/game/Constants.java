package com.samb.minesweeper.game;

public final class Constants {

    public static final int CELL_SIZE = 50;

    // Количество мин и поле
    public static final int N_MINES = 44;
    public static final int N_COLS = 9; // Столбцы
    public static final int N_ROWS = 9; // Строки


    public static final int BOARD_WIDTH = N_COLS * CELL_SIZE + 1;
    public static final int BOARD_HEIGHT = N_ROWS * CELL_SIZE + 1;
    public static final int ALL_CELLS = N_ROWS * N_COLS;

    public static final int DIGIT_WIDTH = 30;
    public static final int DIGIT_HEIGHT = 54;
    public static final int MARGIN_WIDTH = 2;
    public static final int MARGIN_HEIGHT = 5;

    public static final int PANEL_HEIGHT = DIGIT_HEIGHT + 2 * MARGIN_HEIGHT;
}
