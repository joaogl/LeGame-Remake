#version 330 compatibility
#define LIGHTS_COUNT 50

uniform sampler2D texture;
in vec2 texCoords;

uniform float dayLight;

uniform int lightAmount;

uniform vec2 lightPosition[LIGHTS_COUNT];
uniform vec3 lightColor[LIGHTS_COUNT];
uniform float lightIntensity[LIGHTS_COUNT];
uniform float lightType[LIGHTS_COUNT];
uniform float lightSize[LIGHTS_COUNT];
uniform float lightFacing[LIGHTS_COUNT];

void main() {		
	vec4 color = vec4(1.0, 1.0, 1.0, 1.0);
	vec4 tex = texture2D(texture, gl_TexCoord[0].st);
	color = tex;
		
	float alpha = 1.0;
	if (tex.x == 1.0 && tex.y == 0.0 && tex.z == 1.0) {
		alpha = 0.0;
	}
	
	vec4 result = vec4(color.xyz, alpha);
	color = result;	
	vec4 dcolor = vec4(result * vec4(dayLight, dayLight, dayLight, dayLight));	
	 
	int num = 0;
	int changed = 0;			
	
	for (int i = 0; i < lightAmount + 1; i++) {
		vec2 pos = lightPosition[i];
		if (pos.x == 0 && pos.y == 0) continue;
		vec3 col = lightColor[i];
		float ints = lightIntensity[i];
		
		float distance = length(pos - gl_FragCoord.xy);
		float attenuation = 1.0 / distance;
		
		float falloff = 50 - distance / 5.0f / ints;			
			  
		if (lightType[i] == 1) {
			color *= vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4((col / distance * 15) * ints, 1.0) + 0.01;
		
			color /= (distance / (ints * falloff));
			
			changed = 1;
			num++;
		} else if (lightType[i] == 2) {
			float angle = degrees(acos((pos.x - gl_FragCoord.x) / distance));		
			if (pos.y < gl_FragCoord.y) angle = 360 - angle;
			
			float max = (lightFacing[i] + (lightSize[i] / 2));
			float min = (lightFacing[i] - (lightSize[i] / 2));	
			bool spec = false;
			
			if (max > 360) {
				max = max - 360;
				spec = true;
			}
			if ((lightFacing[i] - (lightSize[i] / 2)) < 0) {
				min = 360 - abs(lightFacing[i] - (lightSize[i] / 2));
				spec = true;
			}
	
			if ((angle >= min && angle <= max && !spec) || ((angle >= min || angle <= max) && spec)) {
				color *= vec4(attenuation, attenuation, attenuation, pow(attenuation, 3)) * vec4((col / distance * 15) * ints, 1.0) + 0.01;
			
				color /= (distance / (ints * falloff));
				changed = 1;
				num++;
			}
		}
	}
	
	if (changed == 1) color *= 30.0 * pow(166.0, float(num - 1)) + 1;
	else color = dcolor;
		
	gl_FragColor = min(result, max(dcolor, color));
}