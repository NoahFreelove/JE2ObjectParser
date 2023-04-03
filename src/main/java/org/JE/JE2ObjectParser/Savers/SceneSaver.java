package org.JE.JE2ObjectParser.Savers;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Scene.Scene;

import java.io.File;
import java.io.FileWriter;

public class SceneSaver {

    public static void saveSceneToFolder(Scene input, String folderPath){
        GameObject[] objects = input.world.gameObjects.toArray(new GameObject[0]);

        // if folderPath ends with // or \ remove it
        if(folderPath.endsWith("\\") || folderPath.endsWith("//"))
            folderPath = folderPath.substring(0, folderPath.length()-1);

        if(!new File(folderPath).exists())
            new File(folderPath).mkdirs();

        for (GameObject object: objects) {
            writeToFile(saveAllGameObjectData(object), folderPath + "\\" + object.identity().uniqueID + ".txt");
        }
    }

    public static String[] saveAllGameObjectData(GameObject object){
        ObjectSaver<GameObject> saver = new ObjectSaver<>();

        String[] objectLines = saver.saveObject(object);

        StringBuilder appender = new StringBuilder();
        for (String str : objectLines) {
            appender.append(str).append("\n");
        }

        object.getScripts().forEach(script -> {
            appender.append("Script:").append(script.getClass().getName()).append("\n");

            ObjectSaver<Script> scriptSaver = new ObjectSaver<>();
            for (String line : scriptSaver.saveObject(script)) {
                appender.append(line).append("\n");
            }
        });
        appender.append("Script:").append("\n");

        return appender.toString().split("\n");
    }

    public static void writeToFile(String[] data, String path){
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(path);
            for (String line : data) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
