import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private static final int PORT = 4321;          // server details
    private static final String HOST = "localhost";
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    public Client() throws IOException {

    }

    public void run() {
        makeContact();

        while (true) {
            try {
                if (in.ready()) {
                    System.out.println(in.readLine());
                }

                out.println("this is merely a test");
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    private void makeContact() {
        try {
            sock = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true); // autoflush

//            new FBFWatcher(this, in).start(); // start watching for server msgs
        } catch (Exception e) { // System.out.println(e);
            System.out.println("Cannot contact the Drag Racer Server");
            System.exit(0);
        }
    }

    public String getMessage() throws IOException {
       if (in.ready()) {
           return in.readLine();
       }

       return null;
    }


}
