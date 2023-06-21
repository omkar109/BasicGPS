import java.util.ArrayList;
import java.util.List;

public class LocationDW implements LocationInterface{
    String data;
    List<Object> adjacents;//a list of arrays of length 2
    public LocationDW(String data)
    {
        this.data=data;
        this.adjacents=new ArrayList<>();
    }
    @Override
    public String getName() {

        return data;
    }

    @Override
    public List<Object> getAdjacents() {


        return adjacents;
    }

    @Override
    public void addAdjacent(LocationInterface adj, double cost) {

        Object[] arr= new Object[2];

        arr[0]=adj;
        arr[1]=cost;

        adjacents.add(arr);
    }

    @Override
    public void setName(String newName) {

        this.data=newName;
    }
    
    @Override
    public String toString() {
      return data;
    }
}
