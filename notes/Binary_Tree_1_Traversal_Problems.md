# 🌳 二叉树遍历类题目 Binary Tree Traversal Problems

---

## 一、概述 Overview

遍历类题目目标：访问树中所有节点并按特定顺序处理。  
本质上是 **深度优先搜索（DFS）** 或 **广度优先搜索（BFS）** 的不同变体。

---

## 二、深度优先遍历 DFS Traversals

### 1. 前序遍历 Preorder Traversal

**访问顺序：** 根 → 左 → 右  
**思路：**
- 先访问当前节点（根），再递归左、右子树。
- 属于“自顶向下”逻辑。

**特点：**
- 每到一个节点就立即处理自身。
- 适合树的复制、路径构建、序列化。

**时间复杂度：** O(n)  
**空间复杂度：** O(h)，其中 h 为树高（递归栈深度）。

**典型题：**
- LC 144 Binary Tree Preorder Traversal  
- LC 257 Binary Tree Paths  
- LC 112 Path Sum

---

### 2. 中序遍历 Inorder Traversal

**访问顺序：** 左 → 根 → 右  
**思路：**
- 先递归访问左子树，再访问当前节点，最后访问右子树。
- 在二叉搜索树（BST）中可得到有序序列。

**特点：**
- 左到右天然有序。
- 常用于验证BST、查找第k小节点等。

**时间复杂度：** O(n)  
**空间复杂度：** O(h)

**典型题：**
- LC 94 Binary Tree Inorder Traversal  
- LC 98 Validate BST  
- LC 230 Kth Smallest Element in BST

---

### 3. 后序遍历 Postorder Traversal

**访问顺序：** 左 → 右 → 根  
**思路：**
- 必须等左右子树都访问完后再访问根。
- 属于“自底向上”逻辑。

**特点：**
- 子树结果在先，根节点汇总结果。
- 常用于计算深度、直径、LCA、路径和。

**时间复杂度：** O(n)  
**空间复杂度：** O(h)

**典型题：**
- LC 145 Binary Tree Postorder Traversal  
- LC 104 Maximum Depth of Binary Tree  
- LC 543 Diameter of Binary Tree  
- LC 236 Lowest Common Ancestor

---

## 三、广度优先遍历 BFS Traversal

### 层序遍历 Level Order Traversal

**访问顺序：** 按层从上到下，从左到右。  
**思路：**
- 使用队列存储每层节点。  
- 出队一个节点时，将其左右子节点入队。  

**特点：**
- 按层处理问题（如锯齿层序、右视图）。
- 天然用于最短路径搜索。

**时间复杂度：** O(n)  
**空间复杂度：** O(n)（队列最多存储一层节点）

**典型题：**
- LC 102 Binary Tree Level Order Traversal  
- LC 103 Zigzag Level Order Traversal  
- LC 199 Binary Tree Right Side View

---

## 四、统一模板 Unified DFS Framework

```java
void traverse(TreeNode root) {
    if (root == null) return;

    // 前序位置：第一次到达节点
    traverse(root.left);
    // 中序位置：第二次到达节点
    traverse(root.right);
    // 后序位置：第三次到达节点
}
```

**三种遍历区别：**
- 前序：在第一次到达时处理节点。  
- 中序：在第二次到达时处理节点。  
- 后序：在第三次到达时处理节点。  

---

## 五、复杂度总结 Complexity Summary

| 遍历类型 | 时间复杂度 | 空间复杂度 | 说明 |
|-----------|--------------|-------------|------|
| 前序 Preorder | O(n) | O(h) | 递归或栈深度取决于树高 |
| 中序 Inorder | O(n) | O(h) | BST中用于升序输出 |
| 后序 Postorder | O(n) | O(h) | 汇总左右子树信息 |
| 层序 Level Order | O(n) | O(n) | 队列存储每层节点 |

---

## 六、应用方向 Applications

| 应用场景 | 适用遍历 | 原因 |
|-----------|------------|------|
| 构造路径、打印结构 | 前序 | 根节点先访问，便于记录路径 |
| 验证BST、输出升序 | 中序 | 左<根<右天然有序 |
| 求深度、路径和、LCA | 后序 | 子树结果汇总后处理根 |
| 按层输出或最短路径 | 层序 | BFS天然分层 |

---

_English Summary_

Each node is visited in a specific order:
- **Preorder:** process root first (top-down tasks)
- **Inorder:** process between left and right (sorted order in BST)
- **Postorder:** process after children (bottom-up computations)
- **Level Order:** process by levels (BFS)

All have O(n) time. Recursive DFS uses O(h) stack space; BFS uses O(n) queue space.
