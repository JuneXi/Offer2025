package topinterview;

import model.TreeNode;

public class PathSum3 {
    public int pathSum(TreeNode root, int targetSum) {
        if(root == null){
            return 0;
        }
        if(root.val == targetSum){
            return 1 + pathSum(root.left, 0) + pathSum(root.right, 0);
        }
        return pathSum(root.left, targetSum - root.val) + pathSum(root.right, targetSum - root.val) + pathSum(root.left, targetSum) + pathSum(root.right, targetSum);


    }
}
