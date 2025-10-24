# 动态规划系统笔记（DP System Handbook）
更新时间：2025-10-23

> 目标：面向刷题与面试。分类型给出模型化思路、选型准则、模板、复杂度、常见坑、代表题与 Java 参考实现。

---

## 目录
1. 一维 DP（线性态）  
2. 二维网格 DP（路径与形状）  
3. 序列 DP（LIS/LCS/编辑距离等）  
4. 背包 DP（0/1、完全、多重、变体）  
5. 区间 DP（区间合并与回文）  
6. 状态压缩 DP（Bitmask / TSP / 组合填装）  
7. 树形 DP（树上信息聚合）  
8. 数位 DP（Digit DP）  
9. 图上 DP（DAG 拓扑序 DP、最短路型 DP）  
10. 概率与期望 DP  
11. DP 优化（滚动数组、单调队列、分治优化、斜率优化、Knuth/Quadrangle）  
12. 位运算 DP（Bitset/SOS DP 简介）  
13. 设计套路与调试清单

---

## 1. 一维 DP（线性态）
**识别信号**：答案关于前缀/位置 i 的最优或计数，且只依赖少量前缀状态。  
**范式**：`dp[i] = best/ways over dp[i-1], dp[i-2], ...`  
**常见题型**：爬楼梯、打家劫舍、最大子数组、硬币问题（最少张数）。

### 例 1：70. Climbing Stairs
- 转移：`dp[i] = dp[i-1] + dp[i-2]`  
- 复杂度：O(n) 时间，O(1) 空间（滚动）

```java
class Solution {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) {
            int c = a + b;
            a = b;
            b = c;
        }
        return b;
    }
}
```

### 例 2：322. Coin Change（最少硬币）
- 定义：`dp[x]` 金额 x 的最少硬币数。  
- 初始化：`dp[0]=0`，其余初始化为 `INF`。  
- 转移：`dp[x] = min(dp[x], dp[x - c] + 1)`。  
- 复杂度：O(n*amount)。

```java
class Solution {
    public int coinChange(int[] coins, int amount) {
        int INF = 1_000_000_000;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for (int c : coins) {
            for (int x = c; x <= amount; x++) {
                if (dp[x - c] + 1 < dp[x]) dp[x] = dp[x - c] + 1;
            }
        }
        return dp[amount] >= INF ? -1 : dp[amount];
    }
}
```

**常见坑**：  
- 初始化 INF 与越界判断。  
- 完全背包与 0/1 背包转移方向区分（见 §4）。

---

## 2. 二维网格 DP（路径与形状）
**识别信号**：网格上移动、统计路径数/最小路径和/最大矩形或正方形。  
**边界**：首行首列初始化至关重要。

### 例：64. Minimum Path Sum
`dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])`  
O(mn) 时间，O(n) 空间可滚动。

```java
class Solution {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) dp[j] = dp[j-1] + grid[0][j];
        for (int i = 1; i < m; i++) {
            dp[0] += grid[i][0];
            for (int j = 1; j < n; j++) {
                dp[j] = Math.min(dp[j], dp[j-1]) + grid[i][j];
            }
        }
        return dp[n-1];
    }
}
```

### 例：221. Maximal Square
- 定义：`dp[i][j]` 以 (i,j) 为右下角的最大正方形边长。  
- 转移：若 `matrix[i][j] == '1'`，`dp[i][j] = 1 + min(up, left, up-left)`。

```java
class Solution {
    public int maximalSquare(char[][] a) {
        int m=a.length, n=a[0].length, ans=0;
        int[] dp = new int[n+1];
        int prev = 0; // dp[i-1][j-1]
        for (int i=1;i<=m;i++){
            prev = 0;
            for (int j=1;j<=n;j++){
                int tmp = dp[j];
                if (a[i-1][j-1]=='1') {
                    dp[j] = Math.min(Math.min(dp[j], dp[j-1]), prev) + 1;
                    ans = Math.max(ans, dp[j]);
                } else dp[j]=0;
                prev = tmp;
            }
        }
        return ans*ans;
    }
}
```

**常见坑**：坐标偏移、`prev` 保存左上角、字符‘1’与数值 1 区分。

---

## 3. 序列 DP（LIS/LCS/编辑距离）
### 3.1 LIS（最长递增子序列）
两种写法：
- O(n^2) DP：`dp[i]` 以 i 结尾的 LIS 长度。  
- O(n log n) 贪心+二分（tails 数组）。

