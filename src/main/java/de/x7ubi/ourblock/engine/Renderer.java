package de.x7ubi.ourblock.engine;

import de.x7ubi.ourblock.game.chunk.ChunkGeneration;
import de.x7ubi.ourblock.game.controller.Controller;
import de.x7ubi.ourblock.game.texture.TextureLoader;

public class Renderer {

    public void initialize() {
        TextureLoader.getInstance().loadTextureAtlas();
    }

    public void render(double deltaTime) {
        Controller.getInstance().update(deltaTime);
        ChunkGeneration.getInstance().render();
    }

    public void cleanup() {
        TextureLoader.getInstance().cleanup();
        ChunkGeneration.getInstance().cleanup();
    }
}
