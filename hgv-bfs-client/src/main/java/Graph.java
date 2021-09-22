import org.json.JSONObject;

import java.util.HashMap;
import java.util.Vector;

public class Graph {
    private int[] v;
    private int[][] e;

    private Vector<Integer> visited = new Vector<>();

    public Graph(String received) {
        System.out.println(received);
        JSONObject graph = new JSONObject(received);
        v = new int[graph.getJSONArray("nodes").length()];
        e = new int[graph.getJSONArray("edges").length()][2];
        int prefixSum = 0;
        Object[] nodes = graph.getJSONArray("nodes").toList().toArray();
        for (int i = 0; i < nodes.length; i++) {
            v[i] = ((HashMap<String, Integer>)nodes[i]).get("id");
        }
        Object[] edges = graph.getJSONArray("edges").toList().toArray();
        for (int i = 0; i < edges.length; i++) {
            e[i][0] = ((HashMap<String, Integer>)edges[i]).get("node1");
            e[i][1] = ((HashMap<String, Integer>)edges[i]).get("node2");
        }
    }

    public void bfs(int startNode, HgvClient hgv) {
        visited.add(startNode);
        int lastSize = 0;
        while(lastSize != visited.size()) {
            lastSize = visited.size();
            Vector<Integer> visitedThisIter = new Vector<>();
            for (int[] edge : e) {
                if (hasVisited(edge[0]) && !hasVisited(edge[1])) {
                    visitedThisIter.add(edge[1]);
                    visit(edge[1], hgv);
                } else if (!hasVisited(edge[0]) && hasVisited(edge[1])) {
                    visitedThisIter.add(edge[0]);
                    visit(edge[0], hgv);
                }
            }
            visited.addAll(visitedThisIter);
            hgv.render();
            synchronized (this) {
                try {
                    wait(2000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            hgv.pause();
        }
    }

    private void visit(int i, HgvClient hgv) {
        hgv.setColor(i, "#0000FF");
    }

    private boolean hasVisited(int id) {
        for (Integer i : visited) {
            if (i == id) { return true; }
        }
        return false;
    }



    private static int countEdgesFromId(JSONObject graph, int id) {
        int count = 0;
        for (Object edge : graph.getJSONArray("edges")) {
            JSONObject e  = (JSONObject) edge;
            if (e.getInt("node1") == id || e.getInt("node2") == id) {
                count ++;
            }
        }
        return count;
    }
}
