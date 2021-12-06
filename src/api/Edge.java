package api;

public class Edge implements EdgeData {

    private int _src;
    private int _dst;
    private double _weight;
    private int _tag;

    public Edge(int src, int dst, double weight){
        this._src = src;
        this._dst = dst;
        this._weight = weight;
        this._tag = Integer.parseInt(null);
    }


    /**
     * The id of the source node of this edge.
     * @return
     */
    public int getSrc(){
        return _src;
    }
    /**
     * The id of the destination node of this edge
     * @return
     */
    public int getDest(){
        return _dst;
    }
    /**
     * @return the weight of this edge (positive value).
     */
    public double getWeight(){
        return _weight;
    }
    /**
     * Returns the remark (meta data) associated with this edge.
     * @return
     */
    public String getInfo(){
        String info = "src: " + _src +
                "\ndst: " + _dst +
                "\nweight: " + _weight;
        return info;
    }
    /**
     * Allows changing the remark (meta data) associated with this edge.
     * @param s
     */
    public void setInfo(String s){
        return; ///////////////////// complete!!!!!!!
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
}
