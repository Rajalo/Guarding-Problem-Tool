import javax.swing.*;
import java.awt.*;

/**
 * Shows information about the process of the algorithm
 */
public class AlgebraPanel extends JPanel{
    public AlgebraPanel()
    {
        setBackground(new Color(250,210,250));
    }

    /**
     * Paints the info about the next clip on the right side of the screen
     * @param g Graphics object used by JPanel
     */
    public void paint(Graphics g)
    {
        super.paintComponent(g);
        if (Main.phase== Main.PhaseType.DRAW)
            return;
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 1.4F);
        g.setFont(newFont);
        g.drawString("w(P) >= " + Main.gpanel.witnesses.size(),20,40);
        if (Main.gpanel.addWitnessError)
        {
            g.drawString("Invalid witness added, make sure it doesn't overlap with the others",20, 70);
            g.drawString("Left click to try adding an independent witness again",20, 100);

        }
        if (Main.phase == Main.PhaseType.WITNESS)
            return;
        if (Main.gpanel.guards.size()>Main.gpanel.witnesses.size())
            g.drawString("Try to see if you can fill the polygon with less. We want it to be " + Main.gpanel.witnesses.size(),20,70);
        if (Main.gpanel.guards.size()==Main.gpanel.witnesses.size())
        {
            g.drawString("You're golden, make sure the whole polygon is green",20,70);
            g.drawString("If it is, g(P) = " +Main.gpanel.witnesses.size(),20,100);
        }

        g.setFont(currentFont);
    }
}
