import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {
    private static final int PORT = 4321;
    private static final String HOST = "localhost";
    long startTime, endTime; // the start and end times of each race will be stored
    int id; // this will store the ID of the player

    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;

    private boolean counting, raceEnded, messageSent, idReceived; // booleans used to perform necessary functions

    public Client() throws IOException {
        counting = raceEnded = messageSent = idReceived = false; // initialize all the booleans to false
        startTime = endTime = 0; // set the start time to 0
    }

    public void run() {
        makeContact(); // this method call will make contact and connect to a server socket

        while (true) { // run infinitely
            try {
                if (in.ready()) { // if there is a message to be read
                    String str = in.readLine(); // store the message until we decide what to do with it
                    if (!idReceived) { // if the ID has not been received from the playerserverhandler yet
                        id = Integer.parseInt(str); // get the ID
                        idReceived = true; // set that we have received the ID
                    }
                    if (str.startsWith("TIME:")) { // check if the other client has sent you their finishing time
                        Menu.updateTimes(Integer.parseInt(str.split(":")[1]), str.split(":")[2]); // this will set the time in the menu
                    }

                }

            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
            if (raceEnded && !messageSent) { // if the race has ended
                out.println("FINISHED: " + getFinalTime()); // send the time that you achieved to the playerserverhandler
                messageSent = true; // sent the message
                Menu.updateTimes(id, Double.toString(getFinalTime())); // updates the menu locally to show your time
            }
        }
    }

    private void makeContact() { // connects to the server socket
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

    public void startCounting() { // this function will let the counting start to happen
        if (!counting) { // if not already counting
            counting = true;
            startTime = System.currentTimeMillis(); // save the start time
        }
    }

    public boolean isCounting() {
        return counting;
    } // return if the race is still ongoing

    public void stopCounting() { // if the car has hit the finish line
        if (counting) {
            counting = false;
            raceEnded = true;
            endTime = System.currentTimeMillis(); // save the end time
        }
    }

    public double getFinalTime() { // returns the time elapsed to complete the race
        if (raceEnded) {
            return (endTime - startTime) / 1000f; // return the time taken to complete the race
        }
        return -1;
    }

    public boolean isRaceEnded() {
        return raceEnded;
    } // boolean to check if the race has ended or not
}
