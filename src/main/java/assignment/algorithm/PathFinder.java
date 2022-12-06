package assignment.algorithm;

import assignment.algorithm.datastructure.NodePriorityQueue;
import assignment.algorithm.datastructure.Route;
import assignment.algorithm.datastructure.Star;
import com.google.common.graph.ValueGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is mainly using DFS algorithm for find all paths and cycles in a graph. For the
 * shortest path that is not having cycle I used Dijkstra algorithm. For the shortest path that has
 * a cycle, I looked for the cycles and sort the results asc and get the first which should be the
 * shortest path.
 */
@Slf4j
public class PathFinder {

  private final ValueGraph<Star, Integer> graph;

  public PathFinder(ValueGraph<Star, Integer> graph) {
    this.graph = graph;
  }

  /**
   * This method is implementing DFS to find all paths in the graph. It decides whether to find
   * cycles or paths based on the values of from and to.
   *
   * @param from the starting Star @Link
   * @param to   the target Star
   * @return the list of available routes between the starting and target Star
   */
  public List<Route> findPath(Star from, Star to) {
    LinkedList<Star> notVisited = new LinkedList<>(graph.nodes());
    LinkedList<Star> pathList = new LinkedList<>();
    LinkedList<Route> routes = new LinkedList<>();
    //add source to path list
    pathList.add(from);

    // Call recursive method
    if (from.equals(to)) {
      findAllCycles(from, to, notVisited, pathList, routes);
    } else {
      findAllPath(from, to, notVisited, pathList, routes);
    }
    //sort the routes based on travel time ascending
    routes.sort(Comparator.comparing(Route::getTotalTravelTime));
    return routes;
  }

  /**
   * This method implements the DFS in recursive. It travers the graph to find the routes between
   * source and target
   *
   * @param from       The source which is a Star
   * @param to         The target which is a Star
   * @param notVisited The List of not visited nodes in finding the path
   * @param path       the list of stars in the current path in the recursive call
   * @param routes     the list of Routes will be found and returned after recursive method ends.
   */
  private void findAllPath(Star from, Star to, List<Star> notVisited, List<Star> path,
      List<Route> routes) {

    if (from.equals(to)) {
      // if match found, it means we have reached to the destination.
      // We should create the Route and add it to the list of Routes
      Route route = Route.builder().path(new LinkedList<>(path))
          .totalTravelTime(calculateTotalTravelTime(path)).build();
      if (route.getTotalTravelTime() > 0) {
        routes.add(route);
        log.debug("Completed path:{}", route);
      }
      return;
    }
    // Mark the current node by removing from the notVisited list as we have seen it
    notVisited.remove(from);
    // Recur for all of the stars are exiting from the current Star (from)
    for (Star node : graph.successors(from)) {
      if (notVisited.contains(node)) {
        // It's a new node should be added to path
        path.add(node);
        findAllPath(node, to, notVisited, path, routes);
        // After recursion, it is not needed to be in the path. It's been added in other recursions
        path.remove(node);
      }
    }
    // Mark the current node by adding it to the list as it should be available for other recursions
    notVisited.add(from);
  }

  private void findAllCycles(Star from, Star to, List<Star> notVisited, List<Star> path,
      List<Route> routes) {

    // Mark the current node
    notVisited.remove(from);

    for (Star node : graph.successors(from)) {
      if (notVisited.contains(node)) {
        // store current node in the path
        path.add(node);

        findAllCycles(node, to, notVisited, path, routes);

        //remove current node in the path
        path.remove(node);

      } else {
        //if we reach her, in the recursions, it means that we have found the cycle,
        // and the result can be updated
        LinkedList<Star> stars = new LinkedList<>(path);
        stars.addLast(to);
        Route route = Route.builder().path(stars)
            .totalTravelTime(calculateTotalTravelTime(new ArrayList<>(stars))).build();
        if (route.getTotalTravelTime() > 0) {
          routes.add(route);
          log.debug("Completed path:{}", route);
        }
      }
    }
    // Mark the current node
    notVisited.add(from);
  }

