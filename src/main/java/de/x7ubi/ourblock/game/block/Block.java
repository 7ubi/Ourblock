package de.x7ubi.ourblock.game.block;

import de.x7ubi.ourblock.game.texture.Texture;
import org.joml.Vector3d;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class Block {

    protected Texture texture;

    public Block(Texture texture) {
        this.texture = texture;
    }

    public void render(Vector3d position, List<Faces> facesToDraw) {

        glPushMatrix();

        glTranslated(position.x, position.y, position.z);

        texture.bind();

        renderFrontFace(facesToDraw);
        renderBackFace(facesToDraw);
        renderLeftFace(facesToDraw);
        renderRightFace(facesToDraw);
        renderTopFace(facesToDraw);
        renderBottomFace(facesToDraw);

        glPopMatrix();
    }

    private void renderFrontFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.FRONT)) {
            glBegin(GL_TRIANGLES);
            face(-0.5f, -0.5f, 0.5f, 0f, 0f, 0.5f, -0.5f, 0.5f, 1f, 0f, 0.5f, 0.5f, 0.5f, 1f, 1f, -0.5f, 0.5f, 0.5f, 0f, 1f);
            glEnd();
        }
    }

    private void renderBackFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.BACK)) {
            glBegin(GL_TRIANGLES);
            face(0.5f, -0.5f, -0.5f, 0f, 0f, -0.5f, -0.5f, -0.5f, 1f, 0f, -0.5f, 0.5f, -0.5f, 1f, 1f, 0.5f, 0.5f, -0.5f, 0f, 1f);
            glEnd();
        }
    }

    private void renderLeftFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.LEFT)) {
            glBegin(GL_TRIANGLES);
            face(-0.5f, -0.5f, -0.5f, 0f, 0f, -0.5f, -0.5f, 0.5f, 1f, 0f, -0.5f, 0.5f, 0.5f, 1f, 1f, -0.5f, 0.5f, -0.5f, 0f, 1f);
            glEnd();
        }
    }

    private void renderRightFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.RIGHT)) {
            glBegin(GL_TRIANGLES);
            face(0.5f, -0.5f, 0.5f, 0f, 0f, 0.5f, -0.5f, -0.5f, 1f, 0f, 0.5f, 0.5f, -0.5f, 1f, 1f, 0.5f, 0.5f, 0.5f, 0f, 1f);
            glEnd();
        }
    }

    private void renderTopFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.TOP)) {
            glBegin(GL_TRIANGLES);
            face(-0.5f, 0.5f, 0.5f, 0f, 0f, 0.5f, 0.5f, 0.5f, 1f, 0f, 0.5f, 0.5f, -0.5f, 1f, 1f, -0.5f, 0.5f, -0.5f, 0f, 1f);
            glEnd();
        }
    }

    private void renderBottomFace(List<Faces> facesToDraw) {
        if (facesToDraw.contains(Faces.BOTTOM)) {
            glBegin(GL_TRIANGLES);
            face(-0.5f, -0.5f, -0.5f, 0f, 0f, 0.5f, -0.5f, -0.5f, 1f, 0f, 0.5f, -0.5f, 0.5f, 1f, 1f, -0.5f, -0.5f, 0.5f, 0f, 1f);
            glEnd();
        }
    }

    private void face(
            float x1, float y1, float z1, float u1, float v1,
            float x2, float y2, float z2, float u2, float v2,
            float x3, float y3, float z3, float u3, float v3,
            float x4, float y4, float z4, float u4, float v4
    ) {
        vertex(x1, y1, z1, u1, v1);
        vertex(x2, y2, z2, u2, v2);
        vertex(x3, y3, z3, u3, v3);

        vertex(x1, y1, z1, u1, v1);
        vertex(x3, y3, z3, u3, v3);
        vertex(x4, y4, z4, u4, v4);
    }

    private void vertex(float x, float y, float z, float u, float v) {
        glTexCoord2f(u, v);
        glVertex3f(x, y, z);
    }
}
