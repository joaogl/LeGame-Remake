#version 110

uniform sampler2D texture;

void main() {	
	vec4 tex = texture2D(texture, gl_TexCoord[0].st);
	
	float alpha = 1.0;
	if (tex.x == 1.0 && tex.y == 0.0 && tex.z == 1.0) {
		alpha = 0.0;
	}
	
	gl_FragColor = vec4(tex.xyz, alpha);	
}