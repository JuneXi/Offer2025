package dfs;

import model.TreeNode;

public class BinaryTreeMaximumPathSum {
    public int maxPathSum(TreeNode root) {
        return helper(root).max;

    }

    private BTMPResult helper(TreeNode node){
        if(node == null){
            return new BTMPResult(0, Integer.MIN_VALUE);
        }
        BTMPResult left = helper(node.left);
        BTMPResult right = helper(node.right);
        int path = Math.max(left.path, right.path) + node.val;
        path = Math.max(path, 0);
        int max = Math.max(left.max, right.max);
        return new BTMPResult(path,Math.max(left.path + right.path + node.val, max) );
    }
}

 class BTMPResult {
    int path;
    int max;
     BTMPResult(int path, int max){
         this.path = path;
         this.max = max;
     }

}
