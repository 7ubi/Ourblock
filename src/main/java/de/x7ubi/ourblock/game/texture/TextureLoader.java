package de.x7ubi.ourblock.game.texture;

import lombok.Getter;
import org.joml.Vector2d;

import java.util.HashMap;

public class TextureLoader {

    @Getter
    private static final TextureLoader instance = new TextureLoader();

    public static final double UV_SIZE = 0.5;

    @Getter
    private Texture textureAtlas;

    private final HashMap<String, TextureUVRecord> textureUVRecords = new HashMap<>();

    private TextureLoader() {
    }

    public void loadTextureAtlas() {
        textureAtlas = new Texture("textures/textureatlas.png");

        textureUVRecords.put("dirt", new TextureUVRecord(new Vector2d(0.0, 0.5)));
        textureUVRecords.put("cobble", new TextureUVRecord(new Vector2d(0.5, 0.5)));
        textureUVRecords.put("grass_side", new TextureUVRecord(new Vector2d(0.5, 0.0)));
        textureUVRecords.put("grass_top", new TextureUVRecord(new Vector2d(0.0, 0.0)));
    }

    public TextureUVRecord getTextureUV(String name) {
        return textureUVRecords.get(name);
    }

    public void cleanup() {
        textureAtlas.cleanup();
    }
}
