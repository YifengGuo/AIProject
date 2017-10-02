package search_algorithm;

import maze_generation.GenerateRandomMaze;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.stream.Collectors;

/**
 * @author yifengguo
 */
public class SearchPathByShortestDFS {
    class Entry {
        int x;
        int y;
        int value;

        public Entry(int x, int y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry) o;
            return e.x == x
                    && e.y == y
                    && e.value == value;
        }

        // 17 and 31 are common prime number used in hashCode()
        @Override
        public int hashCode() {
            int result = 17;
            result = 31 * result + x;
            result = 31 * result + y;
            result = 31 * result + value;
            return result;
        }
    }

    private int maxFringeSize;
    private int expandedNodes;
    private int pathLength;
    public void setMaxFringeSize(int maximun){
        this.maxFringeSize = maximun;
    }
    public int getMaxFringeSize(){
        return maxFringeSize;
    }
    public void setPathLength(int length){
        this.pathLength = length;
    }
    public int getPathLength(){
        return pathLength;
    }

    public List<Entry> getPath(int[][] maze) {
        List<Entry> res = new ArrayList<>();
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return res;
        }

        Deque<Entry> stack = new LinkedList<>();
        Deque<Entry> shortestPath = new LinkedList<>();

        int[][] visitedDist = new int[maze.length][maze[0].length];

        Entry start = new Entry(0, 0, 0);
        Entry end = new Entry(maze.length - 1, maze.length - 1, 0);

        searchPath(shortestPath, stack, maze, visitedDist, start, end);

        // get shortest path
        while (!shortestPath.isEmpty()) {
            res.add(shortestPath.pollFirst());
        }

        setPathLength(res.size());
        return res;
    }

    private void searchPath(Deque<Entry> shortestPath, Deque<Entry> stack, int[][] weightedMaze, int[][] visitedDist, Entry start, Entry end) {
        visitedDist = weightedMaze;   // initialize visitedDist as weightedMaze

        stack.offerFirst(start);  // push start
        visitedDist[start.x][start.y] = 1;

        maxFringeSize = 1;
        expandedNodes = 1;

        while (!stack.isEmpty()) {  // stack is empty means cannot find shorter path
            Entry next = getAdjacentNotVisitedEntry(stack.peekFirst(), visitedDist, weightedMaze);  // next is always on more optimal path
            if (next.x == -1) {  // cannot find valid path from adjacent entries
                stack.pollFirst();  // backtrack
                continue;
            }
            if (next.equals(end)) {  // find a path with shorter path length than historical shortest path
                visitedDist[next.x][next.y] = visitedDist[stack.peekFirst().x][stack.peekFirst().y] + 1;  // update end to start shortest distance
                stack.offerFirst(next);
                shortestPath.clear();
                Deque<Entry> temp = new LinkedList<>();
                temp.addAll(stack);
                while (!temp.isEmpty()) {  // update shortest path stack
                    shortestPath.offerFirst(temp.pollFirst()); // shortest path is now reverse order
                }

                stack.pollFirst(); // pop out the end entry and continue to try to find shorter path
                continue;
            }
            // if current entry is not end but also is a valid path, push it into stack and update its distance from start
            visitedDist[next.x][next.y] = visitedDist[stack.peekFirst().x][stack.peekFirst().y] + 1;
            stack.offerFirst(next);
            if(stack.size() > getMaxFringeSize()){   // this stack stores the fringe nodes, and can get the largest size of stack
                setMaxFringeSize(stack.size());
            }
        }
    }

    private Entry getAdjacentNotVisitedEntry(Entry e, int[][] visitedDist, int[][] weightedMaze) {
        if (e.x - 1 >= 0) {  // go up
            if (visitedDist[e.x - 1][e.y] == 0 || visitedDist[e.x][e.y] + 1 < visitedDist[e.x - 1][e.y]) {  // not visited or current position is better than former paths
                return new Entry(e.x - 1, e.y, 0);
            }
        }
        if (e.x + 1 < weightedMaze.length) {  // go down
            if (visitedDist[e.x + 1][e.y] == 0 || visitedDist[e.x][e.y] + 1 < visitedDist[e.x + 1][e.y]) {
                return new Entry(e.x + 1, e.y, 0);
            }
        }
        if (e.y - 1 >= 0) {  // go left
            if (visitedDist[e.x][e.y - 1] == 0 || visitedDist[e.x][e.y] + 1 < visitedDist[e.x][e.y - 1]) {
                return new Entry(e.x, e.y - 1, 0);
            }
        }
        if (e.y +  1 < weightedMaze[0].length) {  // go right
            if (visitedDist[e.x][e.y + 1] == 0 || visitedDist[e.x][e.y] + 1 < visitedDist[e.x][e.y + 1]) {
                return new Entry(e.x, e.y + 1, 0);
            }
        }
        // no valid path to go
        return new Entry(-1, -1, -1);
    }

    private Deque<Entry> reverseStack(Deque<Entry> stack) {
        Deque<Entry> path = new LinkedList<>();
        while (!stack.isEmpty()) {
            path.offerFirst(stack.pollFirst());
        }
        return path;
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

    private static void outputMaze(int[][] maze, List<Entry> list) {
        File file = new File("src/data_visualization/DFS/dfs_maze_shortest.csv");
        BufferedWriter bw = null;
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

    private static void outputPath(List<Entry> list) {
        File file = new File("src/data_visualization/DFS/dfs_path_shortest.csv");
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            for (Entry e : list) {
                bw.write(e.x + "," + e.y + "\n");
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

    //return the length of solution path
    public int getPathLength(List<Entry> queue){
        return queue.size();
    }

    public static void main(String[] args) {
        int[][] maze = new GenerateRandomMaze().genereate();
        int[][] weightedMaze = getWeightedMaze(maze);
        SearchPathByShortestDFS dfsClass = new SearchPathByShortestDFS();
        List<Entry> res = dfsClass.getPath(weightedMaze);
        // print maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();


        // print weighted maze
        int visitedCount = 0;
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                if (weightedMaze[i][j] > 0) {
                    visitedCount++;
                }
                System.out.print(weightedMaze[i][j] + " ");
            }
            System.out.println();
        }

        // fail to find the path if stack is empty
        if (res.isEmpty()) {
            System.out.println();
            System.out.println("Fail to find the path.");
        }
        System.out.println();
        for (Entry e : res) {
            System.out.print(e.toString() + " ");
        }

        // path visualization
        outputMaze(maze, res);
        outputPath(res);

        int pathLength = dfsClass.getPathLength(res);
        int maxFringeSize = dfsClass.getMaxFringeSize();
        System.out.println("\n" + "The length of solution path is: " + pathLength);
        System.out.println("The total number of nodes expanded is: "+ visitedCount);
        System.out.println("The maximum size of fringe during runtime is: " + maxFringeSize);

        //System.out.print("Print path length: " + dfsClass.getPathLength());
    }
}