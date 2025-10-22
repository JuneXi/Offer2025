package topinterview;

import java.util.Arrays;

public class BurstBalloons {
    public static void main(String[] args){
        BurstBalloons app = new BurstBalloons();
        int[][] test = {{1,2},{3,4},{5,6},{7,8}};
        app.findMinArrowShots(test);

    }
    public int findMinArrowShots(int[][] points) {
        Arrays.sort(points, (a, b) -> a[0] - b[0]);
        int end = Integer.MAX_VALUE;
        int count = 1;
        for(int i = 0; i < points.length; i++){
            int[] p = points[i];
            if(p[0] <= end){
                end = Math.min(end, p[1]);
                if(i == points.length - 1){
                    count++;
                }
            } else {
                end = p[1];
                count++;
            }
        }
        return count;
    }
}
