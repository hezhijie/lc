package com.m.linked;

public class RevertPerK {

    // k个一组
    public static LinkedNode revertK(LinkedNode node, int k) {

        LinkedNode beforeHead = new LinkedNode(-1);
        beforeHead.next = node;

        LinkedNode head = node;
        LinkedNode startNode = head;
        LinkedNode lastTail = null;
        int i = 1;
        while (i <= k && head.next != null) {
            if (i < k) {
                i++;
                head = head.next;
            } else {
                i = 1;
                head = head.next;
                LinkedNode revert = revert(startNode, head);

                if (lastTail == null) {
                    lastTail = startNode;
                    beforeHead.next = revert;
                } else {
                    lastTail.next = revert;
                    lastTail = startNode;
                }

                System.out.println("fan:"+revert+",start:"+startNode);
                System.out.println("beforeHead.next:" + beforeHead.next);
                startNode = head;
            }
        }
        if (head.next == null) {
            LinkedNode revert = revert(startNode, null);
            if (lastTail == null) {
//                lastTail = startNode;
                beforeHead.next = revert;
            } else {
                lastTail.next = revert;
//                lastTail = startNode;
            }
            System.out.println("fan:"+revert);
        }
        return beforeHead.next;
    }


    public static LinkedNode revert(LinkedNode head, LinkedNode afterNode) {
        LinkedNode newHead = null;
        // 1->null
        // 2->3->4
        while (head != afterNode) {
            LinkedNode next = head.next;
            if (newHead == null) {
                head.next = afterNode;
                newHead = head;
            } else {
                head.next = newHead;
                newHead = head;
            }
            head = next;
        }
        return newHead;
    }

    public static void main(String args[]) {
        LinkedNode head = new LinkedNode(0);
        LinkedNode node1 = new LinkedNode(1);
        LinkedNode node2 = new LinkedNode(2);
        LinkedNode node3 = new LinkedNode(3);
        LinkedNode node4 = new LinkedNode(4);
        LinkedNode node5 = new LinkedNode(5);

        node4.next = node5;
        node3.next = node4;
        node2.next = node3;
        node1.next = node2;
        head.next = node1;

        System.out.println(head);
        System.out.println(revertK(head,4));

    }

}

