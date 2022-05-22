package com.samb.minesweeper.game;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Cell {

    public Image image;
    public Set<Cell> neighbours = new HashSet<>();
    public int code;
    public Boolean isOpen = false;
    public Boolean isFlagged = false;
    public Boolean isMine;

    public Cell(Boolean isMine) {
        this.isMine = isMine;
        image = CellValues.getImage(CellValues.FACED_DOWN);
    }

    public void open(Board board) {
        if (isFlagged) return;
        if (!isOpen) {
            isOpen = true;
            image = CellValues.getImage(code);
            if (code != CellValues.BLANK) {
                board.cellsWithNumbers.add(this);
            }
            else {
                for (Cell cell : neighbours) {
                    cell.open(board);
                }
            }
            board.cellsLeft--;
        }
        if (isMine) {
            board.gameOverLose();
        }
        else if (board.cellsLeft == 0) {
            board.gameOverWin();
        }
        board.repaint();
    }


    public void flag(Board board) {
        if (!isOpen) {
            if (isFlagged) {
                image = CellValues.getImage(CellValues.FACED_DOWN);
                isFlagged = false;
                board.minesLeft++;
            } else {
                image = CellValues.getImage(CellValues.FLAG);
                isFlagged = true;
                board.minesLeft--;
            }
            board.repaint();
        }
    }

    public void makeValue() {
        if (isMine) {
            code = CellValues.MINE;
            return;
        }

        code = CellValues.BLANK;

        for (Cell cell : neighbours) {
            if (cell.isMine) {
                code += 1;
            }
        }
    }
}
