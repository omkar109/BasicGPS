// --== CS400 File Header Information ==--
// Name: Omkar Kendale
// Email: kendale@wisc.edu
// Group and Team: DK, red
// Group TA: Yuye Jiang
// Lecturer: Florain Heimerl
// Notes to Grader: <optional extra notes>

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class extend the functionality of Dikstra's graph by adding method to calculate path and cost
 * with a stop at a node. It also provides a depth first traversal
 * @author omkar
 *
 * @param <NodeType> The data type each node holds
 * @param <EdgeType> The data type of the weight of the edge
 */
public class GpsAE<NodeType, EdgeType extends Number> extends DijkstraGraph<NodeType, EdgeType> 
implements AlgorithmEngineerInterface<NodeType, EdgeType>{

  /**
   * This method creates a list of nodes representing the shortest path from one node to another with a
   * forced stop at a certain node
   * 
   * @param start the node to start the path at
   * @param end the node to end the path at
   * @param stop the node to stop at inbetween the path
   * 
   * @return the list containg nodes representing the shortest path
   */
  @Override
  public List<NodeType> shortestPathWithStop(NodeType start, NodeType end, NodeType stop) {
    //Finds shortest path from start to stop
    LinkedList<NodeType> firstPath = (LinkedList<NodeType>) shortestPathData(start, stop);
    //Finds shortest path from stop to end
    LinkedList<NodeType> secondPath = (LinkedList<NodeType>) shortestPathData(stop, end);
    //Connects the two paths
    for(int i = 1;i < secondPath.size(); i++) {
      firstPath.add(secondPath.get(i));
    }
    return firstPath;
  }
  
  /**
   * This method calculates the cost of the shortest path between two nodes with a forced stop at a certain node
   * 
   * @param start the node to start the path at
   * @param end the node to end the path at
   * @param stop the node the path stops at in between start and end
   * 
   * @return the cost of the path from start to stop to end
   */
  @Override
  public double shortestPathWithStopCost(NodeType start, NodeType end, NodeType stop) {
    return shortestPathCost(start, stop) + shortestPathCost(stop, end);
  }

  /**
   * This method performs a depth first traversal on the graph structure and also outputs the number of nodes
   * that exist in the graph but can't be reached through a path
   * 
   * @param start the node where we want to start the traversal
   * @throws NoSuchElementException if the given data value doesn't exist in the graph
   * @return a string representing the depth first traversal in the following format where X represents a node and
   *         Z represents the number of unreachable nodes:
   *         "X X ... X
   *          There are Z nodes that are in this graph but can't be reached through this traversal"
   */
  @Override
  public String depthFirstTraversal(NodeType start) throws NoSuchElementException{
    
    //If the start node isn't in this graph throw an exception
    if(!nodes.containsKey(start)) {
      throw new NoSuchElementException("this value doesn't exist in the graph");
    }
    
    LinkedList<NodeType> visitedNodes = new LinkedList<NodeType>();
    String output = "";
    //Mark first node as visited
    visitedNodes.add(start);
    //Begin traversal from first node
    output += depthFirstHelper(start, visitedNodes);
    //Calculates number of unreachable nodes
    int unconnectedNodes = nodes.size() - visitedNodes.size();
    output += "/nThere are " + unconnectedNodes + " nodes that are in this graph but can't be reached through this traversal";
    
    return output;
  }
  
  private String depthFirstHelper(NodeType start, LinkedList<NodeType> visitedNodes) {
    String output = "";
    output += start.toString() + " ";
    //Iterate through all neighbors of this node
    for(int i = 0; i < nodes.get(start).edgesLeaving.size(); i++) {
      Edge edge = nodes.get(start).edgesLeaving.get(i);
      //If this neighbor hasn't been visited
      if(!visitedNodes.contains(edge.successor.data)) {
        //Mark current neighbor as visited
        visitedNodes.add(edge.successor.data); 
        //Begin traversal on neighbor's neighbors 
        output += depthFirstHelper(edge.successor.data, visitedNodes);
      }
    }
    return output;
  }
  
  public NodeType get(NodeType toGet) throws NoSuchElementException{
    Enumeration<NodeType> e = nodes.keys();
    while(e.hasMoreElements()) {
      NodeType key = e.nextElement();
      if(key.toString().trim().equals(toGet.toString())) {
        return key;
      }
    }
    throw new NoSuchElementException("That element doesn't exist in the graph");
  }

}
