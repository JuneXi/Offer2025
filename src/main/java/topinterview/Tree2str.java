package topinterview;

import model.TreeNode;

public class Tree2str {
    public String tree2str(TreeNode root) {
        if(root == null){
            return "";
        }
        return helper(root);

    }
    private String helper(TreeNode node){
        if(node.left == null && node.right == null){
            return String.valueOf(node.val);
        }
        if(node.right == null){
            return node.val + "(" + helper(node.left) + ")";
        }
        if(node.left == null){
            return node.val + "()"+helper(node.right);
        }
        return node.val + "(" + helper(node.left) + ")(" + helper(node.right) + ")";
    }
}
