package model;

import api.Operation;

import java.io.Serializable;

public class Message extends Model implements Serializable {
    private Operation operation;
    private Model body;
    private String message;

    public Message() {
    }

    public Message(Model body, Operation operation) {
        this.operation = operation;
        this.body = body;
    }

    public Message(String message, Operation operation) {
        this.message = message;
        this.operation = operation;
    }

    public Message(Model body, String message, Operation operation) {
        this.message = message;
        this.operation = operation;
        this.body = body;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Model getBody() {
        return body;
    }

    public void setBody(Model body) {
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        //TODO: new Method better
        //op:message: login .message. body:message: null .message. message:message: Hello
        //TODO: old method bad
        //op: login . body: null . message: Hello
        //op: login . body: username: waseem . password: waseem123 . message: Hello
        StringBuilder builder = new StringBuilder();
        builder.append("op:message: ").append(getOperationString()).append(" .message. ");
        builder.append("body:message: ").append(body).append(" .message. ");
        builder.append("message:message: ").append(message);
        return builder.toString();
    }

    private String getOperationString() {
        return operation != null ? operation.toString(): null;
    }

    @Override
    public Model parseToModel(String message) {
        Message model = new Message();
        String[] parts = message.split(" .message. ");
        for (String part : parts) {
            String[] keyValue = part.split(":message: ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "op":
                        model.setOperation(Operation.valueOf(value));
                        break;
                    case "body":
                        model.setBody(detectModel(model.operation, value));
                        break;
                    case "message":
                        model.setMessage(value);
                        break;
                }
            }
        }
        return model;
    }

    Model detectModel(Operation operation, String data) {
        //TODO: every Model should Exist here
        switch (operation){
            case Login:
            case SignUp:
                return new LoginRegisterModel().parseToModel(data);
            default:
                return null;
        }
    }
}
