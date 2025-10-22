package bfs;

public class TrappingRainWater2 {
    public int trapRainWater(int[][] heightMap) {
        // water = cubeSum - # of solid - # of air
        // # of solid = sum(height)
        // cubeSum = m * n * Max(height)
        // # air: cube on boundry and all cubes attached to the cubes on boundry by air => bfs, can only go up,left, right
        int height = 0;
        int solid = 0;

        for(int[] a: heightMap) {
            for(int i : a){
                height = Math.max(i, height);
                solid += i;
            }
        }
        int sum = heightMap.length * heightMap[0].length * height;
        //find air

return 0;
    }
}
