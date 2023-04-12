package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2.Resources.DataLoader;
import org.JE.JE2ObjectParser.Loaders.SceneLoader;
import org.JE.JE2ObjectParser.Savers.SceneSaver;

import java.util.Arrays;

public class CompressionTest {
    public static void main(String[] args) throws InterruptedException {
        //SceneSaver.compressScene("Scene", "zip.zip","texture1.png","texture1_N.png");
        String[] loadedAssets = SceneLoader.unzipSceneToFolder("zip.zip","Loaded");
        System.out.println(Arrays.toString(loadedAssets));
        System.out.println(DataLoader.getDataFilePath("/"));
    }
}
