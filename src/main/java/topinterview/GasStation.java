package topinterview;

public class GasStation {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int sum = 0;
        int j = gas.length;

        for(int i = 0; i < j; i++ )    {
            sum = sum + gas[i] - cost[i];
            // has to move 1 step earlier;
            while(sum < 0 && j > i){
                j--;
                sum = sum + gas[j] - cost[j];
            }
        }
        return sum < 0? -1:  j == gas.length?0:j;

    }
}
