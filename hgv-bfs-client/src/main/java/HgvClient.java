import java.io.*;
import java.net.Socket;

public class HgvClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    HgvClient (int port) throws IOException {
        socket = new Socket("localhost", port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out  = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public Graph loadGraph(int id) {
        send("{type: \"GetGraph\", graphId: "+id+"}");
        return new Graph(receive());
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException e) {
            System.out.println("Error occurred while sending");
        }
    }

    private String receive() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println("Error occurred while receiving");
            return null;
        }
    }

    public void setColor(int i, String s) {
        send("{ type: \"ChangeMetadata\", key: \"color\", value: \"" + s + "\", id: "+i+"}");
        System.out.println(receive());
    }

    public void pause() {
        send("{ type: \"Pause\"}");
        synchronized (this) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(receive());
    }

    public void render() {
        send("{ type: \"Render\"}");
        System.out.println(receive().trim());
    }
}
