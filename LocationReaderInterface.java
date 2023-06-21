import java.util.List;
import java.io.FileNotFoundException;
public interface LocationReaderInterface {


        // public LocationReader();
        // make an arraylist to get Locations, put into graph by AE

        public List<LocationInterface> readWordsFromFile(String filename) throws FileNotFoundException;


}
