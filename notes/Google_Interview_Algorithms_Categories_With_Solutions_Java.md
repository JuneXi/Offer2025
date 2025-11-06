# Google 面试常考算法题分类与题解（含 Java 模板）
Google Interview Algorithms — Categories, Patterns, and Java Templates

> 说明：每题给出 **思路要点** + **Java 代码模板**。代码偏模板化，便于面试快速落笔与口述优化。时间复杂度与关键坑位放在每题末尾。

---

## 目录
1. 数组与字符串 Array & String
2. 二分与分治 Binary Search & Divide-Conquer
3. 树与图 Tree & Graph
4. 动态规划 Dynamic Programming
5. 回溯与搜索 Backtracking & Search
6. 栈与队列 Stack & Queue
7. 哈希与堆 Hash & Heap
8. 位运算与数学 Bitmask & Math

---

## 1) 数组与字符串 Array & String

### 1.1 Two Sum
**Idea**：哈希表存补数 `target - x` → O(n)。  
```java
int[] twoSum(int[] a, int target){
    Map<Integer,Integer> idx = new HashMap<>();
    for(int i=0;i<a.length;i++){
        int need = target - a[i];
        if(idx.containsKey(need)) return new int[]{idx.get(need), i};
        idx.put(a[i], i);
    }
    return new int[0];
}
```  
复杂度：O(n) 时间，O(n) 空间。坑：重复元素位置。

---

### 1.2 3Sum
**Idea**：排序 + 双指针，跳重。  
```java
List<List<Integer>> threeSum(int[] nums){
    Arrays.sort(nums);
    List<List<Integer>> res = new ArrayList<>();
    int n=nums.length;
    for(int i=0;i<n;i++){
        if(i>0 && nums[i]==nums[i-1]) continue;
        int l=i+1,r=n-1;
        while(l<r){
            int s=nums[i]+nums[l]+nums[r];
            if(s==0){
                res.add(Arrays.asList(nums[i],nums[l],nums[r]));
                while(l<r && nums[l]==nums[l+1]) l++;
                while(l<r && nums[r]==nums[r-1]) r--;
                l++; r--;
            }else if(s<0) l++; else r--;
        }
    }
    return res;
}
```  
复杂度：O(n²)。坑：去重顺序。

---

### 1.3 Subarray Sum Equals K
**Idea**：前缀和 + 计数哈希。`cnt[sum-k]` 累加到答案。  
```java
int subarraySum(int[] a, int k){
    Map<Integer,Integer> cnt = new HashMap<>();
    cnt.put(0,1);
    int sum=0, ans=0;
    for(int x: a){
        sum += x;
        ans += cnt.getOrDefault(sum-k,0);
        cnt.put(sum, cnt.getOrDefault(sum,0)+1);
    }
    return ans;
}
```  
复杂度：O(n)。坑：初始 `cnt[0]=1`。

---

### 1.4 Longest Substring Without Repeating Characters
**Idea**：滑动窗口 + 位置表。  
```java
int lengthOfLongestSubstring(String s){
    int[] pos = new int[128];
    Arrays.fill(pos, -1);
    int l=0, ans=0;
    for(int r=0;r<s.length();r++){
        char c=s.charAt(r);
        if(pos[c]>=l) l=pos[c]+1;
        pos[c]=r;
        ans=Math.max(ans, r-l+1);
    }
    return ans;
}
```  
复杂度：O(n)。

---

### 1.5 Merge Intervals
**Idea**：按起点排序，能并就并。  
```java
int[][] merge(int[][] itv){
    Arrays.sort(itv, (a,b)->Integer.compare(a[0],b[0]));
    List<int[]> res = new ArrayList<>();
    int s=itv[0][0], e=itv[0][1];
    for(int i=1;i<itv.length;i++){
        if(itv[i][0] <= e){
            e = Math.max(e, itv[i][1]);
        }else{
            res.add(new int[]{s,e});
            s=itv[i][0]; e=itv[i][1];
        }
    }
    res.add(new int[]{s,e});
    return res.toArray(new int[0][]);
}
```  
复杂度：排序 O(n log n)。

---

### 1.6 Product of Array Except Self
**Idea**：前缀乘积 * 后缀乘积，O(1) 额外空间。  
```java
int[] productExceptSelf(int[] a){
    int n=a.length;
    int[] res=new int[n];
    int pre=1;
    for(int i=0;i<n;i++){ res[i]=pre; pre*=a[i]; }
    int suf=1;
    for(int i=n-1;i>=0;i--){ res[i]*=suf; suf*=a[i]; }
    return res;
}
```  
复杂度：O(n)。坑：0 的个数。

---

