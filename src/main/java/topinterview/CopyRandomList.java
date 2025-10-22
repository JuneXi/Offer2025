package topinterview;

import model.Node;

public class CopyRandomList {
    public Node copyRandomList(Node head) {
        Node dummy = new Node(0);
        dummy.next = head;
        //step 1 copy
        while(head != null){
            Node temp = head.next;
            head.next = new Node(head.val);
            head.next.next = temp;
            head = head.next.next;
        }
        head = dummy.next;
        //copy random
        while(head != null){
            if(head.random != null){
                head.next.random = head.random.next;
            }
            head = head.next.next;
        }
        //new node
        head = dummy.next;
        Node newHead = head.next;
        while(head != null){
            Node temp = head.next;
            head.next = temp.next;
            if(temp.next != null){
                temp.next = temp.next.next;
            }
            head = head.next;
        }
        return newHead;

    }
}
