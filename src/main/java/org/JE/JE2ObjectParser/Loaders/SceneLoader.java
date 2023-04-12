package org.JE.JE2ObjectParser.Loaders;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2.Scene.Scene;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class SceneLoader {

    public static void loadToScene(Scene scene, String folderPath){
        // Get all files in directory in arraylist
        if(new File(folderPath).isDirectory()){
            File[] files = new File(folderPath).listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isFile()){
                        // make sure extension is .JEObject
                        if(file.getName().endsWith(".JEObject"))
                            scene.add(loadObjectFromFile(file.getAbsolutePath()));
                    }
                }
            }
        }
    }

    public static GameObject loadObjectFromFile(String filePath){
        GameObject out = new GameObject();
        String[] loadedData = fileReader(filePath);

        System.out.println("Loading file: " + filePath);

        ArrayList<Integer> scriptStartPoints = new ArrayList<>();
        ArrayList<String> scriptNames = new ArrayList<>();

        int i = 0;
        for (String line: loadedData) {
            line = line.trim();
            if(line.startsWith("Script:")){
                scriptStartPoints.add(i);
                scriptNames.add(line.replace("Script:",""));
            }
            i++;
        }
        // split loadedData  by scriptStartPoints
        String[][] splitData = new String[scriptStartPoints.size()+1][];

        for (int j = 0; j < scriptStartPoints.size(); j++) {
            if(j == 0){
                splitData[j] = new String[scriptStartPoints.get(j)];
                System.arraycopy(loadedData, 0, splitData[j], 0, scriptStartPoints.get(j));
            }
            else{
                splitData[j] = new String[scriptStartPoints.get(j)-scriptStartPoints.get(j-1)-1];
                System.arraycopy(loadedData, scriptStartPoints.get(j-1)+1, splitData[j], 0, scriptStartPoints.get(j)-scriptStartPoints.get(j-1)-1);
            }
        }

        splitData[splitData.length-1] = new String[loadedData.length-scriptStartPoints.get(scriptStartPoints.size()-1)-1];

        for (int j = 0; j < splitData.length; j++) {

            if(splitData[j] == null)
                continue;
            if (splitData[j].length == 0)
                continue;

            if(j == 0){
                ObjectLoader<GameObject> objectLoader = new ObjectLoader<>();
                out = objectLoader.parseString(out, splitData[j]);
            }
            else{

                // get script name
                String scriptName = scriptNames.get(j-1);
                // Create constructor instance using reflection
                try {
                    Class<?> scriptClass = Class.forName(scriptName);
                    ObjectLoader<Script> objectLoader = new ObjectLoader<>();
                    Script script = objectLoader.parseString((Script) scriptClass.getConstructor().newInstance(), splitData[j]);
                    if(out.getScripts().size() < j)
                        out.addScript(script);
                    else
                        out.setScript(j-1,script);
                    script.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return out;
    }


    public static String[] fileReader(String filePath){
        ArrayList<String> output = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(filePath));
            while (scanner.hasNextLine()){
                output.add(scanner.nextLine());
            }
        }
        catch (Exception ignore){}
        return output.toArray(new String[0]);
    }

    public static String[] unzipSceneToFolder(String zipPath, String folderPath){
        ArrayList<String> loadedAssets = new ArrayList<>();

        File dir = new File(folderPath);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipPath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                // if fileName does not end with .JEObject, move it to the resource folder;
                String resourceFolder = DataLoader.getDataFilePath("/");

                if(!fileName.endsWith(".JEObject")){
                    File newFile = new File(resourceFolder + File.separator + fileName);

                    String pathFromResourceFolder = newFile.getAbsolutePath().replace(resourceFolder,"");
                    loadedAssets.add(pathFromResourceFolder);
                    System.out.println("Unzipping to "+newFile.getAbsolutePath());
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }
                else {
                    File newFile = new File(folderPath + File.separator + fileName);
                    System.out.println("Unzipping to "+newFile.getAbsolutePath());
                    //create directories for sub directories in zip
                    new File(newFile.getParent()).mkdirs();
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                    //close this ZipEntry
                    zis.closeEntry();
                    ze = zis.getNextEntry();
                }

            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedAssets.toArray(new String[0]);
    }

}
