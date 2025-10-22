package topinterview;

import model.TreeNode;

//TODO check stack solution
public class MaximumBinaryTree {
    public static void main(String[] args) {
        MaximumBinaryTree app = new MaximumBinaryTree();
        int[] test = {3,2,1,6,0,5};
        app.constructMaximumBinaryTree(test);
    }
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        int[][] dp = new int[nums.length][nums.length];
        // dp[i][j] mean from i to j, the max number's index

        // from i to i, the max is nums[i]
        for(int i = 0; i < nums.length; i++){
            dp[i][i] = i;
        }
        for(int l = 1; l < nums.length; l++){
            for(int i = 0; i < nums.length; i++){
                int j = i + l;
                if(j > nums.length - 1){
                    break;
                }
                dp[i][j] = nums[j] > nums[dp[i][j-1]]?j:dp[i][j-1];
            }
        }
        return helper(0, nums.length - 1, nums, dp);
    }

    private TreeNode helper(int left, int right, int[] nums, int[][]dp){
        if(right == -1 || left == nums.length){
            return null;
        }
        int maxIndex = dp[left][right];
        TreeNode node = new TreeNode(nums[maxIndex]);

        node.left = helper(left, maxIndex - 1, nums, dp);
        node.right = helper(maxIndex + 1, right, nums, dp);
        return node;
    }
}
