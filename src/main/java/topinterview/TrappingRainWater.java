package topinterview;

public class TrappingRainWater {
    public int trap(int[] height) {
        int leftM = height[0];
        int rightM = height[height.length - 1];
        int left = 0;
        int right = height.length - 1;
        int res = 0;
        while(left < right){
            if(leftM > rightM) {
                right --;
                rightM = Math.max(height[right], rightM);
                res = res + rightM - height[right];
            } else {
                left ++;
                leftM = Math.max(height[left], leftM);
                res = res + leftM - height[left];
            }
        }
        return res;

    }
}
