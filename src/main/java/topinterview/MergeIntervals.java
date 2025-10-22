package topinterview;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        List<int[]> res = new ArrayList<>();
        int start = -1;
        int end = -1;

        for(int[] i: intervals){
            if(i[0] > end){
                res.add(new int[]{i[0], i [1]});

            } else {
                int[] last = res.get(res.size() - 1);
                int newEnd = Math.max(last[1], i[1]);
                last[1] = newEnd;
            }
        }
        int[][] r = new int[res.size()][2];
        for(int i = 0; i < r.length; i++){
            r[i] = res.get(i);
        }
        return r;

    }
}
