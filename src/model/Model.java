package model;

import java.io.Serializable;

abstract public class Model implements Serializable {
    public abstract String toString();
    public abstract void parseToModel(String message);
}
