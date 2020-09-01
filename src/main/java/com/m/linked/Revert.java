package com.m.linked;

/**
 * @author zhijie.he created on 2020/5/9
 * @version 1.0
 */
public class Revert {

    public LinkedNode build() {
        LinkedNode node1 = new LinkedNode(4, null);
        LinkedNode node2 = new LinkedNode(3, node1);
        LinkedNode node3 = new LinkedNode(2, node2);
        LinkedNode node4 = new LinkedNode(1, node3);
        return node4;
    }

    /**
     * 两两交换相邻节点
     *
     * @param head
     * @return
     */
    public LinkedNode sw(LinkedNode head) {
        System.out.println(head);
        // 0- 1-2-3-4
        LinkedNode zero = new LinkedNode();
        zero.next = head;
        LinkedNode pre = zero;
        while (pre.next != null && pre.next.next!= null) {
            LinkedNode node1 = pre.next;
            LinkedNode node2 = node1.next;
            LinkedNode node3 = node2.next;
            node2.next = node1;
            node1.next = node3;
            pre.next = node2;
            pre = node1;
        }
        return zero.next;
    }

    // 反转
    public LinkedNode res(LinkedNode head) {
        System.out.println(head);
        LinkedNode newHead = null;
        // 1->null
        // 2->3->4
        while (head != null) {
            LinkedNode next = head.next;
            if (newHead == null) {
                head.next = null;
                newHead = head;
            } else {
                head.next = newHead;
                newHead = head;
            }
            head = next;
            //            LinkedNode temp = head.next; // 先保留next值
            //            head.next = newHead; // 截断原来链表并指向新的链表
            //            newHead = head; // 新链表的头指向拿到的新结点
            //            head = temp; // 指针指向next
        }
        return newHead;
    }

    public LinkedNode robin(LinkedNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        LinkedNode temp = head.next;
        LinkedNode tail = robin(temp);
        head.next = null;
        temp.next = head;
        return tail;
    }

    /**
     * 合并两个排序的链表
     * 1-3-5
     * 2-4-6
     *
     * @param node1
     * @param node2
     * @return
     */
    public LinkedNode merge(LinkedNode node1, LinkedNode node2) {
        if (node1 == null) {
            return node2;
        }
        if (node2 == null) {
            return node1;
        }

        LinkedNode node = null;
        LinkedNode point = null;

        while (node1 != null && node2 != null) {
            if (node1.value <= node2.value) {
                if (node == null) {
                    node = node1;
                    point = node;
                } else {
                    point.next = node1;
                    point = point.next;
                }
                node1 = node1.next;
            } else {
                if (node == null) {
                    node = node2;
                    point = node;
                } else {
                    point.next = node2;
                    point = point.next;
                }
                node2 = node2.next;
            }
        }

        if (node1 == null) {
            point.next = node2;
        } else {
            point.next = node1;
        }

        // 递归
        /*if (node1.value<=node2.value) {
            node = node1;
            node.next = merge(node1.next,node2);
        } else {
            node = node2;
            node.next = merge(node1,node2.next);
        }*/

        return node;
    }

    public static void main(String args[]) {
        Revert revert = new Revert();
        LinkedNode node1 = revert.build();
        LinkedNode node2 = revert.build();

        LinkedNode res = revert.sw(node1);

        System.out.println(res);
    }

}
