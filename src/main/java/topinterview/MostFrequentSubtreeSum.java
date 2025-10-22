package topinterview;

import model.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MostFrequentSubtreeSum {
    public int[] findFrequentTreeSum(TreeNode root) {
        Map<Integer, Integer> map = new HashMap<>();
        helper(root, map);
        int r = 0;
        for(Map.Entry<Integer, Integer> entry: map.entrySet()){
            r = Math.max(r, entry.getValue());
        }
        List<Integer> list = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry: map.entrySet()){
            if(entry.getValue() == r){
                list.add(entry.getKey());
            }
        }
        return list.stream().mapToInt(Integer::intValue).toArray();

    }

    private int helper(TreeNode root, Map<Integer, Integer> map){
        // if leaf, put into map
        if(root.left == null && root.right == null){
            map.putIfAbsent(root.val, 0);
            map.put(root.val, map.get(root.val) + 1);
            return root.val;
        }
        int cur = root.val;
        if(root.left != null){
            cur += helper(root.left, map);
        }
        if(root.right != null){
            cur += helper(root.right, map);
        }
        map.putIfAbsent(cur, 0);
        map.put(cur, map.get(cur) + 1);
        return cur;
    }
}
