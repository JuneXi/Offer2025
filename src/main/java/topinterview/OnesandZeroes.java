package topinterview;

import java.util.HashMap;
import java.util.Map;

public class OnesandZeroes {
    public int findMaxForm(String[] strs, int m, int n) {
        // m, n is bag and strs is items
        // dp[m][m]从strs中任选，0<=m, 1<=n 的长度
        int[][] dp = new int[m + 1][n + 1];
        dp[0][0] = 0;//
        // key number of 0 + number of 1, value: count
        Map<String, Integer> count = new HashMap<>();
        for(String s: strs){
            int count_0 = 0;
            int count_1 = 1;
            for(char c: s.toCharArray()){
                if(c - '0' == 0){
                    count_0++;
                }else {
                    count_1++;
                }
            }
            String key = count_0 + "#" + count_1;
            if(count.containsKey(key)){
                count.put(key, count.get(key) + 1);
            } else {
                count.put(key, 1);
            }
        }
        for(int i = 1; i <= m; i++){
            if(count.containsKey(i + "#0")){
                dp[i][0] = count.get(i + "#0" );
            }
        }
        for(int i = 1; i <= n; i++){
            if(count.containsKey( "0#" + i)){
                dp[0][i] = count.get( "0#" + i );
            }
        }
        for(int i = 1; i <= m; i++){
            for(int j = 1; j <=m;j++){
                for()
            }
        }

    }
}
