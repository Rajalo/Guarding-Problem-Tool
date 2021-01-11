import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Shows the left side of the screen with the polygon
 */
public class GraphPanel extends JPanel implements MouseListener, KeyListener {
    SimplePolygon polygon;
    ArrayList<Vertex> vertices;
    ArrayList<GuardWitness> witnesses;
    ArrayList<GuardWitness> guards;
    boolean addWitnessError = false;
    GuardWitness ghostWitness;
    static int pointerX,pointerY;
    public GraphPanel()
    {
        setBackground(Color.white);
        addMouseListener(this);
        setFocusable(true);
        addKeyListener(this);
        polygon = new SimplePolygon(new Vertex[]{});
        vertices = new ArrayList<>();
        witnesses = new ArrayList<>();
        guards = new ArrayList<>();
        repaint();
        pointerX = pointerY = -10;
    }
    /**
     * Paints the left side of the screen
     * @param g Graphics object used by JPanel
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        polygon.paint(g);
        int i = 1;
        if (Main.phase == Main.PhaseType.WITNESS)
        {
            for (GuardWitness witness : witnesses)
            {
                witness.paint(g,i);
                i++;
            }
            if (addWitnessError&&ghostWitness!=null)
                ghostWitness.paint(g,i);
        }
        if (Main.phase == Main.PhaseType.GUARD)
        {
            for (GuardWitness witness : guards)
            {
                witness.paint(g,i);
                i++;
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    /**
     * Determines what to do when mouse is pressed
     * @param e KeyEvent containing info on which mouse button was pressed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        addWitnessError = false;
        if (Main.phase == Main.PhaseType.DRAW) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                vertices.add(new Vertex(e.getX(), e.getY()));
                polygon = new SimplePolygon(vertices);
            }
            if (e.getButton() == MouseEvent.BUTTON3&&vertices.size()>0) {
                Vertex closest = vertices.get(0);
                double dist = Math.hypot(closest.getX() - e.getX(), closest.getY() - e.getY());
                for (int i = 1; i < vertices.size(); i++) {
                    Vertex vertex = vertices.get(i);
                    if (Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY()) < dist) {
                        dist = Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY());
                        closest = vertex;
                    }
                }
                vertices.remove(closest);
                polygon = new SimplePolygon(vertices);
            }
        }
        if (Main.phase == Main.PhaseType.WITNESS) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                GuardWitness witness = new GuardWitness(e.getX(),e.getY(),polygon,true);
                if (witness.getWitnessPolygon().vertices.size()<3)
                {
                    addWitnessError =true;
                    ghostWitness = witness;
                    ghostWitness.ghostify();
                    repaint();
                    Main.algebraPanel.repaint();
                    return;
                }
                for (GuardWitness witness1 : witnesses)
                {
                    if (witness1.getWitnessPolygon().overlapInternal(witness.getWitnessPolygon()))
                    {
                        addWitnessError =true;
                        ghostWitness = witness;
                        ghostWitness.ghostify();
                        repaint();
                        Main.algebraPanel.repaint();
                        return;
                    }
                }
                witnesses.add(witness);
            }
            if (e.getButton() == MouseEvent.BUTTON3&&vertices.size()>0) {
                Vertex closest = witnesses.get(0);
                double dist = Math.hypot(closest.getX() - e.getX(), closest.getY() - e.getY());
                for (int i = 1; i < witnesses.size(); i++) {
                    Vertex vertex = witnesses.get(i);
                    if (Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY()) < dist) {
                        dist = Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY());
                        closest = vertex;
                    }
                }
                witnesses.remove(closest);
            }
        }if (Main.phase == Main.PhaseType.GUARD) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                guards.add(new GuardWitness(e.getX(),e.getY(),polygon,false));
            }
            if (e.getButton() == MouseEvent.BUTTON3&&vertices.size()>0) {
                Vertex closest = guards.get(0);
                double dist = Math.hypot(closest.getX() - e.getX(), closest.getY() - e.getY());
                for (int i = 1; i < guards.size(); i++) {
                    Vertex vertex = guards.get(i);
                    if (Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY()) < dist) {
                        dist = Math.hypot(vertex.getX() - e.getX(), vertex.getY() - e.getY());
                        closest = vertex;
                    }
                }
                guards.remove(closest);
            }
        }
        repaint();
        Main.algebraPanel.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}