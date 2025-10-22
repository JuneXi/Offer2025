package google;

public class RatJump {
    /**
     * @param arr: the steps whether have glue
     * @return: the sum of the answers
     */
    public int ratJump(int[] arr) {
        // Write your code here.
        // from step 0 - step n
        // how many way to this step on odd step
        int[] odd = new int[arr.length + 1];
        int[] even = new int[arr.length + 1];
        //initilize
        odd[0] = 0;
        even[0] = 1;
        odd[1] = arr[1] == 1? 0:1;
        even[1] = 0;
        odd[2] = 1;
        even[2] = 1;
        odd[3] =  1;
        for(int i = 1; i < arr.length; i++){
            if(arr[i] == 1){
                continue;
            }
            odd[i] = buildOdd(i, even);
            even[i] = buildEven(i, odd);
        }

        return odd[arr.length] + even[arr.length] + odd[arr.length - 2] + odd[arr.length - 3] + even[arr.length - 1] + even[arr.length - 3];
    }

    private int buildOdd(int i, int[] even){
        int res = even[i - 1];
        if(i >=3){
            res += even[i - 3];
        }
        if(i >=4){
            res += even[i - 4];
        }
        return res;
    }

    private int buildEven(int i, int[] odd){
        int res = odd[ i - 1];
        if(i >= 2){
            res += odd[i - 2];
        }
        if(i >= 4){
            res += odd[i - 4];
        }
        return res;
    }
}
