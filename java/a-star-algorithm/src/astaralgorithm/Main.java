package astaralgorithm;
import javax.swing.*;

public class Main extends JFrame
{
    public static void main(String[] args)
    {
        JFrame.setDefaultLookAndFeelDecorated(true);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "error");
        }
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                Main w = new Main();
                w.setVisible(true);
            }
        });
    }
    
    public Main()
    {
        super("A* Search Demo - Brett Tolbert");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PolygonPanel pp = new PolygonPanel();
        this.add(pp);
        this.pack();
        this.setVisible(true);
        pp.reset();
        pp.invalidate();
    }
}