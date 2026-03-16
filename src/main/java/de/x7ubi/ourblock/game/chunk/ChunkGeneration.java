package de.x7ubi.ourblock.game.chunk;

import lombok.Getter;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class ChunkGeneration {

    List<Chunk> chunks = new ArrayList<>();

    @Getter
    private final static ChunkGeneration instance = new ChunkGeneration();

    private ChunkGeneration() {
        initialize();
    }

    public void initialize() {
        chunks.add(new Chunk(new Vector3d(0, 0, 0)));
    }

    public void render() {
        for (Chunk chunk : chunks) {
            chunk.render();
        }
    }
}
