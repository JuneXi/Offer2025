package topinterview;

import java.util.HashMap;
import java.util.Map;

public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        // map to store the number and the end of the sequence end with this number in
        Map<Integer, Integer> map = new HashMap<>();
        int res = 0;
        for(int n: nums){
            if(map.containsKey(n + 1)){
                map.put(n, map.get(n + 1));
                map.put(map.get(n + 1), n);
                res = Math.max(res, map.get(n + 1) - n + 1);
            }
            if(map.containsKey(n - 1)){
                map.put(n, map.get(n - 1));
                map.put(map.get(n - 1), n);
                res = Math.max(res, n - map.get(n - 1) + 1);
            }
        }
        return res;


    }
}
