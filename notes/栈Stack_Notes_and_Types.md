# 栈（Stack）知识总结与典型题型归类

> 数据结构核心思想：**后进先出（LIFO, Last In First Out）**  
> 几乎所有“括号、区间、递归、回溯、单调”类问题都可抽象为 Stack 模型。

---

## 一、定义与基本操作

| 操作 | 含义 |
|------|------|
| `push(x)` | 元素入栈（放到顶端） |
| `pop()` | 弹出栈顶元素 |
| `peek()` / `top()` | 读取栈顶元素但不弹出 |
| `isEmpty()` | 栈是否为空 |

Java 常用：
```java
Deque<Integer> stack = new ArrayDeque<>();
stack.push(x);
stack.pop();
stack.peek();
```

---

## 二、为什么需要 Stack

当问题具有「**嵌套结构**」「**局部未完需回溯**」「**要保留现场**」这三类特征时，栈就能完美建模。

- 递归 → 系统调用栈  
- 括号匹配 → 嵌套结构  
- 单调栈 → 局部信息回看  
- 表达式求值 → 临时保存操作符与数值  

---

## 三、栈的主要题型归类

| 类别 | 特征 | 典型题目 / 模板 | 核心思路 |
|------|------|------------------|-----------|
| **1. 括号匹配类** | 成对出现、嵌套 | 20. Valid Parentheses<br>32. Longest Valid Parentheses<br>394. Decode String | 栈保存未匹配的括号或索引；遇右括号弹出匹配。 |
| **2. 单调栈（Monotonic Stack）** | 寻找“下一个更大/更小”元素 | 739. Daily Temperatures<br>496. Next Greater Element I<br>84. Largest Rectangle in Histogram<br>85. Maximal Rectangle | 栈中保持单调性，出栈即找到“下一位更大/更小”。 |
| **3. 区间/面积类** | 需要“左边界-右边界”扩展 | 84. Largest Rectangle in Histogram<br>42. Trapping Rain Water | 单调栈找到边界索引，用面积或宽度计算。 |
| **4. 表达式求值类** | 操作符优先级 | 150. Evaluate Reverse Polish Notation<br>227. Basic Calculator II<br>772. Basic Calculator III | 两个栈（数字栈、运算符栈），按优先级出栈计算。 |
| **5. DFS/递归模拟类** | 系统调用栈显式化 | 144/94/145 Binary Tree Traversal<br>224. Basic Calculator | 栈手动维护返回点或中间状态。 |
| **6. 最小栈/最大栈类** | 额外记录当前最值 | 155. Min Stack<br>716. Max Stack | 辅助栈同步维护当前最小/最大值。 |
| **7. 括号字符串变形** | 嵌套结构 + 多状态 | 394. Decode String | 栈保存当前字符串与重复次数。 |
| **8. 事件嵌套/日志层级** | 层级入栈出栈 | 636. Exclusive Time of Functions | 进入事件 push，退出事件 pop。 |

---

## 四、单调栈（Monotonic Stack）详解

**目标**：在数组中快速找到每个元素的“下一个更大/更小”元素索引或距离。

### 模板
```java
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
```

**应用范围**
- 下一个更大/更小元素
- 股票、气温、价格等间隔问题
- 直方图最大矩形、接雨水
- 滑动窗口极值（双端队列变体）

---

## 五、表达式求值类 Stack 模型

**核心思想**：两个栈分别存数字与运算符。

例：`"3 + 2 * 2"`
```java
Deque<Integer> num = new ArrayDeque<>();
Deque<Character> op = new ArrayDeque<>();
for (每个字符 c) {
    if (是数字) 累积成数;
    if (是运算符) {
        while (!op.isEmpty() && 优先级(op.peek()) >= 优先级(c))
            计算一次;
        op.push(c);
    }
}
最后清空剩余运算。
```

**典型题**
- 150. Evaluate Reverse Polish Notation（后缀表达式）
- 224/227/772. Basic Calculator 系列（带括号优先级）

用单栈的做法

```aidl
class Solution {
    public int calculate(String s) {
		if(s == null || s.length() == 0){
			return 0;
		}
		//sign
		int sign = 1;
		int res = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for(int i = 0; i < s.length(); i++){
			//note: how to write below?
			if(Character.isDigit(s.charAt(i))){
				//note: here - '0'
				int val = s.charAt(i) - '0';
				while(i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))){
					//note: here - '0'
					val = val * 10 + (s.charAt(i + 1) - '0');
					i++;
				}
				res = res + val * sign;
			}else if(s.charAt(i) == ' '){
				continue;
			}else if(s.charAt(i) == '+'){
				sign = 1;
			}else if(s.charAt(i) == '-'){
				sign = -1;
			}else if(s.charAt(i) == '('){
				stack.push(res);
				stack.push(sign);
				//reset
				res = 0;
				sign = 1;
			}else if(s.charAt(i) == ')'){
				res = res * stack.pop() + stack.pop();
			}else;
		}
        return res;
    }
    
}
```
时间复杂度： O(n)
每个字符最多入栈一次出战一次
---

## 六、栈与 DFS 的关系

系统调用栈本身就是 Stack。  
显式模拟递归时，手动 push 当前状态直到回溯。

```java
Deque<TreeNode> stack = new ArrayDeque<>();
TreeNode cur = root;
while (cur != null || !stack.isEmpty()) {
    while (cur != null) {
        stack.push(cur);
        cur = cur.left;
    }
    cur = stack.pop();
    visit(cur);
    cur = cur.right;
}
```

---

## 七、设计类题目

| 题目 | 要点 |
|------|------|
| 155. Min Stack | 主栈 + 辅助栈保存最小值 |
| 716. Max Stack | 主栈 + 辅助栈保存最大值 |

---

## 八、总结表

| 类别 | 栈用途 | 代表题 |
|------|---------|---------|
| 匹配类 | 检查嵌套结构 | 20, 32, 394 |
| 单调栈 | 查找“下一更大/更小” | 496, 739, 84, 42 |
| 表达式 | 模拟计算顺序 | 150, 227, 772 |
| DFS 模拟 | 遍历树/图 | 94, 144, 145 |
| 辅助栈 | 维护最值 | 155, 716 |
| 事件嵌套 | 统计层级时间 | 636 |

---

## 九、结论

> 栈的本质是 **保存未完成的状态**。  
> 当问题中存在“等待配对”“局部未结算”“区间边界未知”“递归嵌套”时，**优先考虑 Stack**。