```java
// O(n log n) LIS
class Solution {
    public int lengthOfLIS(int[] nums) {
        int[] tails = new int[nums.length];
        int len = 0;
        for (int x : nums) {
            int i = Arrays.binarySearch(tails, 0, len, x);
            if (i < 0) i = -i - 1;
            tails[i] = x;
            if (i == len) len++;
        }
        return len;
    }
}
```

**坑**：是否允许等于；严格递增 vs 非递减决定二分边界。

### 3.2 LCS（最长公共子序列）
`dp[i][j]` 表示 `A[0..i-1]` 与 `B[0..j-1]` 的 LCS。

```java
class Solution {
    public int longestCommonSubsequence(String a, String b) {
        int m=a.length(), n=b.length();
        int[][] dp = new int[m+1][n+1];
        for(int i=1;i<=m;i++){
            for(int j=1;j<=n;j++){
                if(a.charAt(i-1)==b.charAt(j-1)) dp[i][j]=dp[i-1][j-1]+1;
                else dp[i][j]=Math.max(dp[i-1][j], dp[i][j-1]);
            }
        }
        return dp[m][n];
    }
}
```

### 3.3 编辑距离（72. Edit Distance）
`dp[i][j]`=把 `a[0..i)` 变成 `b[0..j)` 的最少操作；三选一最小。

```java
class Solution {
    public int minDistance(String a, String b) {
        int m=a.length(), n=b.length();
        int[][] dp=new int[m+1][n+1];
        for(int i=0;i<=m;i++) dp[i][0]=i;
        for(int j=0;j<=n;j++) dp[0][j]=j;
        for(int i=1;i<=m;i++)
            for(int j=1;j<=n;j++){
                if(a.charAt(i-1)==b.charAt(j-1)) dp[i][j]=dp[i-1][j-1];
                else dp[i][j]=1+Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
            }
        return dp[m][n];
    }
}
```

---

## 4. 背包 DP
### 选型规则
- **0/1 背包**：每件 0 或 1 次。`for w=cap..weight` 逆序。  
- **完全背包**：可无限次。`for w=weight..cap` 正序。  
- **多重背包**：件数有限。可二进制分组转为 0/1 背包。

### 0/1 背包模板（最大价值）
```java
int[] dp = new int[W+1];
for (int i=0;i<n;i++){
    int wt=w[i], val=v[i];
    for (int cap=W; cap>=wt; cap--) {
        dp[cap] = Math.max(dp[cap], dp[cap-wt]+val);
    }
}
```

### 完全背包模板（最大价值）
```java
int[] dp = new int[W+1];
for (int i=0;i<n;i++){
    int wt=w[i], val=v[i];
    for (int cap=wt; cap<=W; cap++) {
        dp[cap] = Math.max(dp[cap], dp[cap-wt]+val);
    }
}
```

### 例：416. Partition Equal Subset Sum（可用 bitset 优化）
```java
class Solution {
    public boolean canPartition(int[] nums) {
        int sum=0; for(int x:nums) sum+=x;
        if ((sum&1)==1) return false;
        int target=sum/2;
        BitSet bs = new BitSet(target+1);
        bs.set(0);
        for (int x: nums) {
            BitSet shifted = bs.get(0, target+1);
            shifted.shiftLeft(x); // 自定义：BitSet 不含内置左移，需手动或用循环；面试中可用倒序 DP 替代。
        }
        return false; // 给出思路，实际实现常用倒序 boolean DP。
    }
}
```
> 注：Java 标准 BitSet 无直接左移 API，工程上常用 `boolean[]` 逆序转移。

### 例：518. Coin Change II（计数，完全背包）
```java
class Solution {
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount+1];
        dp[0] = 1;
        for (int c: coins) {
            for (int s=c; s<=amount; s++) {
                dp[s] += dp[s-c];
            }
        }
        return dp[amount];
    }
}
```

### 例：494. Target Sum（带符号子集和）
转化为：选正号和为 `P=(S+sum(nums))/2` 的方案数。

```java
class Solution {
    public int findTargetSumWays(int[] nums, int target) {
        int sum=0; for(int x:nums) sum+=x;
        if ((sum+target)%2!=0 || Math.abs(target)>sum) return 0;
        int P=(sum+target)/2;
        int[] dp=new int[P+1]; dp[0]=1;
        for(int x: nums){
            for(int s=P; s>=x; s--) dp[s]+=dp[s-x];
        }
        return dp[P];
    }
}
```

