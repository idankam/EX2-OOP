import implementations.GeoLoc;
import implementations.Node;
import org.junit.jupiter.api.Test;
import api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Ex2Test {

    @Test
    void getGrapg() {
        try{
            String file = "data/G3.json";
            DirectedWeightedGraph graph = Ex2.getGrapg(file);
            graphFuncsTest(graph);
        }
        catch(Exception e){
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void getGrapgAlgo() {
        try{
            String file = "data/G3.json";
            DirectedWeightedGraphAlgorithms graphA = Ex2.getGrapgAlgo(file);
            DirectedWeightedGraph graph = graphA.getGraph();
            assertTrue(graphA.isConnected());
            assertTrue(graphA.center().getKey() == 40);
            graphA.shortestPath(5,39);

            List<NodeData> list_cities = new ArrayList<>();
            int[] cities_keys = {3,6,10,32,16,1,5,40};
            for (int key : cities_keys){
                list_cities.add(graphA.getGraph().getNode(key));
            }

            List<Node> list_ans = new ArrayList<>();
            int[] ans_keys = {32,31,36,37,38,39,40,39,15,16,15,14,13,11,9,1,10,12,3,13,5,6};
            for (int key : ans_keys){
                list_ans.add((Node) graphA.getGraph().getNode(key));
            }

            List<NodeData> path = graphA.tsp(list_cities);

            assertEquals(path.size(), list_ans.size());
            for (int i = 0; i < path.size(); i++) {
                assertEquals(path.get(i).getKey(), list_ans.get(i).getKey());
            }

            graphFuncsTest(graph);
            assertTrue(!graphA.isConnected());
            graphA.load("data/G2.json");
            graphA.save("data/saved.json");
        }
        catch(Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private void graphFuncsTest(DirectedWeightedGraph graph) {
        assertTrue(graph.getMC() == 0);

        graph.edgeIter();
        graph.nodeIter();

        GeoLocation g =new GeoLoc(1,2,3);
        NodeData n = new Node(70, g);
        graph.addNode(n);
        graph.removeNode(30);
        graph.connect(20,70, 1.2);

        assertTrue(graph.getMC() == 3);
        assertTrue(graph.nodeSize() == 48);
    }

    @Test
    void main() {
        try{
            String[] data = {"data/G3.json"};
            Ex2.main(data);
            String[] data0 = {};
            Ex2.main(data0);
            String[] data1 = {"fileNotExist"};
            Ex2.main(data1);
        }catch(Exception e){
            e.printStackTrace();
            assertTrue(false);
        }
    }
}