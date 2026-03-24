#version 330 core

in vec2 vUV;
in vec3 vNormalWS;

out vec4 FragColor;

uniform sampler2D uAtlas;
uniform vec3 uLightDirWS;

void main() {
    vec3 albedo = texture(uAtlas, vUV).rgb;

    float ndl = max(dot(normalize(vNormalWS), normalize(-uLightDirWS)), 0.45);
    FragColor = vec4(albedo * ndl, 1.0);
}