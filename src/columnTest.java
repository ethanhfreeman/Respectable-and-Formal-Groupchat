import java.awt.*;
import javax.swing.*;

public class columnTest extends JFrame {
    public columnTest() {
        // Create the two tall panels
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(200, 400));
        panel1.setBackground(Color.RED);

        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(200, 400));
        panel2.setBackground(Color.BLUE);

        // Create the buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(new JButton("Button 1"));
        buttonPanel.add(new JButton("Button 2"));
        buttonPanel.add(new JButton("Button 3"));

        // Set the layout manager for the content pane
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Add the two tall panels with constraints
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.5;
        c.weighty = 1.0;
        getContentPane().add(panel1, c);

        c.gridx = 1;
        c.gridy = 0;
        getContentPane().add(panel2, c);

        // Add the buttons panel with constraints
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.insets = new Insets(10, 10, 10, 10);
        getContentPane().add(buttonPanel, c);

        // Set the frame properties
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new columnTest();
    }
}
