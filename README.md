### **Star Highways**

The problem is to find the shortest/available paths/cycles between two stars. The star
system can be modeled as direct graph whereas different star systems are nodes,
and if there is path between two stars, it can be assumed as an edge. The travel
time between two stars, can be considered as the weight of edge.

#### **Solution**

This problem is common in computer science and algorithms. There are several
classic algorithms like Dijkstra, A-Star and Prime which solve this problem for finding the shortest
path. On the other hand, the Depth-first Search (DFS) is widely used to find the available paths in
the graph. I have extended DFS to find the cycles to solve a couple of requests.

The implementation requires a data structure for graph. I have checked Guava Value Graph
, a Google core library, and seemed to provide suitable methods for
the implementation. More information can be found
at: https://github.com/google/guava/wiki/GraphsExplained

The design is following KISS paradigm. I didn't want to make things complex as the problem is
typically complex. In addition, I have followed TDD for the development.




