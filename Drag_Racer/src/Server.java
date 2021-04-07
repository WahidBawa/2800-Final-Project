import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 4321;

    private static final int MAX_PLAYERS = 1;         // two-person game
    private final static int PLAYER1 = 1;
    private final static int PLAYER2 = 2;             // use to be MACHINE

//    private static final int MAX_PLAYERS = 2;         // two-person game
//    private final static int PLAYER1 = 1;
//    private final static int PLAYER2 = 2;             // use to be MACHINE

    /* data structures shared by the handlers */
    private final PlayerServerHandler[] handlers;        // handlers for players
    private int numPlayers;

    public Server() {                    // Concurrently process players
        handlers = new PlayerServerHandler[MAX_PLAYERS];
//        handlers[0] = handlers[1] = null;
        handlers[0] = null;
        numPlayers = 0;

        ServerSocket serverSock;
        try {
            serverSock = new ServerSocket(PORT);
            Socket clientSock;
            while (true) {
                System.out.println("Waiting for a client...");
                clientSock = serverSock.accept();
                new PlayerServerHandler(clientSock, this).start();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // *************************************
    public static void main(String[] args) {
        new Server();
    }

    /* methods for child threads to access shared data structures */
    synchronized public boolean enoughPlayers() {
        return (numPlayers == MAX_PLAYERS);
    }

    /* store a reference to the handler, and return a player ID to the handler.
     * The ID is the array index where the handler is stored + 1. */
    synchronized public int addPlayer(PlayerServerHandler h) {
        for (int i = 0; i < MAX_PLAYERS; i++)
            if (handlers[i] == null) {
                handlers[i] = h;
                numPlayers++;
                return i + 1; // playerID is 1 or 2 (array index + 1)
            }
        return -1; // means we have enough players already
    }

    synchronized public void removePlayer(int playerID) {
        handlers[playerID - 1] = null;      // no checking done of player value
        numPlayers--;
    }

    /* a function to send message to the other player */
    synchronized public void tellOther(int playerID, String msg) {
        int otherID = ((playerID == PLAYER1) ? PLAYER2 : PLAYER1);
        if (handlers[otherID - 1] != null) // index is ID-1
            handlers[otherID - 1].sendMessage(msg);
    }

}
