package gui;

import implementations.DWGAlgorithms;
import implementations.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Menu_GUI implements ActionListener {


    JMenuBar menu_bar;
    JMenu file, edit, nodes, edges, algorithms;
    JMenuItem save, load, add_node, add_edge, delete_node, delete_edge, is_connected_algo, shortest_path_weight_algo, find_center_algo, tsp_algo, shortest_path_list_algo;

    private DWGAlgorithms graph_algo;
    private window_GUI frame;

    public Menu_GUI(window_GUI g) {
        frame = g;
        //graph_algo = g.getAlgo();

        menu_bar = new JMenuBar();
        file = new JMenu("File");
        save = new JMenuItem("Save");
        load = new JMenuItem("Load");
        file.add(save);
        file.add(load);

        edit = new JMenu("Edit");
        nodes = new JMenu("Nodes");
        edges = new JMenu("Edges");
        add_node = new JMenuItem("Add Node");
        add_edge = new JMenuItem("Add Edge");
        delete_node = new JMenuItem("Remove Node");
        delete_edge = new JMenuItem("Remove Edge");
        edit.add(nodes);
        edit.add(edges);
        nodes.add(add_node);
        edges.add(add_edge);
        nodes.add(delete_node);
        edges.add(delete_edge);

        algorithms = new JMenu("Operations");
        is_connected_algo = new JMenuItem("Is Connected");
        shortest_path_weight_algo = new JMenuItem("Shortest Path Weight");
        find_center_algo = new JMenuItem("Center");
        tsp_algo = new JMenuItem("TSP");
        shortest_path_list_algo = new JMenuItem("Shortest Path");
        algorithms.add(is_connected_algo);
        algorithms.add(shortest_path_weight_algo);
        algorithms.add(shortest_path_list_algo);
        algorithms.add(find_center_algo);
        algorithms.add(tsp_algo);

        save.addActionListener(this);
        load.addActionListener(this);

        add_node.addActionListener(this);
        add_edge.addActionListener(this);
        delete_node.addActionListener(this);
        delete_edge.addActionListener(this);

        is_connected_algo.addActionListener(this);
        shortest_path_weight_algo.addActionListener(this);
        shortest_path_list_algo.addActionListener(this);
        find_center_algo.addActionListener(this);
        tsp_algo.addActionListener(this);

        menu_bar.add(file);
        menu_bar.add(edit);
        menu_bar.add(algorithms);
    }

    public JMenuBar getMenuBar() {
        return menu_bar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();
        TempFrame_UI temp = new TempFrame_UI(frame);

        if (source == save) { // save
            remove_unique();
            temp.setTitle("Save To File");
            temp.save_to_file();
            temp.setVisible(true);
        }
        if (source == load){ // load
            JFileChooser j = new JFileChooser("data/");
            j.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");
            j.addChoosableFileFilter(filter);
            int r = j.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                String file_path = j.getSelectedFile().getAbsolutePath();
                System.out.println(file_path); // set the label to the path of the selected directory
                if(frame.is_initialized == true){
                    frame.getContentPane().removeAll();
                    frame.revalidate();
                    frame.repaint();
                }
                else {
                    frame.is_initialized = true;
                }

                graph_algo = new DWGAlgorithms();
                graph_algo.load(file_path);
                frame.setAlgo(graph_algo);

                frame.unique_nodes.clear();
                show_graph();
            }

        }

        if (source == add_node) { // add node
            temp.setTitle("Add Node");
            temp.add_node();
            temp.setVisible(true);
        }

        if (source == add_edge){ // add edge
            temp.setTitle("Add Edge");
            temp.add_edge();
            temp.setVisible(true);
        }


        if (source == delete_node) // remove node
        {
            remove_unique();
            temp.setTitle("Remove Node");
            temp.remove_node();
            temp.setVisible(true);
        }

        if (source == delete_edge) // remove edge
        {
            remove_unique();
            temp.setTitle("Remove Edge");
            temp.remove_edge();
            temp.setVisible(true);
        }

        if (source == is_connected_algo) { // connectivity
            remove_unique();
            temp.setTitle("Is Connected");
            JLabel l = new JLabel(graph_algo.isConnected() ? "TRUE" : "FALSE");
            l.setBounds(130, 60, 100, 30);
            temp.add(l);
            temp.setVisible(true);
        }

        if (source == shortest_path_weight_algo) // shortest path
        {
            remove_unique();
            temp.setTitle("Shortest Path Weight");
            temp.shortestPathWeight();
            temp.setVisible(true);
        }

        if (source == shortest_path_list_algo) // shortest path
        {
            temp.setTitle("Shortest Path");
            temp.shortestPathList();
            temp.setVisible(true);
        }

        if (source == find_center_algo) { // center
            temp.setTitle("Center");
            Node center_node = (Node) graph_algo.center();
            JLabel l;
            if (center_node!=null){

                ArrayList<Integer> unique_n = new ArrayList<>();
                unique_n.add(center_node.getKey());
                frame.unique_nodes = unique_n;
                show_graph();

                l = new JLabel("Center is: " + center_node.getKey());
            }else{
                l = new JLabel("<html>the graph is not connected!</br>    there is no center in this graph</html>");
            }
            l.setBounds(50, 60, 200, 50);
            temp.add(l);
            temp.setVisible(true);
        }

        if (source == tsp_algo) {// tsp
            temp.setTitle("TSP Algorithm");
            temp.tsp();
            temp.setVisible(true);
        }
    }

    private void show_graph() {
        Nodes_UI nodes = new Nodes_UI(graph_algo, frame);
        Edges_UI edges = new Edges_UI(graph_algo, frame);
        frame.setNodes_ui(nodes);
        frame.setEdges_ui(edges);
        frame.add(nodes, BorderLayout.CENTER);
        frame.add(edges, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    private void remove_unique(){
        frame.unique_nodes.clear();
        show_graph();
    }
}