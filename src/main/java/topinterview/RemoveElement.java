package topinterview;

public class RemoveElement {
    public int removeElement(int[] nums, int val) {
        if(val > 50) {
            return nums.length;
        }

        int start = 0;
        int end = nums.length -1;
        int count = 0;
        while(start <= end){
            if(nums[start] == val){
                count++;
                while(start < end && nums[end]==val){
                    end--;
                    count++;
                }
                nums[start]=nums[end];
                nums[end] = val;
                start++;
                end--;
            } else {
                start++;
            }
        }
        return nums.length - count;
    }
}
