package assignment.algorithm.datastructure;


import assignment.algorithm.PathFinder;
import lombok.Data;

/**
 * Data structure containing a node, it's total distance from the start and its previous.
 * <p>
 * Used by {@link PathFinder}.
 */
@Data
public class NodePriorityQueue<N> implements Comparable<NodePriorityQueue<N>> {

  private final N node;
  private int totalDistance;
  private NodePriorityQueue<N> previous;

  public NodePriorityQueue(N node, int totalDistance, NodePriorityQueue<N> previous) {
    this.node = node;
    this.totalDistance = totalDistance;
    this.previous = previous;
  }

  @Override
  public int compareTo(NodePriorityQueue<N> other) {
    return Integer.compare(this.totalDistance, other.totalDistance);
  }
}
