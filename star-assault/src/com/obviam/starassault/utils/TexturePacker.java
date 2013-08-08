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
        TexturePacker2.process(
                "star-assault-android\\assets\\data\\images\\",
                "star-assault-android\\assets\\data\\textures\\",
                "textures.pack");
    }
}
