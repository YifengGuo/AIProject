package search_algorithm;


import maze_generation.GenerateRandomMaze;

import java.util.*;

/**
 *  @author Yunfan xu
 *
 */
public class SearchPathByBFS {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[][] marked;
    //private int[][][] edgeTo;
    private int[][] distTo;
    class Node {
        int x;
        int y;
        Node parent;

        public Node(int x, int y){
            this.x = x;
            this.y = y;
        }
        public Node(int x, int y, Node parent){
            this.x = x;
            this.y = y;
            this.parent = parent;
        }
    }

    public SearchPathByBFS(int[][] maze){
        marked = new boolean[maze.length][maze.length];
        distTo = new int[maze.length][maze.length];
       // edgeTo = new int[maze.length][maze.length][2];  // edgeTo is to store the x and y in to 2d array;

    }

    private void bfs(int[][] maze, Node start, Node end){
        Queue<Node> queue = new LinkedList<Node>();
        marked = new boolean[maze.length][maze.length];
        distTo = new int[maze.length][maze.length];

        for(int i = 0; i < maze.length ;i++){
            for(int j = 0; j<maze.length ; j++) {
                distTo[i][j] = INFINITY;
                marked[i][j] = false;
            }
        }
        distTo[start.x][start.y] = 0;
        marked[start.x][start.y] = true;
        queue.add(start);

        while(!queue.isEmpty()) {
            Node v = queue.poll();
            for (Node w : getAdjacentNotmarkedNode(v, marked, maze)) {
                if(!marked[w.x][w.y]){
                    w.parent = v;
                    distTo[w.x][w.y] = distTo[v.x][v.y] + 1;
                    marked[w.x][w.y] =true;
                    if((w.x == end.x) && (w.y == end.y)){
                        end = w;
                    }
                    queue.add(w);
                }

            }
        }

        if(!hasPath(end)){
            System.out.println("Fail to find the path.");
        }
        else{
            System.out.print("( "+start.x+" , "+start.y+" )");
            Stack<Node> temp = pathTo(end);
            while(!temp.empty()){
                Node a = temp.pop();
                System.out.print(" ( "+a.x+" , "+a.y+" )");
            }

            }



    }

    public boolean hasPath(Node v){
        return marked[v.x][v.y];
    }


    public Node setStart(int x, int y){
        return new Node(x,y);
    }

    public Node setEnd(int x, int y){
        return new Node(x,y);
    }

    public Stack<Node> pathTo(Node v){
        if(!hasPath(v)){
            return null;
        }
        Stack<Node> path = new Stack<Node>();
        while(v.parent != null){
            path.add(v);
            v = v.parent;
        }
        return path;
    }

    private Queue<Node> getAdjacentNotmarkedNode(Node e, boolean[][] marked, int[][] maze) {
       Queue<Node> adj = new LinkedList<Node>();
        if (e.x - 1 >= 0 && marked[e.x - 1][e.y] == false && maze[e.x - 1][e.y] == 1) {  // go up
            adj.add(new Node(e.x - 1, e.y));
        }
        if (e.x + 1 <= maze.length - 1 && marked[e.x + 1][e.y] == false && maze[e.x + 1][e.y] == 1) {  // go down
            adj.add(new Node(e.x + 1, e.y));
        }
        if (e.y - 1 >= 0 && marked[e.x ][e.y - 1] == false && maze[e.x][e.y - 1] == 1) {  // go left
            adj.add(new Node(e.x, e.y - 1));
        }
        if (e.y + 1 <= marked.length -1 && marked[e.x ][e.y + 1] == false && maze[e.x][e.y + 1] == 1) {  // go right
            adj.add(new Node( e.x , e.y + 1));
        }
        return adj;

    }

    public static void main(String[] args){
        int[][] maze = new GenerateRandomMaze().genereate();
        // print maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }

       SearchPathByBFS bfs = new SearchPathByBFS(maze);
        bfs.bfs(maze,                                                  //set the maze
                bfs.setStart(0 , 0),                            //set the start point
                bfs.setEnd(maze.length - 1, maze.length - 1)    //set the end point
        );



    }
}