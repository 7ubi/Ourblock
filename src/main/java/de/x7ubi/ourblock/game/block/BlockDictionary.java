package de.x7ubi.ourblock.game.block;

import java.util.HashMap;

public class BlockDictionary {

    public static final HashMap<Byte, Block> blockMap = new HashMap<>();

    static {
        blockMap.put((byte) 1, new DirtBlock());
        blockMap.put((byte) 2, new CobbleStoneBlock());
        blockMap.put((byte) 3, new GrassBlock());
    }

    public static Block getBlockById(byte id) {
        return blockMap.get(id);
    }
}
