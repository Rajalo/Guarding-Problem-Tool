import java.awt.*;
import java.util.ArrayList;

public class GuardWitness extends Vertex{
    private SimplePolygon witnessPolygon;
    /**
     * Constructs a vertex
     *
     * @param x the x-coord of the vertex
     * @param y the y-coord of the vertex
     */
    public GuardWitness(int x, int y, SimplePolygon outside, boolean isWitness) {
        super(x, y);
        witnessPolygon = constructWitnessPolygon(outside);
        witnessPolygon.setColor((isWitness)?new Color(200,150,150):new Color(150,200,150));
    }

    /**
     * Makes the color of the witnessPolygon lighter for if the witness is invalid
     */
    public void ghostify()
    {
        Color old = witnessPolygon.getColor();
        witnessPolygon.setColor(new Color(old.getRed()+50,old.getGreen()+50,old.getBlue()+50));
    }

    public SimplePolygon getWitnessPolygon() {
        return witnessPolygon;
    }

    /**
     * Constructs a witness polygon from the polygon the witness/guard point is inside
     * @param outside the polygon in which the witness/guard point lies
     * @return the subpolygon that the witness/guard point sees
     */
    public SimplePolygon constructWitnessPolygon(SimplePolygon outside)
    {
        ArrayList<Vertex> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        int i = 0;
        for (Vertex vertex: outside.vertices)
        {
            if (outside.seesBoundary(vertex,this))
            {
                vertices.add(vertex);
                indices.add(i);
            }
            i++;
        }
        for (int j = 0; j < vertices.size();j++)
        {
            if ((indices.get((j+1)%indices.size())-indices.get(j)+outside.vertices.size())%outside.vertices.size()!=1)
            {
                for (int k = (indices.get(j)+1)%outside.vertices.size();k!=indices.get(j);k=(k+1)%outside.vertices.size())
                {
                    Vertex intersection = outside.edges.get(k).linearIntersection(this,vertices.get(j), outside.clockwise);
                    if (intersection!= null&&outside.seesBoundary(vertices.get(j),intersection)&&!intersection.equals(vertices.get((j+vertices.size()-1)% vertices.size())))
                    {
                        vertices.add(j+1,intersection);
                        indices.add(j+1,(indices.get(j)+1)%outside.vertices.size());
                        j++;
                        break;
                    }
                }
                for (int k = (indices.get((j+1)%indices.size())+1)%outside.vertices.size();k!=indices.get((j+1)%indices.size());k=(k+1)%outside.vertices.size())
                {
                    Vertex intersection = outside.edges.get(k).linearIntersection(this,vertices.get((j+1)%indices.size()), outside.clockwise);
                    if (intersection!= null&&outside.seesBoundary(vertices.get((j+1)%vertices.size()),intersection)&&!intersection.equals(vertices.get(j)))
                    {
                        vertices.add(j+1,intersection);
                        indices.add(j+1,(indices.get(j)+1)%outside.vertices.size());
                        j++;
                        break;
                    }
                }
            }
        }
        return new SimplePolygon(vertices,false);
    }

    /**
     * Paints the point and the witnessPolygon
     * @param g
     * @param index
     */
    public void paint(Graphics g, int index)
    {
        witnessPolygon.paintsub(g);
        g.setColor(new Color(100,100,150));
        g.fillOval(this.getX()-5,this.getY()-5,10,10);
        g.drawString(""+index,this.getX()+10,this.getY()+10);
    }
}
