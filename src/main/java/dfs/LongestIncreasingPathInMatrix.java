package dfs;

public class LongestIncreasingPathInMatrix {
    int res;
    public int longestIncreasingPath(int[][] matrix) {
        if(matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int[][] dp = new int[matrix.length][matrix[0].length];
            for (int i = 0; i < matrix.length; i++){
                for(int j = 0; j < matrix[0].length; j++){
                    dfs(matrix, 0, 0,dp );
                }
            }

        return res;

    }

    private void dfs(int[][] matrix, int m, int n, int[][]dp){

        int cur = matrix[m][n];
        if(m >0){
            if(dp[m -1][n] == 0){
                dfs(matrix, m -1, n, dp);
            } else {
                if(matrix[m - 1][n] > cur) {
                    dp[m][n] = Math.max(dp[m][n], dp[m - 1][n] + 1);
                }
            }
        }

        if(m < matrix.length - 1){
            if(dp[m + 1][n] == 0){
                dfs(matrix, m +1, n, dp);
            }else {
                if(matrix[m + 1][n] > cur) {
                    dp[m][n] = Math.max(dp[m][n], dp[m + 1][n] + 1);
                }
            }
        }

        if(n > 0){
            if(dp[m][n - 1] == 0){
                dfs(matrix, m, n -1, dp);
            }else {
                if(matrix[m][n - 1] > cur) {
                    dp[m][n] = Math.max(dp[m][n], dp[m][n - 1] + 1);
                }
            }
        }

        if(n < matrix[0].length - 1){
            if(dp[m][n + 1] == 0){
                dfs(matrix, m, n+ 1, dp);
            }else {
                if(matrix[m][n + 1] > cur) {
                    dp[m][n] = Math.max(dp[m][n], dp[m][n + 1] + 1);
                }
            }
        }
        dp[m][n] = Math.max(dp[m][n],1);
        res = Math.max(dp[m][n], res);

    }
}
