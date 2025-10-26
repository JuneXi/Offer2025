# 🌳 二叉树遍历统一框架 Binary Tree Traversal Framework

## 一、核心思想 Core Idea

每个节点在递归过程中会被“经过三次”，三种遍历方式的区别仅在于**在哪一次访问时处理节点值**。

| 遍历类型 | 处理时机 | 访问顺序 | 根节点位置 |
|-----------|-----------|-----------|-------------|
| 前序 Preorder | 第一次到达节点时 | 根 → 左 → 右 | 根在最前 |
| 中序 Inorder | 从左子树返回时 | 左 → 根 → 右 | 根在中间 |
| 后序 Postorder | 从右子树返回后 | 左 → 右 → 根 | 根在最后 |

## 二、示例树 Example Tree

```
        1
       /       2   3
```

## 三、统一递归框架 Unified Recursive Framework

```java
void traverse(TreeNode root) {
    if (root == null) return;

    // 前序位置 Preorder position
    traverse(root.left);

    // 中序位置 Inorder position
    traverse(root.right);

    // 后序位置 Postorder position
}
```

三种遍历只是把“打印”操作放在不同位置：

```java
// 前序遍历 Preorder
void preorder(TreeNode root) {
    if (root == null) return;
    System.out.print(root.val + " ");   // 第一次到达
    preorder(root.left);
    preorder(root.right);
}

// 中序遍历 Inorder
void inorder(TreeNode root) {
    if (root == null) return;
    inorder(root.left);
    System.out.print(root.val + " ");   // 第二次到达
    inorder(root.right);
}

// 后序遍历 Postorder
void postorder(TreeNode root) {
    if (root == null) return;
    postorder(root.left);
    postorder(root.right);
    System.out.print(root.val + " ");   // 第三次到达
}
```

## 四、调用栈展开示意 Call Stack Visualization

```
traverse(1)
 ├─ 前序位置（第一次到达 1）
 │
 ├─ traverse(2)
 │   ├─ 前序位置（第一次到达 2）
 │   ├─ traverse(null) → 返回
 │   ├─ 中序位置（第二次到达 2）
 │   ├─ traverse(null) → 返回
 │   └─ 后序位置（第三次到达 2）
 │
 ├─ 中序位置（第二次到达 1）
 │
 ├─ traverse(3)
 │   ├─ 前序位置（第一次到达 3）
 │   ├─ traverse(null) → 返回
 │   ├─ 中序位置（第二次到达 3）
 │   ├─ traverse(null) → 返回
 │   └─ 后序位置（第三次到达 3）
 │
 └─ 后序位置（第三次到达 1）
```

## 五、路径与访问顺序 Traversal Paths

| 遍历类型 | 输出顺序 | 应用场景 |
|-----------|-----------|-----------|
| 前序 Preorder | 1 2 4 5 3 | 序列化/复制树结构 |
| 中序 Inorder | 4 2 5 1 3 | BST中获得升序序列 |
| 后序 Postorder | 4 5 2 3 1 | 删除节点/求路径和 |

## 六、要点总结 Key Takeaways

1. 每个节点递归过程都被**访问三次**。  
2. “前、中、后”仅表示**根节点相对左右子树的访问位置**。  
3. 所有深度优先搜索（DFS）树题都能归纳到此框架。  

_English Summary_

Every node in recursion is visited three times:
- Before exploring the left subtree → **Preorder**
- Between left and right subtrees → **Inorder**
- After exploring both subtrees → **Postorder**

All DFS tree problems are variants of this unified traversal framework.
