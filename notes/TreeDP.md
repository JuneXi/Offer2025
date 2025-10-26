# 树形动态规划与状态类（Tree DP）  
# Tree Dynamic Programming and State Problems (Tree DP)

## 一、定义  
## I. Definition

树形动态规划（Tree DP）是在树结构上用递归（DFS）进行动态规划的问题。  
Tree DP refers to dynamic programming performed on a tree structure using recursion (DFS).

核心思想：**后序遍历 + 合并子树状态**。  
Core idea: **Post-order traversal + merging subtree states**.

```java
void dfs(int u, int parent) {
    for (int v : graph[u]) {
        if (v == parent) continue;
        dfs(v, u);
        // 状态转移：合并子树 v 的信息
    }
    // 根据子树信息计算当前节点的 dp[u]
}
```
```java
void dfs(int u, int parent) {
    for (int v : graph[u]) {
        if (v == parent) continue;
        dfs(v, u);
        // State transition: merge information from subtree v
    }
    // Compute dp[u] based on subtree info
}
```

## 二、常见状态设计  
## II. Common State Patterns

| 类型 | 状态定义示例 | 思路 |
|------|---------------|------|
| 子树属性类 | `dp[u] = f(子树)` | 计算子树的高度、大小或权值和 |
| 路径类 | `dp[u] = 以u为端点的最优路径` | 求树的直径、最大路径和 |
| 选取类 | `dp[u][0/1]` | 树上独立集、最大权选点问题 |
| 分配类 | `dp[u][k]` | 树上选k个节点的最优值 |
| 换根类 | `dp1[u]`, `dp2[u]` | reroot DP，每个节点为根的答案 |

| Type | State Example | Idea |
|------|---------------|------|
| Subtree Property | `dp[u] = f(subtree)` | Height, size, or weight sum of a subtree |
| Path | `dp[u] = best path ending at u` | Find tree diameter or max path sum |
| Selection | `dp[u][0/1]` | Tree independent set or node selection |
| Distribution | `dp[u][k]` | Choose k nodes optimally in subtree |
| Rerooting | `dp1[u]`, `dp2[u]` | Compute result for all roots via rerooting |

## 三、典型题型与模板  
## III. Typical Problem Patterns and Templates

### 1. 树的直径 / 最大路径和  
### 1. Tree Diameter / Maximum Path Sum

**定义**：树中两点间最长路径。  
**Definition**: The longest path between two nodes in the tree.

**状态**：`dp[u] = 以u为根的最大向下路径长度`  
**State**: `dp[u] = longest downward path from node u`

```java
int res = 0;
int dfs(int u, int parent) {
    int max1 = 0, max2 = 0;
    for (int v : graph[u]) {
        if (v == parent) continue;
        int d = dfs(v, u) + 1;
        if (d > max1) { max2 = max1; max1 = d; }
        else if (d > max2) max2 = d;
    }
    res = Math.max(res, max1 + max2);
    return max1;
}
```

**复杂度**：O(n)  
**Complexity**: O(n)

### 2. 树上独立集（最大权选点，父子不能同选）  
### 2. Tree Independent Set (Maximum Weighted Node Selection)

**题意**：在树中选点使父子不同时选，权值和最大。  
**Problem**: Choose nodes so that no parent and child are both selected, maximizing total weight.

**状态：**  
`dp[u][0]` = 不选u时的最大和  
`dp[u][1]` = 选u时的最大和  

**State:**  
`dp[u][0]` = max sum when u is not chosen  
`dp[u][1]` = max sum when u is chosen  

```java
void dfs(int u, int p) {
    dp[u][0] = 0;
    dp[u][1] = val[u];
    for (int v : g[u]) {
        if (v == p) continue;
        dfs(v, u);
        dp[u][0] += Math.max(dp[v][0], dp[v][1]);
        dp[u][1] += dp[v][0];
    }
}
```
结果：`max(dp[root][0], dp[root][1])`  
Result: `max(dp[root][0], dp[root][1])`  

**复杂度：**O(n)  
**Complexity:** O(n)

### 3. 树形背包（Tree Knapsack）  
### 3. Tree Knapsack

**题意**：每节点是一个物品，子树必须整体选或不选，容量为K求最大价值。  
**Problem**: Each node is an item; subtrees must be taken as a whole; maximize value under capacity K.

**状态定义：**  
`dp[u][k]` = 在u子树中选k个点的最大价值  
**State:**  
`dp[u][k]` = max value by choosing k nodes in subtree of u  

```java
void dfs(int u, int p) {
    dp[u][1] = val[u];
    size[u] = 1;
    for (int v : g[u]) {
        if (v == p) continue;
        dfs(v, u);
        for (int i = size[u]; i >= 1; i--) {
            for (int j = 1; j <= size[v] && i + j <= K; j++) {
                dp[u][i + j] = Math.max(dp[u][i + j], dp[u][i] + dp[v][j]);
            }
        }
        size[u] += size[v];
    }
}
```

**复杂度：**O(nK²)  
**Complexity:** O(nK²)

### 4. 换根DP（Rerooting DP）  
### 4. Rerooting DP

**题意**：计算以每个节点为根时的答案。  
**Problem**: Compute results assuming each node is the root.

**思想**：  
1. DFS1 计算子树值。  
2. DFS2 从父传子“外部贡献”。  
**Idea:**  
1. DFS1 computes subtree values.  
2. DFS2 propagates parent contribution downward.  

```java
int[] dp, ans, size;
void dfs1(int u, int p) {
    dp[u] = 0; size[u] = 1;
    for (int v : g[u]) {
        if (v == p) continue;
        dfs1(v, u);
        dp[u] += dp[v] + size[v];
        size[u] += size[v];
    }
}
void dfs2(int u, int p) {
    ans[u] = dp[u];
    for (int v : g[u]) {
        if (v == p) continue;
        int du = dp[u], dv = dp[v], su = size[u], sv = size[v];
        dp[u] -= dp[v] + sv;
        size[u] -= sv;
        dp[v] += dp[u] + size[u];
        size[v] += size[u];
        dfs2(v, u);
        dp[v] = dv; dp[u] = du; size[v] = sv; size[u] = su;
    }
}
```
**复杂度：**O(n)  
**Complexity:** O(n)

## 四、常见错误  
## IV. Common Pitfalls

1. 忘记传递父节点，导致死循环。  
1. Forget to pass parent parameter, causing infinite loop.  

2. 状态覆盖错误，如未合并完就更新 `dp[u]`。  
2. Overwriting dp[u] before fully merging child states.  

3. 树形背包循环方向错误，应倒序遍历防止重复计数。  
3. Wrong loop direction in tree knapsack; must iterate backward to avoid overcounting.  

4. 初始化错误，max问题应用 `Integer.MIN_VALUE`。  
4. Initialization mistakes; for max problems, set to `Integer.MIN_VALUE`.  

## 五、复杂度总结  
## V. Complexity Summary

| 类型 | 时间复杂度 | 空间复杂度 |
|------|-------------|-------------|
| 一般Tree DP | O(n) | O(n) |
| 树形背包 | O(nK²) | O(nK) |
| 换根DP | O(n) | O(n) |

| Type | Time | Space |
|------|------|-------|
| Normal Tree DP | O(n) | O(n) |
| Tree Knapsack | O(nK²) | O(nK) |
| Rerooting DP | O(n) | O(n) |
