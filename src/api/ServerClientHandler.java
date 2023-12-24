package api;

import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerClientHandler implements Runnable {
    private static final List<ServerClientHandler> clients = new ArrayList<>();
    private final Socket clientSocket;
    private final ObjectOutputStream sender;
    private final ObjectInputStream receiver;

    public ServerClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        clients.add(this);
        System.out.println("New client is connected: " + clientSocket.getInetAddress());
        try {
            // Establish Sender
            sender = new ObjectOutputStream(clientSocket.getOutputStream());

            // Establish Receiver
            receiver = new ObjectInputStream(clientSocket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing client handler.");
        }
    }

    @Override
    public void run() {
        try {
            // Handle client requests
            while (true) {
                // Read a message from the client
                Message request = (Message) receiver.readObject();

                // Check if the message is not null (indicating that the client has disconnected)
                if (request == null) {
                    System.out.println("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }

                System.out.println("\nClient: " + request);

                // Process the received message or send a response if needed
                Message response = handleClientRequests(request);
                sender.writeObject(response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Remove this client handler from the list
            clients.remove(this);

            // Close the streams and socket properly when done
            try {
                clientSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private Message handleClientRequests(Message request) {
        switch (request.getOperation()) {
            case None:
                return new Message("None", Operation.None);
            case Login:
                //TODO: write login function Here
                return new Message("Login", Operation.Login);
            case SignUp:
                //TODO: write SignUp function Here
                return new Message("SignUp", Operation.SignUp);
            default:
                return new Message("Determine Operation Type Please", Operation.None);
        }
    }
}
