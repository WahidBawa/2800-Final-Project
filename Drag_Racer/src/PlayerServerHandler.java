// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Handle messages from the client.

   Upon initial connection:
       response to client is:
             ok <playerID>   or    full
       message to other client if player is accepted:
             added <playerID>

   Other client messages:
   * disconnect
     message to other client:
             removed <playerID>
       
   * try <posn>
     response to client
        tooFewPlayers
      message to other client if turn accepted
        otherTurn <playerID> <posn>
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerServerHandler extends Thread {
    private final Server server;
    private final Socket clientSock;
    private BufferedReader in;
    private PrintWriter out;

    private int playerID; // this player id is assigned by CodeNet4By4.FBFServer

    public PlayerServerHandler(Socket s, Server serv) {
        clientSock = s;
        server = serv;
        System.out.println("Player connection request");
        try {
            in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
            out = new PrintWriter(clientSock.getOutputStream(), true);     // autoflush
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run() {
        playerID = server.addPlayer(this);

        out.println(playerID);
        System.out.println(playerID);

        while (true) {
            try {
                if (in.ready()) {
                    String str = in.readLine();
                    if (str.startsWith("FINISHED: ")) {
                        server.tellOther(playerID, "TIME:" + playerID + ":" + str.split(" ")[1]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void sendMessage(String msg) {
        try {
            out.println(msg);
        } catch (Exception e) {
            System.out.println("Handler for player " + playerID + "\n" + e);
        }
    }

    synchronized public String getMessage() {
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