### 1.7 Container With Most Water
**Idea**：双指针，移动短板。  
```java
int maxArea(int[] h){
    int l=0,r=h.length-1,ans=0;
    while(l<r){
        ans=Math.max(ans, (r-l)*Math.min(h[l],h[r]));
        if(h[l]<h[r]) l++; else r--;
    }
    return ans;
}
```  
复杂度：O(n)。

---

## 2) 二分与分治 Binary Search & Divide-Conquer

### 2.1 Search in Rotated Sorted Array

Given a rotated sorted array nums (no duplicates) and a target value target, find the index of target. If not found, return -1.
You must solve it in O(log n) time.

**Idea**：在有序半边二分。  
```java
int search(int[] a, int t){
    int l=0,r=a.length-1;
    while(l<=r){
        int m=(l+r)>>>1;
        if(a[m]==t) return m;
        if(a[l]<=a[m]){
            if(a[l]<=t && t<a[m]) r=m-1; else l=m+1;
        }else{
            if(a[m]<t && t<=a[r]) l=m+1; else r=m-1;
        }
    }
    return -1;
}
```

---

### 2.2 Find Kth Largest Element (Quickselect)
**Idea**：原地分区，第 k 大转第 n-k 小。  
```java
int findKthLargest(int[] a, int k){
    int n=a.length, target=n-k;
    int l=0,r=n-1;
    while(true){
        int p=partition(a,l,r);
        if(p==target) return a[p];
        if(p<target) l=p+1; else r=p-1;
    }
}
int partition(int[] a, int l, int r){
    int pivot=a[r], i=l;
    for(int j=l;j<r;j++) if(a[j]<=pivot) {int t=a[i];a[i]=a[j];a[j]=t;i++;}
    int t=a[i];a[i]=a[r];a[r]=t;
    return i;
}
```  
期望 O(n)。

---

### 2.3 Median of Two Sorted Arrays
**Idea**：对短数组二分，保证左半段最大 ≤ 右半段最小。  
```java
double findMedianSortedArrays(int[] A, int[] B){
    if(A.length>B.length) return findMedianSortedArrays(B,A);
    int n=A.length,m=B.length, half=(n+m+1)/2;
    int l=0,r=n;
    while(l<=r){
        int i=(l+r)>>>1, j=half-i;
        int L1=(i==0?Integer.MIN_VALUE:A[i-1]);
        int R1=(i==n?Integer.MAX_VALUE:A[i]);
        int L2=(j==0?Integer.MIN_VALUE:B[j-1]);
        int R2=(j==m?Integer.MAX_VALUE:B[j]);
        if(L1<=R2 && L2<=R1){
            if(((n+m)&1)==1) return Math.max(L1,L2);
            return (Math.max(L1,L2)+Math.min(R1,R2))/2.0;
        }else if(L1>R2) r=i-1; else l=i+1;
    }
    return -1;
}
```  
复杂度：O(log min(n,m))。

---

### 2.4 Pow(x, n)
**Idea**：二分幂，n 可能为负。  
```java
double myPow(double x, long n){
    if(n<0){ x=1/x; n=-n; }
    double res=1;
    while(n>0){
        if((n&1)==1) res*=x;
        x*=x; n>>=1;
    }
    return res;
}
```

---

## 3) 树与图 Tree & Graph

### 3.1 Binary Tree Level Order Traversal
**Idea**：队列 BFS。  
```java
List<List<Integer>> levelOrder(TreeNode root){
    List<List<Integer>> res=new ArrayList<>();
    if(root==null) return res;
    Queue<TreeNode> q=new ArrayDeque<>();
    q.offer(root);
    while(!q.isEmpty()){
        int sz=q.size(); List<Integer> cur=new ArrayList<>(sz);
        for(int i=0;i<sz;i++){
            TreeNode n=q.poll(); cur.add(n.val);
            if(n.left!=null) q.offer(n.left);
            if(n.right!=null) q.offer(n.right);
        }
        res.add(cur);
    }
    return res;
}
```
O(n) O(n)

---

### 3.2 Lowest Common Ancestor (Binary Tree)
**Idea**：后序返回命中数，命中两次即 LCA。  
```java
TreeNode lowestCommonAncestor(TreeNode r, TreeNode p, TreeNode q){
    if(r==null || r==p || r==q) return r;
    TreeNode L=lowestCommonAncestor(r.left,p,q);
    TreeNode R=lowestCommonAncestor(r.right,p,q);
    if(L!=null && R!=null) return r;
    return L!=null?L:R;
}
```

---

