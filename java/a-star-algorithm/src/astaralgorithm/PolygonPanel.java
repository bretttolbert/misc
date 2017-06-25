package astaralgorithm;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.lang.Thread;
import java.util.*;

public class PolygonPanel extends JPanel
{
    //members used directly by the algorithm
    Polygon[] polygons;
    Point startPoint;
    Point endPoint;
    ArrayList<Point> pointList; //all points in the system, minus startPoint
    PriorityQueue<Node> pq; //priority queue for a*
    boolean goalFound = false;
    final double EPSILON = 0.5; //used for floating point equality comparision
    
    //members used only for the graphical representation of
    //the algorithm, not actually used by the algorithm
    ArrayList<LineSegment> linesegs; //this is only used for drawing
    ArrayList<Node> oldPaths; //used to show how the optimal path changes
    Node path; //used to draw the final optimal path
    ArrayList<Point> expanded; //to show which nodes where expanded
    ArrayList<Point> intersections; //used to draw intersecs points
        //(regardless of whether or not those points are contained in both segments)
    ArrayList<Point> segmentIntersections; //to draw segment intersections

    
    //constants used to shift and manipulate the coordinates before they are 
    //drawn to screen, these are only when painting the screen  
    final int X_OFFSET = -300;
    final int Y_OFFSET = 2150;
    final int X_MULTIPLIER = 3;
    final int Y_MULTIPLIER = -3;
    /*
    final int X_OFFSET = 0;
    final int Y_OFFSET = 0;
    final int X_MULTIPLIER = 1;
    final int Y_MULTIPLIER = 1;    
     * */
    
    //gui components
    JLabel expandedLabel;
    JLabel statsLabel;
    JLabel mouseLabel;
    JButton nextButton;
    JButton autoButton;
    JButton resetButton;
    JButton pathButton;
    JButton helpButton;
    JPanel northPanel;
    JPanel southPanel;
    
    DecimalFormat fmt1; //for formating output
    
    //if DEBUG==true, the file choosers will not be shown, to save time
    final boolean DEBUG = false;
    //transparency looks cool, but may make animation choppy, if true,
    //you should set LOOPS_PER_DRAW to a higher value to compensate.
    final boolean TRANSPARENCY_ON = false;
        
    boolean lctrlPressed = false; //true if the left control key is pressed
    boolean placingStart = true; //if true, when the user clicks the mouse
    //while holding lctrl, the startPoint will be placed, otherwise, the 
    //endPoint will be placed
    
    //used by the auto animation
    int repaintCounter = 0;
    final int LOOPS_PER_DRAW = 5;
    
