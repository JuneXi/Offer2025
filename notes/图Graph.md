# 图论详解 (Graph Theory Detailed Notes)

---

## 一、图的基本概念 (Graph Basics)

**定义 Definition**
图由顶点集合 `V` 和边集合 `E` 组成：

* **有向图 Directed Graph** ：边有方向 `(u → v)`
* **无向图 Undirected Graph** ：边无方向 `(u—v)`
* **带权图 Weighted Graph** ：边附带权值
* **稀疏图 Sparse Graph** ：`M ≈ N`
* **稠密图 Dense Graph** ：`M ≈ N²`

**表示 Representation**

| 方式                    | 空间     | 适用  | 说明        |
| --------------------- | ------ | --- | --------- |
| 邻接表 Adjacency List    | O(N+M) | 稀疏图 | 常用，节省空间   |
| 邻接矩阵 Adjacency Matrix | O(N²)  | 稠密图 | 适合小图或快速判边 |

---

## 二、遍历 Traversal

### DFS (Depth First Search)

* **思想** ：递归进入未访问节点
* **应用** ：连通分量、环检测、拓扑
* **复杂度** ：O(N+M)

```java
void dfs(int u, boolean[] vis, List<int[]>[] g) {
    vis[u] = true; // 先标记visit
    for (int[] e : g[u]) {
        int v = e[0];
        if (!vis[v]) dfs(v, vis, g);
    }
}
```

### BFS (Breadth First Search)

* **思想** ：使用队列按层遍历
* **应用** ：无权最短路、二分图
* **复杂度** ：O(N+M)

```java
void bfs(int s, boolean[] vis, List<int[]>[] g) {
    Deque<Integer> q = new ArrayDeque<>();
    vis[s] = true; q.offer(s);
    while (!q.isEmpty()) {
        int u = q.poll();
        for (int[] e : g[u]) {
            int v = e[0];
            if (!vis[v]) { vis[v] = true; q.offer(v); }
        }
    }
}
```

### 连通分量 Connected Components

通过 DFS/BFS 计数未访问点数。O(N+M)

---

## 三、环检测 Cycle Detection

| 图类型 | 方法          | 复杂度    |
| --- | ----------- | ------ |
| 无向图 | DFS+parent  | O(N+M) |
| 有向图 | DFS+color状态 | O(N+M) |

---

无向图用「DFS+parent」或「并查集」。有向图用「DFS三色」或「Kahn 拓扑」。想拿到具体环路径，用「在遇到回边时回溯 parent」。时间复杂度均为 O(N+M)，空间 O(N)。

### 无向图
思路：DFS 时跳过父边。若遇到已访问且不是父节点的邻点，则出现环。
复杂度：O(N+M)，空间 O(N)（含递归栈）。
```aidl
boolean hasCycleUndirected(int n, List<int[]>[] g) {
    boolean[] vis = new boolean[n];
    for (int i = 0; i < n; i++) {
        if (!vis[i] && dfs(i, -1, vis, g)) return true;
    }
    return false;
}
// p: parent
boolean dfs(int u, int p, boolean[] vis, List<int[]>[] g) {
    vis[u] = true;
    for (int[] e : g[u]) {
        int v = e[0];
        if (v == p) continue;                 // ignore the edge back to parent
        if (vis[v]) return true;              // back-edge => cycle
        if (dfs(v, u, vis, g)) return true;
    }
    return false;
}

```
### 并查集
思路：遍历每条无向边 (u,v)。若 find(u)==find(v)，插入该边会产生环。
复杂度：O(M α(N))，空间 O(N)。只告诉你“是否有环”，默认不返路径。

```aidl
static class DSU{
    int[] p, r;
    DSU(int n){ p=new int[n]; r=new int[n]; for(int i=0;i<n;i++) p[i]=i; }
    int find(int x){ return p[x]==x?x:(p[x]=find(p[x])); }
    boolean union(int a,int b){
        int x=find(a), y=find(b);
        if(x==y) return false;               // same set => adding edge makes cycle
        if(r[x]<r[y]){ int t=x; x=y; y=t; }
        p[y]=x; if(r[x]==r[y]) r[x]++;
        return true;
    }
}
boolean hasCycleUndirectedUF(int n, int[][] edges){
    DSU d=new DSU(n);
    for(int[] e: edges){
        int u=e[0], v=e[1];
        if(!d.union(u,v)) return true;
    }
    return false;
}

```
###有向图环检测

2.1 DFS 三色标记

状态：0=未访，1=访问中，2=已完成。遇到 1 即回边，存在有向环。
复杂度：O(N+M)，空间 O(N)。

## 四、拓扑排序 Topological Sort

**定义** ：有向无环图 DAG 中，使每条边 (u→v) 中，u 在 v 前面出现。
**复杂度** ：O(N+M)

一、核心思想

拓扑排序本质是 在有依赖关系的任务中确定执行顺序。
常用于：任务调度、编译顺序、课程依赖、构建系统依赖分析。

有两种主流实现方式：

1. Kahn算法（BFS入度法）

思路：

统计每个节点入度。

入度为0的节点先入队。

每取出一个节点，就删去它的出边（对应邻居节点入度减1）。

若邻居入度变为0，则入队。

最后输出顺序即拓扑序。
复杂度：