### 3.3 Number of Islands
**Idea**：DFS/BFS 染色或并查集。  
```java
int numIslands(char[][] g){
    int R=g.length, C=g[0].length, ans=0;
    int[][] dirs={{1,0},{-1,0},{0,1},{0,-1}};
    for(int i=0;i<R;i++)for(int j=0;j<C;j++){
        if(g[i][j]=='1'){
            ans++;
            Deque<int[]> st=new ArrayDeque<>();
            st.push(new int[]{i,j}); g[i][j]='0';
            while(!st.isEmpty()){
                int[] p=st.pop();
                for(int[] d:dirs){
                    int x=p[0]+d[0], y=p[1]+d[1];
                    if(x>=0&&x<R&&y>=0&&y<C&&g[x][y]=='1'){
                        g[x][y]='0'; st.push(new int[]{x,y});
                    }
                }
            }
        }
    }
    return ans;
}
```

O(mn)

---

### 3.4 Course Schedule (Topo Sort)
**Idea**：入度为 0 入队，Kahn。  
```java
boolean canFinish(int num, int[][] pre){
    List<List<Integer>> g=new ArrayList<>();
    for(int i=0;i<num;i++) g.add(new ArrayList<>());
    int[] indeg=new int[num];
    for(int[] e: pre){ g.get(e[1]).add(e[0]); indeg[e[0]]++; }
    Queue<Integer> q=new ArrayDeque<>();
    for(int i=0;i<num;i++) if(indeg[i]==0) q.offer(i);
    int cnt=0;
    while(!q.isEmpty()){
        int u=q.poll(); cnt++;
        for(int v: g.get(u)) if(--indeg[v]==0) q.offer(v);
    }
    return cnt==num; // check 
}
```


空间：邻接表 O(V+E)，队列最坏 O(V)。

---

### 3.5 Word Ladder (BFS)
**Idea**：图隐式构造，按通配符桶连接。  
```java
int ladderLength(String begin, String end, List<String> wordList){
    Set<String> dict=new HashSet<>(wordList);
    if(!dict.contains(end)) return 0;
    Map<String,List<String>> bucket=new HashMap<>();
    int L=begin.length();
    for(String w: dict){
        for(int i=0;i<L;i++){
            String k=w.substring(0,i)+"*"+w.substring(i+1);
            bucket.computeIfAbsent(k, z->new ArrayList<>()).add(w);
        }
    }
    Queue<String> q=new ArrayDeque<>();
    q.offer(begin);
    Map<String,Integer> dist=new HashMap<>();
    dist.put(begin,1);
    while(!q.isEmpty()){
        String cur=q.poll(); int step=dist.get(cur);
        for(int i=0;i<L;i++){
            String k=cur.substring(0,i)+"*"+cur.substring(i+1);
            for(String nxt: bucket.getOrDefault(k, List.of())){
                if(nxt.equals(end)) return step+1;
                if(!dist.containsKey(nxt)){
                    dist.put(nxt, step+1);
                    q.offer(nxt);
                }
            }
        }
    }
    return 0;
}
```

---

### 3.6 Dijkstra（最短路，非负权）
给定一个包含 n 个节点的有向图，每条边 times[i] = (u, v, w) 表示从 u 到 v 需要时间 w。
从源点 k 出发，求所有节点收到信号的最短时间。
若有节点不可达，返回 -1。

**Idea**：堆优化 O((V+E) log V)。  
```java
int[] dijkstra(int n, List<int[]>[] g, int s){
    int INF=1_000_000_000;
    int[] dist=new int[n]; Arrays.fill(dist, INF);
    boolean[] vis=new boolean[n];
    PriorityQueue<int[]> pq=new PriorityQueue<>(Comparator.comparingInt(x->x[1]));
    dist[s]=0; pq.offer(new int[]{s,0});
    while(!pq.isEmpty()){
        int[] cur=pq.poll(); int u=cur[0];
        if(vis[u]) continue; vis[u]=true;
        for(int[] e: g[u]){
            int v=e[0], w=e[1];
            if(dist[v]>dist[u]+w){
                dist[v]=dist[u]+w;
                pq.offer(new int[]{v, dist[v]});
            }
        }
    }
    return dist;
}
```

---

## 4) 动态规划 Dynamic Programming

### 4.1 Longest Increasing Subsequence (LIS)
**Idea**：贪心+二分维护 `tails`。  
```java
int lengthOfLIS(int[] a){
    int[] t=new int[a.length]; int sz=0;
    for(int x: a){
        int i=Arrays.binarySearch(t,0,sz,x);
        if(i<0) i=-(i+1);
        t[i]=x; if(i==sz) sz++;
    }
    return sz;
}
```  
复杂度：O(n log n)。

---

### 4.2 Edit Distance
**Idea**：`dp[i][j]` 变更为 min(增删改)。  
```java
int minDistance(String a, String b){
    int n=a.length(), m=b.length();
    int[][] dp=new int[n+1][m+1];
    for(int i=0;i<=n;i++) dp[i][0]=i;
    for(int j=0;j<=m;j++) dp[0][j]=j;
    for(int i=1;i<=n;i++)
        for(int j=1;j<=m;j++)
            if(a.charAt(i-1)==b.charAt(j-1)) dp[i][j]=dp[i-1][j-1];
            else dp[i][j]=1+Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
    return dp[n][m];
}
```

