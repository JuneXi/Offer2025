package topinterview;

import model.ListNode;

public class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        // 1 find length
        int size1 = 0;
        ListNode dummy1 = l1;
        while (l1 != null) {
            size1++;
            l1 = l1.next;
        }

        int size2 = 0;
        ListNode dummy2 = l2;
        while (l2 != null) {
            size2++;
            l2 = l2.next;
        }

        // let l1 be the longest one
        if (size1 < size2) {
            ListNode temp = dummy1;
            dummy1 = dummy2;
            dummy2 = temp;
            int t = size1;
            size1 = size2;
            size2 = t;
        }
        int diff = Math.abs(size1 - size2);
        l1 = dummy1;
        l2 = dummy2;
        while (diff > 0) {
            l1 = l1.next;
            diff--;
        }
        ListNode node = helper(l1, l2);
        // not done
        return node;
    }


        private ListNode helper(ListNode l1, ListNode l2){
            if (l1 == null) {
                return null;
            }
            ListNode next = helper(l1.next, l2.next);

            int add = next.val/10;
            next.val = next.val%10;
            ListNode cur = new ListNode(l1.val + l2.val + add);
            cur.next = next;
            return cur;

        }

}
