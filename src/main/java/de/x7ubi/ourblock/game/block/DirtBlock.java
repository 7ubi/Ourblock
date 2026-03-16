package de.x7ubi.ourblock.game.block;

import de.x7ubi.ourblock.game.texture.TextureLoader;

public class DirtBlock extends Block {
    public DirtBlock() {
        super(TextureLoader.getInstance().getTexture("dirt"));
    }
}
