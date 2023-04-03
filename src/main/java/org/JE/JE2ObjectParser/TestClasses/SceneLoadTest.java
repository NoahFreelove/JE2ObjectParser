package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2.Manager;
import org.JE.JE2.Objects.GameObject;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2ObjectParser.Loaders.SceneLoader;
import org.JE.JE2ObjectParser.Savers.SceneSaver;

public class SceneLoadTest {

    public static void main(String[] args) {
        Manager.run();
        Scene coolScene = new Scene();

        SceneLoader.loadToScene(coolScene, "");

        System.out.println(coolScene.world.gameObjects.get(0).getTransform().position());
        System.out.println(coolScene.world.gameObjects.get(0).getScript(Camera.class).backgroundColor);
        coolScene.setCamera(coolScene.world.gameObjects.get(0).getScript(Camera.class));
        Manager.setScene(coolScene);
    }
}
