package topinterview;

public class LongestValidParentheses {
    public int longestValidParentheses(String s) {
        /*
        区间动态规划:
        For any interval [i, j] to be valid:
        - s[i] == '(' && s[j] == ')'
        Valid patterns:
        1. ((...))     -> dp[i+1][j-1] is valid
        2. ((...()     -> s[j-1] == '(' && dp[i+1][j-2] is valid
        3. ()...()     -> s[i+1] == ')' && s[j-1] == '(' && dp[i+1][j-1] is valid
        4. ()...))     -> s[i+1] == ')' && dp[i+2][j-1] is valid
         */

        int n = s.length();
        if (n < 2) return 0;

        char[] a = s.toCharArray();
        boolean[][] dp = new boolean[n][n];
        int res = 0;

        // Initialize length-2 valid "()" substrings
        for (int i = 0; i < n - 1; i++) {
            if (a[i] == '(' && a[i + 1] == ')') {
                dp[i][i + 1] = true;
                res = 2;
            }
        }

        // Interval DP by length
        for (int len = 4; len <= n; len += 2) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;

                if (a[i] != '(' || a[j] != ')') continue;

                // case: ((...))
                if (dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                }

                // case: ((...()
                else if (j - 2 >= i + 1 && a[j - 1] == '(' && dp[i + 1][j - 2]) {
                    dp[i][j] = true;
                }

                // case: ()...()
                else if (a[i + 1] == ')' && a[j - 1] == '(' && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                }

                // case: ()...))
                else if (i + 2 <= j - 1 && a[i + 1] == ')' && dp[i + 2][j - 1]) {
                    dp[i][j] = true;
                }

                if (dp[i][j]) {
                    res = Math.max(res, j - i + 1);
                }
            }
        }

        return res;
    }
}
