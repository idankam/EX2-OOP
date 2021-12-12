package implementations;
import api.*;

public class Edge implements EdgeData {

    private int src;
    private int dest;
    private double w;
    private int _tag;
    private String _info = "";

    public Edge(int src, int dest, double w){
        this.src = src;
        this.dest = dest;
        this.w = w;
        this._tag = -1;

    }


    /**
     * The id of the source node of this edge.
     * @return
     */
    public int getSrc(){
        return src;
    }
    /**
     * The id of the destination node of this edge
     * @return
     */
    public int getDest(){
        return dest;
    }
    /**
     * @return the weight of this edge (positive value).
     */
    public double getWeight(){
        return w;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    public String getInfo(){
        return _info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
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
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     * @param t - the new value of the tag
     */
    public void setTag(int t){
        this._tag = t;
    }

    public Edge copy(){
        Edge edge_copy = new Edge(this.src, this.dest, this.w);
        edge_copy.setTag(this._tag);
        edge_copy.setInfo(this._info);
        return edge_copy;
    }
}
