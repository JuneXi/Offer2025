# 递归题型与设计指南 Recursion Patterns and Design Guide

---

## 一、结构自相似类（Structural Recursion）

**特点 Characteristics**
- 问题结构自相似（如树、链表、图）。  
- 每层递归处理一个节点或子结构。  
- 常见于树遍历、路径计算、属性求解。

**典型题 Example**：104. Maximum Depth of Binary Tree

```java
int maxDepth(TreeNode root) {
    if (root == null) return 0;
    return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
}
```

**时间复杂度 Time**：O(n)  
**空间复杂度 Space**：O(h)

**易错点 Pitfalls**
- 忘写 base case（root == null）。  
- 忽略 +1（当前节点层）。  
- 未考虑空树。

---

## 二、分治类（Divide and Conquer）

**特点**
- 问题可拆为相同类型的子问题。  
- 各子问题独立求解后合并结果。  
- 常见于排序、搜索、矩阵问题。

**典型题**：912. Sort an Array

```java
int[] sortArray(int[] nums) {
    if (nums.length <= 1) return nums;
    int mid = nums.length / 2;
    int[] left = sortArray(Arrays.copyOfRange(nums, 0, mid));
    int[] right = sortArray(Arrays.copyOfRange(nums, mid, nums.length));
    return merge(left, right);
}
```

**时间复杂度**：O(n log n)  
**空间复杂度**：O(n)

**易错点**
- 合并区间索引出错。  
- 忘终止条件导致无限递归。

---

## 三、数学递推类（Mathematical Recursion）

**特点**
- 问题定义天然递归。  
- 输入规模递减直至最小情况。  
- 可转化为动态规划。

**典型题**：509. Fibonacci Number

```java
int fib(int n) {
    if (n <= 1) return n;
    return fib(n - 1) + fib(n - 2);
}
```

**时间复杂度**：O(2^n)  
**空间复杂度**：O(n)

**易错点**
- 漏写 base case。  
- 重复计算未记忆化。  
- 栈溢出。

---

## 四、回溯类（Backtracking）

**特点**
- 递归用于枚举所有可能。  
- 每层选择 → 递归 → 撤销选择。  
- 常用于组合、排列、子集、括号生成。

**典型题**：46. Permutations

```java
List<List<Integer>> res = new ArrayList<>();
void backtrack(List<Integer> path, boolean[] used, int[] nums) {
    if (path.size() == nums.length) {
        res.add(new ArrayList<>(path));
        return;
    }
    for (int i = 0; i < nums.length; i++) {
        if (used[i]) continue;
        used[i] = true;
        path.add(nums[i]);
        backtrack(path, used, nums);
        path.remove(path.size() - 1);
        used[i] = false;
    }
}
```

**时间复杂度**：O(n!)  
**空间复杂度**：O(n)

**易错点**
- 忘记撤销操作。  
- 忘复制路径。  
- 提前终止条件错误。

---

## 五、路径与搜索类（Path / Search Recursion）

**特点**
- 递归沿不同方向探索路径。  
- 常用于树路径、图DFS、迷宫搜索。  

**典型题**：112. Path Sum

```java
boolean hasPathSum(TreeNode root, int targetSum) {
    if (root == null) return false;
    if (root.left == null && root.right == null)
        return root.val == targetSum;
    return hasPathSum(root.left, targetSum - root.val) ||
           hasPathSum(root.right, targetSum - root.val);
}
```

**时间复杂度**：O(n)  
**空间复杂度**：O(h)

**易错点**
- 忘判断叶节点。  
- 目标值未更新。  
- 逻辑错误用 && 代替 ||。

---

## 六、动态规划转化类（Recursion → DP）

**特点**
- 有重叠子问题。  
- 可自顶向下递归 + 记忆化优化。  
- 常作为DP入门。

**典型题**：62. Unique Paths

```java
int[][] memo;
int uniquePaths(int m, int n) {
    memo = new int[m][n];
    return dfs(m - 1, n - 1);
}
int dfs(int i, int j) {
    if (i == 0 || j == 0) return 1;
    if (memo[i][j] != 0) return memo[i][j];
    memo[i][j] = dfs(i - 1, j) + dfs(i, j - 1);
    return memo[i][j];
}
```

**时间复杂度**：O(m×n)  
**空间复杂度**：O(m×n)

**易错点**
- 忘初始化记忆数组。  
- 未检测缓存。  
- 越界条件写错。

---

## 通用递归设计与调试指南（Universal Design Guide）

### 一、递归函数四要素

| 要素 | 含义 | 思考方式 | 常见错误 |
|------|------|-----------|-----------|
| 输入参数 | 当前状态 | 哪些变量决定状态？ | 参数冗余或缺失 |
| Base Case | 停止条件 | 最小规模是否已知？ | 无限递归 |
| 返回值 | 子结果或状态 | 递归返回什么？ | 返回混乱或漏return |
| 状态变化方向 | 逼近Base Case | 状态是否收敛？ | 死循环 |

### 二、Base Case设计

- 从最小规模找出口：空节点、数组长度0、索引越界、目标为0。  
- 确保每条路径都能触发终止条件。  
- Base Case应能直接返回结果。

### 三、返回值选择

1. **结果型**（如深度、数量）→ return合并结果。  
2. **状态型**（如路径）→ 用外部集合收集，不返回。  
3. **布尔型**（存在性判断）→ 返回true/false剪枝。  
4. **聚合型**（合并多个结果）→ 返回组合值。

### 四、输入参数选择

- 仅传必要状态（索引、节点、目标值）。  
- 避免依赖全局变量。  
- 参数应在每层变化以收敛。

### 五、递归方向控制

- 索引前进或缩小区间。  
- 节点逐步向叶节点。  
- 目标或计数递减。  

### 六、调试技巧

- 打印递归进入/退出日志。  
- 画递归树分析调用关系。  
- 从小规模输入验证过程。  

### 七、性能优化

| 问题 | 解决 |
|------|------|
| 重叠子问题 | 记忆化缓存 |
| 递归太深 | 尾递归或迭代 |
| 栈空间不足 | 显式栈 |
| 无效搜索 | 剪枝 |

---

**核心思路总结**  
> 递归三问：  
> 1. 什么时候停？（Base Case）  
> 2. 每次做什么？（递推逻辑）  
> 3. 下一步往哪走？（状态收敛）  
>  
> 若三问皆有答案，则可安全写递归。
