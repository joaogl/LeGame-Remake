#version 110

uniform sampler2D texture;
in vec2 texCoords;

uniform float dayLight;

void main() {	
	vec4 tex = texture2D(texture, gl_TexCoord[0].st);
	vec4 color = tex;
	
	float alpha = 1.0;
	if (tex.x == 1.0 && tex.y == 0.0 && tex.z == 1.0) {
		alpha = 0.0;
	}
	
	vec4 finalColor = vec4(color);	
	gl_FragColor = vec4(finalColor.xyz, alpha);
	
}