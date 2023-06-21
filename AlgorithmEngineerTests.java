// --== CS400 File Header Information ==--
// Name: Omkar Kendale
// Email: kendale@wisc.edu
// Group and Team: DK, red
// Group TA: Yuye Jiang
// Lecturer: Florain Heimerl
// Notes to Grader: <optional extra notes>

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This class tests the various methods in the GpsAE class
 * @author omkar
 *
 */
class AlgorithmEngineerTests {

  /**
   * This method tests integration of methods between the backend, data wrangler, and algorithm engineer 
   */
  @Test
  public void IntegrationTest1() {
    //Creates backend object
    Backend backend = new Backend(new LocationReaderDW(), new GpsAE<LocationDW, Double>());
    //Creates nodes we need of different locations
    LocationDW capitol = new LocationDW("Capitol");
    LocationDW rhetas = new LocationDW("Rhetas");
    LocationDW collegeLib = new LocationDW("College Library");
    LocationDW gordons = new LocationDW("Gordons");
    LocationDW bascomHall = new LocationDW("Bascom Hall");
    LocationDW biochem = new LocationDW("Biochemistry building");
    LocationDW engHall = new LocationDW("Engineering Hall");
    //Adds all nodes to the graph
    backend.addLocation(capitol);
    backend.addLocation(rhetas);
    backend.addLocation(collegeLib);
    backend.addLocation(gordons);
    backend.addLocation(bascomHall);
    backend.addLocation(biochem);
    backend.addLocation(engHall);
    //Creates edges between the nodes to create the graph
    backend.insertEdge(capitol, rhetas, 5.0);
    backend.insertEdge(rhetas, capitol, 5.0);
    backend.insertEdge(collegeLib, rhetas, 2.0);
    backend.insertEdge(rhetas, collegeLib, 2.0);
    backend.insertEdge(capitol, collegeLib, 4.0);
    backend.insertEdge(collegeLib, capitol, 4.0);
    backend.insertEdge(capitol, gordons, 4.0);
    backend.insertEdge(gordons, capitol, 4.0);
    backend.insertEdge(rhetas, gordons, 3.0);
    backend.insertEdge(gordons, rhetas, 3.0);
    backend.insertEdge(collegeLib, bascomHall, 3.0);
    backend.insertEdge(bascomHall, collegeLib, 3.0);
    backend.insertEdge(rhetas, bascomHall, 2.0);
    backend.insertEdge(bascomHall, rhetas, 2.0);
    backend.insertEdge(bascomHall, biochem, 7.0);
    backend.insertEdge(biochem, bascomHall, 7.0);
    backend.insertEdge(bascomHall, engHall, 5.0);
    backend.insertEdge(engHall, bascomHall, 5.0);
    backend.insertEdge(engHall, biochem, 2.0);
    backend.insertEdge(biochem, engHall, 2.0);
    
    //Test getDistance
    Assertions.assertEquals(backend.getDistance(capitol, biochem), 14.0);
    Assertions.assertEquals(backend.getDistance(engHall, collegeLib), 8.0);
    
    //Test depthFirstTraversal
    Assertions.assertEquals(backend.DepthFirstSearch(capitol), 
        "Capitol Rhetas College Library Bascom Hall Biochemistry building Engineering Hall Gordons "
        + "/nThere are 0 nodes that are in this graph but can't be reached through this traversal");
    
    //Test PathWithStop
   LinkedList<LocationInterface> list = (LinkedList<LocationInterface>) backend.PathWithStop(capitol, gordons, bascomHall);
   Assertions.assertEquals(list.get(0).getName(), "Capitol");
   Assertions.assertEquals(list.get(1).getName(), "Rhetas");
   Assertions.assertEquals(list.get(2).getName(), "Bascom Hall");
   Assertions.assertEquals(list.get(3).getName(), "Rhetas");
   Assertions.assertEquals(list.get(4).getName(), "Gordons");
   
   //Test PathWithStopCost
   Assertions.assertEquals(backend.PathWithStopCost(capitol, engHall, collegeLib), 12.0);
   Assertions.assertEquals(backend.PathWithStopCost(engHall, biochem, rhetas), 16.0);
   
   //Test getStatistics
   Assertions.assertEquals(backend.getStatistics(), "You Inserted a location 7times\n"
       + "You Inserted an edge 20times\n"
       + "You removed a location 0times\n"
       + "You removed an edge 0times\n");
   
   //Test removeLocation
   backend.removeLocation(engHall);
   try {
     backend.getDirections(capitol, engHall);
     Assertions.fail();
   }
   catch(NoSuchElementException e) {
     
   }
   
   //Test removeEdge
   backend.removeEdge(rhetas, gordons);
   backend.removeEdge(gordons, rhetas);
   //The distance should now be longer because there is no direct edge between rhetas and gordons
   Assertions.assertEquals(backend.getDistance(rhetas, gordons), 9.0);
   
   
  }
  
