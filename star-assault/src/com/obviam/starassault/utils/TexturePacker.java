package com.obviam.starassault.utils;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

/**
 * User: Ceryni
 * Date: 8/8/13
 * Time: 5:02 PM
 */
//Assumes working directory is the project level directory
public class TexturePacker {
    public static void main(String[] args) {
        String fileSeparator = System.getProperty("file.separator");
        String rootPath = "star-assault-android" + fileSeparator + "assets" + fileSeparator + "data";
        String imagesPath = rootPath + fileSeparator + "images" + fileSeparator;
        String texturesPath = rootPath + fileSeparator + "textures" + fileSeparator;

        TexturePacker2.process(
                imagesPath,
                texturesPath,
                "textures.pack");
    }
}
