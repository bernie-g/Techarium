package software.bernie.techarium.client.render;

import lombok.Getter;

public class Color {
    @Getter
    float r,g,b,a;

    public Color(int color) {
        r = ((color >> 16) & 0xFF) / 255f;
        g = ((color >> 8) & 0xFF) / 255f;
        b = ((color) & 0xFF) / 255f;
        a = ((color >> 24) & 0xFF) / 255f;
    }
}
