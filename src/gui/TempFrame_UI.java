package gui;


import api.NodeData;
import implementations.Node;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TempFrame_UI extends JFrame implements ActionListener {

    private window_GUI frame;

    JTextField tf;
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
        String label = "<html>Enter source node and destination</br> as follows: key1,key2</html>";
        String text = "Find Path Weight";
        this.boxINIT(text, label);
    }

    public void shortestPathList() {
        String label = "<html>Enter source node and destination</br> as follows: key1,key2</html>";
        String text = "Find Path";
        this.boxINIT(text, label);
//
//        tf = new JTextField();
//        tf.setBounds(105, 60, 100, 20);
//
//        b = new JButton("Find Path");
//        b.setBounds(100, 90, 110, 30);
//
//        l = new JLabel("<html>Enter source node and destination</br> as follows: key1,key2</html>");
//        l.setBounds(10, 25, 300, 30);
//
//        add(b);
//        add(tf);
//        add(l);
//
//        b.addActionListener(this);
    }

    public void tsp() {

        String label = "<html>Enter cities nodes (keys)</br> as follows: key1,key2,...,keyN</html>";
        String text = "Find TSP";
        this.boxINIT(text, label);

//        tf = new JTextField();
//        tf.setBounds(105, 60, 100, 20);
//
//        c = new JButton("Find TSP");
//        c.setBounds(100, 90, 110, 30);
//
//        l = new JLabel("<html>Enter cities nodes (keys)</br> as follows: key1,key2,...,keyN</html>");
//        l.setBounds(10, 25, 300, 30);
//
//        add(c);
//        add(tf);
//        add(l);
//
//        c.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Find Path Weight")) {
            int src = Integer.parseInt(tf.getText().split(",")[0]);
            int dst = Integer.parseInt(tf.getText().split(",")[1]);

            double weight = frame.getAlgo().shortestPathDist(src, dst);
            output = "Path Weight = " + weight;
            showAns();
        }
        if (e.getActionCommand().equals("Find Path")) {
            int src = Integer.parseInt(tf.getText().split(",")[0]);
            int dst = Integer.parseInt(tf.getText().split(",")[1]);
            output = "";
            List<NodeData> list = frame.getAlgo().shortestPath(src, dst);
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    output += "" + list.get(i).getKey() + " -> ";
                } else output += "" + list.get(i).getKey();
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
                output = "One of the keys is not in the graph!";
                showAns();
            }

            output = "";
            List<NodeData> list_cities = new ArrayList<>();
            for (String key : keys){
                list_cities.add(frame.getAlgo().getGraph().getNode(Integer.parseInt(key)));
            }
            List<Node> list_ans = frame.getAlgo().tsp(list_cities);
            int count = 0;
            for (Node node : list_ans){
                if (count != list_ans.size() - 1) {
                    output += node.getKey() + " -> ";
                } else output += node.getKey();
                count++;
            }
            showAns();
        }
    }

    private void showAns() {
        dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        JFrame show = new TempFrame_UI(frame);
        JLabel x = new JLabel(output);
        x.setBounds(10, 60, 1000, 20);
        x.setVisible(true);
        show.add(x);
        show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        show.setVisible(true);
    }

}