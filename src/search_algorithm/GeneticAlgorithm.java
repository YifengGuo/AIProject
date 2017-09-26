package search_algorithm;


import maze_generation.GenerateRandomMaze;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by guoyifeng on 9/24/17.
 */

/*
 * goal state : hardest maze: number of path cell -> 2* (2 * n - 1)
 * evaluation function: f(n) = number of path cell of maze
 *
 */
public class GeneticAlgorithm {
    // Random Number for Selection with 2-bit accuracy
    public static final double[] randomArray = getRandomArray();

    // four random mazes for Genetic Algorithm
//    int[][] maze1 = new GenerateRandomMaze().genereate();
//    int[][] maze2 = new GenerateRandomMaze().genereate();
//    int[][] maze3 = new GenerateRandomMaze().genereate();
//    int[][] maze4 = new GenerateRandomMaze().genereate();

    // four random mazes in list format and store in an ArrayList
    public static List<int[][]> mazeList = getMazeList();
//
//
//    // evaluation function value(number of 1) for each maze
//    int f1 = f(mazeList.get(0));
//    int f2 = f(mazeList.get(1));
//    int f3 = f(mazeList.get(2));
//    int f4 = f(mazeList.get(3));
//    int totalNum = f1 + f2 + f3 + f4;
//
//    // calculate selection probability of each maze
//    double p1 = getProbability(f1, totalNum);
//    double p2 = getProbability(f2, totalNum);
//    double p3 = getProbability(f3, totalNum);
//    double p4 = getProbability(f4, totalNum);
//    double[] probArray = new double[]{p1, p2, p3, p4};
//
//    // get the selected mazes for reproduction
//    List<int[]> selectedMaze1 = selectedMaze(randomArray, 0, probArray, mazeList);
//    List<int[]> selectedMaze2 = selectedMaze(randomArray, 1, probArray, mazeList);
//    List<int[]> selectedMaze3 = selectedMaze(randomArray, 2, probArray, mazeList);
//    List<int[]> selectedMaze4 = selectedMaze(randomArray, 3, probArray, mazeList);




