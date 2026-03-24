#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aUV;
layout (location = 2) in vec3 aNormal;

out vec2 vUV;
out vec3 vNormalWS;
out vec3 vPosWS;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;

void main() {
    vec4 worldPos = uModel * vec4(aPos, 1.0);
    vPosWS = worldPos.xyz;

    mat3 normalMat = mat3(transpose(inverse(uModel)));
    vNormalWS = normalize(normalMat * aNormal);

    vUV = aUV;

    gl_Position = uProj * uView * worldPos;
}