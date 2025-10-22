package dfs;

import model.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class MyUtils {

    private MyUtils() {}


    public static TreeNode buildTree(List<Integer> values){
        if (values == null || values.isEmpty() || values.get(0) == null) return null;

        TreeNode root = new TreeNode(values.get(0));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int i = 1;

        while (i < values.size()) {
            TreeNode current = queue.poll();

            if (i < values.size() && values.get(i) != null) {
                current.left = new TreeNode(values.get(i));
                queue.add(current.left);
            }
            i++;

            if (i < values.size() && values.get(i) != null) {
                current.right = new TreeNode(values.get(i));
                queue.add(current.right);
            }
            i++;
        }

        return root;
    }
}
