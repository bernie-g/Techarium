package software.bernie.techarium.client.screen.draw;

import net.minecraft.util.ResourceLocation;

import static software.bernie.techarium.TechariumBotanica.ModID;

public class GuiAddonTextures {

    public static void init(){

    }

    //Botarium Sheet
    private static final UiTexture BOTARIUM_COMPONENTS = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_components.png"),256,256);

    //Botanium Gui Background
    public static final IDrawable BOTARIUM_BASE_TIER_1 = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_tier1_main.png"),203,184).getFullArea();
    public static final IDrawable BOTARIUM_BASE_TIER_2 = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_tier2_main.png"),203,184).getFullArea();
    public static final IDrawable BOTARIUM_BASE_TIER_3 = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_tier3_main.png"),203,184).getFullArea();
    public static final IDrawable BOTARIUM_BASE_TIER_4 = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_tier4_main.png"),203,184).getFullArea();
    public static final IDrawable BOTARIUM_BASE_TIER_5 = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_tier5_main.png"),203,184).getFullArea();
    public static final IDrawable BOTARIUM_BASE_TIER_6 = new UiTexture(new ResourceLocation(ModID,"textures/gui/botarium/botarium_tier6_main.png"),203,184).getFullArea();


    //Defaults
    public static final IDrawable DEFAULT_FLUID_TANK = BOTARIUM_COMPONENTS.getArea(0,50,14,50);
    public static final IDrawable DEFAULT_ENERGY_BAR = BOTARIUM_COMPONENTS.getArea(18,53,12,48);

}
