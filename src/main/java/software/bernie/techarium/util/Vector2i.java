package software.bernie.techarium.util;

import lombok.*;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
@Data
public class Vector2i {

    public static final Vector2i ZERO = new Vector2i();
    int x = 0;
    int y = 0;


    public Vector2i add(int x, int y) {
        return new Vector2i(this.x + x, this.y + y);
    }

    public Vector2i add(Vector2i p) {
        return add(p.x, p.y);
    }

    public Vector2i copy() {
        return new Vector2i(x,y);
    }
}
