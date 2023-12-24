package model;

abstract public class Model {
    public abstract String toString();
    public abstract Model parseToModel(String message);
}
