package de.x7ubi.ourblock.game.texture;

import org.joml.Vector2d;

public record TextureUVRecord(Vector2d offset) {

    public Vector2d getTopLeft() {
        return new Vector2d(offset.x, offset.y + TextureLoader.UV_SIZE);
    }

    public Vector2d getTopRight() {
        return new Vector2d(offset.x + TextureLoader.UV_SIZE, offset.y + TextureLoader.UV_SIZE);
    }

    public Vector2d getBottomLeft() {
        return new Vector2d(offset.x, offset.y);
    }

    public Vector2d getBottomRight() {
        return new Vector2d(offset.x + TextureLoader.UV_SIZE, offset.y);
    }
}
