package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Rendering.Texture;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2ObjectParser.Savers.SceneSaver;

public class SceneSaveTest {
    public static void main(String[] args){
        Manager.run();
        Scene coolScene = new Scene();
        GameObject test = new GameObject();
        test.setPosition(1,0);
        SpriteRenderer sr = new SpriteRenderer();
        sr.defaultShaderIndex = 1;
        sr.setTexture(Texture.checkExistElseCreate("text1",-1,"texture1.png"));
        sr.setNormalTexture(Texture.checkExistElseCreate("text1",-1,"texture1_N.png"));
        test.addScript(sr);
        sr.load();

        Camera cam = new Camera();
        cam.backgroundColor = Color.RED;
        test.addScript(cam);
        coolScene.add(test);
        coolScene.add(new GameObject(), new GameObject(), new GameObject());

        SceneSaver.saveSceneToFolder(coolScene, "C:\\Users\\s201063813\\Desktop\\JE2ObjectParser\\Scene");

        Manager.setScene(coolScene);
        coolScene.setCamera(cam);
    }
}
