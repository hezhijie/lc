package com.m.linked;

import java.util.Date;

/**
 * @author zhijie.he created on 2020/7/28
 * @version 1.0
 */
public class Lru {
    class Node {
        private String key;
        private String value;
        private Node next;
        private Node pre;

        public Node(String key,String value) {
            this.key = key;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date.getMonth());
    }
}
