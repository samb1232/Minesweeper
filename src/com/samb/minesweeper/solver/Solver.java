package com.samb.minesweeper.solver;

import com.samb.minesweeper.game.Board;
import com.samb.minesweeper.game.Cell;
import com.samb.minesweeper.game.Status;

import javax.swing.*;
import java.util.*;

public class Solver {
    private final Board board;

    public int SLEEP_TIME = 50;

    public Solver(Board board) {
        this.board = board;
    }

    private void updateBoard() {
        board.update(board.getGraphics());
        try {
            Thread.sleep(SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openWithDelay(Cell cell) {
        cell.open(board);
        updateBoard();
    }

    private void flagWithDelay(Cell cell) {
        cell.flag(board);
        updateBoard();
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


    public void begin() {
        boolean modified = true;
        while (modified) {
            modified = false;

            // Classic algorithm
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

            List<Cell> cellsToRemove = new LinkedList<>();

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

            if (modified) continue;
            if (board.minesLeft == 0) break;


            // Backtracking algorithm
            Map<Cell, Boolean[]> coveredCellsMap = new HashMap<>();
            for (Cell cell : board.cellsWithNumbers) {
                for (Cell neighbour : cell.neighbours) {
                    if (!neighbour.isOpen && !neighbour.isFlagged && !coveredCellsMap.containsKey(neighbour)) {
                        // Добавляем к ячейке две дополнительные метки.
                        // первый - canBeMine, второй - isChecked.
                        coveredCellsMap.put(neighbour, new Boolean[]{false, false});
                    }
                }
            }


            List<Cell> coveredCells = new ArrayList<>(coveredCellsMap.keySet());
            coveredCells.sort(Comparator.comparingInt(cell -> cell.index));

            if (coveredCells.size() == 0) break;
            int[] mineDistribution = new int[coveredCells.size()];

            boolean makeMine = false;
            int cellIndex = 0;

            while (cellIndex != -1) {
                Cell currentCell = coveredCells.get(cellIndex);
                boolean shouldBeMine = false;
                boolean canBeMine = true;

                for (Cell openNeighbour : currentCell.neighbours) {
                    if (!openNeighbour.isOpen) continue;

                    int flagCount = 0;
                    int uncheckedCellsCount = 0;

                    for (Cell subNeighbour : openNeighbour.neighbours) {
                        if (subNeighbour.isFlagged ||
                                (coveredCellsMap.containsKey(subNeighbour) && coveredCellsMap.get(subNeighbour)[0])) {
                            flagCount++;
                        }
                        if (!subNeighbour.isOpen &&
                                (coveredCellsMap.containsKey(subNeighbour) && !coveredCellsMap.get(subNeighbour)[1])) {
                            uncheckedCellsCount++;
                        }
                    }


                    if (openNeighbour.code == flagCount) {
                        canBeMine = false;
                    }
                    if (makeMine || uncheckedCellsCount == openNeighbour.code - flagCount) {
                        shouldBeMine = true;
                        makeMine = false;
                    }
                }


                if (shouldBeMine) {
                    if (canBeMine) {
                        coveredCellsMap.get(currentCell)[0] = true;

                        //отрисовка
                        currentCell.image = new ImageIcon("resources/blocks/reverseFlag.png").getImage();
                        updateBoard();

                    } else {
                        //backtrack
                        do {
                            coveredCellsMap.get(currentCell)[0] = false;
                            coveredCellsMap.get(currentCell)[1] = false;

                            //отрисовка
                            currentCell.image = new ImageIcon("resources/blocks/10.png").getImage();
                            updateBoard();

                            cellIndex--;
                            if (cellIndex == -1) break;
                            currentCell = coveredCells.get(cellIndex);

                        } while (coveredCellsMap.get(currentCell)[0]);

                        makeMine = true;

                        continue;
                    }
                }

                coveredCellsMap.get(currentCell)[1] = true;
                cellIndex++;

                if (cellIndex == coveredCells.size()) {
                    for (int i = 0; i < mineDistribution.length; i++) {
                        if (coveredCellsMap.get(coveredCells.get(i))[0]) {
                            mineDistribution[i]++;
                        }
                    }

                    cellIndex--;
                    currentCell = coveredCells.get(cellIndex);
                    if (coveredCellsMap.get(currentCell)[0]) {
                        do {
                            coveredCellsMap.get(currentCell)[0] = false;
                            coveredCellsMap.get(currentCell)[1] = false;
                            cellIndex--;
                            currentCell = coveredCells.get(cellIndex);
                        } while (coveredCellsMap.get(currentCell)[0]);
                    }
                    makeMine = true;
                }
            }

            int min = mineDistribution[0];
            Cell minCell = coveredCells.get(0);
            for (int index = 1; index < mineDistribution.length; index++) {
                if (mineDistribution[index] < min) {
                    min = mineDistribution[index];
                    minCell = coveredCells.get(index);
                }
            }

            openWithDelay(minCell);
            modified = true;

            if (board.gameStatus == Status.WIN) {
                board.gameOverWin();
                modified = false;
            }
            if (board.gameStatus == Status.LOSE) {
                board.gameOverLose();
                modified = false;
            }
        }
    }
}

