package model;

import api.Operation;

import java.io.Serializable;

public class Message extends Model implements Serializable {
    private Operation operation;
    private Model body;
    private String message;

    public Message() {}

    public Message(Model body, Operation operation) {
        this.operation = operation;
        this.body = body;
    }

    public Message(String message, Operation operation) {
        this.message = message;
        this.operation = operation;
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
        StringBuilder builder = new StringBuilder();
        builder.append("op: ").append(operation.toString()).append(" . ");
        builder.append("body: ").append(body).append(" . ");
        if(message != null){
            builder.append("message: ").append(message);
        }
        return builder.toString();
    }

    @Override
    public Model parseToModel(String message) {
        Message model = new Message();
        String[] parts = message.split(" . ");
        for (String part : parts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "op":
                        model.setOperation(Operation.valueOf(value));
                        break;
                    case "body":
                        model.setBody(body.parseToModel(value));
                        break;
                    case "message":
                        model.setMessage(value);
                        break;
                }
            }
        }
        return model;
    }
}
