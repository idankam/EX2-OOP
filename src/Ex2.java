import gui.GUI_WINDOW;
import implementations.DWGAlgorithms;
import api.*;

/**
 * This class is the main class for Ex2 - your implementation will be tested using this class.
 */
public class Ex2 {
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraph getGrapg(String json_file) {
        DWGAlgorithms algo_graph = new DWGAlgorithms();
        algo_graph.load(json_file);
        DirectedWeightedGraph ans = algo_graph.getGraph();
        return ans;
    }
    /**
     * This static function will be used to test your implementation
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     * @return
     */
    public static DirectedWeightedGraphAlgorithms getGrapgAlgo(String json_file) {
        DirectedWeightedGraphAlgorithms ans = (DirectedWeightedGraphAlgorithms) new DWGAlgorithms();
        ans.load(json_file);
        return ans;
    }
    /**
     * This static function will run your GUI using the json fime.
     * @param json_file - a json file (e.g., G1.json - G3.gson)
     *
     */
    public static void runGUI(String json_file) {
        if (json_file == null){
            new GUI_WINDOW();
        }
        else{
            try {
                new GUI_WINDOW(json_file);
                }
            catch (Exception e){
                new GUI_WINDOW();
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1){
            runGUI(null);
        }
        else{
            runGUI(args[0]);
        }
    }
}