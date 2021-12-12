package tests;

import implementations.DWGAlgorithms;
import implementations.Node;
import org.junit.jupiter.api.BeforeAll;
import api.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGAlgorithmsTest {

    private static DWGAlgorithms  a, b, c, d, e, f, g;
    @BeforeAll
    public static void init(){

        a = new DWGAlgorithms();
        a.load("data/G1.json");

        b = new DWGAlgorithms();
        b.load("data/G2.json");

        c = new DWGAlgorithms();
        c.load("data/G3.json");

        d = new DWGAlgorithms();
        d.load("data/Gtest.json");

        e = new DWGAlgorithms();
        e.load("data/1000Nodes.json");

        f = new DWGAlgorithms();
        f.load("data/10000Nodes.json");

        g = new DWGAlgorithms();
        g.load("data/notConnected.json");
    }


    @org.junit.jupiter.api.Test
    void isConnected() {

        assertTrue(a.isConnected());
        assertTrue(b.isConnected());
        assertTrue(c.isConnected());
        assertTrue(d.isConnected());
        assertTrue(e.isConnected());
        assertTrue(f.isConnected());
        assertFalse(g.isConnected());

    }

    @org.junit.jupiter.api.Test
    void shortestPathDist() {

        assertEquals(d.shortestPathDist(1,2), 1.0);
        assertEquals(d.shortestPathDist(1,4), 3.0);
        assertEquals(d.shortestPathDist(2,0), 5.0);

    }

    @org.junit.jupiter.api.Test
    void shortestPath() {
        assertEquals(d.shortestPath(3,0).size(), 3);
        assertEquals(d.shortestPath(3,0).get(0).getKey(), 3);
        assertEquals(d.shortestPath(3,0).get(1).getKey(), 4);
        assertEquals(d.shortestPath(3,0).get(2).getKey(), 0);
        assertEquals(g.shortestPath(0,3), null);

    }

    @org.junit.jupiter.api.Test
    void center() {

        assertEquals(a.center().getKey(), 8);
        assertEquals(b.center().getKey(), 0);
        assertEquals(c.center().getKey(), 40);
        assertEquals(d.center().getKey(), 0);
    }

    @org.junit.jupiter.api.Test
    void tsp() {

        List<NodeData> list_cities = new ArrayList<>();
        int[] cities_keys = {3,6,10,32,16,1,5,40};
        for (int key : cities_keys){
            list_cities.add(c.getGraph().getNode(key));
        }

        List<Node> list_ans = new ArrayList<>();
        int[] ans_keys = {32,31,36,37,38,39,40,39,15,16,15,14,13,11,9,1,10,12,3,13,5,6};
        for (int key : ans_keys){
            list_ans.add((Node) c.getGraph().getNode(key));
        }

        List<NodeData> path = c.tsp(list_cities);

        assertEquals(path.size(), list_ans.size());
        for (int i = 0; i < path.size(); i++) {
            assertEquals(path.get(i).getKey(), list_ans.get(i).getKey());
        }

        assertEquals(g.tsp(list_cities), null);
    }
}