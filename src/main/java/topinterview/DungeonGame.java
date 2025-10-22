package topinterview;

public class DungeonGame {
    public int calculateMinimumHP(int[][] dungeon) {
        int m = dungeon.length;
        int n = dungeon[0].length;
        // min hp to reach i,j
        int[][] min = new int[m][n];
        // with min hp, the cur hp on i,j
        int[][] cur = new int[m][n];
        min[0][0] = Math.max(1 - dungeon[0][0], 1);
        // initilize the 1st row
        for(int i = 1; i < m ; i++){
            int temp = cur[0][i - 1] + dungeon[0][i];
            if(temp > 0){
                min[0][i] = min[0][i - 1];
                cur[0][i] = cur[0][i - 1] + dungeon[0][i];
            } else {
                min[0][i] = min[0][i - 1] - temp + 1;
                cur[0][i] = 1;
            }
        }
        for(int i = 1; i < m ; i++){
            int temp = cur[i - 1][0] + dungeon[i][0];
            if(temp > 0){
                min[i][0] = min[i-1][0];
                cur[i][0] = cur[i-1][0] + dungeon[i][0];
            } else {
                min[i][0] = min[i-1][0] - temp + 1;
                cur[i][0] = 1;
            }
        }
        for(int i = 1; i< m; i++){
            for(int j = 1; j < n; j++){
                int temp = cur[i - 1][j] + dungeon[i][j];
                if(temp > 0){
                    min[i][j] = min[i-1][j];
                    cur[i][j] = cur[i-1][j] + dungeon[i][j];
                } else {
                    min[i][j] = min[i-1][j] - temp + 1;
                    cur[i][j] = 1;
                }
            }
        }
        return min[m - 1][n - 1];

    }
}
