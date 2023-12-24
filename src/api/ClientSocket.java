package api;

import exception.CustomException;
import model.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket {
    private Socket socket;
    private ObjectOutputStream sender;
    private ObjectInputStream receiver;

    public boolean connectToServer(String serverIP, int serverPort) throws IOException {
        // Establish a socket connection to the server
        socket = new Socket(serverIP, serverPort);

        // Establish Sender
        sender = new ObjectOutputStream(socket.getOutputStream());

        // Establish Receiver
        receiver = new ObjectInputStream(socket.getInputStream());

        // let's print a message for now
        System.out.println("Connected to server at " + serverIP + ":" + serverPort);
        return true;
    }

    public Message sendMessageToServer(Message message) throws CustomException {
        if (sender == null) {
            throw new CustomException("Not connected to the server");
        }
        try {
            sender.writeObject(message);
            sender.flush();

            // Wait for the server response
            return (Message) receiver.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Message newMessage = new Message();
            newMessage.setMessage("Error reading server response");
            return newMessage;
        }
    }

    public void disconnect() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add other methods for handling client requests or perform additional

//    private String listenToServer() {
//        try {
//            while (true) {
//                String message = reader.readLine();
//                if (message == null) {
//                    // Connection closed by the server
//                    System.out.println("Connection closed by the server");
//                    break;
//                }
//                // Handle the incoming message, e.g., display it to the user
//                if (!message.trim().isEmpty()) {
//                    return message;
//                } else {
//                    return "empty message";
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "empty message";
//    }
}
