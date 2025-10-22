import topinterview.TrappingRainWater;

public class TrappingRainWaterTest {
    public static void main (String[] args){
        TrappingRainWater app = new TrappingRainWater();
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(app.trap(height));
    }
}
