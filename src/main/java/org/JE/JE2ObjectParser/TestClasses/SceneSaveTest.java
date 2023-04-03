package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2ObjectParser.Savers.SceneSaver;

public class SceneSaveTest {
    public static void main(String[] args){
        Manager.run();
        Scene coolScene = new Scene();
        GameObject test = new GameObject();
        test.setPosition(1,2);
        Camera cam = new Camera();
        cam.backgroundColor = Color.BLUE;
        test.addScript(cam);
        coolScene.add(test);

        SceneSaver.saveSceneToFolder(coolScene, "");

        Manager.setScene(coolScene);
        coolScene.setCamera(cam);
    }
}
