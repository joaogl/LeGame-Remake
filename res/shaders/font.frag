#version 110

uniform sampler2D texture;

uniform vec3 fontColor;

void main() {	
	gl_FragColor = vec4(fontColor, texture2D(texture, gl_TexCoord[0].st).a);	
}