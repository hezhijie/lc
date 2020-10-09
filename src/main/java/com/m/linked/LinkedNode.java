package com.m.linked;

/**
 * @author zhijie.he created on 2020/5/9
 * @version 1.0
 */
public class LinkedNode {
    public int value;
    public LinkedNode next;

    public LinkedNode(){}
    public LinkedNode(int value){
        this.value = value;
    }

    public LinkedNode(int value,LinkedNode next) {
        this.value = value;
        this.next = next;
    }

    @Override public String toString() {
        if (next == null) {
            return value + "";
        }
        return value + "->" + next.toString();
    }
}
