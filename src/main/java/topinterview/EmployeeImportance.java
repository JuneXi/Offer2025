/*
package topinterview;

import model.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeImportance {
    public int getImportance(List<Employee> employees, int id) {
        Map<Integer, Result> map = new HashMap<>();
        for(Employee e : employees){
            Result res = helper(e, id, map);
            if(res.found){
                return res.sum;
            }
        }
        return 0;

    }

    private Result helper(Employee e, int id, Map<Integer,Result> map){
        if(e == null){
            return new Result(0, false);
        }
        if(map.containsKey(e.id)){
            return map.get(e.id);
        }
        int sum = e.importance;
        for(Employee sub: e.subordinates){
            Result temp = helper(sub, id, map);
            if(temp.found){
                return temp;
            } else {
                sum += temp.sum;
            }
        }
        if(e.id == id){
            map.put(e.id, new Result(sum, true));
        } else {
            map.put(e.id, new Result(sum, false));
        }
        return map.get(e.id);

    }
}

class Result {
    public int sum;
    public boolean found;

    public Result(int sum, boolean found) {
        this.sum = sum;
        this.found = found;
    }
}
*/
