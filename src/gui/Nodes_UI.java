package gui;


import api.NodeData;
import implementations.DWG;
import implementations.DWGAlgorithms;
import implementations.Node;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class Nodes_UI extends JComponent {

    private final DWGAlgorithms algo;
    private final DWG graph;
    private Iterator<Node> iterator;
    private final window_GUI graph_ui;
    private int HEIGHT;
    private int WIDTH;

    public static double Xmax = Double.MIN_VALUE;
    public static double Ymax = Double.MIN_VALUE;
    public static double Xmin = Double.MAX_VALUE;
    public static double Ymin = Double.MAX_VALUE;

    public Nodes_UI(DWGAlgorithms algo, window_GUI graph_ui) {
        this.algo = algo;
        graph = (DWG) algo.getGraph();
        this.graph_ui = graph_ui;
        WIDTH = graph_ui.getWidth();
        HEIGHT = graph_ui.getHeight();
        getCoordBounds();
        setBounds(0, 0, WIDTH, HEIGHT);
    }

    private void getCoordBounds() {
        iterator = graph.nodeIter();

        Xmax = Double.MIN_VALUE;
        Ymax = Double.MIN_VALUE;
        Xmin = Double.MAX_VALUE;
        Ymin = Double.MAX_VALUE;

        while (iterator.hasNext()) {
            NodeData node = iterator.next();

            if (Xmax < node.getLocation().x()) {
                Xmax = node.getLocation().x();
            }
            if (Ymax < node.getLocation().y()) {
                Ymax = node.getLocation().y();
            }
            if (Xmin > node.getLocation().x()) {
                Xmin = node.getLocation().x();
            }
            if (Ymin > node.getLocation().y()) {
                Ymin = node.getLocation().y();
            }
        }
    }

    private void updateSizes(){
        WIDTH = graph_ui.getWidth();
        HEIGHT = graph_ui.getHeight();
        setBounds(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateSizes();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.RED);

        iterator = graph.nodeIter();
        while (iterator.hasNext()) {
            NodeData node = iterator.next();
            // Linearly map the point
            double x = node.getLocation().x() - Xmin;
            double y = node.getLocation().y() - Ymin;
            int scaled_y = (int) ((y / (Ymax - Ymin)) * HEIGHT * 0.8);
            int scaled_x = (int) ((x / (Xmax - Xmin)) * WIDTH * 0.8) + (int) (0.08 * WIDTH);
            g2d.setPaint(Color.white);
            g2d.fillOval(scaled_x,scaled_y , 18, 18);

            // changed
            if(graph_ui.unique_nodes!=null && graph_ui.unique_nodes.contains(node.getKey())){
                g2d.setPaint(Color.GREEN);
                g2d.fillOval(scaled_x,scaled_y , 18, 18);
            }
            else{g2d.setPaint(Color.RED);}

            g2d.drawOval(scaled_x,scaled_y , 18, 18);
            g2d.setPaint(Color.BLACK);
            g2d.drawString(String.valueOf(node.getKey()), scaled_x+3,scaled_y+14);
        }
    }

}
