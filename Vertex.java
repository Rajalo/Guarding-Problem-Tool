import java.awt.*;
import java.util.Objects;

public class Vertex {
    private final int x,y;
    private Vertex next,prev;
    private Color color;

    /**
     * Constructs a vertex
     * @param x the x-coord of the vertex
     * @param y the y-coord of the vertex
     */
    public Vertex(int x,int y)
    {
        this.x = x;
        this.y = y;
        color = new Color(170,170,170);
    }

    /**
     * Makes an array with the coords of the vertex
     * @return integer array [x,y]
     */
    public int[] getCoordsArr()
    {
        return new int[] {x,y};
    }

    /**
     * Makes an array with the coords of the vertex as doubles
     * @return double array [x,y]
     */
    public double[] getCoordsDoubleArr()
    {
        return new double[] {x,y};
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vertex getNext() {
        return next;
    }
    public Vertex getPrev() {
        return prev;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public void setNext(Vertex next) {
        this.next = next;
    }

    public void setPrev(Vertex prev) {
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return x == vertex.x &&
                y == vertex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
