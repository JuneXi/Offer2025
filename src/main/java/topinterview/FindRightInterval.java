package topinterview;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FindRightInterval {
    public static void main(String[] args) {
        FindRightInterval app = new FindRightInterval();
        int[][] test = {{3,4},{2,3},{1,2}};
        app.findRightInterval(test);
    }
    public int[] findRightInterval(int[][] intervals) {
        int[] res = new int[intervals.length];
        // map (start, next internal start)
        Map<Integer, Integer> m1 = new HashMap<Integer, Integer>();
        // map (start, index)
        Map<Integer, Integer> m2 = new HashMap<Integer, Integer>();
        int[] start = new int[intervals.length];
        for(int i = 0; i < intervals.length;i++){
            //m1.put(intervals[i][0], intervals[i][1]);
            m2.put(intervals[i][0], i);
            start[i] = intervals[i][0];
        }
        Arrays.sort(start);
        for(int i = 0; i < intervals.length;i++) {
            int end = intervals[i][1];
            int next = findFirstGreater(start, end);

            res[i] = next == -1? next:m2.get(next);
        }
        return res;

    }
    public int findFirstGreater(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        int result = -1; // or any default value if not found

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] >= target) {
                result = arr[mid];      // possible candidate
                right = mid - 1;        // try to find smaller one
            } else {
                left = mid + 1;         // look on the right side
            }
        }

        return result; // will be -1 if no element > target
    }
}
