package implementations;

import api.*;

import java.util.HashMap;

public class Node implements NodeData, Comparable<Node>{

    private int id;
    private HashMap<Integer, Edge> _edges;
    private GeoLoc pos;
    private int _tag;
    private String _info = "";
    private double _weight;

    public Node(int id, HashMap<Integer, Edge> edges, GeoLoc pos){
        this.id = id;
        this._edges = edges;
        this.pos = pos;
        this._tag = -1;
    }

    public Node(int id, GeoLoc pos){
        this.id = id;
        this._edges = new HashMap<Integer, Edge>();
        this.pos = pos;
        this._tag = -1;
    }


    /**
     * Returns the key (id) associated with this node.
     * @return
     */
    public int getKey(){
        return id;
    }
    /** Returns the location of this node, if none return null.
     * @return
     */
    public GeoLocation getLocation(){
        return pos;
    }

    /** Allows changing this node's location.
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(GeoLocation p) {
        this.pos = (GeoLoc) p;
    }

    /**
     * Returns the weight associated with this node.
     * @return
     */
    public double getWeight(){
        return this._weight;
    }
    /**
     * Allows changing this node's weight.
     * @param w - the new weight
     */
    public void setWeight(double w){
        this._weight = w;
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
        Edge new_edge = new Edge(this.id, dst, weight);
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

        Node node_copy = new Node(this.id, edges, this.pos.copy());
        node_copy.setInfo(this._info);
        node_copy.setTag(this._tag);

        return node_copy;
    }

    public void cleanEdges(){
        this._edges.clear();
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure
     * {@code sgn(x.compareTo(y)) == -sgn(y.compareTo(x))}
     * for all {@code x} and {@code y}.  (This
     * implies that {@code x.compareTo(y)} must throw an exception iff
     * {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies
     * {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code x.compareTo(y)==0}
     * implies that {@code sgn(x.compareTo(z)) == sgn(y.compareTo(z))}, for
     * all {@code z}.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * {@code (x.compareTo(y)==0) == (x.equals(y))}.  Generally speaking, any
     * class that implements the {@code Comparable} interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param other the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Node other) {
        return Double.compare(this.getWeight(), other.getWeight());
    }
}

