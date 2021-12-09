package gui;

import implementations.DWGAlgorithms;
import implementations.Node;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUI_MENU implements ActionListener {


    JMenuBar j_Menu_Bar;
    JMenu file_tab, update_graph_tab, update_nodes_tab, update_edges_tab, use_algorithms;
    JMenuItem save_tab, load_tab, add_node, add_edge, delete_node, delete_edge, is_connected_algo, shortest_path_weight_algo, find_center_algo, tsp_algo, shortest_path_list_algo;

    private DWGAlgorithms dwgAlgorithms;
    private GUI_WINDOW gui_window_frame;

    public GUI_MENU(GUI_WINDOW frame) {
        gui_window_frame = frame;

        j_Menu_Bar = new JMenuBar();
        file_tab = new JMenu("FILE");
        save_tab = new JMenuItem("Save_to_json");
        load_tab = new JMenuItem("Load_from_json");
        file_tab.add(save_tab);
        file_tab.add(load_tab);

        update_graph_tab = new JMenu("UPDATE");
        update_nodes_tab = new JMenu("Edit Nodes");
        update_edges_tab = new JMenu("Edit Edges");
        add_node = new JMenuItem("Add Node");
        add_edge = new JMenuItem("Add Edge");
        delete_node = new JMenuItem("Remove Node");
        delete_edge = new JMenuItem("Remove Edge");
        update_graph_tab.add(update_nodes_tab);
        update_graph_tab.add(update_edges_tab);
        update_nodes_tab.add(add_node);
        update_edges_tab.add(add_edge);
        update_nodes_tab.add(delete_node);
        update_edges_tab.add(delete_edge);

        use_algorithms = new JMenu("USE ALGORITHMS");
        is_connected_algo = new JMenuItem("Is Connected");
        shortest_path_weight_algo = new JMenuItem("Get Shortest Path Weight");
        find_center_algo = new JMenuItem("Get Center");
        tsp_algo = new JMenuItem("Get TSP");
        shortest_path_list_algo = new JMenuItem("Get Shortest Path List");
        use_algorithms.add(is_connected_algo);
        use_algorithms.add(shortest_path_weight_algo);
        use_algorithms.add(shortest_path_list_algo);
        use_algorithms.add(find_center_algo);
        use_algorithms.add(tsp_algo);

        save_tab.addActionListener(this);
        load_tab.addActionListener(this);

        add_node.addActionListener(this);
        add_edge.addActionListener(this);
        delete_node.addActionListener(this);
        delete_edge.addActionListener(this);

        is_connected_algo.addActionListener(this);
        shortest_path_weight_algo.addActionListener(this);
        shortest_path_list_algo.addActionListener(this);
        find_center_algo.addActionListener(this);
        tsp_algo.addActionListener(this);

        j_Menu_Bar.add(file_tab);
        j_Menu_Bar.add(update_graph_tab);
        j_Menu_Bar.add(use_algorithms);
    }

    public JMenuBar getMenuBar() {
        return j_Menu_Bar;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) e.getSource();
        GUI_ADDITIONAL_WINDOW temp = new GUI_ADDITIONAL_WINDOW(gui_window_frame);

        // load graph:
        if (source == load_tab){
            JFileChooser j = new JFileChooser("data/");
            j.setAcceptAllFileFilterUsed(false);
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Json files", "json");
            j.addChoosableFileFilter(filter);
            int r = j.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                String file_path = j.getSelectedFile().getAbsolutePath();
                System.out.println(file_path);
                gui_window_frame.getContentPane().removeAll();
                gui_window_frame.revalidate();
                gui_window_frame.repaint();
                dwgAlgorithms = new DWGAlgorithms();
                dwgAlgorithms.load(file_path);
                gui_window_frame.setDwg_algorithms(dwgAlgorithms);
                gui_window_frame.unique_nodes.clear();
                show_graph();
            }
        }
        // save graph:
        else if (source == save_tab) { // save
            remove_unique();
            temp.setTitle("Save To File");
            temp.saveToFile();
            temp.setVisible(true);
        }

        // edit graph options:
        else if (source == add_node) { // add node
            temp.setTitle("Add Node");
            temp.addNode();
            temp.setVisible(true);
        }
        else if (source == delete_node){
            remove_unique();
            temp.setTitle("Remove Node");
            temp.removeNode();
            temp.setVisible(true);
        }
        else if (source == add_edge){
            temp.setTitle("Add Edge");
            temp.addEdge();
            temp.setVisible(true);
        }
        else if (source == delete_edge){
            remove_unique();
            temp.setTitle("Remove Edge");
            temp.remove_edge();
            temp.setVisible(true);
        }

        // algorithms:
        else if (source == is_connected_algo) {
            remove_unique();
            temp.setTitle("Is Connected");
            JLabel l = new JLabel( gui_window_frame.getDwg_algorithms().isConnected() ? "TRUE" : "FALSE");
            l.setBounds(130, 60, 100, 30);
            temp.add(l);
            temp.setVisible(true);
        }
        else if (source == find_center_algo) {
            temp.setTitle("Center");
            Node center_node = (Node) gui_window_frame.getDwg_algorithms().center();
            JLabel label;
            if (center_node!=null){
                ArrayList<Integer> unique_n = new ArrayList<>();
                unique_n.add(center_node.getKey());
                gui_window_frame.unique_nodes = unique_n;
                show_graph();
                label = new JLabel("Center is: " + center_node.getKey());
            }
            else{
                label = new JLabel("<html>the graph is not connected!</br>    there is no center in this graph</html>");
            }
            label.setBounds(50, 60, 200, 50);
            temp.add(label);
            temp.setVisible(true);
        }
        else if (source == shortest_path_weight_algo){
            remove_unique();
            temp.setTitle("Shortest Path Weight");
            temp.shortestPathWeightAlgo();
            temp.setVisible(true);
        }
        else if (source == shortest_path_list_algo)
        {
            temp.setTitle("Shortest Path");
            temp.shortestPathListAlgo();
            temp.setVisible(true);
        }
        else if (source == tsp_algo) {
            temp.setTitle("TSP Algorithm");
            temp.tspAlgo();
            temp.setVisible(true);
        }
    }

    // show the graph after changes / updates:
    private void show_graph() {
        GUI_ADDITIONAL_WINDOW.updateNodesEdges(gui_window_frame);
    }

    // clean the list from unique nodes and show graph after changes:
    private void remove_unique(){
        gui_window_frame.unique_nodes.clear();
        show_graph();
    }
}