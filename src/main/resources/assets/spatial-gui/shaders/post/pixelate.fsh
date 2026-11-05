//? if >=26.3 {
//#version 450
//?} else {
#version 330
//?}

uniform sampler2D InSampler;

//? if >=26.3 {
//layout(location = 0) in vec2 texCoord;
//layout(location = 0) out vec4 fragColor;
//?} else {
in vec2 texCoord;
out vec4 fragColor;
//?}

void main() {
    const float PIXEL_SIZE = 20.0;

    vec2 size = vec2(textureSize(InSampler, 0));
    vec2 pixelatedUV = (floor(texCoord * size / PIXEL_SIZE) + 0.5) * PIXEL_SIZE / size;

    fragColor = texture(InSampler, pixelatedUV);
}