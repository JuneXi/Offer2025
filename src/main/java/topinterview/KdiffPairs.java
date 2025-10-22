package topinterview;

import java.util.Arrays;

public class KdiffPairs {
    public int findPairs(int[] nums, int k) {
        if(nums.length == 1){
            return 0;
        }
        Arrays.sort(nums);
        int start = 0;
        while(start < nums.length -2 && nums[start] == nums[start + 1]){
            start ++;
        }
        if(start == nums.length - 1){
            return k == 0? 1: 0;
        }
        int end = start + 1;
        while(end < nums.length){

        }
        return 0;
    }
}
