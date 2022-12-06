package assignment.algorithm.datastructure;

import java.util.LinkedList;
import lombok.Builder;
import lombok.Data;

/**
 * A route consists of a list of Stars which exist in path and aggregation of weights by travelling
 * from a Star to another projected in the totalTravelTime. It implements {@link Comparable} to
 * enable sort functions
 */
@Builder
@Data
public class Route implements Comparable<Route> {

  LinkedList<Star> path;
  int totalTravelTime;

  public int getPathStops() {
    return path.size() - 1;
  }

  public int getIntermediatePathStops() {
    return Math.max(path.size() - 2, 0);
  }

  @Override
  public int compareTo(Route other) {
    return Integer.compare(this.getTotalTravelTime(), other.getTotalTravelTime());
  }
}
