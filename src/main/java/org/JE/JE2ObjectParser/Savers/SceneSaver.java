package org.JE.JE2ObjectParser.Savers;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2.Scene.Scene;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class SceneSaver {

    public static void saveSceneToFolder(Scene input, String folderPath){
        GameObject[] objects = input.world.gameObjects.toArray(new GameObject[0]);

        // if folderPath ends with // or \ remove it
        if(folderPath.endsWith("\\") || folderPath.endsWith("//"))
            folderPath = folderPath.substring(0, folderPath.length()-1);



        if(!new File(folderPath).exists())
            new File(folderPath).mkdirs();

        for (GameObject object: objects) {
            writeToFile(saveAllGameObjectData(object), folderPath + "\\" + object.identity().uniqueID + ".JEObject");
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

    public static void compressScene(String folderPath, String zipPath, String... assets){
        // if zip exists, delete it
        if(new File(zipPath).exists())
            new File(zipPath).delete();
        Path p = null;
        try {
            p = Files.createFile(Paths.get(zipPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(folderPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException e) {
                            System.err.println(e);
                        }
                    });

            for (String asset: assets) {
                String filepath = DataLoader.getDataFilePath("/"+asset);
                System.out.println(filepath);
                if(filepath == null)
                    continue;
                // include asset in zip, but keep directory structure
                ZipEntry zipEntry = new ZipEntry(asset);
                try {
                    zs.putNextEntry(zipEntry);
                    Files.copy(Paths.get(filepath), zs);
                    zs.closeEntry();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
