package software.bernie.techarium.client.screen.draw;

import net.minecraft.util.ResourceLocation;

import static software.bernie.techarium.Techarium.ModID;

public class GuiAddonTextures {

    public static void init() {

    }

    //Botarium Sheet
    private static final UiTexture BOTARIUM_COMPONENTS = new UiTexture(
            new ResourceLocation(ModID, "textures/gui/botarium/botarium_components.png"), 256, 256
    );

    //Botanium Gui Background
    public static final IDrawable BOTARIUM_DRAWABLE = new UiTexture(
            new ResourceLocation(ModID, "textures/gui/botarium/botarium.png"), 203, 184
    ).getFullArea();

    //Exchange Station Gui Background
    public static final IDrawable EXCHANGE_STATION_DRAWABLE = new UiTexture(
            new ResourceLocation(ModID, "textures/gui/exchangestation/exchangestation.png"), 176, 222
    ).getFullArea();



    //Defaults
    public static final IDrawable DEFAULT_FLUID_TANK = BOTARIUM_COMPONENTS.getArea(0, 50, 14, 50);
    public static final IDrawable DEFAULT_ENERGY_BAR = BOTARIUM_COMPONENTS.getArea(18, 53, 12, 48);
    public static final IDrawable DEFAULT_PROGRESS_BAR = BOTARIUM_COMPONENTS.getArea(18, 50, 28, 2);

    public static final IDrawable BOTARIUM_OUTPUT_SLOT = BOTARIUM_COMPONENTS.getArea(49, 0, 65, 46);

}
