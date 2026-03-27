package de.x7ubi.ourblock.game.block;

import de.x7ubi.ourblock.game.texture.TextureLoader;

public class GrassBlock extends Block {

    public GrassBlock() {
        super(TextureLoader.getInstance().getTextureUV("grass_top"),
                TextureLoader.getInstance().getTextureUV("grass_side"),
                TextureLoader.getInstance().getTextureUV("dirt"));
    }
}
