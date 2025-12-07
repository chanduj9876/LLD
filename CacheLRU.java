package org.example.LLD;

import java.util.*;

class DLL {
    String key;
    String val;
    DLL prev, next;

    DLL(String key, String val) {
        this.key = key;
        this.val = val;
    }
}

class Cache {
    int size;
    int count;
    DLL head, tail;
    HashMap<String, DLL> cacheMap;

    Cache(int size) {
        this.size = size;
        cacheMap = new HashMap<>();

        head = new DLL("", "");
        tail = new DLL("", "");

        head.next = tail;
        tail.prev = head;
    }

    // Move node to front (MRU)
    private void moveToFront(DLL node) {
        removeNode(node);
        addNode(node);
    }

    // Add node right after head (MRU)
    private void addNode(DLL node) {
        DLL temp = head.next;

        head.next = node;
        node.prev = head;

        node.next = temp;
        temp.prev = node;
    }

    // Remove node from the list
    private void removeNode(DLL node) {
        DLL p = node.prev;
        DLL n = node.next;

        p.next = n;
        n.prev = p;
    }

    // Remove LRU (node before tail)
    private void removeLRU() {
        DLL lru = tail.prev;
        removeNode(lru);
        cacheMap.remove(lru.key);
        count--;
    }

    public void put(String key, String val) {
        if (cacheMap.containsKey(key)) {
            DLL node = cacheMap.get(key);
            node.val = val;
            moveToFront(node);
            return;
        }

        DLL newNode = new DLL(key, val);
        cacheMap.put(key, newNode);
        addNode(newNode);
        count++;

        if (count > size) {
            removeLRU();
        }
    }

    public String get(String key) {
        if (!cacheMap.containsKey(key))
            return null;

        DLL node = cacheMap.get(key);
        moveToFront(node);
        return node.val;
    }

    public void display() {
        DLL temp = head.next;
        System.out.print("Cache: ");
        while (temp != tail) {
            System.out.print("(" + temp.key + ":" + temp.val + ") ");
            temp = temp.next;
        }
        System.out.println();
    }
}

public class CacheLRU {
    public static void main(String[] args) {
        Cache c = new Cache(2);
        c.put("A", "1");
        c.put("B", "2");
        c.display();     // A B

        c.get("A");
        c.display();     // B A  (A becomes MRU)

        c.put("C", "3"); // removes B
        c.display();     // A C
    }
}

