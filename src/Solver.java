import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class Solver {
    Board board;

    private final int SLEEP_TIME = 200;

    private void openWithDelay(Cell cell) {
        cell.open(board);
        board.repaint();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void flagWithDelay(Cell cell) {
        cell.flag(board);
        board.repaint();
        try {
            TimeUnit.SECONDS.sleep(1);
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
                            neighbour.flag(board);
                            modified = true;
                        }
                    }
                    cellIterator.remove();
                }
            }



        }
    }
}
