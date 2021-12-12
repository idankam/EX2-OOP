package implementations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import api.*;
import java.io.*;
import java.util.*;

public class DWGAlgorithms implements DirectedWeightedGraphAlgorithms{

    private DWG graph;

    /**
     * Inits the graph on which this set of algorithms operates on.
     * @param g
     */
    public void init(DirectedWeightedGraph g){
        this.graph= (DWG) g;
    }

    /**
     * Returns the underlying graph of which this class works.
     * @return
     */
    public DirectedWeightedGraph getGraph(){
        return graph;
    }

    /**
     * Computes a deep copy of this weighted graph.
     * @return
     */
    public DirectedWeightedGraph copy(){
        return this.graph.copy();
    }

    private static void BFS(DWG graph, int node)
    {
        int WHITE = 0, GREY = 1, BLACK = 2;
        // mark current node as visited
        graph.getNodes().get(node).setTag(GREY);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()){
            int id = queue.poll();
            //Node curr_node = (Node) graph.getNode(id);
            for (Iterator<EdgeData> it = graph.edgeIter(id); it.hasNext(); ) {
                Edge e = (Edge) it.next();
                if (graph.getNode(e.getDest()).getTag() == WHITE){
                    int next_id = e.getDest();
                    graph.getNode(next_id).setTag(GREY);
                    queue.add(next_id);
                }
            }
            graph.getNode(id).setTag(BLACK);
        }
    }


    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     * @return
     */
    public boolean isConnected(){
        this.graph.colorWhite();

        if (this.graph.nodeSize()<2){
            return true;
        }

        Iterator iter = graph.nodeIter();
        int key = ((Node)iter.next()).getKey();

        BFS(graph, (key));
        while (iter.hasNext()){
            Node tmp_node = (Node) iter.next();
            if((tmp_node.getTag()!=DWG.BLACK)){
                return false;
            }
        }

        DWG T_graph = this.graph.transpose();
        T_graph.colorWhite();

        BFS(T_graph, key);
        iter = T_graph.nodeIter();
        while (iter.hasNext()){
            Node tmp_node = (Node) iter.next();
            if(tmp_node.getTag()!=DWG.BLACK){
                return false;
            }
        }
        return true;
    }


