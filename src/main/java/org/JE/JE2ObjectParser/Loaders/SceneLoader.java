package org.JE.JE2ObjectParser.Loaders;

import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Objects.Scripts.Base.Script;
import org.JE.JE2.Scene.Scene;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SceneLoader {

    public static void loadToScene(Scene scene, String folderPath){
        // Get all files in directory in arraylist
        if(new File(folderPath).isDirectory()){
            File[] files = new File(folderPath).listFiles();
            if(files != null){
                for(File file : files){
                    if(file.isFile()){
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

}
