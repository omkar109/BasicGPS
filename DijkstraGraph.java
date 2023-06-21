// --== CS400 File Header Information ==--
// Name: Omkar Kendale
// Email: kendale@wisc.edu
// Group and Team: DK, red
// Group TA: Yuye Jiang
// Lecturer: Florain Heimerl
// Notes to Grader: <optional extra notes>

import java.util.PriorityQueue;
import java.util.Hashtable;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes.  This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
    extends BaseGraph<NodeType,EdgeType>
    implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph.  The final node in this path is stored in it's node
     * field.  The total cost of this path is stored in its cost field.  And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in it's node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;
        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }
        public int compareTo(SearchNode other) {
            if( cost > other.cost ) return +1;
            if( cost < other.cost ) return -1;
            return 0;
        }
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations.  The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *         or when either start or end data do not correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {

     
      //If the node containing start or end don't exist, throw an exception
      if(!containsNode(start) || !containsNode(end)) {
        throw new NoSuchElementException("Either the inputted start or end node doesn't exist");
      }
      
      //Creates the hashtable and priority queue to be used
      PriorityQueue<SearchNode> pq = new PriorityQueue<SearchNode>();
      Hashtable<Node, SearchNode> visitedNodes = new Hashtable<Node, SearchNode>();
      
      //Add the first node of the path to the priority queue and add it to the visitedNodes hashtable
      SearchNode firstNode = new SearchNode(nodes.get(start), 0, null);
      pq.add(firstNode);
      
      //While the priority queue still has edges to look at
      while(pq.size() != 0) {
        //remove and store first element in queue
        SearchNode current = pq.poll();
        //Check if the current node hasn't been visited
        if(!visitedNodes.containsKey(current.node)) {
          visitedNodes.put(current.node, current);
          //For each node connected to current node, add it to the priority queue if not visited
          for(Edge edge : current.node.edgesLeaving) {
            if(!visitedNodes.containsKey(edge.successor)) {
              pq.add(new SearchNode(edge.successor, edge.data.doubleValue() + current.cost, current));
            }
          }
        }
      }
      //After all connected nodes are visited, check if the end node was visited, and if not, there is no
      //valid path
      if(!visitedNodes.containsKey(nodes.get(end))) {
        throw new NoSuchElementException("No valid path from start to end");
      }
      
        return visitedNodes.get(nodes.get(end));
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value.  This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path.  This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
      //Get the search node at the end of the path
      SearchNode currSearchNode = computeShortestPath(start, end);
      LinkedList<NodeType> path = new LinkedList<NodeType>();

      //keep getting predecessors of nodes in the path until you reach start node
      while(!currSearchNode.node.data.equals(start)) {
        path.addFirst(currSearchNode.node.data);
        currSearchNode = currSearchNode.predecessor;
      }
      
      path.addFirst(start);
      
        return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data.  This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        return computeShortestPath(start, end).cost;
    }
    
