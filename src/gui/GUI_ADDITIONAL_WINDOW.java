package gui;


import api.NodeData;
import implementations.GeoLoc;
import implementations.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GUI_ADDITIONAL_WINDOW extends JFrame implements ActionListener {

    private GUI_WINDOW gui_window;

    String output_text;
    JLabel j_label;
    JTextField textField, id, x, y, src, dst, w;



    public GUI_ADDITIONAL_WINDOW(GUI_WINDOW gui_window) {
        // initialize window:
        this.gui_window = gui_window;
        setSize(400, 250);
        setLocationRelativeTo(this.gui_window);
        setResizable(true);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // add fields in the window:
    private void boxINIT(String text, String label){
        textField = new JTextField();
        textField.setBounds(105, 60, 100, 30);
        JButton button = new JButton(text);
        button.setBounds(100, 90, 110, 30);
        this.j_label = new JLabel(label);
        this.j_label.setBounds(10, 25, 300, 30);

        add(button);
        add(textField);
        add(this.j_label);
        button.addActionListener(this);
    }

    public void saveToFile(){
        String label = "<html>Enter path for saving the graph,</br>as follows: dirName/dirName/.../fileName.json</html>";
        String text = "SAVE";
        this.boxINIT(text, label);
    }

    public void shortestPathWeightAlgo() {
        String label = "<html>Enter source node and destination</br>as follows: key1,key2</html>";
        String text = "Find Path Weight";
        this.boxINIT(text, label);
    }

    public void shortestPathListAlgo() {
        String label = "<html>Enter source node and destination</br>as follows: key1,key2</html>";
        String text = "Find Path";
        this.boxINIT(text, label);
    }

    public void tspAlgo() {

        String label = "<html>Enter cities nodes (keys)</br>as follows: key1,key2,...,keyN</html>";
        String text = "Find TSP";
        this.boxINIT(text, label);
    }

    public void addNode() {
        String label = "<html>Enter node information:</html>";
        String text = "ADD NODE";

        id = new JTextField("insert id here");
        id.setBounds(105, 50, 100, 20);

        x = new JTextField("insert X here");
        x.setBounds(105, 75, 100, 20);

        y = new JTextField("insert Y here");
        get_info_box(label, text, y, id, x);
    }

    public void removeNode() {
        String label = "<html>Enter node id:</html>";
        String text = "REMOVE NODE";
        this.boxINIT(text, label);
    }

    public void addEdge() {
        src = new JTextField("insert src here");
        src.setBounds(105, 50, 100, 20);

        dst = new JTextField("insert dest here");
        dst.setBounds(105, 75, 100, 20);

        String label_text = "<html>Enter edge information:</html>";
        String button_text = "ADD EDGE";

        w = new JTextField("insert weight here");
        get_info_box(label_text, button_text, w, src, dst);
    }

    public void remove_edge() {
        String label = "<html>Enter edge information:</html>";
        String text = "REMOVE EDGE";

        src = new JTextField("insert src here");
        src.setBounds(105, 50, 100, 20);

        dst = new JTextField("insert dest here");
        dst.setBounds(105, 75, 100, 20);

        JButton button = new JButton(text);
        button.setBounds(100, 130, 110, 30);
        this.j_label = new JLabel(label);
        this.j_label.setBounds(10, 10, 300, 30);

        add(button);
        add(src);
        add(dst);
        add(this.j_label);

        button.addActionListener(this);
    }

    private void get_info_box(String label_text, String button_text, JTextField first, JTextField second, JTextField third) {
        first.setBounds(105, 100, 100, 20);

        JButton button = new JButton(button_text);
        button.setBounds(100, 130, 110, 30);
        this.j_label = new JLabel(label_text);
        this.j_label.setBounds(10, 10, 300, 30);

        add(button);
        add(second);
        add(third);
        add(first);
        add(this.j_label);

        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Find Path")) {
            int src = Integer.parseInt(textField.getText().split(",")[0]);
            int dst = Integer.parseInt(textField.getText().split(",")[1]);
            output_text = "";
            List<NodeData> list = gui_window.getDwg_algorithms().shortestPath(src, dst);
            if (list!=null){

                ArrayList<Integer> unique_n = new ArrayList<>();
                for (NodeData node : list){
                    unique_n.add(node.getKey());
                    System.out.println(node.getKey());
                }
                gui_window.unique_nodes = unique_n;
                update_graph_display();

                for (int i = 0; i < list.size(); i++) {
                    if (i != list.size() - 1) {
                        output_text += "" + list.get(i).getKey() + " -> ";
                    } else output_text += "" + list.get(i).getKey();
                }
            }
            else{
                output_text = "<html>there is no path between the nodes!</html>";
            }
            showAns();
        }

        else if (e.getActionCommand().equals("Find Path Weight")) {
            int src = Integer.parseInt(textField.getText().split(",")[0]);
            int dst = Integer.parseInt(textField.getText().split(",")[1]);
            double weight = gui_window.getDwg_algorithms().shortestPathDist(src, dst);
            if (weight  == Double.MAX_VALUE){
                output_text = "there is no path between the nodes!";
            }else{
                output_text = "Path Weight = " + weight;
            }
            showAns();
        }

        else if (e.getActionCommand().equals("Find TSP")) {
            String[] keys = textField.getText().split(",");
            boolean flag = true;
            for ( String key : keys){
                Node tmp = (Node) gui_window.getDwg_algorithms().getGraph().getNode(Integer.parseInt(key));
                if(tmp == null){
                    flag = false;
                    break;
                }
            }
            if(!flag){
                System.err.println("There is no such key! try again");
                output_text = "<html>One of the keys is not in the graph!</html>";
            }
            else{
                output_text = "";
                List<NodeData> list_cities = new ArrayList<>();
                for (String key : keys){
                    list_cities.add(gui_window.getDwg_algorithms().getGraph().getNode(Integer.parseInt(key)));
                }
                List<Node> list_ans = gui_window.getDwg_algorithms().tsp(list_cities);
                if(list_ans!=null){
                    ArrayList<Integer> unique_n = new ArrayList<>();
                    for (Node node : list_ans){
                        unique_n.add(node.getKey());
                        System.out.println(node.getKey());
                    }
                    gui_window.unique_nodes = unique_n;
                    update_graph_display();
                    int count = 0;
                    output_text += "<html>";
                    for (Node node : list_ans){
                        if (count != list_ans.size() - 1) {
                            output_text += node.getKey() + " -> ";
                        } else output_text += node.getKey();
                        count++;
                    }
                    output_text += "</html>";
                }
                else{
                    output_text = "<html>there is no path between all the nodes!</html>";
                }
            }
            showAns();
        }

        else if (e.getActionCommand().equals("SAVE")) {
            String path = textField.getText();

            Boolean success = gui_window.getDwg_algorithms().save(path);
            if(success){
                output_text = "<html>SAVED SUCCESSFULLY at: </br>" + path+"</html>";
            }
            else{
                output_text = "<html>There was a problem with the file name,</br>please try again!</html>";
            }
            showAns();
        }

        else if (e.getActionCommand().equals("ADD NODE")) {
            GeoLoc pos = new GeoLoc(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()), 0.0);
            Node new_node = new Node(Integer.parseInt(id.getText()), pos);
            gui_window.getDwg_algorithms().getGraph().addNode(new_node);

            ArrayList<Integer> unique_n = new ArrayList<>();
            unique_n.add(new_node.getKey());
            gui_window.unique_nodes = unique_n;
            update_graph_display();

            output_text = "Node Added!";
            showAns();

        }
        else if (e.getActionCommand().equals("ADD EDGE")) {
            int src_value =  Integer.parseInt(src.getText());
            int w_value =  Integer.parseInt(w.getText());
            int dst_value =  Integer.parseInt(dst.getText());
            gui_window.getDwg_algorithms().getGraph().connect(src_value,dst_value, w_value);

            ArrayList<Integer> unique_n = new ArrayList<>();
            unique_n.add(src_value);
            unique_n.add(dst_value);
            gui_window.unique_nodes = unique_n;

            update_graph_display();
            output_text = "Edge Added!";
            showAns();
        }
        else if (e.getActionCommand().equals("REMOVE NODE")) {
            int key =  Integer.parseInt(textField.getText());
            System.out.println(key);
            try{
                gui_window.getDwg_algorithms().getGraph().removeNode(key);
                update_graph_display();
                output_text = "Node Removed!";
            }catch(Exception ex){
                output_text = "there is no such node!";
            }
            showAns();
        }
        else if (e.getActionCommand().equals("REMOVE EDGE")) {
            try{
                gui_window.getDwg_algorithms().getGraph().removeEdge( Integer.parseInt(src.getText()), Integer.parseInt(dst.getText()));
                update_graph_display();
                output_text = "Edge Removed!";
            }catch(Exception ex){
                output_text = "there is no such edge!";
            }
            showAns();
        }
    }

    private void update_graph_display() {
        gui_window.getContentPane().removeAll();
        gui_window.revalidate();
        gui_window.repaint();
        updateNodesEdges(gui_window);
    }

    static void updateNodesEdges(GUI_WINDOW gui_window) {
        GUI_NODES nodes = new GUI_NODES(gui_window.getDwg_algorithms(), gui_window);
        GUI_EDGES edges = new GUI_EDGES(gui_window.getDwg_algorithms(), gui_window);
        gui_window.set_GUI_NODES(nodes);
        gui_window.set_GUI_EDGES(edges);
        gui_window.add(nodes, BorderLayout.CENTER);
        gui_window.add(edges, BorderLayout.CENTER);
        gui_window.setVisible(true);
        gui_window.setResizable(true);
    }

    private void showAns() {
        dispatchEvent(new WindowEvent(gui_window, WindowEvent.WINDOW_CLOSING));
        JFrame show = new GUI_ADDITIONAL_WINDOW(gui_window);
        JLabel x = new JLabel(output_text);
        x.setBounds(10, 60, 200, 60);
        x.setVisible(true);
        show.add(x);
        show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        show.setVisible(true);
    }
}