package topinterview;

import model.Employee;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ExclusiveTimeofFunctions {
    public static void main(String[] args){
        ExclusiveTimeofFunctions app = new ExclusiveTimeofFunctions();
        String[] a = {"0:start:0","0:start:1","0:start:2","0:end:3","0:end:4","0:end:5"};
        List<String> test = Arrays.asList(a);
        app.exclusiveTime(1, test);

    }
    public int[] exclusiveTime(int n, List<String> logs) {
        int[] res = new int[n];
        Stack<int[]> stack = new Stack();
        for(String log: logs){
            String[] parsed = log.split(":");
            int curId = Integer.parseInt(parsed[0]);
            int time = Integer.parseInt(parsed[2]);
            if(parsed[1].equals("start")){
                stack.push(new int[]{curId, time});
                if(!stack.isEmpty()){
                    int[] pre = stack.peek();
                    res[pre[0]] += time - pre[1];
                }
            } else {
                // it is end
                int[] start = stack.pop();
                res[curId] += time - start[1] + 1;
                if(!stack.isEmpty()){
                    //it paused another task
                    int[] pre = stack.pop();
                    stack.push(new int[]{pre[0],time + 1});
                }
            }
        }
        return res;

    }
}
