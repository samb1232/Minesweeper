package com.samb.minesweeper.test;

import com.samb.minesweeper.game.Board;
import com.samb.minesweeper.game.Cell;
import com.samb.minesweeper.game.Constants;
import com.samb.minesweeper.game.Status;
import com.samb.minesweeper.solver.Solver;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class TestBoard extends Board {


    public TestBoard(int testsNumber) {
        initBoard();

        int win_count = 0;
        int lose_count = 0;
        int not_solved_count = 0;

        for (int test = 0; test < testsNumber; test++) {
            newGame();
            solve();

            switch (gameStatus) {
                case WIN -> {
                    System.out.println("Test " + test + ": WIN");
                    win_count++;
                }
                case LOSE -> {
                    System.out.println("Test " + test + ": LOSE");
                    lose_count++;
                }
                case IN_GAME -> {
                    System.out.println("Test " + test + ": NOT SOLVED");
                    not_solved_count++;
                }
            }
        }

        System.out.println("Total tests: " + testsNumber);
        System.out.println("Wins: " + win_count);
        System.out.println("Loses: " + lose_count);
        System.out.println("Not solved: " + not_solved_count);
        System.exit(0);
    }

    @Override
    protected void initBoard() {
        setPreferredSize(new Dimension(Constants.BOARD_WIDTH, Constants.BOARD_HEIGHT + Constants.PANEL_HEIGHT));
    }

    @Override
    public void newGame() {
        gameStatus = Status.IN_GAME;

        minesLeft = Constants.N_MINES;
        cellsLeft = Constants.ALL_CELLS - Constants.N_MINES;

        field = new Cell[Constants.ALL_CELLS];
        cellsWithNumbers = new LinkedList<>();

        for (int i = 0; i < Constants.ALL_CELLS; i++) {
            field[i] = new Cell(i);
        }


        Cell randomCell = field[new Random().nextInt(field.length)];
        fillNeighbours();
        randomCell.isOpen = true;
        for (Cell neighbour : randomCell.neighbours) {
            neighbour.isOpen = true;
        }
        placeBombs();
        randomCell.isOpen = false;
        for (Cell neighbour : randomCell.neighbours) {
            neighbour.isOpen = false;
        }
        makeAllValues();

        randomCell.open(this);

    }

    private void solve() {
        gameStatus = Status.SOLVING;
        Solver solver = new Solver(this);
        solver.SLEEP_TIME = 0;
        solver.begin();
        if (gameStatus == Status.SOLVING) gameStatus = Status.IN_GAME;
    }
}
