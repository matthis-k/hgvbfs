import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        int graphId = Integer.parseInt(args[1]);
        int startId = 1;
        HgvClient hgv = new HgvClient(port);
        Graph g = hgv.loadGraph(graphId);
        g.bfs(1, hgv);

    }
}
