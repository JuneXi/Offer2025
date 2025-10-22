package topinterview;

public class SudokuSolver {
    public void solveSudoku(char[][] board) {
        // for each location, we should try 1 -9, and check if it is valid
        // row[i][j] means there is a number j on row i
        boolean[][] row = new boolean[9][9];
        boolean[][] c = new boolean[9][9];
        boolean[][] sub = new boolean[9][9];

        //initize
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int cur = board[i][j] == '.' ? 0 : board[i][j]-'0';
                if (cur != 0) {
                    row[i][cur - 1] = true;
                    c[j][cur - 1] = true;
                    sub[i / 3 * 3 + j / 3][cur - 1] = true;

                }
            }
        }
        helper(board, row, c, sub);

    }

    private boolean helper(char[][] board, boolean[][] row, boolean[][] c, boolean[][] sub) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9;j++){
                if (board[i][j] == '.') {
                    for (int ni = 1; ni <= 9; ni++) {
                        // try to fill number ni
                        if (isValid(i, j, ni, row, c, sub)) {
                            fillNumber(i, j, ni - 1, row, c, sub, board);
                            boolean res = helper(board, row, c, sub);
                            if (res) {
                                return true;
                            } else {
                                removeNumber(i, j, ni - 1, row, c, sub, board);
                            }
                        }
                    }
                    return false;
                }

            }
        }
        return true;

    }

    // fill board[i][j] with number n works or not
    private boolean isValid(int i, int j, int n, boolean[][] row, boolean[][] c, boolean[][] sub) {
        return !row[i][n - 1] && !c[j][n - 1] && !sub[i / 3 * 3 + j / 3][n - 1];
    }

    private void fillNumber(int i, int j, int n, boolean[][] row, boolean[][] c, boolean[][] sub, char[][] board) {
        row[i][n] = true;
        c[j][n] = true;
        sub[i / 3 * 3 + j / 3][n] = true;
        board[i][j] = (char) n;
    }

    private void removeNumber(int i, int j, int n, boolean[][] row, boolean[][] c, boolean[][] sub, char[][] board) {
        row[i][n] = false;
        c[j][n] = false;
        sub[i / 3 * 3 + j / 3][n] = false;
        board[i][j] = '.';
    }

}