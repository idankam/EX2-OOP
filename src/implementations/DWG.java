package implementations;

import api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This interface represents a Directional Weighted Graph,
 * As in: http://math.oxford.emory.edu/site/cs171/directedAndEdgeWeightedGraphs/
 * The interface has a road-system or communication network in mind -
 * and should support a large number of nodes (over 100,000).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 */

public class DWG implements DirectedWeightedGraph {

    public static final int WHITE = 0, GREY = 1, BLACK = 2;
    private HashMap<Integer, Node> Nodes;
    private HashMap<String, Edge> Edges;
    private int changes_counter = 0;
    public boolean is_load_now = true;

    public DWG(){
        this.Nodes = new HashMap<>();
        this.Edges = new HashMap<>();
    }

    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    public NodeData getNode(int key){
        return this.Nodes.get(key);
    }
    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return
     */
    public EdgeData getEdge(int src, int dest){
        return (EdgeData) this.Nodes.get(src).getEdge(dest);
    }
    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     * @param n
     */
    public void addNode(NodeData n) {
        if (!this.is_load_now)
        {
            this.changes_counter++;
        }
        this.Nodes.put(n.getKey(), (Node) n);
    }
    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    public void connect(int src, int dest, double w){
        if (!this.is_load_now)
        {
            this.changes_counter++;
        }
        Edge new_edge = new Edge(src, dest, w);
        String edge_name = src + "," + dest;
        Edges.put(edge_name, new_edge);
        this.Nodes.get(src).setEdge(dest, w);
    }
    /**
     * This method returns an Iterator for the
     * collection representing all the nodes in the graph.
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     * @return Iterator<node_data>
     */
    public Iterator<NodeData> nodeIter(){
        List<NodeData> l  = new ArrayList<>( this.Nodes.values() );
        return l.iterator();
    }
    /**
     * This method returns an Iterator for all the edges in this graph.
     * Note: if any of the edges going out of this node were changed since the iterator was constructed - a RuntimeException should be thrown.
     * @return Iterator<EdgeData>
     */
    public Iterator<EdgeData> edgeIter(){
        List<EdgeData> l  = new ArrayList( this.Edges.values() );
        return l.iterator();
    }


    /**
     * This method returns an Iterator for edges getting out of the given node (all the edges starting (source) at the given node).
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     * @return Iterator<EdgeData>
     */
    public Iterator<EdgeData> edgeIter(int node_id){
        List<EdgeData> l  = new ArrayList(this.Nodes.get(node_id).getEdges().values());
        return l.iterator();
        //return (Iterator<EdgeData>) this.Nodes.get(node_id).getEdges().values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    public NodeData removeNode(int key){
        this.changes_counter++;

        Node removedNode = this.Nodes.remove(key);

        ArrayList<String> edges_to_remove = new ArrayList<>();
        for(String name : this.Edges.keySet()){
            if(Integer.parseInt(name.split(",")[0]) == key || (Integer.parseInt(name.split(",")[1])) == key){
                edges_to_remove.add(name);
            }
        }
        for(String name : edges_to_remove){
            Edges.remove(name);
        }

        for (Node node: Nodes.values()){
            if (node.getEdges().containsKey(key)){
                node.removeEdge(key);
            }
        }
        return removedNode;
    }
    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    public EdgeData removeEdge(int src, int dest){
        this.changes_counter++;

        Edges.remove(src+","+dest);
        return (EdgeData) this.Nodes.get(src).removeEdge(dest);
    }
    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    public int nodeSize(){
        return this.Nodes.size();
    }
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    public int edgeSize(){
        return Edges.size();
    }
    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    public int getMC(){
        return this.changes_counter;
    }

    private void setEdges(HashMap<String, Edge> edges){
        this.changes_counter = 0;
        this.Edges = edges;
    }

    private void setNodes(HashMap<Integer, Node> nodes){
        this.changes_counter = 0;
        this.Nodes = nodes;
    }

    public DWG copy(){
        HashMap<Integer, Node> nodes_copy = new HashMap<>();
        HashMap<String, Edge> edges_copy = new HashMap<>();

        for (Integer key: this.Nodes.keySet()){
            nodes_copy.put(key, nodes_copy.get(key).copy());
        }
        for (String key: this.Edges.keySet()){
            edges_copy.put(key, edges_copy.get(key).copy());
        }

        DWG copy_graph = new DWG();
        copy_graph.setEdges(edges_copy);
        copy_graph.setNodes(nodes_copy);

        return copy_graph;
    }

    public HashMap<Integer, Node> getNodes(){
        return this.Nodes;
    }

    public void colorWhite(){
        for(Iterator<NodeData> it = this.nodeIter(); it.hasNext();){
            NodeData node = it.next();
            node.setTag(WHITE);
        }
    }

    public DWG transpose() {
        DWG T_dwg = new DWG();
        Iterator<NodeData> iter = this.nodeIter();
        while (iter.hasNext()){
            Node node = ((Node) iter.next()).copy();
            node.cleanEdges();
            T_dwg.addNode(node);
        }
        Iterator<EdgeData> iter_edge = this.edgeIter();
        while (iter_edge.hasNext()){
            Edge e = ((Edge)iter_edge.next()).copy();
            T_dwg.connect(e.getDest(), e.getSrc(), e.getWeight());
        }
        return T_dwg;
    }
}

