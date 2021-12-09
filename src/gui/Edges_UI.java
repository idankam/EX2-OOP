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

public class Edges_UI extends JComponent {
    private final DWGAlgorithms algo;
    private final DWG graph;
    private Iterator<Edge> iterator;
    private window_GUI graph_ui;
    private int HEIGHT;
    private int WIDTH;

    private double Xmax = Nodes_UI.Xmax;
    private double Ymax = Nodes_UI.Ymax;
    private double Xmin = Nodes_UI.Xmin;
    private double Ymin = Nodes_UI.Ymin;

    public Edges_UI(DWGAlgorithms algo, window_GUI graph_ui) {
        this.algo = algo;
        graph = (DWG) algo.getGraph();
        iterator = graph.edgeIter();
        this.graph_ui = graph_ui;

        WIDTH = graph_ui.getWidth();
        HEIGHT = graph_ui.getHeight();
    }

    private void updateSizes() {
        WIDTH = graph_ui.getWidth();
        HEIGHT = graph_ui.getHeight();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        updateSizes();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.BLACK);

        ArrayList<String> unique_edges = new ArrayList<>();
        if(graph_ui.unique_nodes.size()>1){
            for(int i =0 ; i <graph_ui.unique_nodes.size()-1; i++){
                unique_edges.add(graph_ui.unique_nodes.get(i) + "," + graph_ui.unique_nodes.get(i+1));
            }
        }


        iterator = graph.edgeIter();
        while (iterator.hasNext()) {
            EdgeData edge = iterator.next();

            double x1 = graph.getNode(edge.getSrc()).getLocation().x() - Xmin;
            double x2 = graph.getNode(edge.getDest()).getLocation().x() - Xmin;
            double y1 = graph.getNode(edge.getSrc()).getLocation().y() - Ymin;
            double y2 = graph.getNode(edge.getDest()).getLocation().y() - Ymin;

            x1 = (int) ((x1 / (Xmax - Xmin)) * WIDTH * 0.8) + (int) (0.08 * WIDTH) + 7;
            x2 = (int) ((x2 / (Xmax - Xmin)) * WIDTH * 0.8) + (int) (0.08 * WIDTH) + 7;
            y1 = (int) ((y1 / (Ymax - Ymin)) * HEIGHT * 0.8) + 10;
            y2 = (int) ((y2 / (Ymax - Ymin)) * HEIGHT * 0.8) + 10;

            if(unique_edges.contains(edge.getSrc()+","+ edge.getDest()) || unique_edges.contains(edge.getDest()+","+ edge.getSrc())) {
                g2d.setPaint(Color.GREEN);
                g2d.draw(new Line2D.Double(x1, y1, x2, y2));
            }else{
                g2d.setPaint(Color.BLACK);
                g2d.draw(new Line2D.Double(x1, y1, x2, y2));
            }
            if(unique_edges.contains(edge.getSrc()+","+ edge.getDest())){

                g2d.setPaint(Color.BLACK);
                DecimalFormat df = new DecimalFormat("#.##");
                df.setRoundingMode(RoundingMode.DOWN);
                if(x2>x1){
                    g2d.drawString(String.valueOf(df.format(edge.getWeight())), (int)(x1+x2)/2 +6,(int)(y1+y2)/2 +6);
                }
                else{
                    g2d.drawString(String.valueOf(df.format(edge.getWeight())), (int)(x1+x2)/2 -6,(int)(y1+y2)/2 -6);
                }
            }



        }

    }

    /**
     * Draw an arrow line between two points.
     * @param g the graphics component.
     * @param x1 x-position of first point.
     * @param y1 y-position of first point.
     * @param x2 x-position of second point.
     * @param y2 y-position of second point.
     * @param d  the width of the arrow.
     * @param h  the height of the arrow.
     */
    private void drawArrowLine(Graphics g, double x1, double y1, double x2, double y2, double d, double h) {
        double dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {(int)x2, (int) xm, (int) xn};
        int[] ypoints = {(int)y2, (int) ym, (int) yn};

        g.drawLine((int)x1,(int) y1,(int) x2,(int) y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }

}
