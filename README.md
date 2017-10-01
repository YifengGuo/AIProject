# Group Project of Intro to Artificial Intelligence

## Project 1
+ Path Search Algorithms
+ Genetic Algorithm for generating hard maze
### File Structure 
```java
src
├── data_visualization
│   ├── Astar
│   │   ├── astar_euc.m
│   ├── BFS
│   │   └── bfs_script.m
│   └── DFS
│       └── dfs_script.m
├── maze_generation
│   └── GenerateRandomMaze.java 			// generate random mazes
└── search_algorithm
    ├── BFSLengthOfSolutionPathReturn.java	// GA solving BFS condition 1
    ├── BFSMaximumSizeOfFringe.java			// GA solving BFS condition 2
    ├── BFSTotalNodesOfExpanded.java		// GA solving BFS condition 3
    ├── FindHardMazeByAstar.java			// GA solving Astar using Euclidean 
    ├── FindHardMazeByAstar_man.java		// GA solving Astar using Manhattan
    ├── FindHardMazeByBFS.java
    ├── GeneticAlgorithm.java
    ├── GeneticAlgorithmScalar.java			// GA solving DFS 
    ├── SearchPathByAstar_Euclidean.java	// Path searching by Astar(Euclidean)
    ├── SearchPathByAstar_Manhattan.java	// Path searching by Astar(Manhattan)
    ├── SearchPathByBFS.java				// Path searching by BFS
    ├── SearchPathByDFS.java				// Path searching by DFS
    └── SearchPathByShortestDFS.java		// Path searching by DFS(find shortest path)
```

### How To Run

#### Path Searching & Maze Generation

1. Load project in Java IDE, e.g, Eclipse/ IntelliJ
2. Build Project
3. Run relative source file

#### Visualization

1. Open *.m file in Matlab
2. Run
3. Find GIF image in `images` folder

