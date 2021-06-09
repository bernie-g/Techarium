package software.bernie.techarium.display.screen.widget.awt;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    public int x,y;

    public Point add (Point p) {
        return new Point(p.x + x, p.y + y);
    }
}
