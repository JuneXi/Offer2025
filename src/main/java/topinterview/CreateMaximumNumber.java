package topinterview;

import java.util.Stack;

public class CreateMaximumNumber {
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int m = nums1.length;
        int n = nums2.length;

        // we need to store the index of max before i, not include i
        int[] max1 = new int[m];
        max1[0] = -1;
        int[] max2 = new int[n];
        max2[0] = -1;
        for(int i = 1; i < m - 1;i++){
            if(i == 1){
                max1[i] = 0;
                continue;
            }
            max1[i] = Math.max(nums1[max1[i - 2]], nums1[i - 1]);
        }
        for(int i = 1; i < m - 1;i++){
            if(i == 1){
                max2[i] = 0;
                continue;
            }
            max2[i] = Math.max(nums2[max2[i - 2]], nums2[i - 1]);
        }

        Stack<Integer> s1 = generate(nums1);
        Stack<Integer> s2 = generate(nums2);
        while(s1.size() + s2.size()<k){
            // we need more number so we need to find the index of the max number before the stack bottom
            int bottom1 = s1.get(0); // index
            int preMax = max1[0];
            int bottom2 = s2.get(0);
        }
        return null;

    }
    private Stack<Integer> generate(int[] n){
        // stack to store the index, only keep desending order
        Stack<Integer> stack = new Stack<>();
        for(int i = 0; i < n.length; i++){
            while(!stack.isEmpty()&&n[i] > n[stack.peek()]){
                stack.pop();
            }
            stack.push(i);
        }
        return stack;
    }
}
