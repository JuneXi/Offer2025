package dfs;

import model.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathSum2 {


    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {

        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        helper(res, path, targetSum, root);
        return res;
    }

    private void helper(List<List<Integer>> res, List<Integer> path, int targetSum, TreeNode root ){
        if(root == null){
            System.out.print("Node is null, return" );
            System.out.println();
            return;
        }
        System.out.print("we are at node " );
        System.out.print(root.val );
        System.out.println();
        if(root.val == targetSum){
            path.add(root.val);
            System.out.print("find a res " );
            System.out.println(path.stream().map(String::valueOf).collect(Collectors.joining(",")));
            System.out.println();
            res.add(new ArrayList<>(path));
            path.remove(path.size() - 1);
        }
        path.add(root.val);
        System.out.print("Start to look for left, target is " );
        System.out.print(targetSum - root.val );
        System.out.println();
        helper(res, path, targetSum - root.val, root.left);
        System.out.print("Start to look for right, target is " );
        System.out.print(targetSum - root.val );
        System.out.println();
        helper(res, path, targetSum - root.val, root.right);
        System.out.print("Didn't find target is " );
        System.out.print(targetSum - root.val );
        System.out.println();
        path.remove(path.size() - 1);
    }

    public static void  main (String[] args){
        PathSum2 app = new PathSum2();
        List<Integer> values = Arrays.asList(5,4,8,11,null,13,4,7,2,null,null,5,1);

        TreeNode root = MyUtils.buildTree(values);
        app.pathSum(root, 22);
    }
}
