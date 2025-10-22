package topinterview;

import model.TreeNode;

public class LongestUnivaluePath {
    int res;
    public int longestUnivaluePath(TreeNode root) {
        return helper(root);

    }

    // return the length if there is a path include  the current node
    private int helper(TreeNode node){
        if(node == null){
            return 0;
        }
        int left = helper(node.left);
        if(node.left != null){
            if(node.val == node.left.val){
                left = left + 1;
            }else{
                left = 0;
            }
        }
        int right = helper(node.right);
        if(node.right != null){
            if(node.val == node.right.val){
                right = right + 1;
            }else {
                right = 0;
            }
        }
            res = Math.max(res, left + right);

        return Math.max(left,right);

    }
}
