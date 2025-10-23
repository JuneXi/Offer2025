# 堆完整指南（中英文对照版） / Heap Complete Guide (Bilingual)

> 说明：本文件中文在前，英文在后；仅保留 Java 代码示例。  
> Note: Chinese first, then English; Java-only code samples.

---

## 一、堆的定义与性质（中文）
堆（Heap）是基于**完全二叉树**的抽象数据结构，常用来在**动态数据**中高效维护极值（最大或最小）。
- **结构性质**：完全二叉树（从左到右逐层填充，最后一层可不满）。
- **堆序性质**：任一节点与子节点满足固定比较规则（最大堆或最小堆）。
- **数组表示**：通常用数组存储，无需指针；根在索引0。  
  - `left(i) = 2*i + 1`，`right(i) = 2*i + 2`，`parent(i) = (i-1)//2`
- **非全局有序**：只保证父子局部有序，不保证中序遍历有序。

### 常见操作与复杂度
| 操作 | 含义 | 复杂度 |
|---|---|---|
| 插入（push） | 插入末尾后上浮恢复堆序 | O(log n) |
| 删除堆顶（pop） | 堆顶与末尾交换，缩小堆并下沉 | O(log n) |
| 查看堆顶（peek） | 读取根节点 | O(1) |
| 建堆（heapify） | 无序数组转堆（自底向上） | O(n) |

---

## I. Definition and Properties (English)
A heap is an abstract data structure built on a **complete binary tree**, used to maintain the **extreme** (max or min) efficiently in **dynamic data**.
- **Structure**: Complete binary tree (filled level by level, last level left-aligned).
- **Heap Order**: Each node compares with children under a fixed rule (max-heap or min-heap).
- **Array Layout**: Usually stored in an array; root at index 0.  
  - `left(i) = 2*i + 1`, `right(i) = 2*i + 2`, `parent(i) = (i-1)//2`
- **Not globally sorted**: Only parent–child order is guaranteed.

### Operations and Complexity
| Operation | Meaning | Complexity |
|---|---|---|
| Insert (push) | Append then sift-up | O(log n) |
| Pop top | Swap root with last, shrink heap, sift-down | O(log n) |
| Peek | Read root | O(1) |
| Heapify | Build from array, bottom-up | O(n) |

---

## 二、最大堆与最小堆（中文）
- **最大堆（Max Heap）**：父节点值 ≥ 子节点值；根为全局最大。  
- **最小堆（Min Heap）**：父节点值 ≤ 子节点值；根为全局最小。  
- **Java**：`PriorityQueue` 默认是**最小堆**；最大堆可用 `new PriorityQueue<>(Collections.reverseOrder())`。

### 易错点
- “完全二叉树≠满二叉树”。
- 插入用上浮，下沉用在删除堆顶后。
- 比较器要与需求一致（升序=最小堆；降序=最大堆）。
- 堆不支持 O(log n) 的任意元素删除（需额外索引结构）。

---

## II. Max-Heap vs Min-Heap (English)
- **Max Heap**: Parent ≥ children; root is the global max.  
- **Min Heap**: Parent ≤ children; root is the global min.  
- **Java**: `PriorityQueue` is a **min-heap** by default; construct a max-heap via `new PriorityQueue<>(Collections.reverseOrder())`.

### Pitfalls
- Complete tree is not necessarily full.
- Use sift-up for insert, sift-down after removing root.
- Comparator must match need.
- Arbitrary deletions are not supported efficiently without extra indices.

---

## 三、应用场景（中文）
1) 优先队列调度（按优先级取最高/最低）  
2) Top K 维护（前 K 大 / 前 K 小）  
3) 堆排序（原地、O(1) 额外空间、不稳定）  
4) 图算法（Dijkstra/Prim 等的最小堆优化）  
5) 数据流中位数（双堆）  
6) 合并 K 个有序序列（最小堆按头元素）

---

## III. Applications (English)
1) Priority queue scheduling  
2) Top-K maintenance  
3) Heap sort (in-place, O(1) extra space, unstable)  
4) Graph algorithms (Dijkstra/Prim)  
5) Streaming median (two-heaps)  
6) Merge K sorted sequences (min-heap on heads)

---

# 四、六大经典题（中文在前 / English follows）

## 1) LeetCode 703. Kth Largest Element in a Stream

**题干（中文）**  
给定整数 `k` 和初始数组 `nums`，多次调用 `add(val)` 向数据流加入元素；每次调用需返回当前第 `k` 大元素。

**思路（中文，详细）**  
- 使用**容量为 k 的最小堆**。堆顶保存“第 k 大”的边界。  
- 初始化：将 `nums` 逐个入堆，若堆大小超出 k，弹出堆顶。  
- `add(val)`: 入堆；若堆大小>k，弹出堆顶；返回 `peek()`。  
- 复杂度：初始化 O(n log k)，每次 `add` O(log k)，空间 O(k)。

**易错点 & 边界（中文）**  
- 容量控制必须严格 `>k` 时 `poll()`。  
- `k > nums.length` 合法，堆可起初不满；`k=1` 返回全局最大。  
- 负数、重复值均无需特殊处理。

