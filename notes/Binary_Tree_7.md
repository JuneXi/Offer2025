## Binary Tree Serialization and Deserialization 二叉树序列化与反序列化

---

### I. Preorder Traversal Method (DFS)

#### 思路 (Chinese)

* **序列化**：用前序遍历方式，将节点值输出到字符串中，`null` 节点记为 `#`，用分隔符（如`,`）分开。
* **反序列化**：从字符串列表头部依次取值，遇到 `#` 返回 `null`，否则创建节点并递归建立左右子树。

#### Key Points (English)

* Preorder ensures a unique reconstruction order (root → left → right).
* Serialized data can be stored or transmitted.
* Use a queue or pointer to consume tokens sequentially.

#### Java Implementation

```java
public class Codec {
    // Serialize the tree using preorder traversal
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }

    private void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("#,"); // use # for null
            return;
        }
        sb.append(node.val).append(",");
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    // Deserialize the string back to tree
    public TreeNode deserialize(String data) {
        String[] nodes = data.split(",");
        Queue<String> q = new LinkedList<>(Arrays.asList(nodes));
        return deserializeHelper(q);
    }

    private TreeNode deserializeHelper(Queue<String> q) {
        if (q.isEmpty()) return null;
        String val = q.poll();
        if (val.equals("#")) return null;
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializeHelper(q);
        node.right = deserializeHelper(q);
        return node;
    }
}
```

#### Complexity

* **Time**: O(n), each node is visited once.
* **Space**: O(n), recursion + queue.

---

### II. Level-Order Traversal Method (BFS)

#### 思路 (Chinese)

* **序列化**：按层输出节点，`null` 用 `#` 表示。
* **反序列化**：用队列层层重建子树。

#### English Explanation

* Serialize by levels using a queue.
* Deserialize iteratively, attaching children left-to-right.

#### Java Implementation

```java
public class CodecBFS {
    // Serialize tree using level-order traversal
    public String serialize(TreeNode root) {
        if (root == null) return "";
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (cur == null) {
                sb.append("#,");
                continue;
            }
            sb.append(cur.val).append(",");
            q.offer(cur.left);
            q.offer(cur.right);
        }
        return sb.toString();
    }

    // Deserialize from level-order string
    public TreeNode deserialize(String data) {
        if (data.isEmpty()) return null;
        String[] vals = data.split(",");
        TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int i = 1;
        while (!q.isEmpty()) {
            TreeNode cur = q.poll();
            if (i < vals.length && !vals[i].equals("#")) {
                cur.left = new TreeNode(Integer.parseInt(vals[i]));
                q.offer(cur.left);
            }
            i++;
            if (i < vals.length && !vals[i].equals("#")) {
                cur.right = new TreeNode(Integer.parseInt(vals[i]));
                q.offer(cur.right);
            }
            i++;
        }
        return root;
    }
}
```

#### Complexity

* **Time**: O(n)
* **Space**: O(n)

---

### III. Comparison 对比总结

| Method          | 特点           | 优点        | 缺点          |
| --------------- | ------------ | --------- | ----------- |
| Preorder DFS    | 根→左→右，递归结构简单 | 实现简洁、恢复唯一 | 栈深易溢出（深树）   |
| Level-order BFS | 按层扫描         | 直观、层次清晰   | 序列较长，需要显式队列 |

---

**Usage Tip:** Choose DFS for most algorithmic serialization; use BFS when human readability or structure visualization is needed.
