package gui;


import implementations.DWG;
import implementations.DWGAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class window_GUI extends JFrame {

    private DWGAlgorithms algo;
    private DWG graph;
    private Nodes_UI nodes_ui;
    private Edges_UI edges_ui;
    private Menu_GUI menu;
    public boolean is_initialized = false;
    public ArrayList<String> unique_edges = null;
    public ArrayList<Integer> unique_nodes = new ArrayList<>();

//    public static final int HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
//    public static final int WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2);

    public window_GUI() {
        this.setTitle("GUI");
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new Menu_GUI(this);

        setJMenuBar(menu.getMenuBar());
        setVisible(true);

        setResizable(true);
    }

    public DWGAlgorithms getAlgo() {return algo;}

    public void setAlgo(DWGAlgorithms algo){
        this.algo = algo;
    }

    public void setNodes_ui(Nodes_UI nodes){
        this.nodes_ui = nodes;
    }

    public void setEdges_ui(Edges_UI edges){
        this.edges_ui = edges;
    }


    public static void main(String[] args) {
        window_GUI test = new window_GUI();
    }
}
