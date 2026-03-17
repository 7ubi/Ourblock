package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.articdive.jnoise.generators.noisegen.perlin.PerlinNoiseGenerator;
import lombok.Getter;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class ChunkGeneration {

    @Getter
    private static final ChunkGeneration instance = new ChunkGeneration();

    private static final long SEED = 12345;

    private final NoiseGenerator noiseGenerator = PerlinNoiseGenerator.newBuilder().setSeed(SEED).setInterpolation(Interpolation.COSINE).build();

    List<Chunk> chunks = new ArrayList<>();

    private ChunkGeneration() {
        initialize();
    }

    public void initialize() {
        chunks.add(new Chunk(new Vector3d(0, 0, 0), noiseGenerator));
    }

    public void render() {
        for (Chunk chunk : chunks) {
            chunk.render();
        }
    }
}