    /*
     * calculate number of path of mazes for evaluation function
     */
    private static int f(int[][] maze) {
        int count = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /*
     * get array of random number for selection
     */
    private static double[] getRandomArray() {
        Random rand = new Random();
        double rand1 = Math.floor(rand.nextDouble() * 100) / 100;
        double rand2 = Math.floor(rand.nextDouble() * 100) / 100;
        double rand3 = Math.floor(rand.nextDouble() * 100) / 100;
        double rand4 = Math.floor(rand.nextDouble() * 100) / 100;

        return new double[]{rand1, rand2, rand3, rand4};
    }

    private static int[][] selectedMaze(double[] randomArray, int i, double[] probArray, List<int[][]> mazeList) {
        if (randomArray[i] >= 0 && randomArray[i] <= probArray[0]) {
            return mazeList.get(0);
        } else if (randomArray[i] > probArray[0] && randomArray[i] <= probArray[0] + probArray[1]) {
            return mazeList.get(1);
        } else if (randomArray[i] > probArray[0] + probArray[1] && randomArray[i] <= probArray[0] + probArray[1] + probArray[2]) {
            return mazeList.get(2);
        } else {
            return mazeList.get(3);
        }
    }


//    private static List<int[]> twoDArrayToList(int[][] twoDArray) {
//        List<int[]> list = new ArrayList<>();
//        for (int[] array : twoDArray) {
//            list.addAll(Arrays.asList(array));
//        }
//        return list;
//    }

    private static List<int[][]> getMazeList() {
        List<int[][]> mazeList = new ArrayList<>();
        int[][] maze1 = new GenerateRandomMaze().genereate();
        int[][] maze2 = new GenerateRandomMaze().genereate();
        int[][] maze3 = new GenerateRandomMaze().genereate();
        int[][] maze4 = new GenerateRandomMaze().genereate();
        mazeList.add(maze1);
        mazeList.add(maze2);
        mazeList.add(maze3);
        mazeList.add(maze4);
        return mazeList;
    }

    private static double getProbability(int fi, int totalNum) {
        return fi / totalNum;
    }

    /*
     * Crossover
     */
    private static List<int[][]> crossover(List<int[][]> selectedMazeList) {
        int crossIdx1 = getRandomCrossoverIndex(selectedMazeList);
        int crossIdx2 = getRandomCrossoverIndex(selectedMazeList);

        swap(crossIdx1, selectedMazeList.get(0), selectedMazeList.get(1));
        swap(crossIdx2, selectedMazeList.get(2), selectedMazeList.get(3));

        return selectedMazeList;
    }
    // helper function for swap partial maze in pairs
    private static void swap(int crossIdx, int[][] selectedMazeOne, int[][] selectedMazeTwo) {
        int[][] tmp = selectedMazeOne;
        for (int i = 0; i < selectedMazeOne.length; i++) {
            for (int j = crossIdx; j < selectedMazeOne[0].length; j++) {
                selectedMazeOne[i][j] = selectedMazeTwo[i][j];
            }
        }
        for (int i = 0; i < selectedMazeTwo.length; i++) {
            for (int j = crossIdx; j < selectedMazeTwo[0].length; j++) {
                selectedMazeTwo[i][j] = tmp[i][j];
            }
        }

    }  // end of crossover

    /*
     * mutation
     */
    private static void mutate(List<int[][]> mazeList) {
        int randIdx1 = getRandomNumberFromGivenRange(0, 3);
        int randIdx2 = getRandomNumberFromGivenRange(0, 3);
        int[] mutateCoordinate1 = getRandomMutationCoordinate();
        int[] mutateCoordinate2 = getRandomMutationCoordinate();
        int[][] mutateMaze1 = mazeList.get(randIdx1);
        int[][] mutateMaze2 = mazeList.get(randIdx2);

        // mutate process
        mutateMaze1[mutateCoordinate1[0]][mutateCoordinate1[1]] = 0;
        mutateMaze2[mutateCoordinate2[0]][mutateCoordinate2[1]] = 0;
    }


    private static int getRandomCrossoverIndex(List<int[][]> mazeList) {
        int totalCol = mazeList.get(0)[0].length; // total column number of maze
        // choose random index between (0, totalCol) as crossover index
        int crossoverIndex = getRandomNumberFromGivenRange(1, totalCol);
        return crossoverIndex;
    }

    private static int[] getRandomMutationCoordinate() {
        int totalCol = mazeList.get(0)[0].length;; // total column number of maze
        int totalRow = mazeList.get(0).length; // total row number of maze
        Random rand = new Random();
        int mutateRowIdx = getRandomNumberFromGivenRange(0, totalRow);
        int mutateColIdx = getRandomNumberFromGivenRange(0, totalCol);
        return new int[]{mutateRowIdx, mutateColIdx};
    }

    private static int getRandomNumberFromGivenRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min)) + min; // (min, max) exclusive
        return randomNum;
    }

    private static int[][] getWeightedMaze(int[][] maze) {
        int[][] weightedMaze = new int[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 0) {
                    weightedMaze[i][j] = -1;  // wall is -1 in weighted maze
                } else {
                    weightedMaze[i][j] = 0;   // path is 0 in weighted maze
                }
            }
        }
        return weightedMaze;
    }

    private static int getValidPathCellNumber(int[][] maze) {
        int count = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 1) { // if it is valid path cell
                    count++;
                }
            }
        }
        return count;
    }


    public static void main(String[] args) {
        int pathNum = Integer.MIN_VALUE; // termination condition of while loop
        int[][] hardest;
        SearchPathByShortestDFS dfs = new SearchPathByShortestDFS();
        int epoch = 0;

        while (true) {
            // evaluation function value(number of 1) for each maze
            int f1 = f(mazeList.get(0));
            int f2 = f(mazeList.get(1));
            int f3 = f(mazeList.get(2));
            int f4 = f(mazeList.get(3));
            int totalNum = f1 + f2 + f3 + f4;

            // calculate selection probability of each maze
            double p1 = getProbability(f1, totalNum);
            double p2 = getProbability(f2, totalNum);
            double p3 = getProbability(f3, totalNum);
            double p4 = getProbability(f4, totalNum);
            double[] probArray = new double[]{p1, p2, p3, p4};

            // get the selected mazes for reproduction
            int[][] selectedMaze1 = selectedMaze(randomArray, 0, probArray, mazeList);
            int[][] selectedMaze2 = selectedMaze(randomArray, 1, probArray, mazeList);
            int[][] selectedMaze3 = selectedMaze(randomArray, 2, probArray, mazeList);
            int[][] selectedMaze4 = selectedMaze(randomArray, 3, probArray, mazeList);

            // update maze list with selected mazes
            mazeList.clear();
            mazeList.add(selectedMaze1);
            mazeList.add(selectedMaze2);
            mazeList.add(selectedMaze3);
            mazeList.add(selectedMaze4);

            // crossover
            mazeList = crossover(mazeList);

            // mutate
            mutate(mazeList);

            for (int[][] maze : mazeList) {
                if (getValidPathCellNumber(maze) < 2 * maze.length - 1) {
                    System.out.println("Fail to construct hardest maze for cells cannot build a path.");
                    return;
                }
                int[][] weightedMaze = getWeightedMaze(maze);
                int pathLength = dfs.getPath(weightedMaze).size();
                if (pathLength > 30) {
                    pathNum = pathLength;
                    hardest = maze;
                    for (int i = 0; i < hardest.length; i++) {
                        for (int j = 0; j < hardest[0].length; j++) {
                            System.out.print(hardest[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.print("path length = " + pathLength + " ");
                    return;
                }
                System.out.print("path length = " + pathLength + " ");
                System.out.println("epoch times: "+epoch++);
            }
        }
    }
}



