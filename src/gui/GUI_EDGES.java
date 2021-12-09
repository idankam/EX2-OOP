package gui;

import api.EdgeData;
import implementations.DWG;
import implementations.DWGAlgorithms;
import implementations.Edge;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class GUI_EDGES extends JComponent {

    private int HEIGHT;
    private int WIDTH;

    private final DWGAlgorithms dwgAlgorithms;
    private final DWG dwGraph;
    private Iterator<Edge> edgeIterator;
    private GUI_WINDOW gui_window;

    private double X_maxValue = GUI_NODES.X_maxValue;
    private double Y_maxValue = GUI_NODES.Y_maxValue;
    private double X_minValue = GUI_NODES.X_minValue;
    private double Y_minValue = GUI_NODES.Y_minValue;

    public GUI_EDGES(DWGAlgorithms dwgAlgorithms, GUI_WINDOW gui_window) {
        this.dwgAlgorithms = dwgAlgorithms;
        dwGraph = (DWG) dwgAlgorithms.getGraph();
        edgeIterator = dwGraph.edgeIter();
        this.gui_window = gui_window;
        WIDTH = gui_window.getWidth();
        HEIGHT = gui_window.getHeight();
    }

    private void updateWidthHeight() {
        HEIGHT = gui_window.getHeight();
        WIDTH = gui_window.getWidth();
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        updateWidthHeight();
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setPaint(Color.BLACK);

        ArrayList<String> unique_edges = new ArrayList<>();
        if(gui_window.unique_nodes.size()>1){
            for(int i = 0; i < gui_window.unique_nodes.size()-1; i++){
                unique_edges.add(gui_window.unique_nodes.get(i) + "," + gui_window.unique_nodes.get(i+1));
            }
        }

        edgeIterator = dwGraph.edgeIter();
        while (edgeIterator.hasNext()) {
            EdgeData curr_edge = edgeIterator.next();

            double x_src = dwGraph.getNode(curr_edge.getSrc()).getLocation().x() - X_minValue;
            double x_dest = dwGraph.getNode(curr_edge.getDest()).getLocation().x() - X_minValue;
            double y_src = dwGraph.getNode(curr_edge.getSrc()).getLocation().y() - Y_minValue;
            double y_dest = dwGraph.getNode(curr_edge.getDest()).getLocation().y() - Y_minValue;

            x_src = (int) ((x_src / (X_maxValue - X_minValue)) * WIDTH * 0.8) + (int) (0.08 * WIDTH) + 7;
            x_dest = (int) ((x_dest / (X_maxValue - X_minValue)) * WIDTH * 0.8) + (int) (0.08 * WIDTH) + 7;
            y_src = (int) ((y_src / (Y_maxValue - Y_minValue)) * HEIGHT * 0.8) + 10;
            y_dest = (int) ((y_dest / (Y_maxValue - Y_minValue)) * HEIGHT * 0.8) + 10;

            if(unique_edges.contains(curr_edge.getSrc()+","+ curr_edge.getDest()) || unique_edges.contains(curr_edge.getDest()+","+ curr_edge.getSrc())) {
                graphics2D.setPaint(Color.GREEN);
                graphics2D.draw(new Line2D.Double(x_src, y_src, x_dest, y_dest));
            }else{
                graphics2D.setPaint(Color.BLACK);
                graphics2D.draw(new Line2D.Double(x_src, y_src, x_dest, y_dest));
            }
            if(unique_edges.contains(curr_edge.getSrc()+","+ curr_edge.getDest())){

                graphics2D.setPaint(Color.BLACK);
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.DOWN);
                if(x_dest>x_src){
                    graphics2D.drawString(String.valueOf(df.format(curr_edge.getWeight())), (int)(x_src+x_dest)/2 +6,(int)(y_src+y_dest)/2 +6);
                }
                else{
                    graphics2D.drawString(String.valueOf(df.format(curr_edge.getWeight())), (int)(x_src+x_dest)/2 -6,(int)(y_src+y_dest)/2 -6);
                }
            }
        }
    }
}
