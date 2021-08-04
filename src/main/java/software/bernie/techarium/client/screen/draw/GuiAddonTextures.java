package software.bernie.techarium.client.screen.draw;

import lombok.experimental.UtilityClass;
import software.bernie.techarium.Techarium;
import software.bernie.techarium.util.Vector2i;

@UtilityClass
public class GuiAddonTextures {

    //Botarium Sheet
    private static final UiTexture BOTARIUM_COMPONENTS = new UiTexture(
            Techarium.rl( "textures/gui/botarium/botarium_components.png"), 256, 256
    );

    //Botanium Gui Background
    public static final IDrawable BOTARIUM_DRAWABLE = new UiTexture(
            Techarium.rl( "textures/gui/botarium/botarium.png"), 203, 184
    ).getFullArea();

    private static final UiTexture MACHINE_COMPONENTS = new UiTexture(
            Techarium.rl( "textures/gui/machine_components.png"), 256, 256
    );

    //Exchange Station Gui Background
    public static final IDrawable EXCHANGE_STATION_DRAWABLE = new UiTexture(
            Techarium.rl( "textures/gui/exchangestation/exchangestation.png"), 193, 230
    ).getFullArea();

    //Arboretum Sheet
    private static final UiTexture ARBORETUM_COMPONENTS = new UiTexture(
            Techarium.rl(  "textures/gui/arboretum/arboretum_components.png"), 256, 256
    );

    //Arboretum Gui Background
    public static final IDrawable ARBORETUM_DRAWABLE = new UiTexture(
            Techarium.rl( "textures/gui/arboretum/arboretum.png"), 203, 184
    ).getFullArea();


    //Defaults
    public static final IDrawable DEFAULT_FLUID_TANK = BOTARIUM_COMPONENTS.getArea(new Vector2i(0, 50), new Vector2i(14, 50));
    public static final IDrawable DEFAULT_ENERGY_BAR = BOTARIUM_COMPONENTS.getArea(new Vector2i(18, 53), new Vector2i( 12, 48));
    public static final IDrawable DEFAULT_PROGRESS_BAR = BOTARIUM_COMPONENTS.getArea(new Vector2i(18, 50), new Vector2i( 28, 2));

    public static final IDrawable BOTARIUM_OUTPUT_SLOT = MACHINE_COMPONENTS.getArea(new Vector2i(50, 1), new Vector2i(29, 81));
    public static final IDrawable ARBORETUM_OUTPUT_SLOT = MACHINE_COMPONENTS.getArea(new Vector2i(80, 1), new Vector2i( 29, 81));

}