**常见坑**：  
- 方向错误：0/1 必须逆序，完全正序。  
- 题目是计数还是最值。  
- 溢出风险（计数可能很大）。

---

## 5. 区间 DP
**识别信号**：答案建立在子区间合并或两端决策。复杂度常 O(n^3)。

### 例：516. Longest Palindromic Subsequence
`dp[i][j]`：区间 `[i,j]` 的 LPS。

```java
class Solution {
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i=n-1;i>=0;i--) {
            dp[i][i]=1;
            for (int j=i+1;j<n;j++) {
                if (s.charAt(i)==s.charAt(j)) dp[i][j]=dp[i+1][j-1]+2;
                else dp[i][j]=Math.max(dp[i+1][j], dp[i][j-1]);
            }
        }
        return dp[0][n-1];
    }
}
```

### 例：312. Burst Balloons（戳气球）
- 设虚拟两端 1。`dp[l][r]` 表示戳开 `(l,r)` 的最大收益。  
- 枚举最后一个气球 k。

```java
class Solution {
    public int maxCoins(int[] nums) {
        int n=nums.length;
        int[] a=new int[n+2];
        a[0]=a[n+1]=1;
        for(int i=0;i<n;i++) a[i+1]=nums[i];
        int[][] dp=new int[n+2][n+2];
        for(int len=2; len<=n+1; len++){
            for(int l=0; l+len<=n+1; l++){
                int r=l+len;
                for(int k=l+1;k<r;k++){
                    dp[l][r]=Math.max(dp[l][r], dp[l][k]+dp[k][r]+a[l]*a[k]*a[r]);
                }
            }
        }
        return dp[0][n+1];
    }
}
```

**常见坑**：开区间/闭区间定义要一致；枚举长度或端点顺序。

---

## 6. 状态压缩 DP（Bitmask）
**识别信号**：n≤20 左右，需要表示子集选择或全排列。  
**存储**：`mask` 的二进制位表示选与不选。

### 例：698. Partition to K Equal Sum Subsets
- 预检：`sum % k==0`，降序排序剪枝。  
- `dp[mask]` 为当前桶的累积和（取模 `target`）。

```java
class Solution {
    public boolean canPartitionKSubsets(int[] nums, int k) {
        int sum=0; for(int x:nums) sum+=x;
        if (sum%k!=0) return false;
        int target=sum/k, n=nums.length;
        Arrays.sort(nums);
        if (nums[n-1]>target) return false;
        // 降序能更快剪枝
        for (int i=0;i<n/2;i++) {
            int tmp=nums[i]; nums[i]=nums[n-1-i]; nums[n-1-i]=tmp;
        }
        int full=1<<n;
        int[] dp=new int[full];
        Arrays.fill(dp, -1);
        dp[0]=0;
        for (int mask=0; mask<full; mask++){
            if (dp[mask]==-1) continue;
            for (int i=0;i<n;i++){
                if (((mask>>i)&1)==1) continue;
                if (nums[i]+dp[mask] <= target){
                    int nxt=mask|(1<<i);
                    dp[nxt]=(dp[mask]+nums[i])%target;
                }
                if (dp[full-1]==0) return true; // 早停
            }
        }
        return dp[full-1]==0;
    }
}
```

### 例：旅行商 TSP（最短哈密顿回路）
`dp[mask][i]`：已经访问集合 `mask`，当前在 i 的最小代价。

```java
// n<=20 较吃内存，常见 n<=15
int[][] dp = new int[1<<n][n];
for (int[] row: dp) Arrays.fill(row, INF);
dp[1<<start][start]=0;
for (int mask=0; mask<(1<<n); mask++){
    for (int u=0; u<n; u++){
        if (((mask>>u)&1)==0) continue;
        if (dp[mask][u]>=INF) continue;
        for (int v=0; v<n; v++){
            if (((mask>>v)&1)==1) continue;
            dp[mask|1<<v][v] = Math.min(dp[mask|1<<v][v], dp[mask][u]+w[u][v]);
        }
    }
}
```

**常见坑**：位运算优先级，记忆化哈希表键大小，降序排序剪枝。

---

## 7. 树形 DP
**识别信号**：答案在树上，子树信息汇总到父节点。多为后序遍历。

### 例：337. House Robber III
```java
class Solution {
    public int rob(TreeNode root) {
        int[] r = dfs(root);
        return Math.max(r[0], r[1]);
    }
    int[] dfs(TreeNode x){
        if (x==null) return new int[2];
        int[] L=dfs(x.left), R=dfs(x.right), res=new int[2];
        res[0]=Math.max(L[0],L[1])+Math.max(R[0],R[1]);
        res[1]=x.val + L[0] + R[0];
        return res;
    }
}
```

