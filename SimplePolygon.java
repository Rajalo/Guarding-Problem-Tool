import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class SimplePolygon {
    ArrayList<Vertex> vertices;
    ArrayList<Edge> edges;
    boolean clockwise;
    int[][] coordsArray;
    Color color = new Color(150,160,250);
    public static Color[] colors = {new Color(170,170,170),new Color(200,100,100),new Color(100,200,100),new Color(100,100,200)};

    /**
     * Constructs a SimplePolygon from an array of vertices
     * @param vertices the vertices of the polygon
     */
    public SimplePolygon(Vertex[] vertices)
    {
        this.vertices = new ArrayList<>();
        this.edges = new ArrayList<>();
        Collections.addAll(this.vertices, vertices);
        if (vertices.length==0)
        {
            return;
        }
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(Edge.polygonalEdge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        Vertex lowest = this.vertices.get(0);
        for (Vertex vertex:this.vertices)
        {
            if (vertex.getY()<lowest.getY()||(vertex.getY()==lowest.getY()&&vertex.getX()<lowest.getX()))
                lowest = vertex;
            vertex.setColor(colors[0]);
        }
        clockwise = left(lowest.getCoordsArr(),lowest.getPrev().getCoordsArr(),lowest.getNext().getCoordsArr());
    }
    /**
     * Constructs a SimplePolygon from a list of vertices
     * @param vertices the vertices of the polygon
     */
    public SimplePolygon(ArrayList<Vertex> vertices)
    {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>();
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(Edge.polygonalEdge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        Vertex lowest = this.vertices.get(0);
        for (Vertex vertex:this.vertices)
        {
            if (vertex.getX()<lowest.getX())
                lowest = vertex;
            vertex.setColor(colors[0]);
        }
        clockwise = left(lowest.getCoordsArr(),lowest.getPrev().getCoordsArr(),lowest.getNext().getCoordsArr());
    }
    public SimplePolygon(ArrayList<Vertex> vertices, boolean b)
    {
        this.vertices = new ArrayList<>(vertices);
        this.edges = new ArrayList<>();
        for (int i = 0; i < this.vertices.size(); i++)
        {
            edges.add(new Edge(this.vertices.get(i),this.vertices.get((i+1)%this.vertices.size())));
        }
        clockwise = b;
    }

    /**
     * Tests to see if two subpolygons of the same polygon overlap
     * @param other the other subpolygon
     * @return true if the subpolygons overlap, false otherwise
     */
    public boolean overlapInternal(SimplePolygon other)
    {
        for (Vertex vertex: vertices)
        {
            if (other.vertices.contains(vertex))
            {
                return true;
            }
        }
        for (Edge edge: edges)
        {
            for (Edge edge1: other.edges)
            {
                if (intersectsProp(edge.getStart().getCoordsArr(),edge.getEnd().getCoordsArr(),edge1.getStart().getCoordsArr(),edge1.getEnd().getCoordsArr()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Draws the polygon
     * @param g the graphics object being used to draw
     */
    public void paint(Graphics g)
    {
        g.setColor(color);
        g.fillPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        g.setColor(Color.BLACK);
        g.drawPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        int i = 0;
        for (Vertex vertex : vertices)
        {
            g.setColor(vertex.getColor());
            g.fillOval(vertex.getX()-5,vertex.getY()-5,10,10);
            g.drawString(""+i++, vertex.getX()+5, vertex.getY()+5);
        }
    }
    /**
     * Draws the polygon without vertex labels
     * @param g the graphics object being used to draw
     */
    public void paintsub(Graphics g)
    {
        g.setColor(color);
        g.fillPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
        g.setColor(Color.BLACK);
        g.drawPolygon(getCoordsArray()[0],getCoordsArray()[1],vertices.size());
    }

    /**
     * Generates a coordinate array of the vertices compliant with g.drawPolygon()'s specifications
     * @return coordinate array
     */
    public int[][] getCoordsArray() {
        if (coordsArray != null&&coordsArray.length== vertices.size())
            return coordsArray;
        coordsArray = new int[2][vertices.size()];
        int i = 0;
        for (Vertex vertex : vertices)
        {
            coordsArray[0][i] = vertex.getX();
            coordsArray[1][i++] = vertex.getY();
        }
        return coordsArray;
    }

    /**
     * Determines if two vertices can see each other
     * @param v0 first vertex
     * @param v1 second vertex
     * @return true if no edges overlap with line between vertices, false otherwise.
     */
    public boolean diagonalie(Vertex v0, Vertex v1)
    {
        for (Edge edge : edges)
        {
            if (edge.contains(v0)||edge.contains(v1))
            {
                continue;
            }
            if (intersectsProp(edge.getStart().getCoordsArr(),edge.getEnd().getCoordsArr(),v0.getCoordsArr(), v1.getCoordsArr()))
            {
                return false;
            }
        }
        return true;
    }
    /**
     * Determines whether a vertex is in the "open cone" of another vertex.
     * @param v0 vertex who's in the cone is analyzed
     * @param v1 vertex who may or may not be in the cone
     * @return true if vertex is in the cone, false otherwise
     */
    public boolean inCone(Vertex v0, Vertex v1)
    {
        Vertex next = vertices.get((vertices.indexOf(v0)+1)% vertices.size());
        Vertex prev = vertices.get((vertices.indexOf(v0)-1+vertices.size())% vertices.size());
        if (leftOn(v0.getCoordsArr(),next.getCoordsArr(),prev.getCoordsArr()))
        {
            return left(v0.getCoordsArr(),v1.getCoordsArr(),prev.getCoordsArr())&&left(v1.getCoordsArr(),v0.getCoordsArr(),next.getCoordsArr());
        }
        return !(leftOn(v0.getCoordsArr(),v1.getCoordsArr(),next.getCoordsArr())&&leftOn(v1.getCoordsArr(),v0.getCoordsArr(),prev.getCoordsArr()));
    }

    /**
     * Used to find if a point inside the polygon sees a vertex of that polygon
     * @param boundary a vertex of the polygon
     * @param point the point inside the polygon (or not)
     * @return returns true if point is in the polygon and can see the boundary vertex, false otherwise
     */
    public boolean seesBoundary(Vertex boundary, Vertex point)
    {
        return diagonalie(boundary,point)&&(inCone(boundary,point)!=clockwise);
    }
    
    /**
     * Determines if two line segments intersect
     * @param a first endpoint of first line segment
     * @param b second endpoint of first line segment
     * @param c first endpoint of second line segment
     * @param d first endpoint of second line segment
     * @return true if they intersect, false otherwise
     */
    public static boolean intersectsProp(int[] a, int[] b, int[] c, int[] d)
    {
        if (collinear(a,b,c) && collinear(a,b,d))
        {
            return (between(a,b,c)||between(a,b,d));
        }
        if ((between(a,b,c)||between(a,b,d)||between(c,d,a)||between(c,d,b)))
        {
            return true;
        }
        if (collinear(a,b,c) || collinear(a,b,d) || collinear (c, d, a) || collinear(c,d,b))
        {
            return false;
        }
        return (left(a,b,c) != left(a,b,d)) && (left(c,d,a) != left(c,d,b));
    }
    /**
     * Determines if a point is between two other points (all collinear)
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point between two other points
     * @return true if c is between a and b, false elsewise.
     */
    public static boolean between(int[]a, int[] b, int[] c)
    {
        if (!collinear(a,b,c))
            return false;
        if (a[0] != b[0])
            return ((a[0] <= c[0]) && (c[0] <=b[0])) ||
                    ((a[0] >= c[0]) && (c[0] >= b[0]));
        return ((a[1] <= c[1]) && (c[1] <= b[1])) ||
                ((a[1] >= c[1]) && (c[1] >= b[1]));
    }
    /**
     * Determines if a point is to the left of two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be to the left
     * @return true if c is to the left of a and b, false elsewise.
     */
    public static boolean left(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) > 0;
    }
    /**
     * Determines if a point is to the left of or inline with two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be to the left
     * @return true if c is to the left of or collinear with a and b, false elsewise.
     */
    public static boolean leftOn(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) >= 0;
    }
    /**
     * Determines if a point is to the collinear with two other points
     * @param a coordinates of a point
     * @param b coordinates of another point
     * @param c coordinates of the point that may or may not be collinear.
     * @return true if c is to the left of a and b, false elsewise.
     */
    public static boolean collinear(int[]a, int[] b, int[] c) {
        return crossProduct(a,b,a,c) == 0;
    }
    /**
     * Determines signed area of the parallelogram with points a,b,c,d
     * @param a coordinates of point a
     * @param b coordinates of point b
     * @param c coordinates of point c
     * @param d coordinates of point d
     * @return signed area of the parallelogram with points a,b,c,d
     */
    public static int crossProduct(int[] a, int[] b, int[] c, int[]d)
    {
        return (b[0] - a[0]) * (d[1] - c[1]) - (b[1] - a[1]) * (d[0] - c[0]);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("SimplePolygon{" +
                "vertices=");
        for (Vertex vertex: vertices)
        {
            str.append(Main.gpanel.vertices.indexOf(vertex)).append(",");
        }
        return str+"}";
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
