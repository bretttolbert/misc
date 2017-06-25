package astaralgorithm;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class PolygonFileReader
{
    ArrayList<Polygon> polygonList;
    
    public PolygonFileReader() { }
    
    //opens a file containing polygon data and parses it
    public void open(File file) throws IOException
    {
        polygonList = new ArrayList<Polygon>();
        Scanner scan = new Scanner(file);
        
        while (scan.hasNext())
        {
            String line = scan.nextLine();
            //chop the brackets off the line (first and last character)
            line = line.substring(2, line.length() - 1);
            //split the line into comma separated coordinate pairs
            String[] pairs = line.split("[()]+");
            Polygon p = new Polygon();
            //now parse each pair
            for (String pair : pairs)
            {
                String[] xy = pair.split(",");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                p.addPoint(x, y);
            }
            //add closing point (same as first point, may not be necessary)
            //p.addPoint(p.xpoints[0], p.ypoints[0]);
            //add new polygon to list
            polygonList.add(p);
        }   
    }
    
    public Polygon[] getPolygons()
    {
        //convert the polygon list to an array and return it
        return (Polygon[])polygonList.toArray(new Polygon[polygonList.size()]);
    }
}