  /**
   * DFS doesn't consider the weights in the path finding. We need to calculate the path total cost
   * in this method
   *
   * @param path List of stars which composed the path between source and target
   * @return the value representing the amount time which should be spent for this path travelling
   */
  private int calculateTotalTravelTime(List<Star> path) {
    Objects.requireNonNull(path, "path list should not be null");
    int sum = 0;
    if (path.size() > 0) {
      Star start = path.get(0);
      for (int i = 1; i < path.size(); i++) {
        sum = sum + graph.edgeValueOrDefault(start, path.get(i), Integer.MAX_VALUE);
        start = path.get(i);
      }
    }
    return sum;
  }

  /**
   * In order to find the shortest cycle length, I managed to find the cycles and sort them and
   * finally return the first one from the list. This solution has been selected due to the
   * time-being.
   *
   * @param from Starting star
   * @param to   Ending Star
   * @return The route which has been found to be the minimum cycle.
   */
  public Route findShortestPathInCycle(Star from, Star to) {
    List<Route> path = this.findPath(from, to);
    return path.stream().sorted().findFirst()
        .orElseThrow(() -> new IllegalStateException("there is not any route"));
  }

  /**
   * This is the implementation of Dijkstra algorithm for finding the shortest path.
   *
   * @param source The Star we start from
   * @param target The Star we should reach.
   * @return The Route we found as having the minimum cost
   */
  public Route findShortestPath(Star source, Star target) {
    Map<Star, NodePriorityQueue<Star>> nodeWrappers = new HashMap<>();
    PriorityQueue<NodePriorityQueue<Star>> queue = new PriorityQueue<>();
    Set<Star> shortestPathFound = new HashSet<>();

    // Add source to queue
    NodePriorityQueue<Star> sourceWrapper = new NodePriorityQueue<>(source, 0, null);
    nodeWrappers.put(source, sourceWrapper);
    queue.add(sourceWrapper);

    while (!queue.isEmpty()) {
      NodePriorityQueue<Star> nodeWrapper = queue.poll();
      Star node = nodeWrapper.getNode();
      shortestPathFound.add(node);

      // We have reached the target. Then, we can build our Route
      if (node.equals(target)) {
        return buildPath(nodeWrapper);
      }

      // We should iterate over all neighbours
      for (Star neighbor : graph.successors(node)) {
        // if we found the neighbor in path, we would ignore it
        if (shortestPathFound.contains(neighbor)) {
          continue;
        }

        // The cost of travelling should be calculated here.
        int distance = graph.edgeValueOrDefault(node, neighbor, Integer.MAX_VALUE);
        if (distance == Integer.MAX_VALUE) {
          continue;
        }
        int totalDistance = nodeWrapper.getTotalDistance() + distance;

        // checking the neighbors
        NodePriorityQueue<Star> neighborWrapper = nodeWrappers.get(neighbor);
        if (neighborWrapper == null) {
          neighborWrapper = new NodePriorityQueue<>(neighbor, totalDistance, nodeWrapper);
          nodeWrappers.put(neighbor, neighborWrapper);
          queue.add(neighborWrapper);
        }
        // If we found the neighbor and its cost is less we should update the queue
        else if (totalDistance < neighborWrapper.getTotalDistance()) {
          neighborWrapper.setTotalDistance(totalDistance);
          neighborWrapper.setPrevious(nodeWrapper);

          // This is a priority queue. We have to delete and re-add it in order to change the position
          // we have to remove and reinsert the node
          queue.remove(neighborWrapper);
          queue.add(neighborWrapper);
        }
      }
    }

    // We couldn't build a Route
    return null;
  }

  /**
   * Builds the Route coming from the Dijkstra algorithm.
   *
   * @param nodeWrapper The node data structure which we have completed the algorithm with it.
   * @return the converted Route object
   */
  private Route buildPath(NodePriorityQueue<Star> nodeWrapper) {
    LinkedList<Star> path = new LinkedList<>();
    while (nodeWrapper != null) {
      path.add(nodeWrapper.getNode());
      nodeWrapper = nodeWrapper.getPrevious();
    }
    Collections.reverse(path);
    return Route.builder().path(path).totalTravelTime(calculateTotalTravelTime(path)).build();
  }
}
