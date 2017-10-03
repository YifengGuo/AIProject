package search_algorithm;

import maze_generation.GenerateRandomMaze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by guoyifeng on 9/28/17.
 */
/*
 * goal state : hardest maze: number of path cell -> 2.5 * maze.length for shortestDFS
 * evaluation function: f(n) = number of wall cell of maze
 *
 */
@SuppressWarnings("Duplicates")
public class GeneticAlgorithmScalar {
    // control how many mazes shall be generated for genetic algorithm
    public static final int mazeCount = 200;
    // Random Number for Selection with 2-bit accuracy
    public static final double[] randomArray = getRandomArray(mazeCount); // n random numbers in randArray

    // n random mazes in list format and store in an ArrayList
    public static List<int[][]> mazeList = getMazeList(mazeCount); // list contains n random generated mazes


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
     * get  arrays of n random numbers for selection
     */
    private static double[] getRandomArray(int n ) {
        Random rand = new Random();
        double[] randArray = new double[n];
        for (int i = 0; i < n; i++) {
            randArray[i] = Math.floor(rand.nextDouble() * 100) / 100;
        }
        return randArray;
    }

    /**
     * probability of selection determines maze belongs to which range of [0,1]<br>
     * if randomArray(index) locates between [prefixSum(i), prefixSum(i + 1))
     * then the selected maze is i-th maze in mazeList
     * @param randomArray
     * @param index
     * @param probArray
     * @param mazeList
     * @return selectedMaze
     */
    private static int[][] selectedMaze(double[] randomArray, int index, double[] probArray, List<int[][]> mazeList) {
        // O(n ^ 2) time complexity cost here
        for (int i = 0; i < randomArray.length; i++) {
            if (randomArray[index] >= calculatePrefixSumProb(i, probArray) &&
                    randomArray[index] < calculatePrefixSumProb(i + 1, probArray)) {
                return mazeList.get(i);
            }
        }
        return mazeList.get(mazeList.size() - 1); // error condition control
    }

    /**
     * when select maze from mazeList, need to determine which range does current
     * maze probability of selection belong to
     * @param prefix
     * @param probArray
     * @return prefix sum of probability array
     */
    private static double calculatePrefixSumProb(int prefix, double[] probArray) {
        double prefixSum = 0;
        for (int i = 0; i < prefix; i++) {
            prefixSum += probArray[i];
        }
        return prefixSum;

    }

    /**
     * generate n random mazes
     * @param n
     * @return list of generated mazes
     */
    private static List<int[][]> getMazeList(int n) {
        List<int[][]> mazeList = new ArrayList<>();
        GenerateRandomMaze mazeGenerator = new GenerateRandomMaze();
        for (int i = 0; i < n; i++) {
            mazeList.add(mazeGenerator.generate());
        }
        return mazeList;
    }

    /**
     * return selection probability of current maze
     * rule is number of wall of current maze / total number of wall of all mazes
     * @param fi
     * @param totalNum
     * @return
     */
    private static double getProbability(int fi, int totalNum) {
        return ((fi *1.0) / totalNum);
    }

