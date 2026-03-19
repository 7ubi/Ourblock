package de.x7ubi.ourblock.game.chunk;

import de.articdive.jnoise.core.api.noisegen.NoiseGenerator;
import de.x7ubi.ourblock.game.block.BlockDictionary;
import de.x7ubi.ourblock.game.block.Faces;
import lombok.Getter;
import org.joml.Vector3d;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@Getter
public class Chunk {

    public static final int CHUNK_SIZE = 16;

    public static final int MAX_CHUNK_HEIGHT = 256;

    private static final int MAX_GENERATION_HEIGHT = 80;

    private static final int MIN_CHUNK_HEIGHT = 60;

    private static final double frequency = 0.05;

    @Getter
    private final byte[] blocks = new byte[CHUNK_SIZE * CHUNK_SIZE * MAX_CHUNK_HEIGHT];

    @Getter
    private final byte[] visibleFacesCache = new byte[CHUNK_SIZE * CHUNK_SIZE * MAX_CHUNK_HEIGHT];

    private boolean needsFaceCacheUpdate = true;

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

    public void render(Chunk neighborXPos, Chunk neighborXNeg, Chunk neighborZPos, Chunk neighborZNeg) {

        glPushMatrix();
        glTranslated(position.x, 0, position.z);

        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < MAX_CHUNK_HEIGHT; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    byte block = blocks[getBlockIndex(x, y, z)];
                    if (block != 0) {
                        List<Faces> facesToDraw = getFacesToDraw(x, y, z, neighborXPos, neighborXNeg, neighborZPos, neighborZNeg);

                        if (needsFaceCacheUpdate) {
                            byte visibleFaces = 0;
                            for (Faces face : facesToDraw) {
                                visibleFaces |= (byte) (1 << face.ordinal());
                            }
                            visibleFacesCache[getBlockIndex(x, y, z)] = visibleFaces;
                        }

                        if (!facesToDraw.isEmpty()) {
                            BlockDictionary.getBlockById(block).render(new Vector3d(x, y, z), facesToDraw);
                        }
                    }
                }
            }
        }
        if (needsFaceCacheUpdate) {
            needsFaceCacheUpdate = false;
        }

        glPopMatrix();
    }

    private List<Faces> getFacesToDraw(int x, int y, int z, Chunk neighborXPos, Chunk neighborXNeg, Chunk neighborZPos, Chunk neighborZNeg) {
        List<Faces> facesToDraw = new java.util.ArrayList<>();

        if (!needsFaceCacheUpdate) {
            byte cachedValue = visibleFacesCache[getBlockIndex(x, y, z)];
            for (Faces face : Faces.values()) {
                if ((cachedValue & (1 << face.ordinal())) != 0) {
                    facesToDraw.add(face);
                }
            }
            return facesToDraw;
        }

        if (y == 0 || blocks[getBlockIndex(x, y - 1, z)] == 0) {
            facesToDraw.add(Faces.BOTTOM);
        }
        if (y == MAX_CHUNK_HEIGHT - 1 || blocks[getBlockIndex(x, y + 1, z)] == 0) {
            facesToDraw.add(Faces.TOP);
        }

        if (x == 0) {
            if (neighborXNeg == null || neighborXNeg.blocks[getBlockIndex(CHUNK_SIZE - 1, y, z)] == 0) {
                facesToDraw.add(Faces.LEFT);
            }
        } else if (blocks[getBlockIndex(x - 1, y, z)] == 0) {
            facesToDraw.add(Faces.LEFT);
        }

        if (x == CHUNK_SIZE - 1) {
            if (neighborXPos == null || neighborXPos.blocks[getBlockIndex(0, y, z)] == 0) {
                facesToDraw.add(Faces.RIGHT);
            }
        } else if (blocks[getBlockIndex(x + 1, y, z)] == 0) {
            facesToDraw.add(Faces.RIGHT);
        }

        if (z == CHUNK_SIZE - 1) {
            if (neighborZPos == null || neighborZPos.blocks[getBlockIndex(x, y, 0)] == 0) {
                facesToDraw.add(Faces.FRONT);
            }
        } else if (blocks[getBlockIndex(x, y, z + 1)] == 0) {
            facesToDraw.add(Faces.FRONT);
        }

        if (z == 0) {
            if (neighborZNeg == null || neighborZNeg.blocks[getBlockIndex(x, y, CHUNK_SIZE - 1)] == 0) {
                facesToDraw.add(Faces.BACK);
            }
        } else if (blocks[getBlockIndex(x, y, z - 1)] == 0) {
            facesToDraw.add(Faces.BACK);
        }

        return facesToDraw;
    }

    private int getBlockIndex(int x, int y, int z) {
        return (z * CHUNK_SIZE * MAX_CHUNK_HEIGHT) + (y * CHUNK_SIZE) + x;
    }
}
