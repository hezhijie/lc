package com.m.linked;

//1-2-2-3,  1-2-3
public class DeleteDuplicate {

    public static LinkedNode del(LinkedNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        LinkedNode temp = head;

        while (temp != null && temp.next != null) {
            if (temp.value == temp.next.value) {
                temp.next = temp.next.next;
            }
            temp = temp.next;
        }
        return head;
    }


    public static void main(String args[]) {
        LinkedNode node3 = new LinkedNode(3, null);
        LinkedNode node2 = new LinkedNode(2, node3);
        LinkedNode node22 = new LinkedNode(2, node2);
        LinkedNode node1 = new LinkedNode(1, node22);

        System.out.println(node1);

        del(node1);
        System.out.println(node1);
    }
}
