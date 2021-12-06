package implementations;

import api.*;

import java.util.HashMap;

public class Node implements NodeData {

    private int _key;
    private HashMap<Integer, Edge> _edges;
    private GeoLoc _location;
    private int _tag;
    private String _info = "";

    public Node(int key, HashMap<Integer, Edge> edges, GeoLoc loc){
        this._key = key;
        this._edges = edges;
        this._location = loc;
        this._tag = Integer.parseInt(null);
    }



    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    public int getKey(){
        return _key;
    }
    /** Returns the location of this node, if none return null.
     * @return
     */
    public GeoLocation getLocation(){
        return _location;
    }

    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(GeoLocation p) {
        this._location = (GeoLoc) p;
    }

    /**
     * Returns the weight associated with this node.
     * @return
     */
    public double getWeight(){
        return 0.0; ///////////////////// complete!!!!!!!
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    public void setWeight(double w){
        return; ///////////////////// complete!!!!!!!
    }
    /**
     * Returns the remark (meta data) associated with this node.
     * @return
     */
    public String getInfo(){
        return _info;
    }
    /**
     * Allows changing the remark (meta data) associated with this node.
     * @param s
     */
    public void setInfo(String s){
        this._info = s;
    }
    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     * @return
     */
    public int getTag(){
        return _tag;
    }
    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    public void setTag(int t){
        this._tag = t;
    }

    public HashMap<Integer, Edge> getEdges(){
        return _edges;
    }

    public Edge getEdge(int dst){
        return _edges.get(dst);
    }

    public void setEdge(int dst, double weight){
        Edge new_edge = new Edge(this._key, dst, weight);
        this._edges.put(dst, new_edge);
    }

    public Edge removeEdge(int dst){
        return this._edges.remove(dst);
    }

    public Node copy(){
        HashMap<Integer, Edge> edges = new HashMap<>();
        for (Integer key : _edges.keySet()){
            edges.put(key, _edges.get(key).copy());
        }

        Node node_copy = new Node(this._key, edges, this._location.copy());
        node_copy.setInfo(this._info);
        node_copy.setTag(this._tag);

        return node_copy;
    }

    public void cleanEdges(){
        this._edges.clear();
    }
}

