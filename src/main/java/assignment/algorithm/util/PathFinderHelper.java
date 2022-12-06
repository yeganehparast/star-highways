package assignment.algorithm.util;

import assignment.algorithm.datastructure.PathState;
import assignment.algorithm.datastructure.Route;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinderHelper {

  private static final PathFinderHelper instance = new PathFinderHelper();

  private PathFinderHelper() {

  }

  public static PathFinderHelper getInstance() {
    return instance;
  }

  public String checkPathState(List<Route> routes, Route route) {
    return routes.contains(route) ? PathState.VALID.getMessage() : PathState.INVALID.getMessage();
  }

  public List<Route> getRoutesByMaxNumberOfStops(List<Route> routes, int stop) {
    return routes.stream().filter(route -> route.getPathStops() <= stop)
        .collect(Collectors.toUnmodifiableList());
  }

  public List<Route> getRoutesByNumberOfIntermediateStops(List<Route> routes, int stop) {
    return routes.stream().filter(route -> route.getIntermediatePathStops() == stop)
        .collect(Collectors.toUnmodifiableList());
  }

  public List<Route> getRoutesByTravelTimeLessThanLimit(List<Route> routes, int limit) {
    return routes.stream().filter(route -> route.getTotalTravelTime() <= limit)
        .collect(Collectors.toUnmodifiableList());
  }
}
