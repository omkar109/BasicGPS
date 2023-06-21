// --== CS400 File Header Information ==--
// Name: Omkar Kendale
// Email: kendale@wisc.edu
// Group and Team: DK, red
// Group TA: Yuye Jiang
// Lecturer: Florain Heimerl
// Notes to Grader: <optional extra notes>

import java.util.List;
import java.util.NoSuchElementException;

public interface AlgorithmEngineerInterface<NodeType, EdgeType extends Number> extends GraphADT<NodeType, EdgeType>{

//Other methods such as insertNode, getEdge, etc are not shown because they will be implemented through the extension of GraphADT

//public AlgorithmEngineer();
public List<NodeType> shortestPathWithStop(NodeType start, NodeType end, NodeType stop);
public double shortestPathWithStopCost(NodeType start, NodeType end, NodeType stop);
public String depthFirstTraversal(NodeType start) throws NoSuchElementException;
}
