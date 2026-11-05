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
    vec4 color = texture(InSampler, texCoord);
    fragColor = vec4(color.rgb * 0.25, color.a);
}