//    /**
//     * Tests the dijkstra's algorithm implementation of the example covered in lecture and ensures the cost
//     * and sequence are what we expect. Tested with the starting node 'A'
//     */
//    @Test
//    public void lectureExampleResultsTest() {
//      DijkstraGraph<Character, Integer> tester = new DijkstraGraph<Character, Integer>();
//      //Creates graph
//      tester.insertNode('A');
//      tester.insertNode('B');
//      tester.insertNode('C');
//      tester.insertNode('D');
//      tester.insertNode('E');
//      tester.insertNode('F');
//      tester.insertNode('G');
//      tester.insertNode('H');
//      tester.insertEdge('A', 'B', 4);
//      tester.insertEdge('A', 'C', 2);
//      tester.insertEdge('A', 'E', 15);
//      tester.insertEdge('B', 'D', 1);
//      tester.insertEdge('C', 'D', 5);
//      tester.insertEdge('B', 'E', 10);
//      tester.insertEdge('D', 'E', 3);
//      tester.insertEdge('D', 'F', 0);
//      tester.insertEdge('F', 'D', 2);
//      tester.insertEdge('F', 'H', 4);
//      tester.insertEdge('G', 'H', 4);
//      
//      //Test path between A and A
//      Assertions.assertEquals(tester.shortestPathCost('A', 'A'), 0);
//      
//      //Tests path between A and B
//      Assertions.assertEquals(tester.shortestPathCost('A', 'B'), 4);
//      LinkedList<Character> list = (LinkedList<Character>) tester.shortestPathData('A', 'B');
//      Assertions.assertEquals(list.get(0), 'A');
//      
//      //Tests path between A and C
//      Assertions.assertEquals(tester.shortestPathCost('A', 'C'), 2);
//      list = (LinkedList<Character>) tester.shortestPathData('A', 'C');
//      Assertions.assertEquals(list.get(0), 'A');
//      Assertions.assertEquals(list.get(1), 'C');
//      
//      //Tests path between A and D
//      Assertions.assertEquals(tester.shortestPathCost('A', 'D'), 5);
//      list = (LinkedList<Character>) tester.shortestPathData('A', 'D');
//      Assertions.assertEquals(list.get(0), 'A');
//      Assertions.assertEquals(list.get(1), 'B');
//      Assertions.assertEquals(list.get(2), 'D');
//      
//      //Tests path between A and E
//      Assertions.assertEquals(tester.shortestPathCost('A', 'E'), 8);
//      list = (LinkedList<Character>) tester.shortestPathData('A', 'E');
//      Assertions.assertEquals(list.get(0), 'A');
//      Assertions.assertEquals(list.get(1), 'B');
//      Assertions.assertEquals(list.get(2), 'D');
//      Assertions.assertEquals(list.get(3), 'E');
//      
//      //Tests path between A and F
//      Assertions.assertEquals(tester.shortestPathCost('A', 'F'), 5);
//      list = (LinkedList<Character>) tester.shortestPathData('A', 'F');
//      Assertions.assertEquals(list.get(0), 'A');
//      Assertions.assertEquals(list.get(1), 'B');
//      Assertions.assertEquals(list.get(2), 'D');
//      Assertions.assertEquals(list.get(3), 'F');
//      
//      //Tests path between A and H
//      Assertions.assertEquals(tester.shortestPathCost('A', 'H'), 9);
//      list = (LinkedList<Character>) tester.shortestPathData('A', 'H');
//      Assertions.assertEquals(list.get(0), 'A');
//      Assertions.assertEquals(list.get(1), 'B');
//      Assertions.assertEquals(list.get(2), 'D');
//      Assertions.assertEquals(list.get(3), 'F');
//      Assertions.assertEquals(list.get(4), 'H');
//      
//      //Makes sure no path is found between A and G
//      try {
//        tester.shortestPathCost('A', 'G');
//        Assertions.fail();
//      }
//      catch(NoSuchElementException e1) {
//        
//      }
//      
//      
//    }
//    
//    /**
//     * Tests the dijkstra's algorithm implementation of the example covered in lecture and ensures the cost
//     * and sequence are what we expect. Tested with various different starting nodes besides 'A'
//     */
//    @Test
//    public void lectureExamplediffStartNode() {
//      DijkstraGraph<Character, Integer> tester = new DijkstraGraph<Character, Integer>();
//      //Creates the graph nodes and edges
//      tester.insertNode('A');
//      tester.insertNode('B');
//      tester.insertNode('C');
//      tester.insertNode('D');
//      tester.insertNode('E');
//      tester.insertNode('F');
//      tester.insertNode('G');
//      tester.insertNode('H');
//      tester.insertEdge('A', 'B', 4);
//      tester.insertEdge('A', 'C', 2);
//      tester.insertEdge('A', 'E', 15);
//      tester.insertEdge('B', 'D', 1);
//      tester.insertEdge('C', 'D', 5);
//      tester.insertEdge('B', 'E', 10);
//      tester.insertEdge('D', 'E', 3);
//      tester.insertEdge('D', 'F', 0);
//      tester.insertEdge('F', 'D', 2);
//      tester.insertEdge('F', 'H', 4);
//      tester.insertEdge('G', 'H', 4);
//      
//      //Tests path between G and H
//      Assertions.assertEquals(tester.shortestPathCost('G', 'H'), 4);
//      LinkedList<Character> list = (LinkedList<Character>) tester.shortestPathData('G', 'H');
//      Assertions.assertEquals(list.get(0), 'G');
//      Assertions.assertEquals(list.get(1), 'H');
//      
//      //Tests path between B and E
//      Assertions.assertEquals(tester.shortestPathCost('B', 'E'), 4);
//      list = (LinkedList<Character>) tester.shortestPathData('B', 'E');
//      Assertions.assertEquals(list.get(0), 'B');
//      Assertions.assertEquals(list.get(1), 'D');
//      Assertions.assertEquals(list.get(2), 'E');
//      
//      //Tests path between C and F
//      Assertions.assertEquals(tester.shortestPathCost('C', 'F'), 5);
//      list = (LinkedList<Character>) tester.shortestPathData('C', 'F');
//      Assertions.assertEquals(list.get(0), 'C');
//      Assertions.assertEquals(list.get(1), 'D');
//      Assertions.assertEquals(list.get(2), 'F');
//      
//      
//    }
//    
//    /**
//     * Tests the dijkstra's path algorithm on a self-made path where two nodes are unreachable. Ensures
//     * that a path is not generated to either node using dijkstra's
//     */
//    @Test
//    public void noDirectedEdgesPathTest() {
//      
//      //In this created graph, F is a node that is connected by a path, but in accessible because the path
//      //is directed the wrong way. E is a node that has no connections
//      
//      DijkstraGraph<Character, Integer> tester = new DijkstraGraph<Character, Integer>();
//      tester.insertNode('A');
//      tester.insertNode('B');
//      tester.insertNode('C');
//      tester.insertNode('D');
//      tester.insertNode('E');
//      tester.insertNode('F');
//      tester.insertEdge('A', 'B', 3);
//      tester.insertEdge('A', 'C', 4);
//      tester.insertEdge('B', 'C', 5);
//      tester.insertEdge('C', 'D', 7);
//      tester.insertEdge('D', 'A', 10);
//      tester.insertEdge('F', 'C', 2);
//      
//      //Makes sure there is no valid path between A and F
//      try {
//        tester.shortestPathCost('A', 'F');
//        Assertions.fail();
//      }
//      catch(NoSuchElementException e1) {
//        
//      }
//      
//      //Makes sure there is no valid path between D and E
//      try {
//        tester.shortestPathCost('D', 'E');
//        Assertions.fail();
//      }
//      catch(NoSuchElementException e1) {
//        
//      }
//      
//    }
    
    
    
}