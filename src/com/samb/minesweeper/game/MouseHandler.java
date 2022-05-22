package com.samb.minesweeper.game;

import com.samb.minesweeper.solver.Solver;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {

    Board board;

    public MouseHandler(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int button = e.getButton();

        if (y < Constants.BOARD_HEIGHT && board.gameStatus == Status.IN_GAME) {
            int cellIndex = (y / Constants.CELL_SIZE * Constants.N_COLS) + x / Constants.CELL_SIZE;

            if (button == MouseEvent.BUTTON1) {
                if (board.isFirstClick) {
                    board.isFirstClick = false;
                    board.fillNeighbours();
                    board.field[cellIndex].isOpen = true;
                    for (Cell neighbour : board.field[cellIndex].neighbours) {
                        neighbour.isOpen = true;
                    }
                    board.placeBombs();
                    board.field[cellIndex].isOpen = false;
                    for (Cell neighbour : board.field[cellIndex].neighbours) {
                        neighbour.isOpen = false;
                    }
                    board.makeAllValues();
                }
                board.field[cellIndex].open(board);
            } else if (button == MouseEvent.BUTTON3) {
                board.field[cellIndex].flag(board);
            }
        } else {
            if (button == MouseEvent.BUTTON1) {
                int smileIconLocationX = (Constants.BOARD_WIDTH - Constants.CELL_SIZE) / 2;
                int smileIconLocationY = Constants.BOARD_HEIGHT + Constants.MARGIN_HEIGHT + 1;
                if (x > smileIconLocationX && x < smileIconLocationX + Constants.CELL_SIZE &&
                        y > smileIconLocationY && y < smileIconLocationY + Constants.CELL_SIZE) {
                    board.newGame();
                    board.repaint();
                }

                int solverIconLocationX = Constants.BOARD_WIDTH - Constants.CELL_SIZE * 3 / 2;
                if (x > solverIconLocationX && x < solverIconLocationX + Constants.CELL_SIZE &&
                        y > smileIconLocationY && y < smileIconLocationY + Constants.CELL_SIZE) {
                    if (board.gameStatus == Status.IN_GAME && !board.isFirstClick) {
                        board.gameStatus = Status.SOLVING;
                        Solver solver = new Solver(board);
                        solver.begin();
                        if (board.gameStatus == Status.SOLVING) board.gameStatus = Status.IN_GAME;
                    }
                }
            }
        }
    }
}

