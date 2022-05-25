package com.samb.minesweeper.game;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Board extends JPanel {

    public Cell[] field;

    public int minesLeft;
    public int cellsLeft;

    public Status gameStatus;
    public boolean isFirstClick;

    public List<Cell> cellsWithNumbers; // Для решателя


    public Board() {
        initBoard();
        newGame();
    }

    protected void initBoard() {

        if (Constants.N_MINES > Constants.ALL_CELLS - 9) throw new IllegalArgumentException("Too much mines for this board");

        setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT + Constants.PANEL_HEIGHT));

        addMouseListener(new MouseHandler(this));
    }

    public void newGame() {
        gameStatus = Status.IN_GAME;

        minesLeft = Constants.N_MINES;
        cellsLeft = Constants.ALL_CELLS - Constants.N_MINES;

        field = new Cell[Constants.ALL_CELLS];
        cellsWithNumbers = new LinkedList<>();

        for (int i = 0; i < Constants.ALL_CELLS; i++) {
            field[i] = new Cell(i);
        }

        isFirstClick = true;

    }

    public void placeBombs() {
        Random random = new Random();

        int minesLeft = Constants.N_MINES;
        while (minesLeft != 0) {
            int bombCellIndex = random.nextInt(field.length);
            if (!field[bombCellIndex].isMine && !field[bombCellIndex].isOpen) {
                field[bombCellIndex].isMine = true;
                minesLeft--;
            }
        }
    }

    public void fillNeighbours() {
        int cell_index = 0;

        // Добавляем соседей верхней левой ячейки
        field[cell_index].neighbours.add(field[cell_index + 1]);
        field[cell_index].neighbours.add(field[Constants.N_COLS]);
        field[cell_index].neighbours.add(field[Constants.N_COLS + 1]);

        cell_index++;

        // Добавляем соседей верхних ячеек
        for (; cell_index < Constants.N_COLS - 1; cell_index++) {
            field[cell_index].neighbours.add(field[cell_index - 1]);
            field[cell_index].neighbours.add(field[cell_index + 1]);
            field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS - 1]);
            field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS]);
            field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS + 1]);


        }

        // Добавляем соседей верхней правой ячейки
        field[cell_index].neighbours.add(field[cell_index - 1]);
        field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS - 1]);
        field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS]);

        cell_index++;

        for (; cell_index < field.length - Constants.N_COLS; cell_index++) {

            // Добавляем соседей левой ячейки
            if (cell_index % Constants.N_COLS == 0) {
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS]);
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS + 1]);
                field[cell_index].neighbours.add(field[cell_index + 1]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS + 1]);

            }

            // Добавляем соседей правой ячейки
            else if (cell_index % Constants.N_COLS == Constants.N_COLS - 1) {
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS - 1]);
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS]);
                field[cell_index].neighbours.add(field[cell_index - 1]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS - 1]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS]);
            }

            // Добовляем соседей центральных ячеек
            else {
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS - 1]);
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS]);
                field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS + 1]);
                field[cell_index].neighbours.add(field[cell_index - 1]);
                field[cell_index].neighbours.add(field[cell_index + 1]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS - 1]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS]);
                field[cell_index].neighbours.add(field[cell_index + Constants.N_COLS + 1]);
            }

        }

        //Добавляем соседей нижней левой ячейки
        field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS]);
        field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS + 1]);
        field[cell_index].neighbours.add(field[cell_index + 1]);

        cell_index++;

        // Добавляем соседей нижних ячеек
        for (; cell_index < field.length - 1; cell_index++) {
            field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS - 1]);
            field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS]);
            field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS + 1]);
            field[cell_index].neighbours.add(field[cell_index - 1]);
            field[cell_index].neighbours.add(field[cell_index + 1]);

        }

        // Добавляем соседей нижней правой ячейки
        field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS - 1]);
        field[cell_index].neighbours.add(field[cell_index - Constants.N_COLS]);
        field[cell_index].neighbours.add(field[cell_index - 1]);
    }

    public void makeAllValues() {
        for (Cell cell : field) {
            cell.makeValue();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {

        // Рисуем доску
        for (int i = 0; i < Constants.N_ROWS; i++) {
            for (int j = 0; j < Constants.N_COLS; j++) {
                Cell cell = field[i * Constants.N_COLS + j];

                g.drawImage(cell.image, (j * Constants.CELL_SIZE),
                        (i * Constants.CELL_SIZE), this);
            }
        }

        // Рисуем счетчик мин, Если столбцов больше 5 (Иначе некрасиво)
        if (Constants.N_COLS > 5) {
            for (int i = 2; i >= 0; i--) {
                Image image = new ImageIcon("resources/digits/" + (minesLeft / (int) Math.pow(10, i)) % 10 + ".png").getImage();
                g.drawImage(image, (Constants.MARGIN_WIDTH + Constants.DIGIT_WIDTH * (3 - i)),
                        (Constants.BOARD_HEIGHT + Constants.MARGIN_HEIGHT), this);
            }
        }

        // Рисуем смайлик
        g.drawImage(gameStatus.image, Constants.BOARD_WIDTH / 2 - Constants.CELL_SIZE / 2,
                Constants.BOARD_HEIGHT + Constants.MARGIN_HEIGHT + 1, this);

        Image imageSolver = new ImageIcon("resources/SolveIcon.png").getImage();
        // Рисуем иконку решатора
        g.drawImage(imageSolver, Constants.BOARD_WIDTH - Constants.CELL_SIZE * 3 / 2,
                Constants.BOARD_HEIGHT + Constants.MARGIN_HEIGHT + 1, this);
    }


    public void gameOverWin() {
        gameStatus = Status.WIN;
        minesLeft = 0;
        for (Cell cell : field) {
            if (cell.isMine) {
                cell.image = CellValues.getImage(CellValues.FLAG);
            }
        }
    }

    public void gameOverLose() {
        gameStatus = Status.LOSE;
        for (Cell cell : field) {
            if (cell.isMine) {
                if (!cell.isFlagged){
                    cell.image = CellValues.getImage(CellValues.MINE);
                }
            }
            else if (cell.isFlagged) {
                cell.image = CellValues.getImage(CellValues.WRONG_FLAG);
            }
        }
    }
}
