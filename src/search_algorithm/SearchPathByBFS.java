package search_algorithm;


import maze_generation.GenerateRandomMaze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *  @author Yunfan xu
 *
 */
public class SearchPathByBFS {
    public static final int INFINITY = Integer.MAX_VALUE;
    public boolean[][] marked;
    //public int[][][] edgeTo;
    public int[][] distTo;
    public int maxSizeOfFringe;
    public int expandedNodesNumber;

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
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
         }
     }



    public SearchPathByBFS(int[][] maze){
        marked = new boolean[maze.length][maze.length];
        distTo = new int[maze.length][maze.length];
       // edgeTo = new int[maze.length][maze.length][2];  // edgeTo is to store the x and y in to 2d array;

    }

    // return the total number of nodes expanded
    public int getExpandedNodes(){
        return expandedNodesNumber;
    }

    //return the maximum size of fringe during runtime in BFS
    public  int getMaxSizeOfFringe(){
        return maxSizeOfFringe;
    }

    //return the length of solution path
    public int getPathLength(Queue<Node> queue){
        return queue.size();
    }

    public Queue<Node> bfs(int[][] maze, Node start, Node end){
        Queue<Node> fringe = new LinkedList<>();
        Queue<Node> expandedNodes = new LinkedList<>();

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
        fringe.add(start);

        while(!fringe.isEmpty()) {

            maxSizeOfFringe = Math.max(maxSizeOfFringe, fringe.size());

            Node v = fringe.poll();
            expandedNodes.add(v);

            expandedNodesNumber = expandedNodes.size();

            for (Node w : getAdjacentNotmarkedNode(v, marked, maze)) {
                if(!marked[w.x][w.y]){
                    w.parent = v;
                    distTo[w.x][w.y] = distTo[v.x][v.y] + 1;
                    marked[w.x][w.y] =true;
                    if((w.x == end.x) && (w.y == end.y)){
                        end = w;
                        break;
                    }
                    fringe.add(w);

                }
            }
        }

        if(!hasPath(end)){
            return  null;
            //System.out.println("Fail to find the path.");
        }
        else{
            //System.out.print("( "+start.x+" , "+start.y+" )");
            Queue<Node> result = new LinkedList<Node>();
            result.add(start);
            Stack<Node> temp = pathTo(end);
            while(!temp.empty()){
                Node a = temp.pop();
                result.add(a);
              //  System.out.print(" ( "+a.x+" , "+a.y+" )");
            }
            return result;
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

    public static void outputMaze(int[][] maze, Queue<Node> queue) {
        File file = new File("src/data_visualization/BFS/bfs_maze_shortest.csv");
        BufferedWriter bw = null;
        // change path cell to 2
        while(!queue.isEmpty()){
            Node e = queue.poll();
            System.out.print("( "+e.x+" , "+e.y+" ) ");
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

    public Queue<Node> getAdjacentNotmarkedNode(Node e, boolean[][] marked, int[][] maze) {
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
        int[][] maze = new GenerateRandomMaze().generate();
        // print maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();

       SearchPathByBFS bfs = new SearchPathByBFS(maze);
        Queue<Node> shortestPath = new LinkedList<Node>();
        Queue<Node> temp = new LinkedList<Node>();

        shortestPath = bfs.bfs(maze,                                   //set the maze
                bfs.setStart(0 , 0),                            //set the start point
                bfs.setEnd(maze.length - 1, maze.length - 1)    //set the end point
        );

        if(shortestPath != null) {
            int pathLength = bfs.getPathLength(shortestPath);
            System.out.println("The length of solution path is: " + pathLength);
            System.out.println("The total number of nodes expanded is: " + bfs.getExpandedNodes() );
            System.out.println("The maximum size of fringe during runtime is: " + bfs.getMaxSizeOfFringe() );
            outputMaze(maze, shortestPath);
        }
        else{
            System.out.println("Fail to find the path.");
        }



    }
}