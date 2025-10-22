package topinterview;

public class RemoveDeplicates {
    public int removeDuplicates(int[] nums) {
        int slow = 1;
        int fast = 1;
        while (fast < nums.length) {
            if (nums[slow] <= nums[slow - 1]) {
                while (nums[fast] == nums[fast - 1]) {
                    fast++;
                }
                nums[slow] = nums[fast];
                slow++;
                fast++;
            } else {
                slow++;
            }
        }
        return slow;
    }
}
