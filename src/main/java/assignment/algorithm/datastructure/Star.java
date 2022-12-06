package assignment.algorithm.datastructure;


import lombok.Builder;
import lombok.Data;

/**
 * This class in representing a Star in the graph.
 * <p>
 * Used by {@link assignment.algorithm.PathFinder}
 */
@Data
@Builder
public class Star {

  private final String id;

  private final String name;
}