---

### 4.3 Coin Change
给定硬币面额数组 coins 和目标金额 amount。
求最少需要多少枚硬币凑出 amount，若无法凑出返回 -1。
硬币数量无限。

**Idea**：完全背包最少张数。  
```java
int coinChange(int[] c, int amt){
    int INF=1_000_000_000;
    int[] dp=new int[amt+1];
    Arrays.fill(dp, INF); dp[0]=0;
    for(int x: c) for(int v=x; v<=amt; v++)
        dp[v]=Math.min(dp[v], dp[v-x]+1);
    return dp[amt]>=INF?-1:dp[amt];
}
```

---

### 4.4 House Robber I/II
**Idea**：线性/环形最大不相邻和。  
```java
int rob(int[] a){
    int take=0, skip=0;
    for(int x: a){
        int ntake=skip+x;
        skip=Math.max(skip,take);
        take=ntake;
    }
    return Math.max(take,skip);
}
int rob2(int[] a){
    if(a.length==1) return a[0];
    return Math.max(rob(Arrays.copyOfRange(a,0,a.length-1)),
                    rob(Arrays.copyOfRange(a,1,a.length)));
}
```

---

### 4.5 Unique Paths / Min Path Sum
**Idea**：网格 DP，滚动数组。  
```java
int uniquePaths(int m,int n){
    int[] dp=new int[n];
    Arrays.fill(dp,1);
    for(int i=1;i<m;i++)
        for(int j=1;j<n;j++) dp[j]+=dp[j-1];
    return dp[n-1];
}
int minPathSum(int[][] g){
    int m=g.length,n=g[0].length;
    int[] dp=new int[n];
    Arrays.fill(dp, 1_000_000_000);
    dp[0]=0;
    for(int i=0;i<m;i++)
        for(int j=0;j<n;j++){
            if(j==0) dp[j]+=g[i][j];
            else dp[j]=Math.min(dp[j], dp[j-1])+g[i][j];
        }
    return dp[n-1];
}
```

---

## 5) 回溯与搜索 Backtracking & Search

### 5.1 Permutations / Combinations / Subsets
**Idea**：路径列表 + used/起点剪枝。  
```java
List<List<Integer>> permute(int[] a){
    List<List<Integer>> res=new ArrayList<>();
    boolean[] used=new boolean[a.length];
    Deque<Integer> path=new ArrayDeque<>();
    dfs(a, used, path, res); return res;
}
void dfs(int[] a, boolean[] used, Deque<Integer> path, List<List<Integer>> res){
    if(path.size()==a.length){ res.add(new ArrayList<>(path)); return; }
    for(int i=0;i<a.length;i++){
        if(used[i]) continue;
        used[i]=true; path.addLast(a[i]);
        dfs(a,used,path,res);
        path.removeLast(); used[i]=false;
    }
}
List<List<Integer>> subsets(int[] a){
    List<List<Integer>> res=new ArrayList<>();
    backtrack(0,a,new ArrayDeque<>(),res); return res;
}
void backtrack(int start,int[] a,Deque<Integer> path,List<List<Integer>> res){
    res.add(new ArrayList<>(path));
    for(int i=start;i<a.length;i++){
        path.addLast(a[i]); backtrack(i+1,a,path,res); path.removeLast();
    }
}
```

| 问题类型         | 结果数     | 时间复杂度          | 空间复杂度 | 说明              |
| ------------ | ------- | -------------- | ----- | --------------- |
| Permutations | n!      | O(n × n!)      | O(n)  | 全排列，顺序敏感        |
| Combinations | C(n, k) | O(k × C(n, k)) | O(k)  | 固定选 k 个         |
| Subsets      | 2^n     | O(n × 2^n)     | O(n)  | 所有组合（k 从 0 到 n） |


---

### 5.2 N-Queens
**Idea**：列与两条对角线冲突检测，用位或集合。  
```java
List<List<String>> solveNQueens(int n){
    List<List<String>> res=new ArrayList<>();
    boolean[] col=new boolean[n];
    boolean[] d1=new boolean[2*n], d2=new boolean[2*n];
    char[] row=new char[n]; Arrays.fill(row, '.');
    dfs(0,n,col,d1,d2,new ArrayList<>(),res,row);
    return res;
}
void dfs(int r,int n,boolean[] col,boolean[] d1,boolean[] d2,
         List<String> board,List<List<String>> res,char[] row){
    if(r==n){ res.add(new ArrayList<>(board)); return; }
    for(int c=0;c<n;c++){
        int id1=r-c+n, id2=r+c;
        if(col[c]||d1[id1]||d2[id2]) continue;
        col[c]=d1[id1]=d2[id2]=true;
        row[c]='Q'; board.add(new String(row)); row[c]='.';
        dfs(r+1,n,col,d1,d2,board,res,row);
        board.remove(board.size()-1);
        col[c]=d1[id1]=d2[id2]=false;
    }
}
```