时间：O(V + E)

空间：O(V + E)

特性：

可检测环（若最终输出数量 < 节点数）。

常用于判断任务是否能全部完成（如课程表问题）。

```java
List<Integer> topoOrder(int n, List<int[]>[] g){
    int[] indeg=new int[n];
    for(int u=0;u<n;u++) for(int[] e:g[u]) indeg[e[0]]++;
    Deque<Integer> q=new ArrayDeque<>();
    for(int i=0;i<n;i++) if(indeg[i]==0) q.offer(i);
    List<Integer> order=new ArrayList<>();
    while(!q.isEmpty()){
        int u=q.poll(); order.add(u);
        for(int[] e:g[u]) if(--indeg[e[0]]==0) q.offer(e[0]);
    }
    return order.size()==n ? order : List.of();
}
```


2. DFS后序法

思路：

从任意未访问节点出发DFS。

递归访问所有后继节点。

当前节点DFS完成后加入栈。

最后逆序输出栈即为拓扑序。

---

## 五、二分图 Bipartite Graph

**思想** ：两种颜色染色，如果相邻节点同色，则非二分图。
**复杂度** ：O(N+M)

---

## 六、最短路 Shortest Path

| 算法             | 适用  | 复杂度        | 特点   |
| -------------- | --- | ---------- | ---- |
| BFS            | 无权  | O(N+M)     | 层次最短 |
| Dijkstra       | 非负权 | O(M log N) | 贪心最优 |
| Bellman-Ford   | 含负权 | O(N×M)     | 检测负环 |
| Floyd-Warshall | 全源  | O(N³)      | 小图适用 |

**Dijkstra**

从起点 s 出发，不断选择 当前距离最短且未确定最短路径的节点，
然后用它去 更新邻居节点的距离。
类似“贪心 + 松弛（Relaxation）”过程。

前提：图中所有边权非负。

```java
int[] dijkstra(int n, List<int[]>[] g, int s){
    int INF=1<<30; int[] d=new int[n]; Arrays.fill(d, INF);
    boolean[] vis=new boolean[n];
    PriorityQueue<int[]> pq=new PriorityQueue<>(Comparator.comparingInt(a->a[1]));
    d[s]=0; pq.offer(new int[]{s,0});
    while(!pq.isEmpty()){
        int[] cur=pq.poll(); int u=cur[0]; if(vis[u]) continue; vis[u]=true;
        for(int[] e:g[u]){
            int v=e[0], w=e[1];
            if(d[v]>d[u]+w){ d[v]=d[u]+w; pq.offer(new int[]{v,d[v]}); }
        }
    }
    return d;
}
```

---

## 七、最小生成树 Minimum Spanning Tree (MST)

| 算法      | 思想               | 复杂度        | 特点   |
| ------- | ---------------- | ---------- | ---- |
| Kruskal | 按边权排序+Union-Find | O(M log M) | 稀疏图快 |
| Prim    | 从点扩展+Heap        | O(M log N) | 稠密图快 |

**Kruskal 示例**

```java
class DSU {
    int[] p;
    DSU(int n){ p=new int[n]; for(int i=0;i<n;i++) p[i]=i; }
    int find(int x){ return p[x]==x?x:(p[x]=find(p[x])); }
    boolean union(int a,int b){
        int x=find(a), y=find(b);
        if(x==y) return false;
        p[y]=x; return true;
    }
}

int kruskalMST(int n, int[][] edges){
    Arrays.sort(edges, Comparator.comparingInt(e->e[2]));
    DSU d=new DSU(n); int cost=0, cnt=0;
    for(int[] e: edges){
        if(d.union(e[0], e[1])){ cost+=e[2]; cnt++; }
    }
    return cnt==n-1? cost : -1;
}
```

---

## 八、强连通分量 SCC (Tarjan)

**思想** ：通过 DFS + 时间戳 找到最大可互达子图
**复杂度** ：O(N+M)

---

## 九、桥与割点 Bridges & Articulation Points

**定义** ：

* 桥：删该边后连通分量增加
* 割点：删该点后连通分量增加
  **复杂度** ：O(N+M)

---

## 十、DAG 上的 DP

**思想** ：拓扑排序后，按顺序更新 dp[v] = max(dp[v], dp[u] + w)
**复杂度** ：O(N+M)

---

## 十一、欧拉路径 Euler Path

| 类型  | 条件           |
| --- | ------------ |
| 无向图 | 连通 + 0或2个奇度点 |
| 有向图 | 入出度差为0或±1    |

**复杂度** ：O(N+M)

---

## 十二、总结 Summary

* **数据结构** ：稀疏用邻接表，稠密用矩阵
* **算法选择** ：无权图 BFS，非负权 Dijkstra，含负权 Bellman-Ford，全源 Floyd
* **常见问题类** ：最短路、MST、环检测、SCC、拓扑DP

**关键复杂度总结**

| 算法             | 复杂度        |
| -------------- | ---------- |
| DFS/BFS        | O(N+M)     |
| Dijkstra       | O(M log N) |
| Bellman-Ford   | O(NM)      |
| Floyd-Warshall | O(N^3)     |
| Kruskal        | O(M log M) |
| Prim           | O(M log N) |
| Tarjan         | O(N+M)     |

---
