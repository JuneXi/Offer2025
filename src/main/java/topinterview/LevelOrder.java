package topinterview;

import model.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LevelOrder {
    public List<List<Integer>> levelOrder(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        q.offer(root);
        while(!q.isEmpty()){
            int l = q.size();
            List<Integer> level = new ArrayList<>();
            for(int i = 0; i < l;i++){
                TreeNode cur = q.poll();
                if(cur.left != null){
                    q.offer(cur.left);
                }
                if(cur.right != null){
                    q.offer(cur.right);
                }
                level.add(cur.val);
            }
            res.add(level);
        }
        return res;

    }
}
