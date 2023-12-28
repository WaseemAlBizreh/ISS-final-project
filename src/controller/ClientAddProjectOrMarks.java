package controller;

import api.ClientSocket;
import api.Encryption;
import api.Operation;
import exception.CustomException;
import model.Message;
import model.AddData;

public class ClientAddProjectOrMarks {

    private final ClientSocket clientSocket;

    public ClientAddProjectOrMarks(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public int addProject(AddData model) throws CustomException {

        Message request = new Message( model , Operation.Project);

        Message response = clientSocket.sendMessageToServer(request, Encryption.None);
        int id = Integer.parseInt(response.getMessage());
        return id;
        // System.out.println("Server: " + response);

    }
    public int addMaterialMarks(AddData model) throws CustomException {

        Message request = new Message( model , Operation.Marks);

        Message response = clientSocket.sendMessageToServer(request, Encryption.None);
        int id = Integer.parseInt(response.getMessage());
        return id;
        // System.out.println("Server: " + response);

    }


}
