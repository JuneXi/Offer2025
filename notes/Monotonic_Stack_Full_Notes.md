# 单调栈 Monotonic Stack

---

## 一、定义 Definition

单调栈是一种 **栈结构（Stack）**，栈内元素保持 **单调递增** 或 **单调递减**。  
A monotonic stack is a **stack data structure** where elements are kept in either **increasing or decreasing order**.

它用于在 **O(n)** 时间内解决“下一个更大/更小元素”问题。  
It helps find the *next greater/smaller element* in **O(n)** time.

---

## 二、类型 Types

| 类型 Type | 描述 Description |
|------------|------------------|
| **单调递增栈 (Increasing Stack)** | 栈内元素从底到顶递增，遇到更小值弹栈；用于找“前一个更大”或“下一个更小”。 |
| **单调递减栈 (Decreasing Stack)** | 栈内元素从底到顶递减，遇到更大值弹栈；用于找“前一个更小”或“下一个更大”。 |

---

## 三、核心思想 Core Idea

遍历数组，每个元素最多 **入栈一次、出栈一次**，整体复杂度 **O(n)**。  
Each element is pushed and popped at most once, giving an **O(n)** overall time complexity.

**操作规则 Rules:**
1. 栈为空或满足单调性 → 入栈。  
2. 若不满足单调性 → 弹栈并处理结果。  
3. 弹栈表示当前元素是前一个元素的“界限”。

---

## 四、经典题型 Classic Problems

### 1. 下一个更大元素 Next Greater Element

```java
public int[] nextGreaterElements(int[] nums) {
    int n = nums.length;
    int[] res = new int[n];
    Arrays.fill(res, -1);
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
            int idx = stack.pop();
            res[idx] = nums[i];
        }
        stack.push(i);
    }
    return res;
}
```

**Time:** O(n)  
**Space:** O(n)

---

### 2. 柱状图最大矩形 Largest Rectangle in Histogram

🧠 思路分析 / Thinking

每根柱子能向左右扩展，直到遇到比它矮的柱子为止。
我们要找出 每个柱子作为最矮高度时能延伸的最大宽度。

难点：

对每个柱子寻找左边、右边第一个比它矮的柱子。

核心技巧：

用 单调递增栈 保存索引，栈中柱高递增。

当出现更矮的柱子时，弹出栈顶柱子，计算矩形面积。

⚙️ 过程 / Process

栈中保存递增高度的索引。

当前柱子高度小于栈顶柱子 → 栈顶柱子被“右边界”限制。

弹出栈顶索引 idx

左边界是新的栈顶

宽度 = i - left - 1

面积 = heights[idx] * width

遍历结束后加一个高度 0，触发清空栈。

```java
public int largestRectangleArea(int[] heights) {
    int n = heights.length;
    Deque<Integer> stack = new ArrayDeque<>();
    int max = 0;

    for (int i = 0; i <= n; i++) {
        int cur = (i == n ? 0 : heights[i]);
        while (!stack.isEmpty() && cur < heights[stack.peek()]) {
            int h = heights[stack.pop()];
            int left = stack.isEmpty() ? -1 : stack.peek();
            int width = i - left - 1;
            max = Math.max(max, h * width);
        }
        stack.push(i);
    }
    return max;
}
```

---

### 3. 每日温度 Daily Temperatures

```java
public int[] dailyTemperatures(int[] T) {
    int n = T.length;
    int[] res = new int[n];
    Deque<Integer> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && T[i] > T[stack.peek()]) {
            int idx = stack.pop();
            res[idx] = i - idx;
        }
        stack.push(i);
    }
    return res;
}
```

---

## 五、常见应用场景 Applications

| 目标 Goal | 栈类型 Stack Type | 示例 Example |
|------------|-------------------|--------------|
| 找下一个更大元素 | 递减栈 Decreasing Stack | 739 Daily Temperatures |
| 找下一个更小元素 | 递增栈 Increasing Stack | 84 Largest Rectangle |
| 找前一个更大元素 | 递减栈 Decreasing Stack | Histogram left boundary |
| 找前一个更小元素 | 递增栈 Increasing Stack | Histogram left boundary |

---

## 六、易错点 Pitfalls

1. 栈中应存 **索引 index**，非数值 value。  
2. 若需处理所有元素，遍历结束时加 **哨兵值 sentinel**。  
3. 注意等号条件决定是否弹栈。  
4. 输出结果需与栈操作方向一致。

---

## 七、Stack 与 ArrayDeque 的区别

| 对比维度 | Stack | ArrayDeque |
|-----------|--------|------------|
| 来源 | `java.util.Stack` (老旧类) | `java.util.ArrayDeque` (推荐) |
| 继承结构 | 继承 Vector（线程安全） | 实现 Deque（非线程安全） |
| 性能 | 慢（同步锁） | 快（无锁） |
| 用途 | 仅支持栈 | 可作栈或队列 |
| 推荐使用 | ❌ | ✅ |

**推荐使用 ArrayDeque 代替 Stack。**

---

## 八、单调栈存值 vs 存索引

### 存值（Value-based）

```java
int[] calculateGreaterElement(int[] nums) {
    int n = nums.length;
    int[] res = new int[n];
    Stack<Integer> s = new Stack<>();
    for (int i = n - 1; i >= 0; i--) {
        while (!s.isEmpty() && s.peek() <= nums[i]) s.pop();
        res[i] = s.isEmpty() ? -1 : s.peek();
        s.push(nums[i]);
    }
    return res;
}
```

适合仅找“更大值”，无法求距离。

---

### 存索引（Index-based）

```java
while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
    int idx = stack.pop();
    res[idx] = i - idx; // 可计算距离
}
stack.push(i);
```

适合需要**位置信息或区间宽度**的问题。

---

## 九、单调栈 vs 堆 (Heap)

| 对比项 | 单调栈 Monotonic Stack | 堆 Heap |
|---------|-------------------------|----------|
| 数据结构 | 栈 (LIFO) | 完全二叉树 (Complete Binary Tree) |
| 性质 | 保持单调性 | 父节点最大或最小 |
| 操作复杂度 | 入/出栈 O(1) | 插入/删除 O(log n) |
| 用途 | 查找相邻关系、边界 | 动态维护全局最值 |
| 是否全局有序 | 否 | 局部堆序 |
| 典型题 | 739, 84, 496 | 215, 703 |

---

## 十、总结 Summary

| 特性 Feature | 单调栈 Monotonic Stack |
|---------------|-------------------------|
| 时间复杂度 | O(n) |
| 空间复杂度 | O(n) |
| 适用场景 | 区间边界、相邻比较 |
| 存储内容 | 索引（推荐） |
| 常见错误 | 忘记哨兵、错误等号条件 |
| 关键优势 | 线性时间处理“下一个更大/更小”问题 |

---

**核心记忆：**  
> 存索引，掌控位置；  
> 保单调，线性扫描。  
> Heap 求全局，Stack 求局部。