### 例：124. Binary Tree Maximum Path Sum
- `down[i]` 表示以 i 为端点向下延伸的最大和。  
- 全局更新过点路径最大值。

```java
class Solution {
    int ans = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) { dfs(root); return ans; }
    int dfs(TreeNode x){
        if (x==null) return 0;
        int L=Math.max(0, dfs(x.left));
        int R=Math.max(0, dfs(x.right));
        ans = Math.max(ans, L+R+x.val);
        return x.val + Math.max(L, R);
    }
}
```

**常见坑**：全局答案在递归中更新；负数要截断或考虑空路径。

---

## 8. 数位 DP（Digit DP）
**识别信号**：统计 ≤N 的整数个数，且约束基于十进制位（无前导零、相邻位关系、禁止某位数）。  
**核心状态**：`f[pos][tight][leadingZero][...约束...]`  
- `pos`：当前处理到第几位。  
- `tight`：是否受上界 N 的当前前缀限制。  
- `leadingZero`：是否仍在前导零区间。

### 模板（记忆化）
```java
class Solver {
    char[] s;
    Integer[][][][] memo; // 视约束数调整维度
    int dfs(int pos, int tight, int lead, int prev) {
        if (pos==s.length) return 1; // 已构成有效数
        if (memo[pos][tight][lead][prev+1]!=null) return memo[pos][tight][lead][prev+1];
        int up = tight==1 ? s[pos]-'0' : 9;
        int res=0;
        for (int d=0; d<=up; d++){
            int nt = (tight==1 && d==up) ? 1 : 0;
            int nl = (lead==1 && d==0) ? 1 : 0;
            if (/* violate constraint with prev & d & nl ? */) continue;
            int np = nl==1 ? -1 : d; // 仍前导零时 prev 无意义
            res += dfs(pos+1, nt, nl, np);
        }
        return memo[pos][tight][lead][prev+1]=res;
    }
    int solve(long N){
        s = String.valueOf(N).toCharArray();
        memo = new Integer[s.length][2][2][11]; // prev ∈ {-1..9} → 偏移 +1
        return dfs(0,1,1,-1);
    }
}
```

**示例约束**：相邻位不等、无连续 1、禁止某数字出现次数>k。

---

## 9. 图上 DP（DAG /Shortest-Path 型）
- **DAG DP**：拓扑序遍历，`dp[v] = combine(dp[u], w(u,v))`。  
- **最短路径即 DP**：Dijkstra/Bellman-Ford 可看作 DP。

### 例：课程安排最长链（DAG 最长路径）
```java
int[] indeg = new int[n];
List<int[]>[] g = new ArrayList[n];
for(int i=0;i<n;i++) g[i]=new ArrayList<>();
// build edges u->v
int[] dp = new int[n];
Queue<Integer> q=new ArrayDeque<>();
for(int i=0;i<n;i++) if(indeg[i]==0){ q.add(i); dp[i]=1; }
while(!q.isEmpty()){
    int u=q.poll();
    for(int[] e: g[u]){
        int v=e[0];
        dp[v]=Math.max(dp[v], dp[u]+1);
        if(--indeg[v]==0) q.add(v);
    }
}
int ans=0; for(int x:dp) ans=Math.max(ans,x);
```

---

## 10. 概率与期望 DP
**识别信号**：随机过程若干阶段，每阶段状态转移概率固定。

### 例：688. Knight Probability in Chessboard
`dp[step][i][j]`，或滚动二维数组。

```java
class Solution {
    public double knightProbability(int n, int k, int row, int col) {
        int[][] dir={{1,2},{2,1},{2,-1},{1,-2},{-1,-2},{-2,-1},{-2,1},{-1,2}};
        double[][] dp=new double[n][n];
        dp[row][col]=1.0;
        for(int step=0; step<k; step++){
            double[][] ndp=new double[n][n];
            for(int i=0;i<n;i++)
                for(int j=0;j<n;j++)
                    if(dp[i][j]>0){
                        for(int[] d:dir){
                            int x=i+d[0], y=j+d[1];
                            if(0<=x&&x<n&&0<=y&&y<n) ndp[x][y]+=dp[i][j]/8.0;
                        }
                    }
            dp=ndp;
        }
        double ans=0;
        for(double[] r:dp) for(double v:r) ans+=v;
        return ans;
    }
}
```

---

