# BFS（广度优先搜索）详解与经典题 Word Ladder

---

## 一、核心思想 Core Idea  
逐层扩展，从起点开始，依次访问所有距离为1、2、3…的节点。  
Start from the source node and explore all nodes level by level.

**关键特征 Key Characteristics**  
- 使用 **队列 Queue** 实现（FIFO）  
- 适用于 **最短路径 / 连通分量 / 层级遍历**  
- 每个节点只访问一次，防止重复  

---

## 二、算法框架 Algorithm Framework  

**模板（无权图）Java：**
```java
void bfs(Node start) {
    Queue<Node> q = new LinkedList<>();
    Set<Node> visited = new HashSet<>();
    q.offer(start);
    visited.add(start);

    while (!q.isEmpty()) {
        Node cur = q.poll();
        // 访问当前节点 process current node
        for (Node next : cur.neighbors) {
            if (!visited.contains(next)) {
                q.offer(next);
                visited.add(next);
            }
        }
    }
}
```

**复杂度分析 Complexity**
- 时间：O(V + E)  
- 空间：O(V)

---

## 三、V 和 E 的含义 Meaning of V and E  

| 符号 | 含义 | 示例 |
|------|------|------|
| V | 顶点数（节点数量） | 图中一共有多少个节点，例如城市、用户、格点 |
| E | 边数 | 节点之间的连接数量，例如道路、好友关系、可走路径 |

BFS 每个节点访问一次，每条边检查一次，因此复杂度为 O(V + E)。

---

## 二、Deque 与 Queue 详解

### 1. 定义 Definition

| 结构 | 全称 | 特点 |
|------|------|------|
| Queue | 队列（先进先出 FIFO） | 只能尾插头取 |
| Deque | 双端队列（Double-Ended Queue） | 两端都可插入/删除 |

**常用实现：**
- ArrayDeque（循环数组实现，最快）
- LinkedList（链表实现，空间略大）

---

### 2. 常用方法 Common Methods

| 方法 | 功能 | Queue | Deque |
|------|------|--------|--------|
| offer(e) | 尾部入队 | ✅ | ✅ |
| poll() | 头部出队 | ✅ | ✅ |
| peek() | 查看头部 | ✅ | ✅ |
| offerFirst(e) | 头部插入 | ❌ | ✅ |
| offerLast(e) | 尾部插入 | ✅ | ✅ |
| pollFirst() | 头部弹出 | ✅ | ✅ |
| pollLast() | 尾部弹出 | ❌ | ✅ |
| peekFirst() / peekLast() | 查看头/尾 | ❌ | ✅ |

---

### 3. 应用示例 Use Cases

#### （1）普通队列（BFS）
```java
Queue<Integer> q = new LinkedList<>();
q.offer(1);
q.offer(2);
System.out.println(q.poll()); // 1
```

#### （2）双端队列（滑动窗口最大值）
```java
Deque<Integer> dq = new ArrayDeque<>();
for (int i = 0; i < n; i++) {
    while (!dq.isEmpty() && nums[dq.peekLast()] < nums[i]) dq.pollLast();
    dq.offerLast(i);
    if (dq.peekFirst() <= i - k) dq.pollFirst();
    if (i >= k - 1) res.add(nums[dq.peekFirst()]);
}
```

#### （3）栈（Stack）替代
```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(10);
stack.push(20);
System.out.println(stack.pop()); // 20
```

---

## 四、典型应用 Scenarios  

| 类型 Type | 应用场景 Example | 说明 Explanation |
|------------|----------------|----------------|
| 图遍历 Graph Traversal | 计算连通分量 | 每次新起点触发一次BFS |
| 最短路径 Shortest Path | 无权图最短距离 | BFS层数即最短路径长度 |
| 树层级遍历 Level Order | 二叉树、N叉树 | 每层出队遍历所有节点 |
| 状态搜索 State Search | 迷宫、棋盘、数独 | 每个状态作为节点，转移为边 |
| 拓扑层次 Topological | DAG层次结构 | BFS + 入度表（Kahn算法） |

---

## 五、二维网格 BFS（Grid BFS）

