package topinterview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Triangle {
    public int minimumTotal(List<List<Integer>> triangle) {
        int[] dp = new int[triangle.get(triangle.size() - 1).size()];
        for(List<Integer> row : triangle){
            int l = row.size();
            dp[l - 1] = row.get(l -1) + dp[l -2];
            for(int i = row.size() - 2; i >0; i--){
                dp[i] = Math.min(dp[i], dp[i - 1]) + row.get(i);
                dp[0] = dp[0] + row.get(0);
            }
        }

        return Arrays.stream(dp).min().getAsInt();

    }
}