**Java**
```java
import java.util.PriorityQueue;

class KthLargest {
    private final int k;
    private final PriorityQueue<Integer> minHeap = new PriorityQueue<>();

    public KthLargest(int k, int[] nums) {
        this.k = k;
        for (int x : nums) add(x);
    }
    public int add(int val) {
        minHeap.offer(val);
        if (minHeap.size() > k) minHeap.poll();
        return minHeap.peek();
    }
}
```

**Problem (English)**  
Given `k` and an initial array `nums`, support `add(val)` and return the current k-th largest after each addition.

**Approach (English)**  
Maintain a **min-heap of size k**. Push elements; if size exceeds k, pop. The top is the k-th largest.  
Complexities as above.

---

## 2) LeetCode 347. Top K Frequent Elements

**题干（中文）**  
返回数组中出现频率最高的 `k` 个元素。顺序不限。

**思路（中文，详细）**  
- 哈希表计数 `num -> freq`。  
- 用按频次升序的**最小堆**维护容量 `k` 的候选集。  
- 遍历频次表入堆，若超出 `k` 弹出堆顶（频次最小）。  
- 输出堆中元素即可（如需从高到低，可逆序弹出）。  
- 复杂度：计数 O(n)，入堆 O(m log k)，m 为不同元素数。

**易错点 & 边界（中文）**  
- 比较器按“频次”而非“数值”。  
- `k` 可能等于元素种数；元素可重复、可为负。

**Java**
```java
import java.util.*;

class Solution347 {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> cnt = new HashMap<>();
        for (int x : nums) cnt.put(x, cnt.getOrDefault(x, 0) + 1);

        PriorityQueue<int[]> heap = new PriorityQueue<>((a,b) -> a[1] - b[1]); // freq asc
        for (Map.Entry<Integer,Integer> e : cnt.entrySet()) {
            heap.offer(new int[]{e.getKey(), e.getValue()});
            if (heap.size() > k) heap.poll();
        }
        int[] res = new int[k];
        for (int i = k - 1; i >= 0; i--) res[i] = heap.poll()[0];
        return res;
    }
}
```

**Problem (English)**  
Return the `k` most frequent elements in the array. Order not required.

**Approach (English)**  
Count frequencies, then keep a size-`k` **min-heap** keyed by frequency. Pop when size exceeds k. Output the heap contents.

---

## 3) LeetCode 912. Sort an Array（堆排序 / Heap Sort）

**题干（中文）**  
对整数数组进行升序排序。

**思路（中文，详细）**  
- 原地构建**最大堆**：从最后一个非叶子结点向上做 `heapify`。  
- 循环：交换 `a[0]` 与当前区间末尾，缩小堆区间，对根 `heapify`。  
- 复杂度：建堆 O(n)，排序阶段 O(n log n)，总 O(n log n)，空间 O(1)。

**易错点 & 边界（中文）**  
- `heapify` 时边界用当前堆大小 `n` 判断子节点是否存在。  
- 下沉选更大的子节点交换。  
- 处理空数组、单元素、全相等、逆序、含负数。

**Java**
```java
class Solution912 {
    public int[] sortArray(int[] nums) {
        int n = nums.length;
        for (int i = n/2 - 1; i >= 0; i--) heapify(nums, i, n);
        for (int end = n - 1; end > 0; end--) {
            swap(nums, 0, end);
            heapify(nums, 0, end);
        }
        return nums;
    }
    private void heapify(int[] a, int i, int n) {
        while (true) {
            int l = 2*i + 1, r = 2*i + 2, largest = i;
            if (l < n && a[l] > a[largest]) largest = l;
            if (r < n && a[r] > a[largest]) largest = r;
            if (largest == i) break;
            swap(a, i, largest);
            i = largest;
        }
    }
    private void swap(int[] a, int i, int j) { int t = a[i]; a[i]=a[j]; a[j]=t; }
}
```

**Problem (English)**  
Sort an integer array in ascending order using heap sort.

**Approach (English)**  
Build a **max-heap** in-place, then repeatedly swap the root with the end, shrink the heap, and sift down. Complexity as above.

---

## 4) LeetCode 743. Network Delay Time（Dijkstra + Min-Heap）

**题干（中文）**  
给定 `times`（边 `u->v` 权重 `w`）、节点数 `n`、起点 `k`，求从 `k` 发送信号到所有节点的最短时间；若不可达返回 `-1`。

**思路（中文，详细）**  
- 建邻接表；`dist[]` 初始化为 `INF`，`dist[k]=0`。  
- 最小堆按 `(node, dist)` 存储。弹出最小 `dist` 的未确定节点进行松弛。  
- 若弹出时 `d > dist[u]`，跳过（多次入堆的冗余条目）。  
- 遍历出边 `(v,w)`，若 `dist[v] > d+w` 更新并入堆。  
- 最终取 `dist` 最大值，若存在 `INF` 则返回 `-1`。

**易错点 & 边界（中文）**  
- 必要的“过期条目”剪枝 `if (d > dist[u]) continue`。  
- 节点编号 1..n；图可能不连通；可能有环与并行边。

