package puzzles.common.solver;

import java.util.*;


public class Solver {
    // TODO
    private static int totalConfig = 0;
    private static int uniqueConfig = 0;


    /**
     *
     * @param config
     * @return path list along with the total configs and unique configs
     */
    public static Collection<Configuration> solve(Configuration config){
        Queue<Configuration> queue = new LinkedList<>();
        queue.add(config);
        HashMap<Configuration, Configuration> pred = new HashMap<>();
        pred.put(config, null);
        while(!queue.isEmpty()){
            Configuration curr = queue.remove();
            if(curr.isSolution()){
                System.out.println("Total Configs: " + totalConfig);
                System.out.println("Unique Configs: " + uniqueConfig);
                return buildPathBFS(curr, pred);
            }
            else{
                for(Configuration n : curr.getNeighbors()){
                    totalConfig++;
                    if (!pred.containsKey(n)){
                        pred.put(n, curr);
                        queue.offer(n);
                        uniqueConfig++;
                    }
                }
            }
        }
        System.out.println("Total Configs: " + totalConfig);
        System.out.println("Unique Configs: " + uniqueConfig);
        return null;
    }

    /**
     * @param curr
     * @param pred
     * @return the constructed path, sending it back to solve().
     */
    public static List<Configuration> buildPathBFS(Configuration curr, HashMap<Configuration, Configuration> pred){
        List<Configuration> path = new LinkedList<>();
        System.out.println(pred);
        while (curr != null){
            path.add(0, curr);
            curr = pred.get(curr);
        }
        return path;
    }
}
