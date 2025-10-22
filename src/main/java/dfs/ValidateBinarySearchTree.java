package dfs;

import model.TreeNode;
class Result{
    boolean isBST;
    int max;
    int min;
    Result(int max, int min, boolean isBST){
        this.isBST = isBST;
        this.min = min;
        this.max = max;
    }
}

public class ValidateBinarySearchTree {
    public boolean isValidBST(TreeNode root) {
        return helper(root).isBST;

    }

    private Result helper(TreeNode node){
        if(node == null){
            return new Result(Integer.MAX_VALUE, Integer.MIN_VALUE, true);
        }
        if(node.left == null && node.right == null){
            return new Result(node.val, node.val, true);
        }


        Result left = helper(node.left);
        Result right = helper(node.right);
        if(left.isBST && right.isBST && node.val > left.max && node.val < right.min){
            return new Result(right.max, left.min, true);
        }
        return new Result(0, 0, false);
    }
}
