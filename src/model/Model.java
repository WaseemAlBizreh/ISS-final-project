package model;

import java.io.Serializable;

abstract public class Model implements Serializable {
    public abstract void parseToModel(String message);
}
