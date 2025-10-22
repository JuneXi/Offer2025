package topinterview;

import java.util.Arrays;

public class Heaters {
    public static void main(String[] args){
        Heaters app = new Heaters();
        int[] houses = {617819336,399125485,156091745,356425228};
        int[] heaters = {585640194,937186357};
        app.findRadius(houses, heaters);
    }
    public int findRadius(int[] houses, int[] heaters) {
        int start = 0;
        // index of heater
        int h = 0;
        int res = 0;
        Arrays.sort(houses);
        Arrays.sort(heaters);

        if(houses[houses.length - 1] < heaters[0]){
            return heaters[0] - houses[0];
        }
        if(houses[0] > heaters[heaters.length - 1]){
            return houses[houses.length - 1] - heaters[heaters.length - 1];
        }
        if(heaters.length == 1){
            return Math.max(houses[houses.length - 1] - heaters[0], heaters[0] - houses[0]);

        }
        if(houses[0] < heaters[0]){
            res = heaters[0] - houses[0];

            while(houses[start] < heaters[0]){
                start++;
            }
        }

        // now houses[start] will be after the first heater
        for(int i = start; i < houses.length; i++){
            // find the closed heater > house
            while(h<heaters.length && heaters[h] <= houses[i]){
                h++;
            }
            //now
            if(h == heaters.length){
                //there is no more heaters, the last heater has to cover the last house
                return Math.max(houses[houses.length - 1] - heaters[heaters.length - 1], res);

            }
            int power = Math.min(heaters[h] - houses[i], houses[i] - heaters[h-1]);
            res = Math.max(res, power);
            h--;
        }
        return res;


    }
}
