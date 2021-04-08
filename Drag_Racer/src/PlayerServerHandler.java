import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerServerHandler extends Thread {
    private final Server server; // this will store the server
    private final Socket clientSock; // this will store the client socket

    // the following will be used to read and write from the client
    private BufferedReader in;
    private PrintWriter out;

    private int playerID; // this will store the player's ID

    public PlayerServerHandler(Socket s, Server serv) {
        clientSock = s;
        server = serv;

        try { // instantiate the input and output for the client-server
            in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            out = new PrintWriter(clientSock.getOutputStream(), true);     // autoflush
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run() { // this will run upon the .start() method being called
        playerID = server.addPlayer(this); // this will add itself as a handler and receive the player's ID

        out.println(playerID); // this will now send the client it's player ID so that it can store it

        while (true) {
            try {
                String str = in.readLine(); // this will read in the message
                if (str != null) { // if there is something to be read
                    if (str.startsWith("FINISHED: ")) { // if the client is reporting that it finished the race
                        server.tellOther(playerID, "TIME:" + playerID + ":" + str.split(" ")[1]); // send a message to the other client so that it can update
                    }
                }

                if (str == null) { // this will check if the client is disconnected
                    server.removePlayer(playerID); // remove it if it is
                }

            } catch (IOException e) {
                System.out.println(e);
                System.out.println("Error");
                e.printStackTrace();
            }
        }
    }

    synchronized public void sendMessage(String msg) { // this function is used to send a message to the client
        try {
            out.println(msg);
        } catch (Exception e) {
            System.out.println("Handler for player " + playerID + "\n" + e);
        }
    }

    synchronized public String getMessage() { // you can use this function to read in any message
        try {
            if (in.ready()) {
                return in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
} 