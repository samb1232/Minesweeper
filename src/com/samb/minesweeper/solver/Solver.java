package com.samb.minesweeper.solver;

import com.samb.minesweeper.game.*;

import java.util.Iterator;
import java.util.LinkedList;

public class Solver {
    Board board;

    private final int SLEEP_TIME = 10;

    private void openWithDelay(Cell cell) {
        cell.open(board);
        board.update(board.getGraphics());
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void flagWithDelay(Cell cell) {
        cell.flag(board);
        board.update(board.getGraphics());
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getNearCoveredCellsNumber(Cell cell) {
        int coveredCellsCount = 0;
        for (Cell neighbour : cell.neighbours) {
            if (!neighbour.isOpen) {
                coveredCellsCount++;
            }
        }
        return coveredCellsCount;
    }

    private int getNearFlaggedCellsNumber(Cell cell) {
        int flaggedCellsCount = 0;
        for (Cell neighbour : cell.neighbours) {
            if (neighbour.isFlagged) {
                flaggedCellsCount++;
            }
        }
        return flaggedCellsCount;
    }

    public Solver(Board board) {
        this.board = board;
    }

    public void begin() {
        boolean modified = true;
        while (modified) {
            modified = false;

            // If a number is touching the same number of cells, flag them
            Iterator<Cell> cellIterator = board.cellsWithNumbers.listIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.code == getNearCoveredCellsNumber(cell)) {
                    for (Cell neighbour : cell.neighbours) {
                        if (!neighbour.isFlagged && !neighbour.isOpen) {
                            flagWithDelay(neighbour);
                            modified = true;
                        }
                    }
                    cellIterator.remove();
                }
            }

            LinkedList<Cell> cellsToRemove = new LinkedList<>();

            cellIterator = board.cellsWithNumbers.listIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.code == getNearFlaggedCellsNumber(cell)) {
                    for (Cell neighbour : cell.neighbours) {
                        if (!neighbour.isFlagged && !neighbour.isOpen) {
                            openWithDelay(neighbour);
                            modified = true;
                        }
                    }
                    cellsToRemove.add(cell);
                    break;
                }
            }

            for (Cell cell : cellsToRemove) {
                board.cellsWithNumbers.remove(cell);
            }
        }

        if (board.minesLeft == 0) {
            for (Cell cell : board.field) {
                if (!cell.isOpen && !cell.isFlagged) {
                    openWithDelay(cell);
                }
            }
        }
    }
}