| 项目    | 复杂度       | 说明       |
| ----- | --------- | -------- |
| 时间    | O(N!)     | 回溯枚举并剪枝  |
| 空间    | O(N)      | 栈 + 标记数组 |
| 输出所有解 | O(N × 解数) | 每个解长度 N  |


---

### 5.3 Sudoku Solver
**Idea**：位掩码记录行列宫候选，递归填空。  
```java
void solveSudoku(char[][] b){
    int[] row=new int[9], col=new int[9], box=new int[9];
    List<int[]> blanks=new ArrayList<>();
    for(int i=0;i<9;i++)for(int j=0;j<9;j++){
        if(b[i][j]=='.') blanks.add(new int[]{i,j});
        else{
            int d=b[i][j]-'1', bit=1<<d, k=(i/3)*3+(j/3);
            row[i]|=bit; col[j]|=bit; box[k]|=bit;
        }
    }
    dfs(0,blanks,b,row,col,box);
}
boolean dfs(int t,List<int[]> blanks,char[][] b,int[] row,int[] col,int[] box){
    if(t==blanks.size()) return true;
    int i=blanks.get(t)[0], j=blanks.get(t)[1], k=(i/3)*3+(j/3);
    int used=row[i]|col[j]|box[k];
    for(int d=0; d<9; d++){
        int bit=1<<d;
        if((used&bit)!=0) continue;
        b[i][j]=(char)('1'+d);
        row[i]|=bit; col[j]|=bit; box[k]|=bit;
        if(dfs(t+1,blanks,b,row,col,box)) return true;
        row[i]^=bit; col[j]^=bit; box[k]^=bit; b[i][j]='.';
    }
    return false;
}
```

| 方法                                | 时间复杂度（最坏）                        | 空间复杂度                    | 说明                |
| --------------------------------- | -------------------------------- | ------------------------ | ----------------- |
| 朴素回溯                              | `O(9^E)`                         | `O(E)`                   | 每步尝试 1..9，无剪枝     |
| 回溯 + 约束传播（行列宫去候选、MRV/最少剩余值、前向检查）  | 仍为指数，常以 `O(∏ domain_i)` 表示       | `O(E)`                   | 大幅削减搜索树，但不存在多项式上界 |
| CSP/AC-3 等一致化                     | 指数（传播近似 `O(k·e·d^3)`/轮），总体仍由搜索主导 | `O(E)`                   | 传播只降分支，不改复杂度类别    |
| 精确覆盖 + Algorithm X（Dancing Links） | 指数（分支限界）                         | 约 `O(n_constraints + E)` | 实践很快，但理论仍指数       |


---

## 6) 栈与队列 Stack & Queue

### 6.1 Valid Parentheses
**Idea**：栈匹配。  
```java
boolean isValid(String s){
    Deque<Character> st=new ArrayDeque<>();
    for(char c: s.toCharArray()){
        if(c=='('||c=='['||c=='{') st.push(c);
        else{
            if(st.isEmpty()) return false;
            char t=st.pop();
            if((t=='('&&c!=')')||(t=='['&&c!=']')||(t=='{'&&c!='}')) return false;
        }
    }
    return st.isEmpty();
}
```

---

### 6.2 Min Stack（two stacks）
**Idea**：数据栈 + 同步最小栈。  
```java
class MinStack{
    Deque<Integer> s=new ArrayDeque<>(), m=new ArrayDeque<>();
    void push(int x){ s.push(x); m.push(m.isEmpty()?x:Math.min(x,m.peek())); }
    void pop(){ s.pop(); m.pop(); }
    int top(){ return s.peek(); }
    int getMin(){ return m.peek(); }
}
```

---

### 6.3 Largest Rectangle in Histogram（单调栈）

## 💡 思路
维护一个**单调递增栈**（存索引）。  
当遇到更矮的柱子时，说明以更高柱子为高的矩形无法再向右延伸，应立即结算面积。