## 11. DP 优化
### 11.1 滚动数组
- 依赖仅上一行/列 → 压缩一维，注意覆盖顺序。

### 11.2 单调队列优化（窗口转移）
形如：`dp[i] = max_{i-w<=j<i} (dp[j] + a[j]) + b[i]`。  
用 deque 维护候选 j 的最优性（LC 1425 Constrained Subsequence Sum）。

```java
class Solution {
    public int constrainedSubsetSum(int[] nums, int k) {
        int n=nums.length;
        int[] dp=new int[n];
        Deque<Integer> dq=new ArrayDeque<>();
        int ans=Integer.MIN_VALUE;
        for(int i=0;i<n;i++){
            if(!dq.isEmpty() && dq.peekFirst()<i-k) dq.pollFirst();
            dp[i]=nums[i] + (dq.isEmpty()?0:Math.max(0, dp[dq.peekFirst()]));
            while(!dq.isEmpty() && dp[dq.peekLast()]<=dp[i]) dq.pollLast();
            dq.addLast(i);
            ans=Math.max(ans, dp[i]);
        }
        return ans;
    }
}
```

### 11.3 分治优化（Convex/Quadrangle 之外）
形如：`dp[k][m] = min_{j< m}(dp[k-1][j] + C(j+1, m))` 且**决策点单调**。  
使用 Divide&Conquer 计算每行 dp，复杂度从 O(n^2 k) 降到 O(n k log n)。

### 11.4 斜率优化（Convex Hull Trick）
形如：`dp[i] = min_j (m_j * x_i + b_j)`，m 单调或 x 单调可用维护下凸壳。

### 11.5 Knuth 优化 / Quadrangle Inequality
- 典型：区间合并代价 `dp[i][j] = min_{k}(dp[i][k]+dp[k][j]) + w[i][j]`。  
- 满足四边形不等式和单调性时，最优 k 单调。复杂度降为 O(n^2)。  
- 经典应用：石子合并、最优二叉搜索树（需检查代价函数性质）。

**实战要点**：先写对 O(n^3)；再检查是否满足单调/凸性再优化。

---

## 12. 位运算 DP（Bitset / SOS）
- **Bitset 子集和**：用位集做卷积，实现 `f |= f << w_i`（C++ 更友好）。  
- **SOS DP**：对所有子集或超集做 DP（掩码枚举），用于快速统计子集频次。

**Java 实用建议**：面试中更稳妥用 boolean/整型 DP；BitSet 左移可手动循环或自写方法。

---

## 13. 设计套路与调试清单
**选型表**：  
- 线性位置依赖 → 一维 DP。  
- 网格行列 → 二维 DP。  
- 序列关联（两串） → LCS/编辑距离类。  
- 能否选出和/价值 → 背包。  
- 区间分治/合并 → 区间 DP。  
- n≤20 且“选或不选” → 状压 DP。  
- 树结构 → 树形 DP。  
- 统计 ≤N 的数字个数且看十进制位 → 数位 DP。  
- 有窗口约束的转移最大/最小 → 单调队列优化。  
- 线性函数最小化 → 斜率优化。

**调试清单**：  
1) 状态定义是否闭合且足够表达答案。  
2) 初值与非法值（-INF/INF）是否正确。  
3) 迭代顺序是否满足依赖。  
4) 背包的正序/逆序是否正确。  
5) 边界：空串/空区间/负数/极端容量。  
6) 降序排序是否有助剪枝（如 698）。  
7) 记忆化 map 的 key 组合是否唯一且无遗漏。  
8) 是否可滚动降维，是否会覆盖需要的旧值。  
9) 是否可早停、可剪枝、可换更优数据结构。  
10) 复杂度评估，是否需要优化技术（§11）。

---

## 参考题单（按类型）
- 一维：70, 198, 213, 53, 322, 300(O(n^2)).  
- 二维网格：62, 63, 64, 120, 221, 174。  
- 序列：1143(LCS), 72(Edit), 583, 650。  
- 背包：416, 494, 518, 1049, 474(二维背包)。  
- 区间：516, 312, 1547, 1000。  
- 状压：464(Can I Win), 698, 847, 943。  
- 树形：337, 124, 968。  
- 数位：1012, 600。  
- 优化：1425(单调队列), 1547/1000(区间+优化判定)。

---

> 建议：按“选型→模板→题目”三步法练。拿到题先判断归类，再套对应状态和转移。先从能 AC 的 O(n^2)/O(n^3) 写起，再考虑 §11 的优化条件。
