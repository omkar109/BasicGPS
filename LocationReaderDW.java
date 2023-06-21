import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class LocationReaderDW implements LocationReaderInterface{
    @Override
    public List<LocationInterface> readWordsFromFile(String filename) throws FileNotFoundException {
        File f=new File(filename);
       Scanner sc=new Scanner(f);
       sc.nextLine();//sc.nextLine();
      // BaseGraph b=new BaseGraph();
        List<LocationInterface> list=new ArrayList<>();
        try {
            String s = "";
            while (sc.hasNext()) {
                s=sc.nextLine();
                //System.out.println(s);
                if(s.equals("}")){break;}
//format: predecessor -> successor [label=<anything we don't care> distance= some number<we care about edge weight>]
//problem TODO: we can't handle the strings we get by parsing the string after splitting it using space as the label can contain
                /*String[] parts_of_line=s.split("\\s+");//splitting a string using delimiteer as a space or many space until a new character is found
                String predecessor=parts_of_line[0].trim().substring(0,parts_of_line[0].trim().length()-1);//remove the extra ":"
                String successor=parts_of_line[2].trim();
                if(parts_of_line)*/
                //assumption and styling of the program-we only take into account the undirected edges
                //System.out.println(s);
                String s_copy = s;
String predecessor="";String successor="";
                if (s_copy != "") {
                    int index_dash = 0;

                    if (!s_copy.startsWith("\"")) {

                        predecessor = s_copy.substring(0, (index_dash = s_copy.indexOf('-')));

                    }
                    else{
                       predecessor=s_copy.substring(1,(index_dash = s_copy.indexOf('-'))-2);
                    }
                    s_copy = s_copy.substring(index_dash + 3);
                }
                if (s_copy != "") {
                    int index_squareb = s_copy.indexOf("[");
                    if (!s_copy.startsWith("\"")) {

                        successor = s_copy.substring(0, index_squareb).trim();

                    }
                    else{
                        successor=s_copy.substring(1,index_squareb-1).trim();
                    }
                    s_copy = s_copy.substring(index_squareb + 1);
                }
                String distance = "";
                if (s_copy != "") {

                    //checking if there is a label
                    if (s_copy.contains("label")) {
                        String distance_intermediate = s_copy.split("\\s++")[1];
                        int l = distance_intermediate.length();
                        distance = distance_intermediate.substring(distance_intermediate.indexOf("=") + 1, l - 2);

                    } else {
                        //it only contains distance
                        distance = s_copy.substring(s_copy.indexOf("=") + 1, s_copy.length() - 2);
                    }
                }
//                System.out.println(predecessor);
//                System.out.println(successor);
//                System.out.println(distance);
                LocationDW n=new LocationDW(predecessor);
                LocationDW su=new LocationDW(successor);
                int c=0;
                for(LocationInterface d:list)
                {
                    if(((LocationDW)d).getName().trim().equals(n.getName().trim()))
                    {
                        d.addAdjacent(su,Double.valueOf(distance));
                        c++;
                    }
                }
                if(c==0) {
                    n.addAdjacent(su, Double.valueOf(distance));
                    list.add(n);
                }
                c=0;
                for(LocationInterface d:list)
                {
                    if(((LocationDW)d).getName().equals(su.getName()))
                    {

                        c++;
                    }
                }
                if(c==0) {
                    list.add(su);
                }
            }
            //System.out.println(list.size());
        }
                catch(Exception e)
                {
                    e.printStackTrace();
                    System.out.println("wrong input string given");
                }
        return list;
            }

/*
public static void main(String[] args)
{
    LocationReaderDW l=new LocationReaderDW();
    try{
    l.readWordsFromFile("test.dot");}
    catch(Exception e)
    {
        e.printStackTrace();
    }
}
*/
}
