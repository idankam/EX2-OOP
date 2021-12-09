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

public class TempFrame_UI extends JFrame implements ActionListener {

    private window_GUI frame;

    JTextField tf, id, x, y, z, src, dst, w;
    JLabel l;
    //JButton a,b,c;
    String output;

    public TempFrame_UI(window_GUI g) {

        frame = g;

        setSize(300, 200);
        setLocationRelativeTo(frame);
        setResizable(true);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void boxINIT(String text, String label){
        tf = new JTextField();
        tf.setBounds(105, 60, 100, 20);

        JButton button = new JButton(text);
        button.setBounds(100, 90, 110, 30);

        l = new JLabel(label);
        l.setBounds(10, 25, 300, 30);

        add(button);
        add(tf);
        add(l);

        button.addActionListener(this);
    }

    public void shortestPathWeight() {
        String label = "<html>Enter source node and destination</br>as follows: key1,key2</html>";
        String text = "Find Path Weight";
        this.boxINIT(text, label);
    }

    public void shortestPathList() {
        String label = "<html>Enter source node and destination</br>as follows: key1,key2</html>";
        String text = "Find Path";
        this.boxINIT(text, label);
    }

    public void tsp() {

        String label = "<html>Enter cities nodes (keys)</br>as follows: key1,key2,...,keyN</html>";
        String text = "Find TSP";
        this.boxINIT(text, label);
    }

    public void save_to_file(){
        String label = "<html>Enter path for saving the graph,</br>as follows: dirName/dirName/.../fileName.json</html>";
        String text = "SAVE";
        this.boxINIT(text, label);
    }

    public void add_node() {
        String label = "<html>Enter node information:</html>";
        String text = "ADD NODE";

        id = new JTextField("insert id here");
        id.setBounds(105, 50, 100, 20);

        x = new JTextField("insert X here");
        x.setBounds(105, 75, 100, 20);

        y = new JTextField("insert Y here");
        get_info_box(label, text, y, id, x);
    }

    public void remove_node() {
        String label = "<html>Enter node id:</html>";
        String text = "REMOVE NODE";
        this.boxINIT(text, label);
    }

    public void add_edge() {
        String label = "<html>Enter edge information:</html>";
        String text = "ADD EDGE";

        src = new JTextField("insert src here");
        src.setBounds(105, 50, 100, 20);

        dst = new JTextField("insert dest here");
        dst.setBounds(105, 75, 100, 20);

        w = new JTextField("insert weight here");
        get_info_box(label, text, w, src, dst);
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

        l = new JLabel(label);
        l.setBounds(10, 10, 300, 30);

        add(button);
        add(src);
        add(dst);
        add(l);

        button.addActionListener(this);
    }

    private void get_info_box(String label, String text, JTextField first, JTextField second, JTextField third) {
        first.setBounds(105, 100, 100, 20);

        JButton button = new JButton(text);
        button.setBounds(100, 130, 110, 30);

        l = new JLabel(label);
        l.setBounds(10, 10, 300, 30);

        add(button);
        add(second);
        add(third);
        add(first);
        add(l);

        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Find Path Weight")) {
            int src = Integer.parseInt(tf.getText().split(",")[0]);
            int dst = Integer.parseInt(tf.getText().split(",")[1]);

            double weight = frame.getAlgo().shortestPathDist(src, dst);
            if (weight  == Double.MAX_VALUE){
                output = "there is no path between the nodes!";
            }else{
                output = "Path Weight = " + weight;
            }
            showAns();
        }
        else if (e.getActionCommand().equals("Find Path")) {
            int src = Integer.parseInt(tf.getText().split(",")[0]);
            int dst = Integer.parseInt(tf.getText().split(",")[1]);
            output = "";
            List<NodeData> list = frame.getAlgo().shortestPath(src, dst);
            if (list!=null){

                ArrayList<Integer> unique_n = new ArrayList<>();
                for (NodeData node : list){
                    unique_n.add(node.getKey());
                    System.out.println(node.getKey());
                }
                frame.unique_nodes = unique_n;
                update_graph_display();

                for (int i = 0; i < list.size(); i++) {
                    if (i != list.size() - 1) {
                        output += "" + list.get(i).getKey() + " -> ";
                    } else output += "" + list.get(i).getKey();
                }
            }
            else{
                output = "<html>there is no path between the nodes!</html>";
            }
            showAns();
        }
        else if (e.getActionCommand().equals("Find TSP")) {
            String[] keys = tf.getText().split(",");

            boolean flag = true;
            for ( String key : keys){
                Node tmp = (Node) frame.getAlgo().getGraph().getNode(Integer.parseInt(key));
                if(tmp == null){
                    flag = false;
                    break;
                }
            }
            if(!flag){
                System.err.println("There is no such key! try again");
                output = "<html>One of the keys is not in the graph!</html>";
                showAns();
            }
            else{

                output = "";
                List<NodeData> list_cities = new ArrayList<>();
                for (String key : keys){
                    list_cities.add(frame.getAlgo().getGraph().getNode(Integer.parseInt(key)));
                }
                List<Node> list_ans = frame.getAlgo().tsp(list_cities);
                if(list_ans!=null){
                    ArrayList<Integer> unique_n = new ArrayList<>();
                    for (Node node : list_ans){
                        unique_n.add(node.getKey());
                        System.out.println(node.getKey());
                    }
                    frame.unique_nodes = unique_n;
                    update_graph_display();
                    int count = 0;
                    output += "<html>";
                    for (Node node : list_ans){
                        if (count != list_ans.size() - 1) {
                            output += node.getKey() + " -> ";
                        } else output += node.getKey();
                        count++;
                    }
                    output += "</html>";
                }
                else{
                    output = "<html>there is no path between all the nodes!</html>";
                }

                showAns();
            }

        }
        else if (e.getActionCommand().equals("SAVE")) {
            String path = tf.getText();

            Boolean success = frame.getAlgo().save(path);
            if(success){
                output = "<html>SAVED SUCCESSFULLY at</br>" + path+"</html>";
            }
            else{
                output = "<html>There was a problem with the file name,</br>please try again!</html>";
            }
            showAns();
        }
        else if (e.getActionCommand().equals("ADD NODE")) {
            GeoLoc pos = new GeoLoc(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()), 0.0);
            Node new_node = new Node(Integer.parseInt(id.getText()), pos);
            frame.getAlgo().getGraph().addNode(new_node);

            ArrayList<Integer> unique_n = new ArrayList<>();
            unique_n.add(new_node.getKey());
            frame.unique_nodes = unique_n;
            update_graph_display();

            output = "Node Added!";
            showAns();

        }
        else if (e.getActionCommand().equals("ADD EDGE")) {
            int src_value =  Integer.parseInt(src.getText());
            int w_value =  Integer.parseInt(w.getText());
            int dst_value =  Integer.parseInt(dst.getText());
            frame.getAlgo().getGraph().connect(src_value,dst_value, w_value);

            ArrayList<Integer> unique_n = new ArrayList<>();
            unique_n.add(src_value);
            unique_n.add(dst_value);
            frame.unique_nodes = unique_n;

            update_graph_display();
            output = "Edge Added!";
            showAns();
        }
        else if (e.getActionCommand().equals("REMOVE NODE")) {
            int key =  Integer.parseInt(tf.getText());
            System.out.println(key);
            try{
                frame.getAlgo().getGraph().removeNode(key);
                update_graph_display();
                output = "Node Removed!";
            }catch(Exception ex){
                output = "there is no such node!";
            }
            showAns();
        }
        else if (e.getActionCommand().equals("REMOVE EDGE")) {
            try{
                frame.getAlgo().getGraph().removeEdge( Integer.parseInt(src.getText()), Integer.parseInt(dst.getText()));
                update_graph_display();
                output = "Edge Removed!";
            }catch(Exception ex){
                output = "there is no such edge!";
            }
            showAns();
        }
    }

    private void update_graph_display() {
        frame.getContentPane().removeAll();
        frame.revalidate();
        frame.repaint();
        Nodes_UI nodes = new Nodes_UI(frame.getAlgo(), frame);
        Edges_UI edges = new Edges_UI(frame.getAlgo(), frame);
        frame.setNodes_ui(nodes);
        frame.setEdges_ui(edges);
        frame.add(nodes, BorderLayout.CENTER);
        frame.add(edges, BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setResizable(true);
    }

    private void showAns() {
        dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        JFrame show = new TempFrame_UI(frame);
        JLabel x = new JLabel(output);
        x.setBounds(10, 60, 200, 60);
        x.setVisible(true);
        show.add(x);
        show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        show.setVisible(true);
    }


}