package software.bernie.techarium.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class Vector2i {

    public static final Vector2i ZERO = new Vector2i();
    int x = 0;
    int y = 0;


    public Vector2i add (Vector2i p) {
        return new Vector2i(p.x + x, p.y + y);
    }
}
