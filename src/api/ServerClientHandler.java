package api;

import controller.ServerAddProjectOrMarks;
import controller.ServerRegistration;
import controller.Server_login_registerController;
import exception.CustomException;
import model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
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
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | CustomException e) {
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
    Server_login_registerController m = new Server_login_registerController();
    ServerAddProjectOrMarks pm = new ServerAddProjectOrMarks();
    ServerRegistration register = new ServerRegistration();
    private Message handleClientRequests(Message request) throws NoSuchAlgorithmException, CustomException {
        switch (request.getOperation()) {
            case None:

                return new Message("None", Operation.None);
            case Login:

                LoginRegisterModel log = (LoginRegisterModel) request.getBody();
                RegistrationModel r=  m.login(log.username,log.password);

                //TODO: write login function Here
                return new Message(r, Operation.Login);


            case SignUp:
                //TODO: write SignUp function Here
                LoginRegisterModel e = (LoginRegisterModel) request.getBody();
              int y=  m.register(e.username,e.password);
                String id = Integer.toString(y);
                return new Message(id, Operation.SignUp);

            case Project:
                //TODO: write Project function Here
                AddData d = (AddData) request.getBody();
                int c=  pm.addProject(d);
                String idd = Integer.toString(c);
                return new Message(idd, Operation.Project);

            case Marks:
                //TODO: write Marks function Here
                AddData da = (AddData) request.getBody();
                int s=  pm.addMaterialMarks(da);
                String v = Integer.toString(s);
                return new Message(v, Operation.Marks);


            case Register:
                RegistrationModel reg = (RegistrationModel) request.getBody();
                RegistrationModel m =  register.Registration(reg);

                //TODO: write Register function Here
                return new Message(m, Operation.Register);
            default:
                return new Message("Determine Operation Type Please", Operation.None);
        }
    }
}