```java
int largestRectangleArea(int[] h) {
    int n = h.length;
    int ans = 0;

    // 栈中存放索引，保持对应高度递增
    Deque<Integer> st = new ArrayDeque<>();

    // 遍历所有柱子，最后多加一个“高度为0”的哨兵
    for (int i = 0; i <= n; i++) {
        // 当前高度（末尾哨兵视为0）
        int cur = (i == n ? 0 : h[i]);

        // 若当前高度比栈顶柱子低，则开始结算面积
        while (!st.isEmpty() && h[st.peek()] > cur) {

            // 弹出栈顶柱子索引，代表以它为高的矩形无法再延伸到右边
            int height = h[st.pop()];

            // 弹出后，新的栈顶是“左边第一个比它矮”的柱子
            // 如果栈空，说明左边没有更矮的柱子，左边界设为 -1
            int left = st.isEmpty() ? -1 : st.peek();

            // 宽度 = 右边界(i) - 左边界(left) - 1
            // 因为 [left+1, i-1] 都 >= height
            int width = i - left - 1;

            // 面积 = 高 * 宽
            ans = Math.max(ans, height * width);
        }

        // 将当前索引入栈（保证栈单调递增）
        st.push(i);
    }

    return ans;
}
```

---

### 6.4 Sliding Window Maximum（单调队列）

#### 💡 思路
在长度为 `k` 的滑动窗口中，要求每次窗口滑动后**最大值**。  
暴力做法每次都扫描窗口，时间 `O(n*k)`。  
高效做法用**单调递减队列（Monotonic Queue）**，保持队首始终是当前窗口最大值，时间降为 `O(n)`。

---

#### 🔧 代码（Java）
```java
int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[n - k + 1];
        Deque<Integer> dq = new ArrayDeque<>(); // 存索引，保证队列内对应值单调递减

        for (int i = 0; i < n; i++) {
        // Step 1: 移除窗口外的索引
        // 当队首索引 < 当前窗口左边界时，弹出
        if (!dq.isEmpty() && dq.peekFirst() <= i - k)
        dq.pollFirst();

        // Step 2: 维持单调递减（队尾元素比当前值小则移除）
        // 这样保证队首始终是窗口内最大值的索引
        while (!dq.isEmpty() && nums[dq.peekLast()] <= nums[i])
        dq.pollLast();

        // Step 3: 当前元素入队
        dq.offerLast(i);

        // Step 4: 窗口形成后（i >= k-1），记录当前最大值
        if (i >= k - 1)
        res[i - k + 1] = nums[dq.peekFirst()];
        }

        return res;
        }
```

---

## 7) 哈希与堆 Hash & Heap

### 7.1 Top K Frequent Elements
**Idea**：计数 + 小根堆/桶。  
```java
int[] topKFrequent(int[] a, int k){
    Map<Integer,Integer> cnt=new HashMap<>();
    for(int x: a) cnt.put(x,cnt.getOrDefault(x,0)+1);
    PriorityQueue<int[]> pq=new PriorityQueue<>((x,y)->x[1]-y[1]);
    for(var e: cnt.entrySet()){
        pq.offer(new int[]{e.getKey(), e.getValue()});
        if(pq.size()>k) pq.poll();
    }
    int[] res=new int[k];
    for(int i=k-1;i>=0;i--) res[i]=pq.poll()[0];
    return res;
}
```

---

### 7.2 Kth Largest in Stream
**Idea**：维护大小为 k 的小根堆。  
```java
class KthLargest{
    int k; PriorityQueue<Integer> pq=new PriorityQueue<>();
    KthLargest(int k,int[] a){ this.k=k; for(int x:a) add(x); }
    int add(int val){
        pq.offer(val); if(pq.size()>k) pq.poll();
        return pq.peek();
    }
}
```

---

### 7.3 Merge K Sorted Lists
**Idea**：k 路归并，小根堆按节点值。  
```java
ListNode mergeKLists(ListNode[] lists){
    PriorityQueue<ListNode> pq=new PriorityQueue<>(Comparator.comparingInt(x->x.val));
    for(ListNode h: lists) if(h!=null) pq.offer(h);
    ListNode d=new ListNode(0), t=d;
    while(!pq.isEmpty()){
        ListNode n=pq.poll();
        t=t.next=n;
        if(n.next!=null) pq.offer(n.next);
    }
    return d.next;
}
```

---

### 7.4 LRU Cache（HashMap + 双向链表）
#### 💡 问题定义
设计一个满足以下操作的缓存结构，容量固定为 `capacity`：
1. **get(key)**：若存在返回对应值，否则返回 -1。
2. **put(key, value)**：插入或更新键值对，若超出容量则删除最久未使用的项。

#### 🔧 解法思路：哈希表 + 双向链表
核心思想：
- **哈希表 (HashMap)** 用于 `O(1)` 定位节点。
- **双向链表 (Doubly Linked List)** 按访问时间维护顺序，最近使用的放在表头，最久未使用的在表尾。
- 每次访问或更新节点时，将节点移到链表头部；插入时若超出容量，删除尾节点。

