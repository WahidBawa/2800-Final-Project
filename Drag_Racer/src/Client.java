import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private static final int PORT = 4321;          // server details
    private static final String HOST = "localhost";
    long startTime, endTime;
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    private boolean counting, raceEnded;


    public Client() throws IOException {
        counting = false;
        startTime = endTime = 0;
        raceEnded = false;
    }

    public void run() {
        makeContact();

        while (true) {
            try {
                if (in.ready()) {
                    System.out.println(in.readLine());
                }

//                out.println("this is merely a test");
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }

            if (!raceEnded) {
                System.out.println(getTimePassed());
            } else {
                System.out.println(getFinalTime());
            }
        }
    }

    private void makeContact() {
        try {
            sock = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true); // autoflush

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

    public void startCounting() {
        if (!counting) {
            counting = true;
            startTime = System.currentTimeMillis();
        }
    }

    public boolean isCounting() {
        return counting;
    }

    public void stopCounting() {
        if (counting) {
            counting = false;
            raceEnded = true;
            endTime = System.currentTimeMillis();
        }
    }

    public double getTimePassed() {
        if (counting) {
            return (System.currentTimeMillis() - startTime) / 1000f;
        }

        return 0;
    }

    public double getFinalTime() {
        if (raceEnded) {
            return (endTime - startTime) / 1000f;
        }

        return -1;
    }

    public boolean isRaceEnded() {
        return raceEnded;
    }
}
