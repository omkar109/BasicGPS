import java.io.FileNotFoundException;
import java.util.List;


public interface BackendInterface{
        //Backend( LocationReader l_reader, graph GraphADT) //Constructor that takes a GraphADT as an argument


        public boolean addLocation(LocationInterface location); //Adds a location to the GPS

        public boolean removeLocation(LocationInterface location); //Removes a location from the GPS

        public void insertEdge(LocationInterface pred, LocationInterface succ, double weight); //Inserts a path into the GPS

        public void removeEdge(LocationInterface source, LocationInterface destination); //Removes a path from the GPS

        public List<LocationInterface> getDirections(LocationInterface start, LocationInterface end); //Use shortestPathData to get directions from the first node to intermediary nodes to the final node

        public int getDistance(LocationInterface start, LocationInterface end); //Gets the distance from the start to the end

        public String getStatistics();

        public void loadData(String filename) throws FileNotFoundException;

        public String DepthFirstSearch(LocationInterface start);


}