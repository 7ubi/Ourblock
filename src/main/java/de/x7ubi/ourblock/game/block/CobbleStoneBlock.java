package de.x7ubi.ourblock.game.block;

import de.x7ubi.ourblock.game.texture.TextureLoader;

public class CobbleStoneBlock extends Block {
    public CobbleStoneBlock() {
        super(TextureLoader.getInstance().getTextureUV("cobble"));
    }
}
