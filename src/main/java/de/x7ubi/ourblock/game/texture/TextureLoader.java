package de.x7ubi.ourblock.game.texture;

import lombok.Getter;

import java.io.File;
import java.util.HashMap;

public class TextureLoader {

    private final HashMap<String, Texture> textures = new HashMap<>();

    @Getter
    private static final TextureLoader instance = new TextureLoader();

    private TextureLoader() {
    }

    public void loadAllTextures() {
        File dir = new File("src/main/resources/textures");
        File[] pngFiles = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));

        if (pngFiles != null) {
            for (File file : pngFiles) {
                String name = file.getName().substring(0, file.getName().length() - 4);
                Texture texture = new Texture("textures/" + file.getName());
                textures.put(name, texture);
                texture.bind();
            }
        }
    }


    public Texture getTexture(String name) {
        return textures.get(name);
    }

    public void cleanup() {
        for (Texture texture : textures.values()) {
            texture.cleanup();
        }
    }
}
