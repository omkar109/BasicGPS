import java.util.List;
    public interface LocationInterface {
        // public LocationDW(String name);
        public String getName();
        public List<Object> getAdjacents();
        public void addAdjacent(LocationInterface adj, double cost);
        public void setName(String newName);


}
