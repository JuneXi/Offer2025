package dfs;

import model.TreeNode;

import java.util.HashMap;
import java.util.Map;

public class HouseRobber3 {
    Map<TreeNode, Integer> rob = new HashMap<>();
    Map<TreeNode, Integer> nonRob = new HashMap<>();
    public int rob(TreeNode root) {
        return Math.max(dfsNoRob(root), dfsRob(root));

    }

    private int dfsRob(TreeNode root){
        if(root == null){
            return 0;
        }
        if(rob.containsKey(root)){
            return rob.get(root);
        }

        int res = root.val + dfsNoRob(root.right) + dfsNoRob(root.left);
        rob.put(root, res);
        return res;
    }

    private int dfsNoRob(TreeNode root){
        if(root == null){
            return 0;
        }
        if(nonRob.containsKey(root)){
            return nonRob.get(root);
        }
        int res = Math.max(dfsNoRob(root.left), dfsRob(root.left)) + Math.max(dfsNoRob(root.right), dfsRob(root.right));
        nonRob.put(root, res);
        return res;

    }
}
