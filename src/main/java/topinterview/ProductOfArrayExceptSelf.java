package topinterview;

public class ProductOfArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        int zeroCount = 0;
        for(int i : nums){
            if(i == 0){
                zeroCount++;
            }
        }

        if(zeroCount > 1){
            for(int i = 0; i < nums.length; i++){
                nums[i] = 0;
            }
        }

        int[] prefix = new int[nums.length];
        int[] suffix = new int[nums.length];
        int pre = 1;
        for(int i = 0; i < nums.length; i++){
            pre = pre * nums[i];
            prefix[i] = pre;
        }
        int suf = 1;
        for(int i = nums.length - 1; i >= 0; i--){
            suf = suf * nums[i];
            suffix[i] = suf;
        }
        for(int i = 1; i < nums.length - 1; i++){
            nums[i]= prefix[i - 1] * suffix[i + 1];
        }
        nums[0] = suffix[1];
        nums[nums.length - 1] = prefix[nums.length - 2];
        return nums;
    }
}