```java
class LRUCache{
    static class Node{ int k,v; Node p,n; Node(int k,int v){this.k=k;this.v=v;} }
    int cap; Map<Integer,Node> map=new HashMap<>();
    Node h=new Node(0,0), t=new Node(0,0);
    LRUCache(int c){ cap=c; h.n=t; t.p=h; }
    int get(int k){ Node x=map.get(k); if(x==null) return -1; moveToHead(x); return x.v; }
    void put(int k,int v){
        Node x=map.get(k);
        if(x!=null){ x.v=v; moveToHead(x); return; }
        if(map.size()==cap){ Node rm=t.p; remove(rm); map.remove(rm.k); }
        Node y=new Node(k,v); addHead(y); map.put(k,y);
    }
    void moveToHead(Node x){ remove(x); addHead(x); }
    void addHead(Node x){ x.n=h.n; x.p=h; h.n.p=x; h.n=x; }
    void remove(Node x){ x.p.n=x.n; x.n.p=x.p; }
}
```

---

### 7.5 LFU Cache（O(1)）

#### 💡 问题定义
设计一个缓存系统，满足：
1. **get(key)**：若存在返回值并增加其访问频率，否则返回 -1
2. **put(key, value)**：插入或更新键值对
    - 若缓存满，则删除“使用频率最低”的元素
    - 若频率相同，则删除**最久未使用**的那个

要求所有操作平均复杂度 **O(1)**。

## 🧩 数据结构设计
为同时满足：
- 按访问频率查找最少使用项
- 同频率内保持使用顺序（LRU）

使用以下组合结构：

| 结构 | 功能 |
|------|------|
| `Map<Integer, Node>` | 根据 key 定位节点 |
| `Map<Integer, LinkedHashSet<Node>>` | 每个频率对应一个有序集合（保证频率内的 LRU） |
| 整数变量 `minFreq` | 当前全局最小频率 |

LinkedHashSet 是 Java 集合框架中的一个类，定义在 java.util 包下。
它结合了 HashSet 和 LinkedList 的特性，既能保证元素唯一性，又能保持插入顺序。

| 特性        | HashSet | LinkedHashSet | TreeSet  |
| --------- | ------- | ------------- | -------- |
| 是否唯一      | ✅ 是     | ✅ 是           | ✅ 是      |
| 是否有序      | ❌ 无序    | ✅ 保留插入顺序      | ✅ 按比较器顺序 |
| 底层结构      | HashMap | LinkedHashMap | TreeMap  |
| 是否允许 null | ✅ 允许一个  | ✅ 允许一个        | ❌ 不允许    |


```java
class LFUCache{
    class Node{int k,v,f; Node p,n; Node(int k,int v){this.k=k;this.v=v;this.f=1;}}
    class DL{Node h=new Node(0,0), t=new Node(0,0); int sz=0;
        DL(){h.n=t;t.p=h;} void add(Node x){x.n=h.n;x.p=h;h.n.p=x;h.n=x;sz++;}
        void rm(Node x){x.p.n=x.n;x.n.p=x.p;sz--;} Node pop(){Node x=t.p; rm(x); return x;}
        boolean empty(){return sz==0;}}
    int cap, minf=0; Map<Integer,Node> map=new HashMap<>(); Map<Integer,DL> freq=new HashMap<>();
    LFUCache(int c){cap=c;}
    int get(int k){ Node x=map.get(k); if(x==null) return -1; touch(x); return x.v; }
    void put(int k,int v){
        if(cap==0) return;
        if(map.containsKey(k)){ Node x=map.get(k); x.v=v; touch(x); return; }
        if(map.size()==cap){ DL lst=freq.get(minf); Node rm=lst.pop(); map.remove(rm.k); }
        Node x=new Node(k,v); map.put(k,x); minf=1;
        freq.computeIfAbsent(1,z->new DL()).add(x);
    }
    void touch(Node x){
        DL lst=freq.get(x.f); lst.rm(x);
        if(minf==x.f && lst.empty()) minf++;
        x.f++; freq.computeIfAbsent(x.f,z->new DL()).add(x);
    }
}
```

---

## 8) 位运算与数学 Bitmask & Math

### 8.1 Single Number II（每位计数 %3）
**Idea**：统计各位出现次数 mod 3。  
```java
int singleNumber(int[] a){
    int ones=0, twos=0;
    for(int x: a){
        ones = (ones ^ x) & ~twos;
        twos = (twos ^ x) & ~ones;
    }
    return ones;
}
```

---

### 8.2 Counting Bits
**Idea**：`dp[i]=dp[i>>1]+(i&1)`。  
```java
int[] countBits(int n){
    int[] dp=new int[n+1];
    for(int i=1;i<=n;i++) dp[i]=dp[i>>1]+(i&1);
    return dp;
}
```

---