  /**
   * This tester tests the backend, data wrangler and algorithm engineer using the load data from a file method
   * and then using the other methods to edit the graph
   */
  @Test
  public void IntegrationTest2() {
    
    
    Backend backend = new Backend(new LocationReaderDW(), new GpsAE<LocationDW, Double>());
    //This dot file creates 5 new locations: madison corner, babcock drive, ogg, smith, selery
    //Also has connecting edges between madison corner and babcock drive (cost:34)
    //Connecting edge between ogg and smith (cost:78) and edge between smith and selery (cost:343)
    try {
      backend.loadData("test.dot");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    //Test getDistance after loading file
    Assertions.assertEquals(backend.getDistance(new LocationDW("ogg"), new LocationDW("smith")), 78); 
    
    //Test getDirections after loading of the file
    LinkedList<LocationInterface> list = (LinkedList<LocationInterface>) backend.getDirections(new LocationDW("ogg"), new LocationDW("selery"));
    Assertions.assertEquals(list.get(0).getName(), "ogg ");
    Assertions.assertEquals(list.get(1).getName(), "smith");
    Assertions.assertEquals(list.get(2).getName(), "selery");
    
    //Test depth first search
    Assertions.assertEquals(backend.DepthFirstSearch(new LocationDW("ogg")),
        "ogg  smith selery /nThere are 2 nodes that are in this graph but can't be reached through this traversal");
    
    //Test removal of a location
    backend.removeLocation(new LocationDW("selery"));
    try {
      backend.getDirections(new LocationDW("selery"), new LocationDW("ogg"));
      Assertions.fail(); //Fail the test if an exception wasn't thrown
    }
    catch(NoSuchElementException e) {
      
    }
    
    Backend bd = new Backend(new LocationReaderDW(), new GpsAE<LocationDW, Double>());
    try {
      bd.loadData("test4.dot");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    //Test getDistance
  Assertions.assertEquals(bd.getDistance(bd.graph.get(new LocationDW("Grainger")),
      bd.graph.get(new LocationDW("Chemistry Building"))), 2);
  
    //Test getPathWithStop
  list = (LinkedList<LocationInterface>) bd.PathWithStop(
      bd.graph.get(new LocationDW("Waters")), bd.graph.get(new LocationDW("Engineering Hall")), bd.graph.get(new LocationDW("Chemistry Building")));
  Assertions.assertEquals(list.get(0).getName(), "Waters");
  Assertions.assertEquals(list.get(1).getName(), "Chemistry Building");
  Assertions.assertEquals(list.get(2).getName(), "Engineering Hall");
  
  //Test getPathWithStop Distance
  Assertions.assertEquals(bd.PathWithStopCost(
      bd.graph.get(new LocationDW("Waters")), bd.graph.get(new LocationDW("Engineering Hall")), bd.graph.get(new LocationDW("Chemistry Building"))), 9.0);
  
  //Test depthFirstTraversal
  Assertions.assertEquals(bd.DepthFirstSearch(bd.graph.get(new LocationDW("The Capitol"))), 
      "The Capitol Memorial Library Gordon's Market Grainger Chemistry Building Engineering Hall "
      + "Camp Randall Steenbock Library Dejope Waters Memorial Union Bascom Hall /nThere are 0 nodes "
      + "that are in this graph but can't be reached through this traversal");
  }
  
  /**
   * This test tests the methods of the LocationDW class
   */
  @Test
  public void CodeReviewOfDataWrangler1() {
    LocationDW tester = new LocationDW("Bascom Hall");
    
    //Tests the getName() method
    Assertions.assertEquals(tester.getName(), "Bascom Hall");
    
    //Tests the setName() method
    tester.setName("Gordons Dining Hall");
    Assertions.assertEquals(tester.getName(), "Gordons Dining Hall");
    
    tester.setName("");
    Assertions.assertEquals(tester.getName(), "");
    
    //Tests the addAdjacent and getAdjacent methods method
    tester.addAdjacent(new LocationDW("Rhetas"), 2.5);
    tester.addAdjacent(new LocationDW("Memorial Union"),3.5);
    
    //Test that the addAdjacent method stores the right names
    Assertions.assertEquals(((LocationDW)(((Object[])(tester.getAdjacents().get(0)))[0])).getName(), "Rhetas");
    Assertions.assertEquals(((LocationDW)(((Object[])(tester.getAdjacents().get(1)))[0])).getName(), "Memorial Union");
    
    //Test that the addAdjacent method stores the right path costs
    Assertions.assertEquals(((((Object[])(tester.getAdjacents().get(0)))[1])), 2.5);
    Assertions.assertEquals(((((Object[])(tester.getAdjacents().get(1)))[1])), 3.5);
    
    
    
    
  }
  
  /**
   * The test tests the methods of the LocationReaderDW class
   */
  @Test
  public void CodeReviewOfDataWrangler2() {
    LocationReaderDW tester = new LocationReaderDW();
    try {
      ArrayList<LocationInterface> locationList = (ArrayList<LocationInterface>) tester.readWordsFromFile("test.dot");
      //Make sure the contents of the list are as expected
      Assertions.assertEquals(locationList.get(0).getName(), "madison corner");
      Assertions.assertEquals(locationList.get(1).getName(), "babcock drive");
      Assertions.assertEquals(locationList.get(2).getName(), "ogg ");
      Assertions.assertEquals(locationList.get(3).getName(), "smith");
      Assertions.assertEquals(locationList.get(4).getName(), "selery");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    //Try to read from a non-existent file and make sure it throws an error
    try {
      tester.readWordsFromFile("noFile");
      Assertions.fail();
    }
    catch(FileNotFoundException e) {
      
    }
    
    
  }
  
  /**
   * This method tests the shortestPathWithStop method and ensures the stored list is correct
   */
  @Test
  public void test1() {
    
  //Creates a test graph with 8 nodes where each node is connected by two directed edges
    GpsAE<Character, Integer> tester = new GpsAE<Character, Integer>();
    tester.insertNode('C');
    tester.insertNode('M');
    tester.insertNode('W');
    tester.insertNode('H');
    tester.insertNode('B');
    tester.insertNode('L');
    tester.insertNode('R');
    tester.insertNode('P');
    tester.insertEdge('C', 'M', 3);
    tester.insertEdge('M', 'C', 3);
    tester.insertEdge('W', 'C', 3);
    tester.insertEdge('C', 'W', 3);
    tester.insertEdge('H', 'W', 5);
    tester.insertEdge('W', 'H', 5);
    tester.insertEdge('M', 'H', 4);
    tester.insertEdge('H', 'M', 4);
    tester.insertEdge('H', 'B', 2);
    tester.insertEdge('B', 'H', 2);
    tester.insertEdge('B', 'R', 3);
    tester.insertEdge('R', 'B', 3);
    tester.insertEdge('B', 'L', 2);
    tester.insertEdge('L', 'B', 2);
    tester.insertEdge('L', 'P', 1);
    tester.insertEdge('P', 'L', 1);
    tester.insertEdge('P', 'R', 2);
    tester.insertEdge('R', 'P', 3);
    
    //Creates a path between 'C' and 'B' with a forced stop at W
    LinkedList<Character> list = (LinkedList<Character>) tester.shortestPathWithStop('C', 'B', 'W');
    Assertions.assertEquals(list.get(0),'C');
    Assertions.assertEquals(list.get(1),'W');
    Assertions.assertEquals(list.get(2),'H');
    Assertions.assertEquals(list.get(3),'B');
    
    //Creates a path between 'M' and 'P' with a forced stop at 'W'
    list = (LinkedList<Character>) tester.shortestPathWithStop('M', 'P', 'W');
    Assertions.assertEquals(list.get(0),'M');
    Assertions.assertEquals(list.get(1),'C');
    Assertions.assertEquals(list.get(2),'W');
    Assertions.assertEquals(list.get(3),'H');
    Assertions.assertEquals(list.get(4),'B');
    Assertions.assertEquals(list.get(5),'L');
    Assertions.assertEquals(list.get(6),'P');
    
    //Creates a path between 'R' and 'L' with a forced stop at 'H'
    list = (LinkedList<Character>) tester.shortestPathWithStop('R', 'L', 'H');
    Assertions.assertEquals(list.get(0),'R');
    Assertions.assertEquals(list.get(1),'B');
    Assertions.assertEquals(list.get(2),'H');
    Assertions.assertEquals(list.get(3),'B');
    Assertions.assertEquals(list.get(4),'L');
    
  }
  
  /**
   * This method tests the shortestPathWithStopCost method and ensures the proper cost is calculated for 
   * various paths
   */
  @Test
  public void test2() {
  //Creates a test graph with 8 nodes where each node is connected by two directed edges
    GpsAE<Character, Integer> tester = new GpsAE<Character, Integer>();
    tester.insertNode('C');
    tester.insertNode('M');
    tester.insertNode('W');
    tester.insertNode('H');
    tester.insertNode('B');
    tester.insertNode('L');
    tester.insertNode('R');
    tester.insertNode('P');
    tester.insertEdge('C', 'M', 3);
    tester.insertEdge('M', 'C', 3);
    tester.insertEdge('W', 'C', 3);
    tester.insertEdge('C', 'W', 3);
    tester.insertEdge('H', 'W', 5);
    tester.insertEdge('W', 'H', 5);
    tester.insertEdge('M', 'H', 4);
    tester.insertEdge('H', 'M', 4);
    tester.insertEdge('H', 'B', 2);
    tester.insertEdge('B', 'H', 2);
    tester.insertEdge('B', 'R', 3);
    tester.insertEdge('R', 'B', 3);
    tester.insertEdge('B', 'L', 2);
    tester.insertEdge('L', 'B', 2);
    tester.insertEdge('L', 'P', 1);
    tester.insertEdge('P', 'L', 1);
    tester.insertEdge('P', 'R', 2);
    tester.insertEdge('R', 'P', 3);
    
    //Tests the cost of the path starting at C, ending at P, with a stop at W
    Assertions.assertEquals(tester.shortestPathWithStopCost('C', 'P', 'W'), 13);
    
    //Tests the cost of the path starting at C, ending at B with a stop at P
    Assertions.assertEquals(tester.shortestPathWithStopCost('C', 'B', 'P'), 15);
    
    //Tests the cost of the path starting at R, ending at L, with a stop at H
    Assertions.assertEquals(tester.shortestPathWithStopCost('R', 'L', 'H'), 9);
    
  }
  
  /**
   * This method tests the depthFirstTraversal method and ensures proper traversal
   */
  @Test
  public void test3() {
    //Creates a test graph with 8 nodes where each node is connected by two directed edges
    GpsAE<Character, Integer> tester = new GpsAE<Character, Integer>();
    tester.insertNode('C');
    tester.insertNode('M');
    tester.insertNode('W');
    tester.insertNode('H');
    tester.insertNode('B');
    tester.insertNode('L');
    tester.insertNode('R');
    tester.insertNode('P');
    tester.insertEdge('C', 'M', 3);
    tester.insertEdge('M', 'C', 3);
    tester.insertEdge('W', 'C', 3);
    tester.insertEdge('C', 'W', 3);
    tester.insertEdge('H', 'W', 5);
    tester.insertEdge('W', 'H', 5);
    tester.insertEdge('M', 'H', 4);
    tester.insertEdge('H', 'M', 4);
    tester.insertEdge('H', 'B', 2);
    tester.insertEdge('B', 'H', 2);
    tester.insertEdge('B', 'R', 3);
    tester.insertEdge('R', 'B', 3);
    tester.insertEdge('B', 'L', 2);
    tester.insertEdge('L', 'B', 2);
    tester.insertEdge('L', 'P', 1);
    tester.insertEdge('P', 'L', 1);
    tester.insertEdge('P', 'R', 2);
    tester.insertEdge('R', 'P', 3);
    String output = tester.depthFirstTraversal('C');
    Assertions.assertEquals(output, "C M H W B R P L /nThere are 0 nodes that are in this graph but can't be reached through this traversal");
    
    //This creates a test graph with 8 test nodes where each node that is connected is connected by two
    //directed edges
    tester = new GpsAE<Character, Integer>();
    tester.insertNode('A');
    tester.insertNode('B');
    tester.insertNode('C');
    tester.insertNode('D');
    tester.insertNode('E');
    tester.insertNode('F');
    tester.insertNode('G');
    tester.insertNode('H');
    tester.insertEdge('A', 'B', 4);
    tester.insertEdge('B', 'A', 4);
    tester.insertEdge('A', 'C', 2);
    tester.insertEdge('C', 'A', 2);
    tester.insertEdge('A', 'E', 15);
    tester.insertEdge('E', 'A', 15);
    tester.insertEdge('B', 'D', 1);
    tester.insertEdge('D', 'B', 1);
    tester.insertEdge('C', 'D', 5);
    tester.insertEdge('D', 'C', 5);
    tester.insertEdge('B', 'E', 10);
    tester.insertEdge('E', 'B', 10);
    tester.insertEdge('D', 'E', 3);
    tester.insertEdge('E', 'D', 3);
    tester.insertEdge('D', 'F', 0);
    tester.insertEdge('F', 'D', 0);
    tester.insertEdge('F', 'H', 4);
    tester.insertEdge('H', 'F', 4);
    tester.insertEdge('G', 'H', 4);
    tester.insertEdge('H', 'G', 4);
    output = tester.depthFirstTraversal('A');
    Assertions.assertEquals(output, "A B D C E F H G /nThere are 0 nodes that are in this graph but can't be reached through this traversal");
  }
  
  /**
   * This method tests the shortest path methods when there is no path to a certain node
   */
  @Test
  public void test4() {
  //This creates a test graph with 8 test nodes where each node that is connected is connected by two
    //directed edges
    GpsAE<Character, Integer> tester = new GpsAE<Character, Integer>();
    tester.insertNode('C');
    tester.insertNode('M');
    tester.insertNode('W');
    tester.insertNode('H');
    tester.insertNode('B');
    tester.insertNode('L');
    tester.insertNode('R');
    tester.insertNode('P');
    tester.insertEdge('C', 'M', 3);
    tester.insertEdge('M', 'C', 3);
    tester.insertEdge('W', 'C', 3);
    tester.insertEdge('C', 'W', 3);
    tester.insertEdge('H', 'W', 5);
    tester.insertEdge('W', 'H', 5);
    tester.insertEdge('M', 'H', 4);
    tester.insertEdge('H', 'M', 4);
    tester.insertEdge('H', 'B', 2);
    tester.insertEdge('B', 'H', 2);
    tester.insertEdge('B', 'R', 3);
    tester.insertEdge('R', 'B', 3);
    tester.insertEdge('B', 'L', 2);
    tester.insertEdge('L', 'B', 2);
    tester.insertEdge('L', 'P', 1);
    tester.insertEdge('P', 'L', 1);
    tester.insertEdge('P', 'R', 2);
    tester.insertEdge('R', 'P', 3);
    
  //Insert a character with no connections to the graph
    tester.insertNode('Z');
    //Try to find a shortest path with this unconnected node as a stop (should throw error)
    try {
    LinkedList<Character> list = (LinkedList<Character>) tester.shortestPathWithStop('C', 'B', 'Z');
    Assertions.fail();
    }
    catch(NoSuchElementException e1) {
      
    }

    //Try to find a shorter path with this unconnected node as the end point (should throw error)
    try {
      LinkedList<Character> list = (LinkedList<Character>) tester.shortestPathWithStop('C', 'Z', 'B');
      Assertions.fail();
      }
    catch(NoSuchElementException e1) {
        
    }
    
    //Try to find the cost of a path with the unconnected node as a stop (should throw error)
    try {
      tester.shortestPathWithStopCost('C', 'B', 'Z');
      Assertions.fail();
      }
    catch(NoSuchElementException e1) {
        
    }
    
    //Try to find the cost of a path with the unconnected node as the end point
    try {
      tester.shortestPathWithStopCost('C', 'Z', 'B');
      Assertions.fail();
      }
    catch(NoSuchElementException e1) {
        
    }
  }
  
  /**
   * This method tests the depthFirstTraversal method when there is a node with no paths to it
   */
  @Test
  public void test5() {
  //This creates a test graph with 8 test nodes where each node that is connected is connected by two
    //directed edges
    GpsAE<Character, Integer> tester = new GpsAE<Character, Integer>();
    tester.insertNode('C');
    tester.insertNode('M');
    tester.insertNode('W');
    tester.insertNode('H');
    tester.insertNode('B');
    tester.insertNode('L');
    tester.insertNode('R');
    tester.insertNode('P');
    tester.insertEdge('C', 'M', 3);
    tester.insertEdge('M', 'C', 3);
    tester.insertEdge('W', 'C', 3);
    tester.insertEdge('C', 'W', 3);
    tester.insertEdge('H', 'W', 5);
    tester.insertEdge('W', 'H', 5);
    tester.insertEdge('M', 'H', 4);
    tester.insertEdge('H', 'M', 4);
    tester.insertEdge('H', 'B', 2);
    tester.insertEdge('B', 'H', 2);
    tester.insertEdge('B', 'R', 3);
    tester.insertEdge('R', 'B', 3);
    tester.insertEdge('B', 'L', 2);
    tester.insertEdge('L', 'B', 2);
    tester.insertEdge('L', 'P', 1);
    tester.insertEdge('P', 'L', 1);
    tester.insertEdge('P', 'R', 2);
    tester.insertEdge('R', 'P', 3);
    
    //Add an unconnected node
    tester.insertNode('Z');
    String output = tester.depthFirstTraversal('C');
    Assertions.assertEquals(output, "C M H W B R P L /nThere are 1 nodes that are in this graph but can't be reached through this traversal");
    
    //Add another unconnected node
    tester.insertNode('D');
    output = tester.depthFirstTraversal('B');
    Assertions.assertEquals(output, "B H W C M R P L /nThere are 2 nodes that are in this graph but can't be reached through this traversal");
    
    //Try the depth first traversal on an unconnected node
    output = tester.depthFirstTraversal('D');
    Assertions.assertEquals(output, "D /nThere are 9 nodes that are in this graph but can't be reached through this traversal");
    
    //Try to start the depthFirstTraversal on a node that doesn't exist in the graph
    try {
      tester.depthFirstTraversal('V');
      Assertions.fail();
    }
    catch(NoSuchElementException e1) {
      
    }
    
  }

}
