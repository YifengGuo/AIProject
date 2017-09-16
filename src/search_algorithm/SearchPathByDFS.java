package search_algorithm;

import maze_generation.GenerateRandomMaze;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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
    }

    public List<Entry> getPath(int[][] maze) {
        List<Entry> res = new ArrayList<>();
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return res;
        }
        Deque<Entry> stack = new LinkedList<>();
        boolean[][] visited = new boolean[maze.length][maze.length];
        Entry start = new Entry(0, 0, 0);
        Entry end = new Entry(maze.length - 1, maze.length - 1, 0);
        searchPath(stack, maze, visited, start, end);
        if (!stack.isEmpty()) {
            stack.offerFirst(end);
        }
        Deque<Entry> path = reverseStack(stack);
        while (!path.isEmpty()) {
            res.add(path.pollFirst());
        }
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited.length; j++) {
                System.out.print(visited[i][j] + " ");
            }
            System.out.println();
        }
        return res;
    }

    private void searchPath(Deque<Entry> stack, int[][] maze, boolean[][] visited, Entry start, Entry end) {
        stack.offerFirst(start);
        visited[start.x][start.y] = true;
        while (!stack.isEmpty() && (stack.peekFirst().x != end.x && stack.peekFirst().y != end.y)) {
            Entry next = getAdjacentNotVisitedEntry(stack.peekFirst(), visited, maze);
            if (next.x != -1) {
                stack.offerFirst(next);
                visited[next.x][next.y] = true;
            } else {  // backtracking
                stack.pollFirst();
                continue;
            }
        }
    }

    private Entry getAdjacentNotVisitedEntry(Entry e, boolean[][] visited, int[][] maze) {
        if (e.x - 1 >= 0 && visited[e.x - 1][e.y] == false && maze[e.x - 1][e.y] == 0) {  // go up
            return new Entry(e.x - 1, e.y, 0);
        }
        if (e.x + 1 < maze.length && visited[e.x + 1][e.y] == false && maze[e.x + 1][e.y] == 0) {  // go down
            return new Entry(e.x + 1, e.y, 0);
        }
        if (e.y - 1 >= 0 && visited[e.x][e.y - 1] == false && maze[e.x][e.y - 1] == 0) {  // go left
            return new Entry(e.x, e.y - 1, 0);
        }
        if (e.y + 1 < maze[0].length && visited[e.x][e.y + 1] == false && maze[e.x][e.y + 1] == 0) {  // go right
            return new Entry(e.x, e.y + 1, 0);
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

    public static void main(String[] args) {
        int[][] maze = new GenerateRandomMaze().genereate();
//        int[][] maze= new int[][]{
//                {0,0,0,0,0},
//                {0,1,0,1,0},
//                {0,1,1,0,0},
//                {0,1,1,0,1},
//                {0,0,0,0,0}
//        };
        List<Entry> res = new SearchPathByDFS().getPath(maze);
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        for (Entry e : res) {
            System.out.print(e.toString() + " ");
        }
    }
}