常用于迷宫、岛屿问题。
```java
int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};
Queue<int[]> q = new LinkedList<>();
boolean[][] visited = new boolean[m][n];
q.offer(new int[]{sx, sy});
visited[sx][sy] = true;
int step = 0;
while (!q.isEmpty()) {
    int size = q.size();
    for (int i = 0; i < size; i++) {
        int[] cur = q.poll();
        if (cur[0] == tx && cur[1] == ty) return step;
        for (int[] d : dirs) {
            int nx = cur[0] + d[0], ny = cur[1] + d[1];
            if (inBound(nx, ny) && !visited[nx][ny] && grid[nx][ny] != '#') {
                visited[nx][ny] = true;
                q.offer(new int[]{nx, ny});
            }
        }
    }
    step++;
}
```

返回最短步数 `step`。  
Each layer = one step farther.

---

## 六、经典题：LeetCode 127. Word Ladder

### 题意 Problem Description  
给定两个单词 `beginWord` 和 `endWord`，以及一个字典 `wordList`。  
每次只能改变一个字母，并且改变后的新单词必须在字典中。  
要求找到从 `beginWord` 到 `endWord` 的 **最短转换序列长度**。  

**示例 Example**
```
Input:
beginWord = "hit"
endWord = "cog"
wordList = ["hot","dot","dog","lot","log","cog"]

Output: 5
Explanation:
"hit" -> "hot" -> "dot" -> "dog" -> "cog"
```

---

### 思路分析 Approach  
- 每个单词看作一个节点  
- 若两个单词相差一个字母，则在它们之间连一条边  
- 问题转化为：**无权图的最短路径问题**  
→ 用 **BFS** 求最短层数  

#### Why BFS?  
因为 BFS 一层一层扩展，第一次到达 `endWord` 时的层数即为最短路径。

---

### Java 实现 Implementation  

```java
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> wordSet = new HashSet<>(wordList);
    if (!wordSet.contains(endWord)) return 0;

    Queue<String> q = new LinkedList<>();
    q.offer(beginWord);
    Set<String> visited = new HashSet<>();
    visited.add(beginWord);

    int step = 1; // 起点算第一层

    while (!q.isEmpty()) {
        int size = q.size();
        for (int i = 0; i < size; i++) {
            String word = q.poll();
            if (word.equals(endWord)) return step;
            char[] arr = word.toCharArray();
            for (int j = 0; j < arr.length; j++) {
                char old = arr[j];
                for (char c = 'a'; c <= 'z'; c++) {
                    arr[j] = c;
                    String next = new String(arr);
                    if (wordSet.contains(next) && !visited.contains(next)) {
                        visited.add(next);
                        q.offer(next);
                    }
                }
                arr[j] = old;
            }
        }
        step++;
    }
    return 0;
}
```

---

### 复杂度分析 Complexity  

| 项 | 说明 | 复杂度 |
|----|------|--------|
| 时间 Time | 每个单词最多扩展 26×L 次（L为单词长度），共 N 个单词 | O(N × L × 26) ≈ O(NL) |
| 空间 Space | 队列 + visited + wordSet | O(NL) |

---

### 双向 BFS 优化 Bidirectional BFS  

```java
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> dict = new HashSet<>(wordList);
    if (!dict.contains(endWord)) return 0;
    Set<String> beginSet = new HashSet<>(), endSet = new HashSet<>(), visited = new HashSet<>();
    beginSet.add(beginWord);
    endSet.add(endWord);
    int len = 1;
    while (!beginSet.isEmpty() && !endSet.isEmpty()) {
        if (beginSet.size() > endSet.size()) {
            Set<String> tmp = beginSet;
            beginSet = endSet;
            endSet = tmp;
        }
        Set<String> next = new HashSet<>();
        for (String word : beginSet) {
            char[] arr = word.toCharArray();
            for (int i = 0; i < arr.length; i++) {
                char old = arr[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    arr[i] = c;
                    String newWord = new String(arr);
                    if (endSet.contains(newWord)) return len + 1;
                    if (dict.contains(newWord) && !visited.contains(newWord)) {
                        next.add(newWord);
                        visited.add(newWord);
                    }
                }
                arr[i] = old;
            }
        }
        beginSet = next;
        len++;
    }
    return 0;
}
```

**复杂度：**O(N × L)，但常数更小。  
双向 BFS 只扩展两边的一半层级，显著提速。

---

## 七、总结 Summary  

| 步骤 | 内容 |
|------|------|
| 1 | 将单词作为节点，差一字母则连边 |
| 2 | BFS 层序遍历求最短路径 |
| 3 | 提前终止：遇到 `endWord` 立即返回 |
| 4 | 可用双向 BFS 优化性能 |

---
