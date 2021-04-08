import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 4321;

    private static final int MAX_PLAYERS = 2;
    private final static int PLAYER1 = 1;
    private final static int PLAYER2 = 2;

    private final PlayerServerHandler[] handlers;        // handlers for players
    private int numPlayers;

    public Server() {                    // Concurrently process players
        handlers = new PlayerServerHandler[MAX_PLAYERS];
        handlers[0] = handlers[1] = null;
        numPlayers = 0;

        ServerSocket serverSock; // this will store the server socket
        try {
            serverSock = new ServerSocket(PORT);
            Socket clientSock;
            while (true) { // loop through until the server makes a connection with a client
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new PlayerServerHandler(clientSock, this).start(); // create a new handler to handle this client-server connection
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        new Server();
    }

    synchronized public boolean enoughPlayers() {
        return (numPlayers == MAX_PLAYERS);
    }

    synchronized public int addPlayer(PlayerServerHandler h) { // this will be called from the playerserverhandler
        for (int i = 0; i < MAX_PLAYERS; i++) // loop through all of the players
            if (handlers[i] == null) { // if this player has not yet been added
                handlers[i] = h; // set the handler
                numPlayers++; // increase the number of handlers
                return i + 1; // playerID is 1 or 2 (array index + 1)
            }

        return -1; // means we have enough players already
    }

    synchronized public void removePlayer(int playerID) {
        handlers[playerID - 1] = null;      // no checking done of player value
        numPlayers--;
    }

    synchronized public void tellOther(int playerID, String msg) { // this function will relay a message from one client to the other
        int otherID = ((playerID == PLAYER1) ? PLAYER2 : PLAYER1);
        if (handlers[otherID - 1] != null) // index is ID-1
            handlers[otherID - 1].sendMessage(msg);
    }

}
