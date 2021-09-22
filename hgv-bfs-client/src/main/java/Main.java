import java.io.BufferedInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = Integer.parseInt(args[0]);
        int graphId = Integer.parseInt(args[1]);
        int startId = Integer.parseInt(args[2]);
        HgvClient hgv = new HgvClient(port);
        Graph g = hgv.loadGraph(graphId);
        g.bfs(startId, hgv);
        hgv.render();

        Object lock = new Object();
        synchronized (lock) {
            lock.wait(10000000);
        }
    }
}
