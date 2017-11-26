package astaralgorithm;
import java.awt.Point;

//this represents a Node used by the search algorithm
public class Node implements Comparable
{
    //static value representing the x and y values of the goal node
    //this is used to compute the heuristic
    public static Point goalPosition;
    
    Point position; //the x and y coordinate of this node
    Node parent; //reference to parent node, possibly null
    
    //this constructor is used to create a node with 
    public Node(Point position, Node parent)
    {
        this.parent = parent;
        this.position = position;
    }
    
    public Node()
    {
        this.parent = null;
        this.position = null;
    }
    
    //computes the f(x) for this node ( f(x) = g(x) + h(x)
    public double getF() { return getG() + getH(); }
    
    //calcuates the h(x) (heuristic estimate) of this node which is the 
    //euclidian distance between this node and the goal node
    public double getH() { return Point.distance(position.x, position.y, 
            goalPosition.x, goalPosition.y);
            //Vector2.distance(position, goalPosition); 
    }
    
    //recursively calculates the g(x) (actual cost) of this node by adding
    //the g(x) of each anscestor node, if this node has no parent, g(x) = 0
    public double getG()
    {
        return (parent == null) ? 0 :
            Point.distance(position.x, position.y, parent.getPosition().x, parent.getPosition().y) + parent.getG();
            //Vector2.distance(position, parent.getPosition()) + parent.getG();
    }
    
    //returns true if the position of this node equals the position of the goal node
    public boolean isGoal() { return (position.equals(goalPosition)); }
    
    public Point getPosition() { return position; }
    
    public int getX() { return position.x; }
    public int getY() { return position.y; }
    
    public int compareTo(Object obj)
    {
        if (this.getF() > ((Node)obj).getF())
            return 1;
        else if (this.getF() < ((Node)obj).getF())
            return -1;
        else
            return 0;
    }
}
