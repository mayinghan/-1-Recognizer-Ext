package $1;

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class Canvas {
    public static void main(String[] args) {
        new Canvas().show();
    }

    public void show() {
        JFrame frame = new JFrame("$1 Recognizer");
        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());

        final DrawPad draw = new DrawPad();

        JPanel panel = new JPanel();
        JTextField textField = new JTextField(8);
        JButton recognize = new JButton("Recognize");
        JButton addTemplate = new JButton("Add template");
        JButton clear = new JButton("Clear");



        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == recognize) {
                    draw.showResult();
                }
            }
        };

        ActionListener add = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == addTemplate) {
                    String name = textField.getText();
                    if(name == null || name.length() == 0) {
                        System.out.println("Empty string");
                    } else {
                        draw.addTemplates(name);
                    }

                }
            }
        };

        ActionListener cl = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                draw.clear();
            }
        };

        recognize.addActionListener(al);
        addTemplate.addActionListener(add);
        clear.addActionListener(cl);
        panel.add(recognize);
        panel.add(addTemplate);
        panel.add(textField);
        panel.add(clear);

        //add components to content panel
        content.add(draw, BorderLayout.CENTER);
        content.add(panel, BorderLayout.SOUTH);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}




class DrawPad extends JComponent{
    int currX, currY, prevX, prevY;
    java.util.List<Point> p = new ArrayList<>();

    Graphics2D graph;
    Image image;
    Recognizer rec = new Recognizer(false);

    public DrawPad() {
        setDoubleBuffered(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prevX = e.getX();
                prevY = e.getY();
                p.add(new Point(prevX, prevY));
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                currX = e.getX();
                currY = e.getY();
                p.add(new Point(currX, currY));

                if(graph != null) {
                    graph.drawLine(prevX, prevY, currX, currY);
                }
                //update draw area
                repaint();
                //update the data
                prevX = currX;
                prevY = currY;
            }
        });
    }

    public void showResult() {
        Long time = System.currentTimeMillis();
        //if(p == null) System.out.println("userinput is null");
        Result r = rec.recognize(p);
        String result = r.result;
        double score = r.score;
        time = System.currentTimeMillis() - time;
        JOptionPane.showMessageDialog(null, "result: " + result + "\ntime: " + time + "milliseconds\n" + "score: " + score, "Result",JOptionPane.INFORMATION_MESSAGE);
        p.clear();
        clear();
    }

    public void paintComponent(Graphics g) {
        if(image == null) {
            image = createImage(getSize().width, getSize().height);
            graph = (Graphics2D) image.getGraphics();

            graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            clear();
        }
        g.drawImage(image, 0, 0, null);
    }

    public void addTemplates(String name) {
        rec.addTemplate(p, name);
        JOptionPane.showMessageDialog(null, "Add " + name + " as template successfully!", "Hint", JOptionPane.INFORMATION_MESSAGE);
        p.clear();
        clear();
    }

    public void clear() {
        p.clear();
        graph.setPaint(Color.white);
        graph.fillRect(0, 0, getSize().width, getSize().height);
        graph.setPaint(Color.blue);
        repaint();
    }

}
