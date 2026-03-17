package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.x7ubi.ourblock.game.block.BlockDictionary;
import de.x7ubi.ourblock.game.block.Faces;
import lombok.Getter;
import org.joml.Vector3d;

import java.util.List;

import static org.lwjgl.opengl.GL11.glTranslated;

@Getter
public class Chunk {

    public static final int CHUNK_SIZE = 16;

    public static final int MAX_CHUNK_HEIGHT = 256;

    private static final int MAX_GENERATION_HEIGHT = 80;

    private static final int MIN_CHUNK_HEIGHT = 60;

    private static final double frequency = 0.05;

    private final byte[] blocks = new byte[CHUNK_SIZE * CHUNK_SIZE * MAX_CHUNK_HEIGHT];

    private final Vector3d position;

    private final NoiseGenerator noiseGenerator;

    public Chunk(Vector3d position, NoiseGenerator noiseGenerator) {
        this.position = position;
        this.noiseGenerator = noiseGenerator;

        generateBlocks();
    }

    private void generateBlocks() {

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                double noiseValue = noiseGenerator.evaluateNoise((position.x + x) * frequency, (position.z + z) * frequency);
                noiseValue = (noiseValue + 1) / 2;
                int dirtHeight = (int) (MIN_CHUNK_HEIGHT + noiseValue * (MAX_GENERATION_HEIGHT - MIN_CHUNK_HEIGHT));
                for (int y = 0; y < MAX_CHUNK_HEIGHT; y++) {
                    int index = getBlockIndex(x, y, z);

                    if (y < dirtHeight) {
                        blocks[index] = y < dirtHeight - 4 ? (byte) 2 : 1;

                    } else {
                        blocks[index] = 0;
                    }
                }
            }
        }
    }

    public void render() {
        glTranslated(position.x, position.y, position.z);

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < MAX_CHUNK_HEIGHT; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    byte block = blocks[getBlockIndex(x, y, z)];
                    if (block != 0) {
                        List<Faces> facesToDraw = getFacesToDraw(x, y, z);

                        BlockDictionary.getBlockById(block).render(new Vector3d(x, y, z), facesToDraw);
                    }
                }
            }
        }
    }

    private List<Faces> getFacesToDraw(int x, int y, int z) {
        List<Faces> facesToDraw = new java.util.ArrayList<>();

        if (y == 0 || blocks[getBlockIndex(x, y - 1, z)] == 0) {
            facesToDraw.add(Faces.BOTTOM);
        }
        if (y == MAX_CHUNK_HEIGHT - 1 || blocks[getBlockIndex(x, y + 1, z)] == 0) {
            facesToDraw.add(Faces.TOP);
        }
        if (x == 0 || blocks[getBlockIndex(x - 1, y, z)] == 0) {
            facesToDraw.add(Faces.LEFT);
        }
        if (x == CHUNK_SIZE - 1 || blocks[getBlockIndex(x + 1, y, z)] == 0) {
            facesToDraw.add(Faces.RIGHT);
        }
        if (z == CHUNK_SIZE - 1 || blocks[getBlockIndex(x, y, z + 1)] == 0) {
            facesToDraw.add(Faces.FRONT);
        }
        if (z == 0 || blocks[getBlockIndex(x, y, z - 1)] == 0) {
            facesToDraw.add(Faces.BACK);
        }

        return facesToDraw;
    }

    private int getBlockIndex(int x, int y, int z) {
        return (z * CHUNK_SIZE * MAX_CHUNK_HEIGHT) + (y * CHUNK_SIZE) + x;
    }
}
