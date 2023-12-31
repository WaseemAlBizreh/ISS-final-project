package model;

public class AddData extends Model{
    public int id;
    public String name;
    public String content;

    public AddData(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
    public AddData(int id,  String content) {
        super();
        this.id = id;
        this.content = content;
    }


    @Override
    public void parseToModel(String message) {

    }
}
