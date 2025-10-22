package topinterview;

public class FlattenMultiDoublyLinkedList {
    public Node flatten(Node head) {
        Node dummy = head;
        helper(head);
        return dummy;
    }

// a helper class to return the last node of the child

    private Node helper(Node node){
        if(node == null) return node;

        if(node.next == null && node.child == null){
            return node;
        }
        //find next node with child
        while(node.next != null && node.child == null){
            node = node.next;
        }
        // now either this is the last node, or child not null or both
        // last node && no child
        if(node.child == null){
            return node;
        } else if(node.next == null) { //last node and child
            node.next = node.child;
            node.next.prev = node;
            node.child = null;
            return helper(node.next);
        }else { // mid node and child
                Node last = helper(node.child); // 12
                Node next = node.next; //9
                node.next = node.child; //
                node.child.prev = node;
                node.child = null;
                last.next = next;
                next.prev = last;
                return helper(next);
            }
        }
    }



class Node {
    public int val;
    public Node prev;
    public Node next;
    public Node child;
};
