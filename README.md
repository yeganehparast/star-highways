### **Star Highways**

This is the solution for the coding challenge of Otto Payments GmbH.

The problem is to find the shortest/available paths between two stars. The star
system can be modeled as direct graph whereas different star systems are nodes,
and if there is path between two stars, it can be assumed as an edge. The travel
time between two stars, can be considered as the weight of edge. 

#### **Solution**

This problem is common in computer science and algorithms. There are several 
classic algorithms like Dijkstra, A-Star and Prime which solve this problem.

Amongst these algorithms, Dijkstra is the most common one, however it is not the
most efficient solution. The reason is that A* is applying the additional information
which can be made it more efficient in comparison to Dijkstra algorithm.

The implementation requires a data structure for graph. I have checked Guava Value Graph
, a Google core library, and seemed to provide suitable methods for 
the implementation. More information can be found at: https://github.com/google/guava/wiki/GraphsExplained




