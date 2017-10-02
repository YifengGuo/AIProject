package search_algorithm;

import maze_generation.GenerateRandomMaze;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.io.File;

/**
 * @author yifengguo
 */
public class SearchPathByDFS {
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

    public List<Entry> getPath(int[][] maze) {
        List<Entry> res = new ArrayList<>();
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return res;
        }
        Deque<Entry> stack = new LinkedList<>();
        boolean[][] visited = new boolean[maze.length][maze.length];
        Entry start = new Entry(0, 0, 1);
        Entry end = new Entry(maze.length - 1, maze.length - 1, 1);
        searchPath(stack, maze, visited, start, end);
        // reverse order to get path from start to end
        Deque<Entry> path = reverseStack(stack);
        while (!path.isEmpty()) {
            res.add(path.pollFirst());
        }
        // track visited matrix
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited.length; j++) {
                System.out.print(visited[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
        return res;
    }

    private void searchPath(Deque<Entry> stack, int[][] maze, boolean[][] visited, Entry start, Entry end) {
        stack.offerFirst(start);
        visited[start.x][start.y] = true;
        while (!stack.isEmpty() && (!stack.peekFirst().equals(end))) {
            Entry next = getAdjacentNotVisitedEntry(stack.peekFirst(), visited, maze);
            if (next.x != -1) {
                visited[next.x][next.y] = true;
                stack.offerFirst(next);
            } else {  // backtracking if current stack.peek() has no choice to go
                stack.pop();
                continue;
            }
        }
    }

    private Entry getAdjacentNotVisitedEntry(Entry e, boolean[][] visited, int[][] maze) {
        if (e.x - 1 >= 0 && visited[e.x - 1][e.y] == false && maze[e.x - 1][e.y] == 1) {  // go up
            return new Entry(e.x - 1, e.y, 1);
        }
        else if (e.x + 1 < maze.length && visited[e.x + 1][e.y] == false && maze[e.x + 1][e.y] == 1) {  // go down
            return new Entry(e.x + 1, e.y, 1);
        }
        else if (e.y - 1 >= 0 && visited[e.x][e.y - 1] == false && maze[e.x][e.y - 1] == 1) {  // go left
            return new Entry(e.x, e.y - 1, 1);
        }
        else if (e.y + 1 < maze[0].length && visited[e.x][e.y + 1] == false && maze[e.x][e.y + 1] == 1) {  // go right
            return new Entry(e.x, e.y + 1, 1);
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

    private static void outputMaze(int[][] maze, List<Entry> list) {
        File file = new File("src/data_visualization/DFS/dfs_maze.csv");
        BufferedWriter bw = null;
        // change path cell to 2
        for (Entry e : list) {
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

    private static void outputPath(List<Entry> list) {
        File file = new File("src/data_visualization/DFS/dfs_path.csv");
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

    public static void main(String[] args) {
        int[][] maze = new GenerateRandomMaze().generate();
        List<Entry> res = new SearchPathByDFS().getPath(maze);
        // print maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j] + " ");
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
    }
}
