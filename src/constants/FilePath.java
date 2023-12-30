package constants;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public  class FilePath {

    public static String desktopPath = System.getProperty("user.home") + "/Desktop\\ISS";

    public static String createFile(String fileName,String type){
        File file= new File(desktopPath);
        if (!file.exists())
            new java.io.File(desktopPath  ).mkdirs();
        new java.io.File(desktopPath+"\\"+type).mkdirs();
        String filePath = desktopPath + "\\" +type+"\\"+ fileName;
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {

        } else {
            try {
                Files.createFile(path);
            } catch (Exception e) {
                System.err.println("Error creating the file: " + e.getMessage());
            }
        }
        return filePath;
    }


}

