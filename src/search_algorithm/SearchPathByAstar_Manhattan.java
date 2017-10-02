package search_algorithm;

import maze_generation.GenerateRandomMaze;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * @author Allen Ni
 */
public class SearchPathByAstar_Manhattan {
    private static int maxFringeSize = 0;
    private static int expandedNodes = 0;
    private static int pathSize;

    static class Entry {
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

        public boolean inMaze(int maze_x, int maze_y) {
        	if (this.x >= maze_x || this.x < 0 || this.y >= maze_y || this.y < 0) {
        		return false;
        	}
        	return true;
        }

        public List<Entry> adjacentNodes(int[][] maze) {
        	List<Entry> nodes = new ArrayList<Entry>();
        	if (this.x - 1 >= 0 && maze[this.x - 1][this.y] == 1) {
        		// up node
        		nodes.add(new Entry(this.x - 1, this.y, 1));
        	}
        	if (this.x + 1 < maze.length && maze[this.x + 1][this.y] == 1) {
        		// down node
        		nodes.add(new Entry(this.x + 1, this.y, 1));
        	}
        	if (this.y - 1 >= 0 && maze[this.x][this.y - 1] == 1) {
        		// left node
        		nodes.add(new Entry(this.x, this.y - 1, 1));
        	}
        	if (this.y + 1 < maze.length && maze[this.x][this.y + 1] == 1) {
        		// left node
        		nodes.add(new Entry(this.x, this.y + 1, 1));
        	}
        	return nodes;
        }

        public boolean isAdjacentTo(Entry e) {
			if ((this.x == e.x && Math.abs(this.y - e.y) == 1) || (this.y == e.y && Math.abs(this.x - e.x) == 1)){
                return true;
            }
            return false;
        }
    }

    class EntryWithValue implements Comparable<EntryWithValue> {
    	private Entry e;
    	private Double score;
    	public EntryWithValue(Entry e, double score) {
    		this.e = e;
    		this.score = score;
    	}
    	public Double getScore() {
    		return this.score;
    	}
        public Entry getEntry() {
            return this.e;
        }

    	@Override
    	public int compareTo(EntryWithValue other) {
    		return this.score.compareTo(other.getScore());
    	}
    }

    public List<Entry> getPath(int[][] maze, Entry start, Entry end) {
    	List<Entry> res = new ArrayList<>();
        if (maze == null || maze.length == 0 || maze[0].length == 0) {
            return res;
        }
        /* check if start and end is reasonable*/
        if (!start.inMaze(maze[0].length, maze.length) || !end.inMaze(maze[0].length, maze.length)) {
        	return res;
        }
        Stack<Entry> path = searchPath(maze, start, end);
        // transfer stack to list
        while (!path.isEmpty()) {
            res.add(path.pop());
        }
		return res;
    }

    private Stack<Entry> searchPath(int[][] maze, Entry start, Entry end) {
    	// open list - available entries
    	HashMap<Entry, Double> openList = new HashMap<Entry, Double>();
    	// Add start to open list
    	openList.put(start, 0.0);
    	// close list - visited entries
    	HashMap<Entry, Double> closeList = new HashMap<Entry, Double>();
        // keep track
        HashMap<Entry, Entry> traceList = new HashMap<Entry, Entry>();
    	// trace stack
    	Stack<Entry> trace = new Stack<Entry>();
        // trace.push(start);
    	PriorityQueue<EntryWithValue> pq = new PriorityQueue<EntryWithValue>();
    	while (!openList.isEmpty() && !openList.containsKey(end)) {
    		List<Entry> open = new ArrayList<Entry>(openList.keySet());
    		expandedNodes += open.size();
    		for (Entry current : open) {
    			if (current.adjacentNodes(maze).size() == 0) {
    				openList.remove(current);
    				closeList.put(current, 0.0);
					continue;
    			}
    			// get adjacent node and calculate score
    			double g = openList.get(current);
    			for (Entry adj: current.adjacentNodes(maze)) {
    				if (!closeList.containsKey(adj)) {
	    				// add adjcent node to open list
    					if (!openList.containsKey(adj)) {
	    					traceList.put(adj, current);
                            openList.put(adj, g+1);
                            
    					} else {
    						// update G score if node already exists in openList
    						if (openList.get(adj) > g+1) {
                                traceList.put(adj, current);
                                openList.put(adj, g+1);
                                
    						}
    					}

    					// add node to priority queue
	    				double score = calculateScore(adj, openList.get(adj) ,end);
	    				pq.add(new EntryWithValue(adj, score));
    				}
    			}
    			// move current node from open list to close list
    			closeList.put(current, openList.get(current));
    			openList.remove(current);
    			// update max fringe size
                if (openList.size() > maxFringeSize)
                    maxFringeSize = openList.size();
        		
    		}    		
    	}
        // Trace back
        if (openList.containsKey(end)) {
            Entry pathNode = end;
            while (!pathNode.equals(start)) {
                trace.push(pathNode);
                pathNode = traceList.get(pathNode);
            }
            trace.add(start);            
        }
        return trace;
    }

    public double calculateScore(Entry start, double g, Entry end) {
    	double h = 0; /* heuristic score */
    	// calculate Euclidean Distance
    	h = Math.abs(end.x - start.x) + Math.abs(end.y - start.y);
    	return g + h;
    }

    private static void outputMaze(int[][] maze, List<Entry> list) {
        File file = new File("src/data_visualization/Astar/Astar_man_maze_shortest.csv");
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
        File file = new File("src/data_visualization/Astar/Astar_man_path_shortest.csv");
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

    public static int getExpandedNodes() {
        return expandedNodes;
    }

    public static int getMaxSizeOfFringe() {
        return maxFringeSize;
    }
    public static int getPathLength() {
        return pathSize;
    }

    public static void main(String[] args) {
        int[][] maze = new GenerateRandomMaze().generate();
        // print maze
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze.length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.println();
        }
        Entry start = new Entry(0, 0, 1);
        Entry end = new Entry(maze.length - 1, maze.length - 1, 1);
        List<Entry> res = new SearchPathByAstar_Manhattan().getPath(maze, start, end);
        pathSize = res.size();
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

        System.out.println(getExpandedNodes());
        System.out.println(getMaxSizeOfFringe());
        System.out.println(getPathLength());
    }
}