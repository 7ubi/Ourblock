package de.x7ubi.ourblock.engine.render;

import de.x7ubi.ourblock.game.chunk.ChunkGeneration;
import de.x7ubi.ourblock.game.controller.Controller;
import de.x7ubi.ourblock.game.texture.TextureLoader;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    public void initialize() {
        TextureLoader.getInstance().loadAllTextures();
    }

    public void render(double deltaTime) {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        Controller.getInstance().update(deltaTime);
        ChunkGeneration.getInstance().render();
    }

    public void cleanup() {
        TextureLoader.getInstance().cleanup();
    }
}
