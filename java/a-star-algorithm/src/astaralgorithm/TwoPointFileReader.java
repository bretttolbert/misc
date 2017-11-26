package astaralgorithm;
import java.awt.Point;
import java.util.Scanner;
import java.io.*;

//this class is designed to read a file containing two points
//this is the format: (x1,y1)(x2,y2)
//after the open method is called with the filename,
//the points can be retreived with the getPoint1() and getPoint2() methods
public class TwoPointFileReader
{
    Point point1, point2;
    public TwoPointFileReader() { }
    
    //opens a file, parses it, and stores the points
    public void open(File file) throws IOException
    {
        Scanner scan = new Scanner(file);
        String line = scan.nextLine();
        //chop the outer parentheses (the first and last character)
        line = line.substring(1, line.length() - 1);
        //split the line into two comma separated pairs
        String[] pairs = line.split("[()]+");
        String[] pair1 = pairs[0].split(",");
        String[] pair2 = pairs[1].split(",");
        point1 = new Point(Integer.parseInt(pair1[0]),
                Integer.parseInt(pair1[1]));
        point2 = new Point(Integer.parseInt(pair2[0]),
                Integer.parseInt(pair2[1]));
    }
    
    public Point getPoint1() { return point1; }
    public Point getPoint2() { return point2; }
}
