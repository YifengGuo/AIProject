package maze_generation;

import java.util.Random;

/**
 * @author yifengguo
 */
public class GenerateRandomMaze {
    private final static int dim = 2;
    private final static double p0 = 0.0;
    private final static double p1 = 0.1;
    private final static double p2 = 0.3;
    private final static double p3 = 0.5;

    public int[][] genereate() {
        int[][] maze = new int[dim][dim];
        Random rand = new Random();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                double p = rand.nextDouble(); // 0.0 - 1.0
                if (p >= p1) {  // change wall occurrence probability
                    maze[i][j] = 0; // path
                } else {
                    maze[i][j] = 1; // wall
                }
            }
        }
        // reset start and goal
        maze[0][0] = 0;
        maze[dim - 1][dim - 1] = 0;
        return maze;
    }

    public static void main(String[] args) {
        int[][] maze = new GenerateRandomMaze().genereate();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
    }
}
