package gui;

import implementations.DWG;
import implementations.DWGAlgorithms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;



public class GUI_WINDOW extends JFrame {

    private DWGAlgorithms dwg_algorithms;
    private DWG dwg_graph;
    private GUI_NODES nodes_gui;
    private GUI_EDGES edges_gui;
    private GUI_MENU menu_gui;
    public ArrayList<Integer> unique_nodes = new ArrayList<>();

    public GUI_WINDOW() {
        setUP();
        setVisible(true);
        setResizable(true);
    }

    public GUI_WINDOW(String filename) {
        setUP();
        this.dwg_algorithms = new DWGAlgorithms();
        this.dwg_algorithms.load(filename);
        this.dwg_graph = (DWG) this.dwg_algorithms.getGraph();

        setVisible(true);
        setResizable(true);
        this.show_graph();
    }

    public void setUP(){
        this.setTitle("GUI_WINDOW");
        this.setLayout(new BorderLayout());
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        menu_gui = new GUI_MENU(this);
        setJMenuBar(menu_gui.getMenuBar());
    }

    public DWGAlgorithms getDwg_algorithms() {return dwg_algorithms;}

    public void setDwg_algorithms(DWGAlgorithms dwg_algorithms){
        this.dwg_algorithms = dwg_algorithms;
    }

    public void set_GUI_NODES(GUI_NODES nodes){
        this.nodes_gui = nodes;
    }

    public void set_GUI_EDGES(GUI_EDGES edges){
        this.edges_gui = edges;
    }

    private void show_graph() {
        GUI_NODES nodes = new GUI_NODES(this.dwg_algorithms, this);
        GUI_EDGES edges = new GUI_EDGES(this.dwg_algorithms, this);
        this.set_GUI_NODES(nodes);
        this.set_GUI_EDGES(edges);
        this.add(nodes, BorderLayout.CENTER);
        this.add(edges, BorderLayout.CENTER);
        this.setVisible(true);
        this.setResizable(true);
    }
    public static void main(String[] args) {
        GUI_WINDOW test = new GUI_WINDOW();
    }
}