private HashMap<Integer, Integer> dijkstra(int src){
    PriorityQueue<NodeData> pQueue = new PriorityQueue<>();
    HashMap<Integer, Integer> previous_pointer = new HashMap<>();
    for (Iterator<NodeData> it = this.graph.nodeIter(); it.hasNext(); ) {
        NodeData node = it.next();
        if (node.getKey() == src){
            node.setWeight(0.0);
            previous_pointer.put(node.getKey(), null);
        }
        else
        {
            node.setWeight(Double.MAX_VALUE);
            previous_pointer.put(node.getKey(), -1);
        }
        pQueue.add(node);
    }

    // The main loop
    while (!pQueue.isEmpty()){
        NodeData curr_node = pQueue.remove(); // Remove and return best vertex
        for (Iterator<EdgeData> it = this.graph.edgeIter(curr_node.getKey()); it.hasNext(); )  // only v that are still in Q
        {
            Edge e_neighbour = (Edge) it.next();
            Node node_neighbour = (Node) this.graph.getNode(e_neighbour.getDest());

            double alternative_weight = curr_node.getWeight() + e_neighbour.getWeight();
            if(alternative_weight < node_neighbour.getWeight()){
                node_neighbour.setWeight(alternative_weight);
                previous_pointer.replace(node_neighbour.getKey(), curr_node.getKey());
                pQueue.remove(node_neighbour);
                pQueue.add(node_neighbour);
            }
        }
    }

    return previous_pointer;
}


    /**
     * Computes the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    public double shortestPathDist(int src, int dest){
        dijkstra(src);
        return this.graph.getNode(dest).getWeight();
    }
    /**
     * Computes the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    public List<NodeData> shortestPath(int src, int dest){
        HashMap<Integer, Integer> pointers = dijkstra(src);
        List<NodeData> path = getPath(pointers, src, dest);
        return path;
    }

    private List<NodeData> getPath(HashMap<Integer, Integer> pointers, int src, int dest) {
        try {
            List<NodeData> path = new ArrayList<>();
            int previous = dest;
            while (previous != src) {
                path.add(0, this.graph.getNode(previous));
                previous = pointers.get(previous);
            }
            path.add(0, this.graph.getNode(src));
            return path;
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Finds the NodeData which minimizes the max distance to all the other nodes.
     * Assuming the graph isConnected, elese return null. See: https://en.wikipedia.org/wiki/Graph_center
     * @return the Node data to which the max shortest path to all the other nodes is minimized.
     */
    public NodeData center(){

        if(!this.isConnected()){
            return null;
        }

        Node min_max_dist_node = null;
        double best_min_max = Double.MAX_VALUE;

        for (Iterator<NodeData> it = this.graph.nodeIter(); it.hasNext(); ) {
            NodeData node = it.next();
            dijkstra(node.getKey());
            double tmp_max = Double.MIN_VALUE;
            for (Iterator<NodeData> it2 = this.graph.nodeIter(); it2.hasNext(); ) {
                NodeData node_tmp = it2.next();
                if(node_tmp.getWeight() > tmp_max)
                tmp_max = node_tmp.getWeight();
            }
            if(tmp_max < best_min_max){
                best_min_max = tmp_max;
                min_max_dist_node = (Node) node;
            }
        }

        return min_max_dist_node;
    }


    /**
     * Computes a list of consecutive nodes which go over all the nodes in cities.
     * the sum of the weights of all the consecutive (pairs) of nodes (directed) is the "cost" of the solution -
     * the lower the better.
     * See: https://en.wikipedia.org/wiki/Travelling_salesman_problem
     * @return
     * @param cities
     */
    public List<NodeData> tsp(List<NodeData> cities){
        try {
            boolean flag = true;
            for (NodeData node : cities) {
                try {
                    Node tmp = (Node) this.graph.getNode(node.getKey());
                } catch (Exception e) {
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                System.err.println("There is no such key! try again");
                return null;
            }

            List<NodeData> answer = new ArrayList<>();

            DWG transpose_graph = this.graph.transpose();
            DWGAlgorithms original = this;
            DWGAlgorithms transpose = new DWGAlgorithms();
            transpose.init(transpose_graph);

            answer.add((Node) cities.get(0));
            cities.remove(0);
            while (cities.size() > 0) {
                HashMap<Integer, Integer> dij_original_pointers = original.dijkstra(answer.get(answer.size() - 1).getKey());
                HashMap<Integer, Integer> dij_transpose_pointers = transpose.dijkstra(answer.get(0).getKey());

                double end_min_weight = Double.MAX_VALUE;
                int end_min_weight_key_node = -1;
                Node node_to_add_at_end = null;
                for (Iterator<NodeData> it = cities.iterator(); it.hasNext(); ) {
                    Node cities_node = (Node) it.next();
                    Node node = (Node) original.graph.getNode(cities_node.getKey());
                    if (node.getWeight() < end_min_weight) {
                        end_min_weight = node.getWeight();
                        end_min_weight_key_node = node.getKey();
                        node_to_add_at_end = cities_node;
                    }
                }

                double start_min_weight = Double.MAX_VALUE;
                int start_min_weight_key_node = -1;
                Node node_to_add_at_start = null;
                for (Iterator<NodeData> it = cities.iterator(); it.hasNext(); ) {
                    Node cities_node = (Node) it.next();
                    Node node = (Node) transpose.graph.getNode(cities_node.getKey());
                    if (node.getWeight() < start_min_weight) {
                        start_min_weight = node.getWeight();
                        start_min_weight_key_node = node.getKey();
                        node_to_add_at_start = cities_node;
                    }
                }

                if (end_min_weight < start_min_weight) {
                    List<NodeData> tmp_list = original.getPath(dij_original_pointers, answer.get(answer.size() - 1).getKey(), end_min_weight_key_node);
                    tmp_list.remove(0);
                    for (Iterator<NodeData> it = tmp_list.iterator(); it.hasNext(); ) {
                        Node tmp_node = (Node) it.next();
                        answer.add(tmp_node);

                    }
                    cities.remove(node_to_add_at_end);
                } else {
                    List<NodeData> tmp_list = transpose.getPath(dij_transpose_pointers, answer.get(0).getKey(), start_min_weight_key_node);
                    tmp_list.remove(0);
                    for (int i = 0; i < tmp_list.size(); i++) {
                        answer.add(0, (Node) original.graph.getNode(tmp_list.get(i).getKey()));
                    }
                    cities.remove(node_to_add_at_start);
                }

                for (Iterator<NodeData> it = cities.iterator(); it.hasNext(); ) {
                    Node tmp_city_node = (Node) it.next();
                    for (Iterator<NodeData> iter = answer.iterator(); iter.hasNext(); ) {
                        NodeData tmp_ans_node = iter.next();
                        if (tmp_ans_node.getKey() == tmp_city_node.getKey()) {
                            cities.remove(tmp_city_node);
                        }
                    }
                }
            }

            return answer;
        }
        catch(Exception e){
            return null;
        }
    }


    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    public boolean save(String file) {

        try {
            Container graph_to_save = new Container();
            ArrayList<Object> nodes = new ArrayList<>();
            ArrayList<Object> edges = new ArrayList<>();

            for (Iterator<NodeData> it = this.graph.nodeIter(); it.hasNext(); ) {
                NodeData node = it.next();
                LinkedTreeMap tmp = new LinkedTreeMap();
                String loc = node.getLocation().x() + "," + node.getLocation().y() + "," + node.getLocation().z();
                tmp.put("pos", loc);
                tmp.put("id", node.getKey());
                nodes.add(tmp);
            }
            for (Iterator<EdgeData> it = this.graph.edgeIter(); it.hasNext(); ) {
                EdgeData edge = it.next();
                LinkedTreeMap tmp = new LinkedTreeMap();

                tmp.put("src", edge.getSrc());
                tmp.put("w", edge.getWeight());
                tmp.put("dest", edge.getDest());

                edges.add(tmp);
            }

            graph_to_save.setNodes(nodes.toArray(new Object[0]));
            graph_to_save.setEdges(edges.toArray(new Object[0]));

            Writer writer = new FileWriter(file);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(graph_to_save, writer);
            writer.flush(); //flush data to file   <---
            writer.close(); //close write          <---
            return true;
        }
        catch (IOException e){
            System.err.println("file cannot be created");
            return false;
        }

    }

    /**
     * This method loads a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    public boolean load(String file) {
        try{
            String filename = file;
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(filename));
            Container data = gson.fromJson(reader, Container.class);

            DWG graph = new DWG();
            for (Object o : data.Nodes){
                String[] pos = ((String)((LinkedTreeMap)o).get("pos")).split(",");
                int key = (int)(double)((LinkedTreeMap)o).get("id");
                GeoLoc loc = new GeoLoc(Double.parseDouble(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]));
                Node node = new Node(key, loc);
                graph.addNode(node);
            }
            for (Object o : data.Edges){
                int src = (int)(double)((LinkedTreeMap)o).get("src");
                int dest = (int)(double)((LinkedTreeMap)o).get("dest");
                double weight = (double)((LinkedTreeMap)o).get("w");
                graph.connect(src, dest, weight);
            }
            graph.is_load_now = false;
            this.init(graph);

            return true;
        }
        catch(FileNotFoundException e){
            return false;
        }
    }
}