    /**
     * crossover process of mazes
     * get random indices for swap
     * swap mazes in pairs (0,99), (1,98)....(49,50)
     * @param selectedMazeList
     * @return
     */
    private static List<int[][]> crossover(List<int[][]> selectedMazeList) {
        int[] crossIdxArray = new int[selectedMazeList.size() / 2];
        // generate random crossover indices and store them in an array
        for (int i = 0; i < crossIdxArray.length; i++) {
            crossIdxArray[i] = getRandomCrossoverIndex(selectedMazeList);
        }
        // swap mazes in pairs
        for (int i = 0; i < crossIdxArray.length; i++) {
            swap(crossIdxArray[i], selectedMazeList.get(i), selectedMazeList.get(selectedMazeList.size() - i - 1));
        }

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

    /**
     * mutation process of mazes
     * @param mazeList
     */
    private static void mutate(List<int[][]> mazeList) {
        // generate random mutate indices for maze in mazeList and store them in an array
        int[] randArray = new int[mazeList.size() / 2];
        for (int i = 0; i < randArray.length; i++) {
            randArray[i] = getRandomNumberFromGivenRange(0, mazeList.size() - 1);
        }
        // generate n / 2 mutation coordinates pair and store them in a 2d array
        int[][] mutateCoordinates = new int[mazeList.size() / 2][2];
        for (int i = 0; i < mutateCoordinates.length; i++) {
            mutateCoordinates[i] = getRandomMutationCoordinate();
        }


        // mutate process
        for (int i = 0; i < randArray.length; i++) {
            // add wall on random position
            mazeList.get(randArray[i])[mutateCoordinates[i][0]][mutateCoordinates[i][1]] = 0;
        }
    }

    /**
     * get crossover index randomly
     * take this index as a pivot, two mazes will exchange the right half by this pivot
     * @param mazeList
     * @return random cross pivot
     */
    private static int getRandomCrossoverIndex(List<int[][]> mazeList) {
        int totalCol = mazeList.get(0)[0].length; // total column number of maze
        // choose random index between (0, totalCol) as crossover index
        int crossoverIndex = getRandomNumberFromGivenRange(1, totalCol);
        return crossoverIndex;
    }

    /**
     * get random coordinate for maze to mutate
     * @return
     */
    private static int[] getRandomMutationCoordinate() {
        int totalCol = mazeList.get(0)[0].length;
        ; // total column number of maze
        int totalRow = mazeList.get(0).length; // total row number of maze
        int mutateRowIdx = getRandomNumberFromGivenRange(0, totalRow);
        int mutateColIdx = getRandomNumberFromGivenRange(0, totalCol);
        return new int[]{mutateRowIdx, mutateColIdx};
    }

    private static int getRandomNumberFromGivenRange(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min)) + min; // (min, max) exclusive
        return randomNum;
    }

    /**
     * only used for shortestDFS test because shortestDFS uses dynamic programming which
     * caches the distance from next node to start node
     * @param maze
     * @return
     */
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

    /**
     * calculate total number of valid path cells of current maze
     * if current maze's valid path cells < 2 * maze.length - 1
     * this maze cannot be solved any more
     * @param maze
     * @return
     */
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

    /**
     *
     * @param mazeList
     * @param EFArray
     * @param totalNum
     * @return array which contains each maze's selection probability
     */
    private static double[] getProbArray(List<int[][]> mazeList, int[] EFArray, int totalNum) {
        double[] probArray = new double[mazeList.size()];
        for (int i = 0; i < probArray.length; i++) {
            probArray[i] = getProbability(EFArray[i], totalNum);
        }
        return probArray;
    }

    /**
     * output hardest maze to do the visualization
     * @param maze
     * @param list
     */
    private static void outputMaze(int[][] maze, List<SearchPathByShortestDFS.Entry> list) {
        // longest solution path
        // File file = new File("src/data_visualization/Q10/DFS_shortest/LongestSolutionPath.csv");
        // most expanded nodes
        //File file = new File("src/data_visualization/Q10/DFS_shortest/MostExpandedNodes.csv");
        // max fringe size
        File file = new File("src/data_visualization/Q10/DFS_shortest/MaxFringeSize.csv");
        BufferedWriter bw = null;
        for (SearchPathByShortestDFS.Entry e : list) {
            maze[e.x][e.y] = 2;
        }
        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < maze.length; i++) {
                for (int j = 0; j < maze.length; j++) {
                    if (j != maze.length - 1) {
                        bw.write(maze[i][j] + ",");
                    } else {
                        bw.write(maze[i][j]+ "");
                    }
                }
                bw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) {
        int[][] hardest;
        SearchPathByShortestDFS dfs = new SearchPathByShortestDFS();
        int epoch = 0;
        int longestSoFar = 0;
        int mostExpandedNodesSoFar = 0;
        int maxFringeSizeSoFar = 0;

        while (true) {
            // evaluation function value(number of 1) for each maze
            int[] EFArray = new int[mazeList.size()]; // evaluation function array record
                                                      // number of valid wall cell in each maze
            for (int i = 0; i < EFArray.length; i++) {
                EFArray[i] = f(mazeList.get(i));
            }
            int totalNum = 0;
            for (int num : EFArray) {
                totalNum += num;
            }

            // calculate selection probability of each maze and store them in probArray
            double[] probArray = getProbArray(mazeList, EFArray, totalNum);

            // get the selected mazes for reproduction
            List<int[][]> selectedMazes = new ArrayList<>();
            for (int i = 0; i < mazeList.size(); i++) {
                selectedMazes.add(selectedMaze(randomArray, i, probArray, mazeList));
            }

            // update maze list with selected mazes
            mazeList.clear();
            mazeList.addAll(selectedMazes);

            // crossover
            mazeList = crossover(mazeList);

            // mutate
            mutate(mazeList);

            // i) solution path length

//            for (int[][] maze : mazeList) {
//                if (getValidPathCellNumber(maze) < 2 * maze.length - 1) {
//                    System.out.println("Fail to construct hardest maze for cells cannot build a path.");
//                    return;
//                }
//                int[][] weightedMaze = getWeightedMaze(maze);
//                List<SearchPathByShortestDFS.Entry> path = dfs.getPath(weightedMaze);
//                int pathLength = path.size();
//                longestSoFar = Math.max(pathLength, longestSoFar);
//                if (pathLength > 3 * maze.length) {  // this if controls loop termination
//                    hardest = maze;
//                    for (int i = 0; i < hardest.length; i++) {
//                        for (int j = 0; j < hardest[0].length; j++) {
//                            System.out.print(hardest[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//
//                    // output maze for visualization
//                    outputMaze(hardest, path);
//
//                    System.out.println("path length = " + pathLength + " ");
//                    for (SearchPathByShortestDFS.Entry e : path) {
//                        System.out.print(e.toString() + " ");
//                    }
//                    return;
//                }
//                System.out.print("path length = " + pathLength + " ");
//                System.out.print("epoch times: " + epoch + " ");
//                System.out.println("Longest Path So Far: " + longestSoFar );
//            }
//            epoch++;


            // ii) expanded nodes

//            for (int[][] maze : mazeList) {
//                if (getValidPathCellNumber(maze) < 2 * maze.length - 1) {
//                    System.out.println("Fail to construct hardest maze for cells cannot build a path.");
//                    return;
//                }
//                int[][] weightedMaze = getWeightedMaze(maze);
//                List<SearchPathByShortestDFS.Entry> path = dfs.getPath(weightedMaze);
//                int visitedCount = 0;
//                for (int i = 0; i < maze.length; i++) {
//                    for (int j = 0; j < maze.length; j++) {
//                        if (weightedMaze[i][j] > 0) {
//                            visitedCount++;
//                        }
//                    }
//                }
//                mostExpandedNodesSoFar = Math.max(visitedCount, mostExpandedNodesSoFar);
//                if (visitedCount > Math.pow(maze.length, 1.8) && !path.isEmpty()) {  // this if controls loop termination
//                    hardest = maze;
//                    for (int i = 0; i < hardest.length; i++) {
//                        for (int j = 0; j < hardest[0].length; j++) {
//                            System.out.print(hardest[i][j] + " ");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println("Total number of expanded nodes is = " + visitedCount + " ");
//                    for (SearchPathByShortestDFS.Entry e : path) {
//                        System.out.print(e.toString() + " ");
//                    }
//                    // output maze for visualization
//                    outputMaze(hardest, path);
//                    return;
//                }
//                System.out.print("expanded nodes = " + visitedCount + " ");
//                System.out.print("epoch times: " + epoch + " ");
//                System.out.println("Most Expanded Nodes So Far: " + mostExpandedNodesSoFar);
//            }
//            epoch++;

            // iii) max fringe size

            for (int[][] maze : mazeList) {
                if (getValidPathCellNumber(maze) < 2 * maze.length - 1) {
                    System.out.println("Fail to construct hardest maze for cells cannot build a path.");
                    return;
                }
                int[][] weightedMaze = getWeightedMaze(maze);
                List<SearchPathByShortestDFS.Entry> path = dfs.getPath(weightedMaze);
                int maxFringe = dfs.getMaxFringeSize();
                maxFringeSizeSoFar = Math.max(maxFringe, maxFringeSizeSoFar);
                if (maxFringe > 3.2 * 3 * maze.length && !path.isEmpty()) {  // this if controls loop termination
                    hardest = maze;
                    for (int i = 0; i < hardest.length; i++) {
                        for (int j = 0; j < hardest[0].length; j++) {
                            System.out.print(hardest[i][j] + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Maximum size of the fringe = " + maxFringe + " ");
                    for (SearchPathByShortestDFS.Entry e : path) {
                        System.out.print(e.toString() + " ");
                    }
                    // output maze for visualization
                    outputMaze(hardest, path);
                    return;
                }
                System.out.print("Maximum size of the fringe = " + maxFringe + " ");
                System.out.print("epoch times: " + epoch + " ");
                System.out.println("Max Fringe Size So Far: " + maxFringeSizeSoFar);
            }
            epoch++;

        }
    }
}
