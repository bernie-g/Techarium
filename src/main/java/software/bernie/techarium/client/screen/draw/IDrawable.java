package software.bernie.techariumbotanica.client.screen.draw;

public interface IDrawable {

    default void draw(double x, double y, double width, double height) {
        drawPartial(x, y, width, height, 0, 0, 1, 1);
    }

    void drawPartial(double x, double y, double width, double height, float x1, float y1, float x2, float y2);

}