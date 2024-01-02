package model;

public class AddData extends Model{
    public int id;
    public String name;
    public String content;
    public String signatureBytes;

    public AddData (){}

    public void setSignatureBytes(String signatureBytes) {
        this.signatureBytes = signatureBytes;
    }

    public String getSignatureBytes() {
        return signatureBytes;
    }

    public AddData(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
    public AddData(int id,  String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id:addDescription: ").append(id).append(" .addDescription. ");
        builder.append("content:addDescription: ").append(content).append(" .addDescription. ");
        builder.append("signatureBytes:addDescription: ").append(signatureBytes).append(" .addDescription. ");
        builder.append("name:addDescription: ").append(name);
        return builder.toString();
    }

    @Override
    public void parseToModel(String message) {

        String[] parts = message.split(" .addDescription. ");
        for (String part : parts) {
            String[] keyValue = part.split(":addDescription: ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "id":
                        this.setId(Integer.parseInt(value));
                        break;
                    case "content":
                        this.setContent(value);
                        break;
                    case "name":
                        this.setName(value);
                        break;
                    case "signatureBytes":
                        this.setSignatureBytes(value);
                        break;
                }
            }
        }
    }
}
