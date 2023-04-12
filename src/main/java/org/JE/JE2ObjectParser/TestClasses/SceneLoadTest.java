package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2.IO.UserInput.Keyboard.KeyReleasedEvent;
import org.JE.JE2.IO.UserInput.Keyboard.Keyboard;
import org.JE.JE2.Manager;
import org.JE.JE2.Rendering.Camera;
import org.JE.JE2.Rendering.Renderers.SpriteRenderer;
import org.JE.JE2.Scene.Scene;
import org.JE.JE2.UI.UIElements.Label;
import org.JE.JE2.UI.UIElements.Style.Color;
import org.JE.JE2.UI.UIObjects.UIWindow;
import org.JE.JE2ObjectParser.Loaders.SceneLoader;
import org.joml.Vector2f;
import org.lwjgl.nuklear.Nuklear;

public class SceneLoadTest {

    public static void main(String[] args) {
        Manager.run();
        Keyboard.addKeyReleasedEvent((key, code) -> {
            if(Keyboard.codeToName(key).equals("F1"))
                refresh();
        });
        Keyboard.triggerKey(Keyboard.nameToCode("F1"),0);
    }

    private static void refresh(){
        Scene coolScene = new Scene();
        UIWindow helpWindow = new UIWindow("Help", Nuklear.NK_WINDOW_NO_SCROLLBAR);
        helpWindow.setSize(new Vector2f(200,30));
        helpWindow.setBackgroundColor(Color.DARK_GREY);
        helpWindow.children.add(new Label("Press F1 to refresh"));
        coolScene.addUI(helpWindow);

        String[] loadedAssets = SceneLoader.unzipSceneToFolder("zip.zip","Loaded");

        SceneLoader.loadToScene(coolScene, "Scene");

        System.out.println(coolScene.world.gameObjects.get(0).getTransform().position());
        System.out.println(coolScene.world.gameObjects.get(0).getScript(SpriteRenderer.class).getTextureFilepath());
        System.out.println(coolScene.world.gameObjects.size());
        coolScene.setCamera(coolScene.world.gameObjects.get(0).getScript(Camera.class));
        Manager.setScene(coolScene);
    }
}