**Java**
```java
import java.util.*;

class Solution743 {
    public int networkDelayTime(int[][] times, int n, int k) {
        List<int[]>[] g = new List[n + 1];
        for (int i = 1; i <= n; i++) g[i] = new ArrayList<>();
        for (int[] e : times) g[e[0]].add(new int[]{e[1], e[2]});

        int[] dist = new int[n + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[k] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[1] - b[1]);
        pq.offer(new int[]{k, 0});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0], d = cur[1];
            if (d > dist[u]) continue;
            for (int[] nx : g[u]) {
                int v = nx[0], w = nx[1];
                if (dist[v] > d + w) {
                    dist[v] = d + w;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        int ans = 0;
        for (int i = 1; i <= n; i++) {
            if (dist[i] == Integer.MAX_VALUE) return -1;
            ans = Math.max(ans, dist[i]);
        }
        return ans;
    }
}
```

**Problem (English)**  
Given edges `times` with weights, nodes `n`, and source `k`, compute time for the signal to reach all nodes. Return `-1` if unreachable. Approach: Dijkstra with a min-heap.

---

## 5) LeetCode 295. Find Median from Data Stream（双堆 / Two Heaps）

**题干（中文）**  
支持两操作：`addNum(num)` 添加一个数；`findMedian()` 返回当前中位数。

**思路（中文，详细）**  
- 维护两个堆：最大堆 `small` 存较小一半；最小堆 `large` 存较大一半。  
- 插入：先入 `small`，再把 `small.peek()` 转移到 `large`，保证两边有序；若 `large` 比 `small` 多，则再把 `large.peek()` 转回 `small`。  
- 取中位数：若 `small` 多一个，中位数为 `small.peek()`；否则为 `(small.peek()+large.peek())/2.0`。

**易错点 & 边界（中文）**  
- 始终维持 `small.size()==large.size()` 或 `small` 多 1。  
- 注意避免整数除法误差。  
- 适用于重复值、负数、奇偶交替长度。

**Java**
```java
import java.util.*;

class MedianFinder {
    private final PriorityQueue<Integer> small =
        new PriorityQueue<>(Collections.reverseOrder()); // max-heap
    private final PriorityQueue<Integer> large =
        new PriorityQueue<>(); // min-heap

    public void addNum(int num) {
        small.offer(num);
        large.offer(small.poll());
        if (large.size() > small.size()) small.offer(large.poll());
    }
    public double findMedian() {
        if (small.size() > large.size()) return small.peek();
        return (small.peek() + large.peek()) / 2.0;
    }
}
```

**Problem (English)**  
Support `addNum` and `findMedian` using two heaps. Insert into max-heap, move top to min-heap, then rebalance. Median as described.

---

## 6) LeetCode 23. Merge k Sorted Lists（最小堆 / Min-Heap）

**题干（中文）**  
合并 `k` 条升序链表为一条升序链表。

**思路（中文，详细）**  
- 将各链表头入**最小堆**，按 `val` 排序。  
- 反复弹出堆顶最小节点接到结果尾部，并将其 `next` 入堆。  
- 堆大小最多 `k`，总复杂度 O(N log k)，N 为总节点数。

**易错点 & 边界（中文）**  
- 入堆前判空；比较器按节点值。  
- 可能存在空链表、极端长度差、重复值。

**Java**
```java
import java.util.*;

class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int v) { val = v; }
    ListNode(int v, ListNode n) { val = v; next = n; }
}

class Solution23 {
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a,b) -> a.val - b.val);
        for (ListNode node : lists) if (node != null) pq.offer(node);

        ListNode dummy = new ListNode(0), cur = dummy;
        while (!pq.isEmpty()) {
            ListNode node = pq.poll();
            cur.next = node;
            cur = cur.next;
            if (node.next != null) pq.offer(node.next);
        }
        return dummy.next;
    }
}
```

**Problem (English)**  
Merge `k` sorted linked lists using a min-heap keyed by node value. Push heads, repeatedly pop the smallest, and push its `next` if present.

---

# 五、实现细节与通用注意事项（中文）
- **索引计算**（数组堆）：`left=2*i+1`，`right=2*i+2`，`parent=(i-1)//2`。  
- **稳定性**：堆排序不稳定；若需稳定性请考虑归并排序。  
- **内存与常数**：`PriorityQueue` 常数较优；最大堆用 `Collections.reverseOrder()`。  
- **过期条目**：图最短路中需剪枝避免重复松弛。  
- **定制比较器**：确保与题意一致（频次 vs 值）。

## V. Implementation Notes (English)
- **Indexing**: `left=2*i+1`, `right=2*i+2`, `parent=(i-1)//2`.  
- **Stability**: Heap sort is unstable.  
- **Memory/Constants**: `PriorityQueue` is efficient; build max-heap via `Collections.reverseOrder()`.  
- **Stale Entries**: Use pruning in Dijkstra.  
- **Custom Comparator**: Match the problem’s key (frequency vs value).

---

## 版权与引用 / Copyright
仅作学习用途。代码为示例实现，可按需修改。

