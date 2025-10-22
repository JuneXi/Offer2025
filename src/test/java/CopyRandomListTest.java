import model.Node;
import topinterview.CopyRandomList;

public class CopyRandomListTest {
    public static void main(String[] args){
        CopyRandomList app = new CopyRandomList();
        Node node0 = new Node(7);
        Node node1 = new Node(13);
        Node node2 = new Node(11);
        Node node3 = new Node(10);
        Node node4 = new Node(1);
        node0.next = node1;
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node1.random = node0;
        node2.random=node4;
        node3.random = node2;
        node4.random = node0;
        app.copyRandomList(node0);
    }
}
