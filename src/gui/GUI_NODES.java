package gui;

import api.*;
import implementations.*;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class GUI_NODES extends JComponent {

    private int HEIGHT;
    private int WIDTH;

    private final DWGAlgorithms dwgAlgorithms;
    private final DWG dwGraph;
    private Iterator<NodeData> nodeIterator;
    private final GUI_WINDOW gui_window;

    public static double X_minValue = Double.MAX_VALUE;
    public static double X_maxValue = Double.MIN_VALUE;
    public static double Y_minValue = Double.MAX_VALUE;
    public static double Y_maxValue = Double.MIN_VALUE;

    public GUI_NODES(DWGAlgorithms dwgAlgorithms, GUI_WINDOW gui_window) {
        this.dwgAlgorithms = dwgAlgorithms;
        dwGraph = (DWG) dwgAlgorithms.getGraph();
        this.gui_window = gui_window;
        getMinMaxValues();
        WIDTH = gui_window.getWidth();
        HEIGHT = gui_window.getHeight();
        setBounds(0, 0, WIDTH, HEIGHT);
    }

    private void getMinMaxValues() {
        nodeIterator = dwGraph.nodeIter();
        X_maxValue = Double.MIN_VALUE;
        Y_maxValue = Double.MIN_VALUE;
        X_minValue = Double.MAX_VALUE;
        Y_minValue = Double.MAX_VALUE;

        while (nodeIterator.hasNext()) {
            NodeData curr_node = nodeIterator.next();

            if (Y_minValue > curr_node.getLocation().y()) {
                Y_minValue = curr_node.getLocation().y();
            }
            if (Y_maxValue < curr_node.getLocation().y()) {
                Y_maxValue = curr_node.getLocation().y();
            }
            if (X_minValue > curr_node.getLocation().x()) {
                X_minValue = curr_node.getLocation().x();
            }
            if (X_maxValue < curr_node.getLocation().x()) {
                X_maxValue = curr_node.getLocation().x();
            }
        }
    }

    private void updateWidthHeight(){
        HEIGHT = gui_window.getHeight();
        WIDTH = gui_window.getWidth();
        setBounds(0, 0, WIDTH, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        updateWidthHeight();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setPaint(Color.RED);

        nodeIterator = dwGraph.nodeIter();
        while (nodeIterator.hasNext()) {
            NodeData curr_node = nodeIterator.next();

            double x_node = curr_node.getLocation().x() - X_minValue;
            double y_node = curr_node.getLocation().y() - Y_minValue;
            int scaled_y = (int) ((y_node / (Y_maxValue - Y_minValue)) * 0.8 * HEIGHT );
            int scaled_x = (int) ((x_node / (X_maxValue - X_minValue)) * 0.8 *WIDTH ) + (int) (0.08 * WIDTH);
            graphics2D.setPaint(Color.white);
            graphics2D.fillOval(scaled_x,scaled_y , 18, 18);

            if(gui_window.unique_nodes.contains(curr_node.getKey())){
                graphics2D.setPaint(Color.GREEN);
                graphics2D.fillOval(scaled_x,scaled_y , 18, 18);
            }
            else{graphics2D.setPaint(Color.RED);}

            graphics2D.drawOval(scaled_x,scaled_y , 18, 18);
            graphics2D.setPaint(Color.BLACK);
            graphics2D.drawString(String.valueOf(curr_node.getKey()), scaled_x+3,scaled_y+14);
        }
    }
}
