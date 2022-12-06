package assignment.algorithm.datastructure;

/**
 * These are the states seen so far in the requirements that a path can have Used by
 * {@link assignment.algorithm.PathFinder}
 */
public enum PathState {
  VALID("PATH IS VALID"),
  INVALID("NO SUCH ROUTE");

  private final String message;

  PathState(String s) {
    this.message = s;
  }

  public String getMessage() {
    return message;
  }
}
