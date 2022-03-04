import java.util.Scanner;

public class Game {
    // fields
    static final int numRows = 6;
    static final int numCols = 7;

    int[][] board;
    int curPlayer;

    // constructor
    public Game() {
        board = new int[numRows][numCols];
        curPlayer = -1;
    }

    // methods
    int findBestMove() { // returns column of best move
        int bestVal = Integer.MIN_VALUE;
        int bestCol = -1;
        for (int i = 0; i < numCols; i++) {
            int row = turn(i, curPlayer);
            if (row != -1) {
                int moveVal = minimax(curPlayer * -1, 0);
                if (moveVal > bestVal) {
                    bestVal = moveVal;
                    bestCol = i;
                }
                board[row][i] = 0;
            }
        }
        return bestCol;
    }

    private int minimax(int player, int depth) {
        int winner = checkWinner();
        if (winner != 0) {
            return winner * 1000 + (winner > 0 ? depth : -depth);
        }

        if (isFull()) {
            return 0;
        }

        if (player == 1) {
            int bestVal = Integer.MIN_VALUE;
            for (int i = 0; i < numCols; i++) {
                int row = turn(i, player);
                if (row != -1) {
                    bestVal = Math.max(minimax(-1, depth + 1), bestVal);
                    board[row][i] = 0;
                }
            }
            return bestVal;
        }

        int bestVal = Integer.MAX_VALUE;
        for (int i = 0; i < numCols; i++) {
            int row = turn(i, player);
            if (row != -1) {
                bestVal = Math.min(minimax(1, depth + 1), bestVal);
                board[row][i] = 0;
            }
        }
        return bestVal;
    }

    boolean isFull() {
        for (int[] row : board)
            for (int val : row) {
                if (val == 0)
                    return false;
            }
        return true;
    }

    int turn(int col, int player) {
        if (board[0][col] != 0)
            return -1;
        for (int i = 1; i < numRows + 1; i++)
            if (i == numRows || board[i][col] != 0) {
                board[i - 1][col] = player;
                return i - 1;
            }
        return -1;
    }

    int checkWinner() {
        // horizontalCheck
        for (int j = 0; j < numCols - 3; j++)
            for (int i = 0; i < numRows; i++)
                if (board[i][j] != 0 && board[i][j] == board[i][j + 1] && board[i][j + 1] == board[i][j + 2] && board[i][j + 2] == board[i][j + 3])
                    return board[i][j];
        // verticalCheck
        for (int i = 0; i < numRows - 3; i++)
            for (int j = 0; j < numCols; j++)
                if (board[i][j] != 0 && board[i][j] == board[i+1][j] && board[i+1][j] == board[i+2][j] && board[i+2][j] == board[i+3][j])
                    return board[i][j];
        // ascendingDiagonalCheck
        for (int i = 3; i < numRows; i++)
            for (int j = 0; j < numCols - 3; j++)
                if (board[i][j] != 0 && board[i][j] == board[i-1][j+1] && board[i-1][j+1] == board[i-2][j+2] && board[i-2][j+2] == board[i-3][j+3])
                    return board[i][j];
        // descendingDiagonalCheck
        for (int i = 3; i < numRows; i++)
            for (int j = 3; j < numCols; j++)
                if (board[i][j] != 0 && board[i][j] == board[i-1][j-1] && board[i-1][j-1] == board[i-2][j-2] && board[i-2][j-2] == board[i-3][j-3])
                    return board[i][j];
        return 0;
    }

    public String toString() {
        String res = "Current Player: " + (curPlayer == 1 ? 2 : 1) + "\n\n";
        for (int[] row : board) {
            for (int i : row)
                res += i + "\t";
            res += "\n";
        }
        return res;
    }

    public static void main(String[] args) {
        Game game = new Game();
        Scanner scanner = new Scanner(System.in);
        do {
            int col = -1;
            if (game.curPlayer == -1) {
                System.out.print("Enter column (0-" + (game.numCols - 1) + "): ");
                col = scanner.nextInt();
                while ((col < 0 || col >= game.numCols) || game.turn(col, game.curPlayer) == -1) {
                    System.out.println("Try again");
                    System.out.print("Enter column (0-" + (game.numCols - 1) + "): ");
                    col = scanner.nextInt();
                }
            } else {
                col = game.findBestMove();
                game.turn(col, game.curPlayer);
            }

            System.out.println(game);
            game.curPlayer *= -1;
        } while (game.checkWinner() == 0 && !game.isFull());
        if (game.checkWinner() == 0)
            System.out.println("Tie!");
        else
            System.out.println("Winner: Player " + (game.curPlayer == -1 ? 2 : 1));
    }
}