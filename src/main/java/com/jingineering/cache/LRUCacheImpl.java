package com.jingineering.cache;

import java.util.HashMap;
import java.util.Map;

public class LRUCacheImpl {
    public static void main(String[] args) {
        LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 1); // cache is {1=1}
        lRUCache.put(2, 2); // cache is {1=1, 2=2}
        System.out.println(lRUCache.get(1) == 1);    // return 1
        lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
        System.out.println(lRUCache.get(2) == -1);    // returns -1 (not found)
        lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
        System.out.println(lRUCache.get(1) == -1);    // return -1 (not found)
        System.out.println(lRUCache.get(3) == 3);    // return 3
        System.out.println(lRUCache.get(4) == 4);    // return 4
    }
}

class LRUCache {
    DListNode head;
    DListNode tail;
    int capacity;
    Map<Integer, DListNode> map = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.head = new DListNode(-1, -1);
        this.tail = new DListNode(-1, -1);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }

    public int get(int key) {
        if (map.get(key) == null) {
            return -1;
        }

        DListNode node = map.get(key);
        moveNodeToHead(node);
        return node.val;
    }

    public void put(int key, int value) {
        if (map.get(key) != null) {
            // update the value now...
            DListNode node = map.get(key);
            node.val = value;
            moveNodeToHead(node);
        } else {
            DListNode node = new DListNode(key, value);
            map.put(key, node);

            moveToHead(node);
            if (map.size() > capacity) {
                int lastNodeVal = deleteTailNode();
                map.remove(lastNodeVal);
            }
        }
    }

    private int deleteTailNode() {
        DListNode lastNode = tail.prev;
        deleteNodeHere(lastNode);
        return lastNode.key; // return the key, not the value.
    }


    private void moveNodeToHead(DListNode node) {
        deleteNodeHere(node);
        moveToHead(node);
    }

    private void moveToHead(DListNode node) {
        DListNode next = head.next;

        head.next = node;
        node.prev = head;

        node.next = next;
        next.prev = node;
    }

    private void deleteNodeHere(DListNode node) {
        DListNode next = node.next;
        DListNode prev = node.prev;

        next.prev = prev;
        prev.next = next;

        node.next = null;
        node.prev = null;
    }
}

class DListNode {
    int key;
    int val;
    DListNode next;
    DListNode prev;

    DListNode(int key, int val) {
        this.key = key;
        this.val = val;
        this.next = null;
        this.prev = null;
    }
}