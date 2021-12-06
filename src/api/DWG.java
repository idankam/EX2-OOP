package api;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * This interface represents a Directional Weighted Graph,
 * As in: http://math.oxford.emory.edu/site/cs171/directedAndEdgeWeightedGraphs/
 * The interface has a road-system or communication network in mind -
 * and should support a large number of nodes (over 100,000).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 */

public class DWG implements DirectedWeightedGraph, Iterator{

    private HashMap<Integer, Node> nodes;

    public DWG(){
        this.nodes = new HashMap<>();
    }

    public DWG(HashMap<Integer, Node> nodes){
        this.nodes = nodes;
    }



    /**
     * returns the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    public NodeData getNode(int key){
        return this.nodes.get(key);
    }
    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     * @param src
     * @param dest
     * @return
     */
    public EdgeData getEdge(int src, int dest){
        return this.nodes.get(src).getEdge(dest);
    }
    /**
     * adds a new node to the graph with the given node_data.
     * Note: this method should run in O(1) time.
     * @param n
     */
    public void addNode(NodeData n){
        this.nodes.put(n.getKey(), (Node) n);
    }
    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     * @param src - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    public void connect(int src, int dest, double w){
        this.nodes.get(src).setEdge(dest, w);
    }
    /**
     * This method returns an Iterator for the
     * collection representing all the nodes in the graph.
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     * @return Iterator<node_data>
     */
    public Iterator<NodeData> nodeIter(){
        return (Iterator<NodeData>) this.nodes.values();
    }
    /**
     * This method returns an Iterator for all the edges in this graph.
     * Note: if any of the edges going out of this node were changed since the iterator was constructed - a RuntimeException should be thrown.
     * @return Iterator<EdgeData>
     */
    public Iterator<EdgeData> edgeIter(){
        ArrayList<EdgeData> edges = new ArrayList<>();
        for (Iterator<NodeData> nodes = this.nodeIter(); nodes.hasNext(); ) {
            NodeData node = nodes.next();
            HashMap<Integer, Edge> curr_edges = ((Node)node).getEdges();
            for (EdgeData edge: curr_edges.values()){
                edges.add(edge);
            }
        }
        return edges.iterator();
    }


    /**
     * This method returns an Iterator for edges getting out of the given node (all the edges starting (source) at the given node).
     * Note: if the graph was changed since the iterator was constructed - a RuntimeException should be thrown.
     * @return Iterator<EdgeData>
     */
    public Iterator<EdgeData> edgeIter(int node_id){
        return (Iterator<EdgeData>) this.nodes.get(node_id).getEdges().values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    public NodeData removeNode(int key){
        Node removedNode = this.nodes.remove(key);
        for (Node node: nodes.values()){
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
        return this.nodes.get(src).removeEdge(dest);
    }
    /** Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    public int nodeSize();
    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    public int edgeSize();
    /**
     * Returns the Mode Count - for testing changes in the graph.
     * @return
     */
    public int getMC();

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Object next() {
        return null;
    }

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.
     * <p>
     * The behavior of an iterator is unspecified if the underlying collection
     * is modified while the iteration is in progress in any way other than by
     * calling this method, unless an overriding class has specified a
     * concurrent modification policy.
     * <p>
     * The behavior of an iterator is unspecified if this method is called
     * after a call to the {@link #forEachRemaining forEachRemaining} method.
     *
     * @throws UnsupportedOperationException if the {@code remove}
     *                                       operation is not supported by this iterator
     * @throws IllegalStateException         if the {@code next} method has not
     *                                       yet been called, or the {@code remove} method has already
     *                                       been called after the last call to the {@code next}
     *                                       method
     * @implSpec The default implementation throws an instance of
     * {@link UnsupportedOperationException} and performs no other action.
     */
    @Override
    public void remove() {
        Iterator.super.remove();
    }

    /**
     * Performs the given action for each remaining element until all elements
     * have been processed or the action throws an exception.  Actions are
     * performed in the order of iteration, if that order is specified.
     * Exceptions thrown by the action are relayed to the caller.
     * <p>
     * The behavior of an iterator is unspecified if the action modifies the
     * collection in any way (even by calling the {@link #remove remove} method
     * or other mutator methods of {@code Iterator} subtypes),
     * unless an overriding class has specified a concurrent modification policy.
     * <p>
     * Subsequent behavior of an iterator is unspecified if the action throws an
     * exception.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     while (hasNext())
     *         action.accept(next());
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEachRemaining(Consumer action) {
        Iterator.super.forEachRemaining(action);
    }
}

