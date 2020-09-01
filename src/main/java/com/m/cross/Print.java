package com.m.cross;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author zhijie.he created on 2020/7/1
 * @version 1.0
 */
public class Print {

    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(30);
    private static final Object lock = new Object();

    public static void main(String[] args) {

        final Print print = new Print();

        new Thread(new Runnable() {
            public void run() {
                print.produce();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    print.say2();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    print.say3();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    print.sayOther();
                }
            }
        }).start();
    }

    public void produce() {
        for (int i = 0; i < 30; i++) {
            queue.offer(i);
            try {
                Thread.sleep(20);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // only 2
    public void say2() {
        synchronized (lock) {
            Integer i = queue.peek();
            if (i == null) {
                return;
            }
            if (i % 2 == 0 && i % 3 != 0) {
                System.out.println("Thread:" + Thread.currentThread().getName() + ",,," + i);
                queue.remove(i);
                lock.notifyAll();
            } else {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // only 3
    public void say3() {
        synchronized (lock) {
            Integer i = queue.peek();
            if (i == null) {
                return;
            }
            if (i % 2 != 0 && i % 3 == 0) {
                System.out.println("Thread:" + Thread.currentThread().getName() + ",,," + i);
                queue.remove(i);
                lock.notifyAll();
            } else {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sayOther() {
        synchronized (lock) {
            Integer i = queue.peek();
            if (i == null) {
                return;
            }
            if ((i % 2 != 0 && i % 3 != 0) || (i % 2 == 0 && i % 3 == 0)) {
                System.out.println("Thread:" + Thread.currentThread().getName() + ",,," + i);
                queue.remove(i);
                lock.notifyAll();
            } else {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
