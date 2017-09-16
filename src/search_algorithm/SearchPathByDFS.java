//package search_algorithm;
//
//import maze_generation.GenerateRandomMaze;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author yifengguo
// */
//public class SearchPathByDFS {
//    class Entry {
//        int x;
//        int y;
//        int value;
//        public Entry(int x, int y, int value) {
//            this.x = x;
//            this.y = y;
//            this.value = value;
//        }
//
//        @Override
//        public String toString() {
//            return "(" + x + "," + y + ")";
//        }
//    }
//    public List<List<Entry>> searchPath(int[][] maze) {
//        List<List<Entry>> res = new ArrayList<>();
//        if (maze == null || maze.length == 0 || maze[0].length == 0) {
//            return res;
//        }
//        List<Entry> plan = new ArrayList<>();
//        dfs(res, plan, maze, 0, 0);
//
//        return res;
//    }
//
//    private void dfs(List<List<Entry>> res, List<Entry> plan, int[][] maze, int x, int y) {
//        if (x == maze.length - 1 && y == maze.length - 1) {
//            plan.add(new Entry(x, y, maze[x][y]));
//            res.add(new ArrayList<>(plan));
//            return;
//        }
//
////        for (int i = x; i < maze.length; i++) {
////            for (int j = y; j < maze[0].length; j++) {
////                if (maze[x][y] == 0) {
////                    plan.add(new Entry(x, y, maze[x][y]));
////                    if (validCoordinate(x + 1, y, maze)) {  // down
////                        dfs(res, plan, maze, x + 1, y);
////                        //plan.remove(plan.get(plan.size() - 1));
////                    }
////                    else if (validCoordinate(x - 1, y, maze)) { // up
////                        dfs(res, plan, maze, x - 1, y);
////                        //plan.remove(plan.get(plan.size() - 1));
////                    }
////                    else if (validCoordinate(x, y + 1, maze)) { // right
////                        dfs(res, plan, maze, x, y + 1);
////                        //plan.remove(plan.get(plan.size() - 1));
////                    }
////                    else if (validCoordinate(x, y - 1, maze)) {  // left
////                        dfs(res, plan, maze, x, y - 1);
////                        //plan.remove(plan.get(plan.size() - 1));
////                    }
////                    else {
////                        return;
////                    }
////                }
////            }
////        }
//    }
//
//    private boolean validCoordinate(int x, int y, int[][] maze) {
//        if (x >= 0 && x < maze.length && y >= 0 && y < maze.length) {
//            return true;
//        }
//        return false;
//    }
//
//    public static void main(String[] args) {
//        int[][] maze = new GenerateRandomMaze().genereate();
//        List<List<Entry>> res = new SearchPathByDFS().searchPath(maze);
//        for (List<Entry> plan : res) {
//            for (Entry e : plan) {
//                System.out.print(e.toString());
//            }
//        }
////        for (int i = 0; i < maze.length; i++) {
////            for (int j = 0; j < maze.length; j++) {
////                System.out.print(maze[i][j] + " ");
////            }
////            System.out.println();
////        }
//    }
//}
