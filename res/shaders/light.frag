uniform float lightInt;
uniform vec2 lightLocation;
uniform vec3 lightColor;
uniform float lightType;
uniform float lightCenter;
uniform float lightSize;
uniform float lightFacing;

void main() {
	float distance = length(lightLocation - gl_FragCoord.xy);
	float attenuation = lightInt / distance;
	vec4 color = vec4(0, 0, 0, 0);
	
	if (lightType == 1) {	
		if (lightCenter == 0 && distance < 30) distance = 30;	
		attenuation = lightInt / distance;
		color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3) * 10) * vec4(lightColor * lightInt, 1);		
	} else if (lightType == 2) {	
		float angle = degrees(acos((lightLocation.x - gl_FragCoord.x) / distance));		
		if (lightLocation.y < gl_FragCoord.y) angle = 360 - angle;
		
		float max = (lightFacing + (lightSize / 2));
		float min = (lightFacing - (lightSize / 2));	
		bool spec = false;
		
		if (max > 360) {
			max = max - 360;
			spec = true;
		}
		if ((lightFacing - (lightSize / 2)) < 0) {
			min = 360 - abs(lightFacing - (lightSize / 2));
			spec = true;
		}

		if (lightCenter == 0 && distance < 30) distance = 30;	
		attenuation = lightInt / distance;
		if ((angle >= min && angle <= max && !spec) || ((angle >= min || angle <= max) && spec)) color = vec4(attenuation, attenuation, attenuation, pow(attenuation, 3) * 10) * vec4(lightColor * lightInt, 1);			
	} 
	gl_FragColor = color;
}