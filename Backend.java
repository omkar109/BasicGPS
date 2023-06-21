import java.io.FileNotFoundException;
import java.util.*;

public class Backend implements BackendInterface{
    private LocationReaderInterface reader;
    public GpsAE<LocationInterface, Double> graph;
    private int[] methodCounts;


    public Backend(LocationReaderInterface reader, GpsAE graph){

        this.reader = reader;
        //Initialize graph
        this.graph = graph;
        methodCounts = new int[7];

    }


    @Override
    public boolean addLocation(LocationInterface location) {

        //checking if the location is null
        if(location==null){
            return false;
        }
        try {
            //adds the location to graph/map
            graph.insertNode(location);
        }
        catch(Exception e){
            return false;
        }

        methodCounts[0]++;
        return true;
    }

    @Override
    public boolean removeLocation(LocationInterface location) {

        LocationInterface l = graph.get(location);


        try {
            boolean removeCheck = graph.removeNode(l);

            if (!removeCheck) {
                return false;
            }
        }
        catch(Exception e){
            return false;
        }
        methodCounts[2]++;

        //Removing the location was succesfull
        return true;

    }

    @Override
    public void insertEdge(LocationInterface pred, LocationInterface succ, double weight) {

        boolean insertCheck = graph.insertEdge(pred, succ, weight);



        if(!insertCheck){
            throw new NoSuchElementException("Edge could not be inserted");
        }
        methodCounts[1]++;
    }

    @Override
    public void removeEdge(LocationInterface source, LocationInterface destination) {

        LocationInterface s = graph.get(source);
        LocationInterface d = graph.get(destination);
        //This method could throw a null Pointer Exception that is thrown by the removeEdge method of graph class
        boolean removeCheck = graph.removeEdge(s, d);

        //removing the path between from the locations adjacents


        if (!removeCheck) {
            throw new NoSuchElementException("There exists no path between the source and destination");
        }
        methodCounts[3]++;


    }

    @Override
    public List<LocationInterface> getDirections(LocationInterface start, LocationInterface end) {

        LocationInterface st = graph.get(start);
        LocationInterface en = graph.get(end);

        LinkedList<LocationInterface> d = (LinkedList<LocationInterface>) graph.shortestPathData(st, en);

        ArrayList<LocationInterface> directions = new ArrayList<>();

        //copying the contents of the linked list into the array list.
        for(int i=0;i<d.size();i++){directions.add(d.get(i));}
        System.out.println(d.size());
        methodCounts[4]++;
        return d;



        /**
         * if the frontend Developer decides to do handle the location interface List remove what's there after the comment
         * return d;
         */

    }
    @Override
    public int getDistance(LocationInterface start, LocationInterface end) {

        LocationInterface start_n = graph.get(start);
        LocationInterface end_n = graph.get(end);


        double dist = 0;


        try {
            //method returns the shortest path between start and end locations
            dist = graph.shortestPathCost(start_n, end_n);
        }
        catch(Exception e){
            throw new NoSuchElementException("Path/locations don't exist");
        }

        methodCounts[4]++;
        return (int) dist;
    }

    @Override
    public String getStatistics() {

        String stats = "You Inserted a location " + methodCounts[0] + "times\n"
                + "You Inserted an edge " + methodCounts[1] + "times\n"
                + "You removed a location " + methodCounts[2] + "times\n"
                + "You removed an edge "+ methodCounts[3] + "times\n";

        return stats;
    }

    @Override
    public void loadData(String filename) throws FileNotFoundException {

        try {

            ArrayList<LocationInterface> l = (ArrayList<LocationInterface>) reader.readWordsFromFile(filename);


            for(int i=0;i<l.size();i++){
                LocationInterface lb = (LocationDW) l.get(i);
                //graph.insertNode(lb);
                try {

                    graph.get(lb);

                }
                catch(Exception e)
                {

                    graph.insertNode(lb);
                    //System.out.println("can't add"+lb.getName());
                }
                List<Object> adjlist=  lb.getAdjacents();


                for(int j=0; j<adjlist.size();j++){
                   // LocationInterface a = (LocationDW) adjlist.get(0);
                    Object[] array = (Object[]) adjlist.get(j);
                    //Object[] array = (Object[]) a.getAdjacents().get(j);
                    LocationInterface la = (LocationDW)array[0];

                    //graph.insertNode(la);

                    try {

                        graph.get(la);

                        graph.insertEdge(lb, graph.get(la), (Double) array[1]);


                    }
                    catch(Exception e)
                    {


                            graph.insertNode(la);
                            graph.insertEdge(lb, la, (Double) array[1]);
                        }

                    }



                }
                /*
                Enumeration<LocationInterface> e = graph.nodes.keys();
                System.out.println("CONTENTS OF HASHTABLE");
                while(e.hasMoreElements()) {

                    LocationDW key = (LocationDW)e.nextElement();
                    System.out.println(key.getName());
                }
                */


        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("This file does not exist");
        }

        methodCounts[5]++;

    }


    @Override
    public String DepthFirstSearch(LocationInterface start) {
        LocationInterface st = graph.get(start);

        methodCounts[6]++;
        System.out.println(graph.depthFirstTraversal(st));
        return graph.depthFirstTraversal(st);
    }

   
    public List<LocationInterface> PathWithStop(LocationInterface start, LocationInterface end , LocationInterface stop){

        LocationInterface start_n = graph.get(start);
        LocationInterface end_n = graph.get(end);
        LocationInterface stop_n = graph.get(stop);

        return graph.shortestPathWithStop(start_n, end_n, stop_n);
    }

    public double PathWithStopCost(LocationInterface start, LocationInterface end , LocationInterface stop){
        LocationInterface start_n = graph.get(start);
        LocationInterface end_n = graph.get(end);
        LocationInterface stop_n = graph.get(stop);
        return graph.shortestPathWithStopCost(start_n, end_n, stop_n);
    }
}
