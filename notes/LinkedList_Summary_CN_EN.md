# **链表（Linked List）总结 | Linked List Summary**

## 一、基本概念 | Basic Concepts
链表是一种线性数据结构，节点在内存中不连续，通过指针连接。  
A linked list is a linear data structure where elements are not stored at contiguous memory locations but connected by pointers.

```java
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
```

## 二、常见类型 | Common Types
| 类型 | Type | 描述 | Description |
|------|------|------|-------------|
| 单链表 | Singly Linked List | 每节点指向下一个节点 | Each node points to the next node |
| 双向链表 | Doubly Linked List | 节点含 prev 与 next | Each node has both prev and next |
| 循环链表 | Circular Linked List | 尾节点指回头节点 | Tail node connects back to head |

## 三、复杂度与特性 | Complexity & Features
| 操作 | Operation | 时间复杂度 | Time Complexity |
|------|------------|-------------|-----------------|
| 插入节点 | Insert | O(1) | O(1) |
| 删除节点 | Delete | O(1) | O(1) |
| 查找节点 | Search | O(n) | O(n) |
| 访问第 k 个节点 | Access kth | O(n) | O(n) |

## 四、经典题型 | Classic Problems
(内容略，包含 206, 141, 19, 21, 876, 234, 142, 148, 23, 138 题，含中英文思路与 Java 实现)