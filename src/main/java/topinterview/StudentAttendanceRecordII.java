package topinterview;

public class StudentAttendanceRecordII {
    final int MOD = 1_000_000_007;

    public int checkRecord(int n) {
        // # of A could be 0 or 1
        // # of continuous L could be 0, 1, or 2
        int[][][] memo = new int[n + 1][2][3];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 3; k++) {
                    memo[i][j][k] = -1;
                }
            }
        }

        return check(0, 0, memo, n);
    }

    private int check(int countA, int countL, int[][][] memo, int n) {
        if (n == 0) {
            return 1;
        }

        if (memo[n][countA][countL] != -1) {
            return memo[n][countA][countL];
        }

        int res = 0;

        // Add 'P' (present): resets countL
        res = (res + check(countA, 0, memo, n - 1)) % MOD;

        // Add 'A' (absent): only if countA == 0, resets countL
        if (countA == 0) {
            res = (res + check(1, 0, memo, n - 1)) % MOD;
        }

        // Add 'L' (late): only if countL < 2
        if (countL < 2) {
            res = (res + check(countA, countL + 1, memo, n - 1)) % MOD;
        }

        memo[n][countA][countL] = res;
        return res;
    }
}
