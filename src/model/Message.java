package model;

import api.Operation;

import java.io.Serializable;

public class Message extends Model implements Serializable {
    private Operation operation;
    private Model body;
    private String message;

    public Message() {}

    public Message(Operation operation, Model body) {
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
        builder.append("Message { op: ").append(operation.toString()).append(". ");
        builder.append("body: ").append(body);
        if(message != null){
            builder.append(". message: ").append(message);
        }
        builder.append(". }");
        return builder.toString();
    }
}
