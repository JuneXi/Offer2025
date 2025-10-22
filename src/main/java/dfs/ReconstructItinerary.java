package dfs;

import model.GNode;

import java.util.*;

public class ReconstructItinerary {
    public List<String> findItinerary(List<List<String>> tickets) {

        GNode root = buildGraphic(tickets);
        List<String> res = new ArrayList<>();
        res.add(root.val);
        while(root.to > 0){
            GNode next = root.nexts.get(0);
            res.add(next.val);
            root.to = root.to - 1;
            root.nexts.remove(0);
            root = next;
        }
        return res;

    }

    private GNode buildGraphic(List<List<String>> tickets){
        Map<String, GNode> map = new HashMap<>();
        for (List<String> t : tickets) {
            String from = t.get(0);
            String to = t.get(1);

            // Get or create the 'from' node
            GNode fromNode = map.computeIfAbsent(from, GNode::new);
            fromNode.to++;

            // Get or create the 'to' node
            GNode toNode = map.computeIfAbsent(to, GNode::new);

            // Add to adjacency list
            fromNode.nexts.add(toNode);
        }
        map.forEach((key, value) -> value.nexts.sort(Comparator.comparing(node -> node.val)));
        return map.get("JFK");
    }
}