### 8.3 Subsets via Bitmask
**Idea**：枚举 0..(1<<n)-1。  
```java
List<List<Integer>> subsetsBit(int[] a){
    int n=a.length; List<List<Integer>> res=new ArrayList<>();
    for(int m=0;m<(1<<n);m++){
        List<Integer> cur=new ArrayList<>();
        for(int i=0;i<n;i++) if(((m>>i)&1)==1) cur.add(a[i]);
        res.add(cur);
    }
    return res;
}
```

---

### 8.4 Maximum XOR of Two Numbers
**Idea**：贪心构造前缀 + Trie/哈希推断。  
```java
int findMaximumXOR(int[] a){
    int ans=0, mask=0;
    for(int b=31;b>=0;b--){
        mask |= (1<<b);
        Set<Integer> pref=new HashSet<>();
        for(int x: a) pref.add(x & mask);
        int cand = ans | (1<<b);
        boolean ok=false;
        for(int p: pref) if(pref.contains(p ^ cand)){ ok=true; break; }
        if(ok) ans=cand;
    }
    return ans;
}
```

---

### 8.5 Traveling Salesman Problem (bitmask DP，小 n)

#### 💡 问题定义
给定 `n` 个城市与它们之间的距离矩阵 `dist[i][j]`，  
要求找出一条最短路径，使得：
- 从某个起点出发；
- 经过每个城市恰好一次；
- 最后回到起点。

**Idea**：

`dp[mask][i]` = 已访问城市集合为 `mask`，且当前在城市 `i` 时的最小路径长度。
- `mask` 是二进制数，`mask & (1 << j)` 表示是否访问城市 `j`。
- 起点设为 `0`（也可任意）。
- 初始：`dp[1<<0][0] = 0`

# TSP（Bitmask DP）——代码注释详解

## 版本一：只求最短路长（详细注释）
```java
import java.util.*;

class TSP {
    /**
     * 旅行商问题（TSP）精确解：状态压缩 DP
     * 输入：dist[i][j] 表示城市 i 到 j 的距离（非负，允许不对称）
     * 约定：从城市 0 出发，最终回到城市 0
     * 返回：最短回路长度
     * 复杂度：时间 O(n^2 * 2^n)，空间 O(n * 2^n)
     */
    public int tsp(int[][] dist) {
        int n = dist.length;
        int N = 1 << n;              // 状态数：每一位表示某城市是否已访问
        final int INF = 1_000_000_000;

        // dp[mask][i]：访问集合为 mask，且当前停在城市 i 的最小代价
        int[][] dp = new int[N][n];
        for (int[] row : dp) Arrays.fill(row, INF);

        // base：只访问起点 0 且停在 0，代价为 0
        dp[1 << 0][0] = 0;

        // 遍历所有子集状态 mask（1..N-1）
        // 外层循环用 mask 是因为动态规划的依赖方向是：
        // 小集合 → 大集合（即访问城市数少 → 访问城市数多）
        for (int mask = 1; mask < N; mask++) {
            // 若起点 0 未被访问，则该 mask 非法（可跳过以小幅优化）
            if ((mask & 1) == 0) continue;

            for (int i = 0; i < n; i++) {
                // i 必须在 mask 中（当前停在 i）
                if ((mask & (1 << i)) == 0) continue;
                int cur = dp[mask][i];
                if (cur == INF) continue;

                // 尝试从 i 扩展到下一个未访问城市 j
                for (int j = 0; j < n; j++) {
                    // j 未访问才能走
                    if ((mask & (1 << j)) != 0) continue;
                    int nextMask = mask | (1 << j);
                    // 进行松弛：到 j 的更优代价
                    int cand = cur + dist[i][j];
                    if (cand < dp[nextMask][j]) {
                        dp[nextMask][j] = cand;
                    }
                }
            }
        }

        // 所有城市都访问完：mask = (1<<n)-1
        int full = N - 1;
        int ans = INF;

        // 最后一步：从任意终点 i 回到起点 0，取最小
        for (int i = 1; i < n; i++) {
            if (dp[full][i] == INF) continue;
            ans = Math.min(ans, dp[full][i] + dist[i][0]);
        }
        return ans;
    }
}

```

| 项目 | 复杂度      |
| -- | -------- |
| 时间 | O(n²·2ⁿ) |
| 空间 | O(n·2ⁿ)  |


---

## 速刷建议 Roadmap
- 先模板：二分、滑窗、单调栈、前缀和、并查集、BFS/DFS。
- 再 DP 代表题：LIS / 背包 / 编辑距离 / 打家劫舍。
- 树图综合：LCA、拓扑、最短路、并查集连通性。
- 系统训练：LRU/LFU、K 路归并、区间题。

> 所有模板均可口述边写边测。面试中优先写**正确简单**版本，再讨论优化。