    public PolygonPanel()
    {
        this.setPreferredSize(new Dimension(850,670));
        this.setBackground(new Color(0xffff99));
        this.setLayout(new BorderLayout());
        
        fmt1 = new DecimalFormat("0.0");
        
        northPanel = new JPanel();
        southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.X_AXIS));
        
        southPanel.add(Box.createRigidArea(new Dimension(20,0)));
        
        expandedLabel = new JLabel();
        expandedLabel.setText("");
        southPanel.add(expandedLabel);
        
        southPanel.add(Box.createRigidArea(new Dimension(200,0)));
        
        statsLabel = new JLabel();
        statsLabel.setText("");
        southPanel.add(statsLabel);
        
        southPanel.add(Box.createHorizontalGlue());
        
        mouseLabel = new JLabel();
        mouseLabel.setText("(0,0)");
        southPanel.add(mouseLabel);
        
        nextButton = new JButton();
        nextButton.setText("next");
        nextButton.addActionListener(new NextButtonListener());
        northPanel.add(nextButton);
        
        autoButton = new JButton();
        autoButton.setText("auto");
        autoButton.addActionListener(new AutoButtonListener());
        northPanel.add(autoButton);
        
        resetButton = new JButton();
        resetButton.setText("reset");
        resetButton.addActionListener(new ResetButtonListener());
        northPanel.add(resetButton);
        
        pathButton = new JButton();
        pathButton.setText("print path");
        pathButton.addActionListener(new PathButtonListener());
        northPanel.add(pathButton);
        
        helpButton = new JButton();
        helpButton.setText("help");
        helpButton.addActionListener(new HelpButtonListener());
        northPanel.add(helpButton);
        
        this.add(northPanel, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.SOUTH);
        
        JFileChooser chooser = new JFileChooser();
        
        if (!DEBUG) {
        int status = chooser.showDialog(this, "select polygon data file");
        if (status != JFileChooser.APPROVE_OPTION)
        {
            //abort program, cannot continue without polygon data file
            alert("no polygon data, program exiting");
            System.exit(1);
        }
        else
        {
            //read the polygon data file
            PolygonFileReader pfr = new PolygonFileReader();
            try {
            pfr.open(chooser.getSelectedFile());
            } catch (IOException ex)
            { alert("error opening polygon data file"); }
            polygons = pfr.getPolygons();
        }
        
        status = chooser.showDialog(this, "select start and end point data file (or cancel to set manually)");
        if (status != JFileChooser.APPROVE_OPTION)
        {
            //just use default start and end point
            startPoint = new Point(110,530);
            endPoint = new Point(350,680);
        }
        else
        {
            //read the start and endpoint data file
            TwoPointFileReader tpfr = new TwoPointFileReader();
            try {
            tpfr.open(chooser.getSelectedFile());
            } catch (IOException ex)
            { alert("error opening start and end point file"); }
            startPoint = tpfr.getPoint1();
            endPoint = tpfr.getPoint2();   
        }
        }
        else //debug mode is on, don't show file choosers
        {
            PolygonFileReader pfr = new PolygonFileReader();
            try {
            pfr.open(new File("original.polygons"));
            } catch (IOException ex)
            { alert("error opening polygon data file"); }
            polygons = pfr.getPolygons();
            TwoPointFileReader tpfr = new TwoPointFileReader();
            try {
            tpfr.open(new File("original.startend"));
            } catch (IOException ex)
            { alert("error opening start and end point file"); }
            startPoint = tpfr.getPoint1();
            endPoint = tpfr.getPoint2(); 
        }
        
        //add all polygon vertices in the system to the pointlist
        pointList = new ArrayList<Point>();
        for (Polygon p : polygons)
        {
            for (int i=0; i<p.npoints; i++)
                pointList.add(new Point(p.xpoints[i], p.ypoints[i]));
        }
        
        //add listeners
        this.addMouseMotionListener(new MyMouseMotionListener());
        this.setFocusable(true);
        this.addKeyListener(new MyKeyListener());
        this.addMouseListener(new MouseClickListener());
    }
    
    private class MyKeyListener implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {
            //lctrl = 17
            if(e.getKeyCode() == 17)
            {
                lctrlPressed = true;
                expandedLabel.setText("lctrl");
            }
        }
        
        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode() == 17)
            {
                lctrlPressed = false;
                expandedLabel.setText("");
            }
        }
        
        public void keyTyped(KeyEvent e) { }
    }
    
    private class NextButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (!goalFound)
            {
            Node n = pq.poll();
            oldPaths.add(path);
            path = n;
            repaint();
            if (n.isGoal())
            {
                updateStats();
                goalFound = true;
                alert("Goal Found!");
            }
            else
                expandNode(n);
            }
            updateStats();
            repaint();
        }
    }
    
    //displays a message box with the coordinates of the path starting
    //with the start point ending with the current node
    private class PathButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            ArrayList<Point> points = new ArrayList<Point>();
            Node t = path;
            while (t.parent != null)
            {
                points.add(new Point(t.getPosition()));
                t = t.parent;
            }
            points.add(t.getPosition());
            String pathString = "";
            int c = 0;
            for (int i=points.size()-1; i>=0; i--, c++)
            {
                pathString += "[" + c + "](" + points.get(i).x + 
                        "," + points.get(i).y + ") ";
                if (c>0 && (c+1)%4==0)
                    pathString += "\n";
            }
            alert(pathString);
        }
    }
    
    private class HelpButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            alert("You can change the start and end points by doing the " +
                    "following:\nhit the \"reset\" button\nhold left control " +
                    "and click to place the start point (green)\nhold left " +
                    "control and click to place the end point (red)\n" +
                    "The \"print path\" button will print the coordinates " +
                    "of the path beginning with the start point and up to " +
                    "the current node, indexed from 0");
        }
    }
    
    private class AutoButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //beginAStarSearch();
            while (!goalFound && !pq.isEmpty())
            {
                doAStar();
            }
            if (pq.isEmpty() && !goalFound)
                alert("queue empty, goal not found");
            updateStats();
            repaint();
        }
    }
    
    private void doAStar()
    {
        Node n = pq.poll();
        oldPaths.add(path);
        path = n;
        if (n.isGoal())
            goalFound = true;
        else
            expandNode(n);
        updateStats();
        if (repaintCounter == LOOPS_PER_DRAW)
        {
            this.paintImmediately(this.getBounds());
            repaintCounter = 0;
        }
        repaintCounter++;
    }
    
    private class ResetButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            reset();
            repaint();
        }
    }
    
    private void updateStats()
    {
        expandedLabel.setText("expanded=" + expanded.size());
        statsLabel.setText("current node: (" + path.getX() +
                "," + path.getY() + ")  " +
                "h=" + fmt1.format(path.getH()) + 
                " g=" + fmt1.format(path.getG()) +
                " f=" + fmt1.format(path.getF()));
    }
    
    private class MouseClickListener implements MouseListener
    {
        public void mouseExited(MouseEvent e) { }
        public void mouseEntered(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) { }
        public void mousePressed(MouseEvent e) { }
        public void mouseClicked(MouseEvent e)
        {
            if (lctrlPressed)
            {
                Point cursor = new Point(
                        antiTransformX(e.getX()),
                        antiTransformY(e.getY()));
                if (placingStart)
                    startPoint = cursor;
                else
                    endPoint = cursor;
                placingStart = !placingStart; //invert it
                reset();
                repaint();
            }
        }
    }
    
    private class MyMouseMotionListener implements MouseMotionListener
    {
        public void mouseDragged(MouseEvent e) { }
        public void mouseMoved(MouseEvent e)
        {
            int x = antiTransformX(e.getX());
            int y = antiTransformY(e.getY());
            mouseLabel.setText("(" + x + "," + y + ")");
            repaint();
        }
    }
    
    void alert(String msg)
    {
        JOptionPane.showMessageDialog(null, msg);
    }
    
    public void reset()
    {
        oldPaths = new ArrayList<Node>();
        goalFound = false;
        linesegs = new ArrayList<LineSegment>();
        path = new Node(startPoint, null);
        expanded = new ArrayList<Point>();   
        intersections = new ArrayList<Point>();
        segmentIntersections = new ArrayList<Point>();
        Node.goalPosition = endPoint;
        pq = new PriorityQueue<Node>();
        Node s = new Node(startPoint, null);
        //pq.enqueue(s, s.getF());
        pq.add(s);
        updateStats();
        this.requestFocusInWindow();
    }
    
    private void expandNode(Node n)
    {
        //add n to the list of expanded nodes (so it can be drawn)
        expanded.add(n.getPosition());
        
        ArrayList<Point> candidates = new ArrayList<Point>();
        
        //for each point in the system, check if that point is equal to n or
        //any ancestors of n, if it is not, add it to the candidate list
        for (int i=0; i<pointList.size(); i++)
        {
            boolean fail = false;
            Node tn = n;
            while (tn.parent != null)
            {
                if (pointList.get(i) == tn.getPosition())
                    fail = true;
                tn = tn.parent;
            }
            if (pointList.get(i) == tn.getPosition())
                fail = true;
            if (!fail)
                candidates.add(pointList.get(i));
        }
        //add the end point to the candidate list
        candidates.add(endPoint);
        
        ArrayList<Point> reachable = new ArrayList<Point>();
        //now eliminate candidates that cannot be reached from n
        for (int i=0; i<candidates.size(); i++)
        {
            //determine if a line drawn from n to the candidate intersects
            //with any of the polygons, if not, add it to the reachable list
            if (isValid(n.getPosition(), candidates.get(i)))
                reachable.add(candidates.get(i));
        }
        
        //now add reachable points (neighbors of n) to the priority queue
        for (Point p : reachable)
        {
            //create a new node with p as it's position and n as it's parent
            Node child = new Node(p, n);
            //enqueue the new node with a priority of 1 / f(x) because we want
            //nodes with a lower f(x) to have a higher priority
            pq.add(child);
            //add a new line segment to the line segment list so that lines from
            //each expanded node to all of their neighbors can be drawn
            linesegs.add(new LineSegment(p, n.getPosition()));
        }
    }
    
    public void paintComponent(Graphics g)
    {   
        Color neighborColor, expandedColor, oldpathColor, pathColor, polygonColor;
        
        if (TRANSPARENCY_ON)
        {
            neighborColor = new Color(0xdd, 0xdd, 0x88, 128);
            expandedColor = new Color(0x44, 0x44, 0x44, 128);
            oldpathColor = new Color(0xff, 0x99, 0x00, 64);
            pathColor = new Color(0x99, 0x66, 0x33);
            polygonColor = new Color(0xff, 0xcc, 0x66);
        }
        else
        {
            neighborColor = new Color(0xdddd88);
            expandedColor = new Color(0x444444);
            oldpathColor = new Color(0xff9900);
            pathColor = new Color(0x996633);
            polygonColor = new Color(0xffcc66);           
        }
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        
        //draw polygons
        g.setColor(polygonColor);
        for (int i=0; i<polygons.length; i++)
            g.fillPolygon(transformPolygon(polygons[i]));
        
        //draw node neighbor lines
        g.setColor(neighborColor);
        //g.setColor(Color.gray);
        g2.setStroke(new BasicStroke(2));
        LineSegment[] lines = (LineSegment[])linesegs.toArray(new LineSegment[linesegs.size()]);
        
        for (LineSegment ls : lines)
        {
            g.drawLine(transformX(ls.p1.x), transformY(ls.p1.y), 
                    transformX(ls.p2.x), transformY(ls.p2.y));
        }
        
        //draw the old paths
        g.setColor(oldpathColor);
        g2.setStroke(new BasicStroke(2));
        for (Node op : oldPaths)
        {
            while (op.parent != null)
            {
                g.drawLine(transformX(op.getX()), transformY(op.getY()),
                        transformX(op.parent.getX()), transformY(op.parent.getY()));
                op = op.parent;
            }
        }
        
        //draw the path
        Node tn = path;
        while (tn.parent != null)
        {
            g2.setColor(pathColor);
            g2.setStroke(new BasicStroke(5));
            g2.drawLine(transformX(tn.getX()), transformY(tn.getY()),
                    transformX(tn.parent.getX()), transformY(tn.parent.getY()));
            tn = tn.parent;
        }
        
        //draw circles around expanded nodes
        Point[] pts = (Point[])expanded.toArray(new Point[expanded.size()]);
        g.setColor(expandedColor);
        for (Point p : pts)
            g.fillOval(transformX(p.x)-5, transformY(p.y)-5, 10, 10);
        
        //draw the current node
        g2.setStroke(new BasicStroke(1));
        g.setColor(Color.blue);
        //g.fillOval(transformX(path.getX())-7, transformY(path.getY())-7, 14, 14);
        g.drawOval(transformX(path.getX())-10, transformY(path.getY())-10, 20, 20);
        
        /*
        //draw intersections
        pts = (Point[])intersections.toArray(new Point[intersections.size()]);
        g.setColor(Color.gray);
        for (Point p : pts)
            g.fillRect(transformX(p.x)-1, transformY(p.y)-1, 2, 2);
        
         //draw segment intersections
        pts = (Point[])segmentIntersections.toArray(new Point[segmentIntersections.size()]);
        g.setColor(Color.black);
        for (Point p : pts)
            g.fillRect(transformX(p.x)-2, transformY(p.y)-2, 4, 4);
         */
        
        //draw start and end points
        g.setColor(new Color(0x669900));
        g.fillOval(transformX(startPoint.x)-8, transformY(startPoint.y)-8, 16, 16);
        g.setColor(new Color(0x993300));
        g.fillOval(transformX(endPoint.x)-8, transformY(endPoint.y)-8, 16, 16);
        
    }
    
    //the transform methods convert real coordinates to screen coordinates
    private int transformX(int x)
    {
        return x * X_MULTIPLIER + X_OFFSET;
    }
    private int transformY(int y)
    {
        return y * Y_MULTIPLIER + Y_OFFSET;
    }
    private Point transformPoint(Point p)
    {
        return new Point(transformX(p.x),transformY(p.y));
    }
    private Polygon transformPolygon(Polygon p)
    {
        
        int[] xpoints = p.xpoints.clone();
        int[] ypoints = p.ypoints.clone();
        int npoints = p.npoints;
        for (int i=0; i<npoints; i++)
        {
            ypoints[i] = transformY(ypoints[i]);
            xpoints[i] = transformX(xpoints[i]);
        }
        return new Polygon(xpoints, ypoints, npoints);
    }
    
    //the antiTransform methods convert screen coordinates to real coordinates
    private int antiTransformX(int x)
    {
        return (x - X_OFFSET) / X_MULTIPLIER;
    }
    private int antiTransformY(int y)
    {
        return (y - Y_OFFSET) / Y_MULTIPLIER;
    }
    

    
    //returns true if a line drawn from a to b does not intersect with
    //any of the polygons or pass through the interior of any polygon
    private boolean isValid(Point a, Point b)
    {
        boolean fail = false; //this becomes true when an itersect is found
        
        //check if a and b are equal
        if (a.equals(b))
            fail = true;
        
        //check each polygon
        for (int i=0; i<polygons.length && !fail; i++)
        {         
            //determine if a and b are both vertices of polygons[i]
            if (isVertex(a, polygons[i]) && isVertex(b, polygons[i]))
            {   
                //determine if a and b are adjacent vertices, if they are not,
                //fail because the segment passes through the interior
                
                //determine index of vertices a and b
                int asub = -1;
                int bsub = -1;
                for (int j=0; j<polygons[i].npoints; j++)
                {
                    if (polygons[i].xpoints[j] == a.x
                            && polygons[i].ypoints[j] == a.y)
                        asub = j;
                    else if (polygons[i].xpoints[j] == b.x
                            && polygons[i].ypoints[j] == b.y)
                        bsub = j;
                }
                if (asub == -1 || bsub == -1)
                    alert("isValid(): error");
                
                //determine if a and b are adjacent
                boolean adjacent = false;
                if (asub == 0 && bsub == polygons[i].npoints - 1)
                    adjacent = true;
                else if (bsub == 0 && asub ==  polygons[i].npoints - 1)
                    adjacent = true;
                else if (Math.abs(asub - bsub) == 1)
                    adjacent = true;
                if (!adjacent)
                    fail = true;
            }
            
            //check each segment in the polygon
            for (int j=0; j<polygons[i].npoints && !fail; j++)
            {
                int next = (j+1 == polygons[i].npoints) ? 0 : j+1;
                Point c = new Point(polygons[i].xpoints[j],
                        polygons[i].ypoints[j]);
                Point d = new Point(polygons[i].xpoints[next],
                        polygons[i].ypoints[next]);
                if (segmentsIntersect(a, b, c, d))
                    fail = true;
            }
            
        }
        
        //check if special case applies
        
        if (specialCaseApplies(a,b))
            fail = true;
         
        
        return !fail;
    }
    
    //returns true if pnt is one of the vertices of pgn
    private boolean isVertex(Point pnt, Polygon pgn)
    {
        for (int i=0; i<pgn.npoints; i++)
        {
            if (pgn.xpoints[i] == pnt.x
                    && pgn.ypoints[i] == pnt.y)
                return true;
        }
        return false;
    }
    
    //returns true if a line drawn from a to b intersects with a line drawn
    //from c to d
    private boolean segmentsIntersect(Point a, Point b, Point c, Point d)
    {   
        //find a, b and c values for both line segments
        double a1 = (b.y - a.y) * -1;
        double b1 = (b.x - a.x);
        double c1 = (a1 * a.x + b1 * a.y);
        
        double a2 = (d.y - c.y) * -1;
        double b2 = (d.x - c.x);
        double c2 = (a2 * c.x + b2 * c.y);
        
        if (Math.abs(a1 / a2 - b1 / b2) < 0.001)
            return false; //lines are parallel
        
        //use cramer's rule to find intersect point
        double denominator = (a1 * b2) - (b1 * a2);
        double xnumerator = (c1 * b2) - (b1 * c2);
        double ynumerator = (a1 * c2) - (c1 * a2);
        double x = xnumerator / denominator;
        double y = ynumerator / denominator;
        //now determine if intersect point is within both segments
        
        Point intersect = new Point((int)Math.round(x),(int)Math.round(y));
        intersections.add(intersect);
        
        if (isWithinSegment(a,b,intersect) && isWithinSegment(c,d,intersect))
        {
            segmentIntersections.add(intersect);
            return true;
        }
        else
        {
            //alert("false");
            return false;
        }
    }
    
    private boolean specialCaseApplies(Point a, Point b)
    {
        int subA = findPolygonSubscript(a);
        int subB = findPolygonSubscript(b);
        
        if (subA == subB)
            return false; //a and b are on same polygon, case doesnot apply
        if (subA != -1)
        {
            if (segmentIntersectsInterior(a,b,polygons[subA]))
                return true;
        }
        if (subB != -1)
        {
            
            if (segmentIntersectsInterior(b,a,polygons[subB]))
                return true;
        }
        return false;
    }
    
    //assumes a is a vertex of polygon p and b is not a vertex of p
    //returns true if segment contains any vertices of p other than a
    private boolean segmentIntersectsInterior(Point a, Point b, Polygon p)
    {
        for (int i=0; i<p.npoints; i++)
        {
            //if polygons[i] != a
            if (!(p.xpoints[i] == a.x && p.ypoints[i] == a.y))
            {
                if (isWithinSegment2(a,b,new Point(p.xpoints[i],p.ypoints[i])))
                    return true;
            }
        }
        return false;
    }
    
    //searches the vertices of each polygon in the Polygons[] array
    //returns the subscript of the polygon which contains p or -1
    private int findPolygonSubscript(Point p)
    {
        for (int i=0; i<polygons.length; i++)
        {
            for (int j=0; j<polygons[i].npoints; j++)
            {
                if (polygons[i].xpoints[j] == p.x 
                        && polygons[i].ypoints[j] == p.y)
                    return i;
            }
        }
        return -1;
    }
    
    //returns the subscript of point pnt within polygon pgn or -1
    private int findVertexSubscript(Point pnt, Polygon pgn)
    {
        for (int i=0; i<pgn.npoints; i++)
        {
            if (pgn.xpoints[i] == pnt.x 
                    && pgn.ypoints[i] == pnt.y)
                return i;
        }
        return -1;
    }
    
    //returns true if point c is within segment ab
    private boolean isWithinSegment(Point a, Point b, Point c)
    {
        
        int dp = dotProduct(difference(b, a), difference(c, a));
        return (dp > 0 && dp < Point.distanceSq(a.x, a.y, b.x, b.y));
    }
    
    private boolean isWithinSegment2(Point a, Point b, Point c)
    {
        
         double d_ac = Point.distance(a.x, a.y, c.x, c.y);
         double d_cb = Point.distance(c.x, c.y, b.x, b.y);
         double d_ab = Point.distance(a.x, a.y, b.x, b.y);
         double diff = Math.abs(d_ab - (d_ac + d_cb));
         /*
         if (a == startPoint && b == endPoint && c.x == 361 && c.y == 528)
            alert("d_ac=" + d_ac + " d_cb=" + d_cb + " d_ab=" + d_ab + " diff=" + diff);
          * */
         //alert("diff="+diff);
         if (diff < EPSILON)
             return true;
         else
             return false;        
    }

    private Point difference(Point a, Point b)
    {
        return new Point(b.x - a.x, b.y - a.y);
    }
    
    private int dotProduct(Point a, Point b)
    {
        return a.x * b.x + a.y * b.y;
    }
    
    private int crossProduct(Point a, Point b)
    {
        return a.x * b.y - a.y * b.x;
    }
    
    private int max(int a, int b)
    {
        return (a > b) ? a : b;
    }
    
    private int min(int a, int b)
    {
        return (a < b) ? a : b;
    }
}