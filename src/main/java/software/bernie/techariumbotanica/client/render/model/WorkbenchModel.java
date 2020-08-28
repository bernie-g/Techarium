// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.12.2 or 1.15.2 (same format for both) for entity models animated with GeckoLib
// Paste this class into your mod and follow the documentation for GeckoLib to use animations. You can find the documentation here: https://github.com/bernie-g/geckolib
// Blockbench plugin created by Gecko
package software.bernie.techariumbotanica.client.render.model;

import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib.animation.model.SpecialAnimatedModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.techariumbotanica.TechariumBotanica;
import software.bernie.techariumbotanica.tile.TileWorkbench;

public class WorkbenchModel extends SpecialAnimatedModel<TileWorkbench>
{

	private final AnimatedModelRenderer machine;
	private final AnimatedModelRenderer left_part;
	private final AnimatedModelRenderer left_holo;
	private final AnimatedModelRenderer holotext2;
	private final AnimatedModelRenderer right_part;
	private final AnimatedModelRenderer right_holo;
	private final AnimatedModelRenderer holotext;
	private final AnimatedModelRenderer middlepart;
	private final AnimatedModelRenderer support_globe;
	private final AnimatedModelRenderer hologlobe;
	private final AnimatedModelRenderer globe;
	private final AnimatedModelRenderer box;
	private final AnimatedModelRenderer frontface;
	private final AnimatedModelRenderer leftface;
	private final AnimatedModelRenderer backface;
	private final AnimatedModelRenderer rightface;
	private final AnimatedModelRenderer topface;

	public WorkbenchModel()
	{
		textureWidth = 128;
		textureHeight = 128;
		machine = new AnimatedModelRenderer(this);
		machine.setRotationPoint(0.0F, 23.75F, 0.0F);

		machine.setModelRendererName("machine");
		this.registerModelRenderer(machine);

		left_part = new AnimatedModelRenderer(this);
		left_part.setRotationPoint(6.0F, 0.0F, 0.0F);
		machine.addChild(left_part);
		left_part.setTextureOffset(64, 33).addBox(5.0F, -3.75F, -7.0F, 4.0F, 4.0F, 14.0F, -0.001F, false);
		left_part.setTextureOffset(38, 4).addBox(0.0F, -12.75F, -6.0F, 8.0F, 13.0F, 12.0F, -0.001F, false);
		left_part.setTextureOffset(32, 35).addBox(0.0F, -15.75F, -8.0F, 8.0F, 3.0F, 16.0F, -0.002F, false);
		left_part.setModelRendererName("left_part");
		this.registerModelRenderer(left_part);

		left_holo = new AnimatedModelRenderer(this);
		left_holo.setRotationPoint(4.0F, -16.0F, 4.0F);
		left_part.addChild(left_holo);
		left_holo.setTextureOffset(0, 5).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 2.0F, -0.001F, false);
		left_holo.setModelRendererName("left_holo");
		this.registerModelRenderer(left_holo);

		holotext2 = new AnimatedModelRenderer(this);
		holotext2.setRotationPoint(0.0F, 0.5F, -1.0F);
		left_holo.addChild(holotext2);
		holotext2.setTextureOffset(23, 32).addBox(-3.0F, 0.0F, -8.0F, 6.0F, 0.0F, 9.0F, -0.001F, false);
		holotext2.setModelRendererName("holotext2");
		this.registerModelRenderer(holotext2);

		right_part = new AnimatedModelRenderer(this);
		right_part.setRotationPoint(-6.0F, 0.25F, 0.0F);
		machine.addChild(right_part);
		right_part.setTextureOffset(0, 32).addBox(-8.0F, -16.0F, -8.0F, 8.0F, 3.0F, 16.0F, -0.001F, false);
		right_part.setTextureOffset(0, 51).addBox(-8.0F, -13.0F, -6.0F, 8.0F, 13.0F, 12.0F, -0.0001F, false);
		right_part.setTextureOffset(72, 51).addBox(-9.0F, -4.0F, -7.0F, 4.0F, 4.0F, 14.0F, -0.001F, false);
		right_part.setModelRendererName("right_part");
		this.registerModelRenderer(right_part);

		right_holo = new AnimatedModelRenderer(this);
		right_holo.setRotationPoint(-4.0F, -16.25F, 4.0F);
		right_part.addChild(right_holo);
		right_holo.setTextureOffset(0, 8).addBox(-3.0F, 0.0F, -2.0F, 6.0F, 1.0F, 2.0F, -0.001F, false);
		right_holo.setModelRendererName("right_holo");
		this.registerModelRenderer(right_holo);

		holotext = new AnimatedModelRenderer(this);
		holotext.setRotationPoint(0.0F, 0.5F, -1.0F);
		right_holo.addChild(holotext);
		holotext.setTextureOffset(19, 54).addBox(-3.0F, 0.0F, -8.0F, 6.0F, 0.0F, 9.0F, -0.001F, false);
		holotext.setModelRendererName("holotext");
		this.registerModelRenderer(holotext);

		middlepart = new AnimatedModelRenderer(this);
		middlepart.setRotationPoint(0.0F, 0.25F, 0.0F);
		machine.addChild(middlepart);
		middlepart.setTextureOffset(72, 72).addBox(-6.0F, -13.0F, -4.0F, 12.0F, 13.0F, 8.0F, -0.001F, false);
		middlepart.setTextureOffset(0, 16).addBox(-6.0F, -15.0F, -7.0F, 12.0F, 2.0F, 14.0F, -0.001F, false);
		middlepart.setTextureOffset(64, 15).addBox(-4.0F, -4.0F, -7.0F, 8.0F, 4.0F, 14.0F, -0.001F, false);
		middlepart.setModelRendererName("middlepart");
		this.registerModelRenderer(middlepart);

		support_globe = new AnimatedModelRenderer(this);
		support_globe.setRotationPoint(0.0F, -15.0F, 0.0F);
		middlepart.addChild(support_globe);
		support_globe.setTextureOffset(84, 6).addBox(-3.0F, -1.0F, -3.0F, 6.0F, 1.0F, 6.0F, -0.001F, false);
		support_globe.setModelRendererName("support_globe");
		this.registerModelRenderer(support_globe);

		hologlobe = new AnimatedModelRenderer(this);
		hologlobe.setRotationPoint(0.0F, -1.0F, 0.0F);
		support_globe.addChild(hologlobe);
		hologlobe.setTextureOffset(0, 0).addBox(-2.0F, -1.0F, -2.0F, 4.0F, 1.0F, 4.0F, -0.001F, false);
		hologlobe.setModelRendererName("hologlobe");
		this.registerModelRenderer(hologlobe);

		globe = new AnimatedModelRenderer(this);
		globe.setRotationPoint(0.0F, -6.0F, 0.0F);
		hologlobe.addChild(globe);
		globe.setTextureOffset(66, 0).addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, -0.001F, false);
		globe.setModelRendererName("globe");
		this.registerModelRenderer(globe);

		box = new AnimatedModelRenderer(this);
		box.setRotationPoint(0.0F, 24.0F, 0.0F);

		box.setModelRendererName("box");
		this.registerModelRenderer(box);

		frontface = new AnimatedModelRenderer(this);
		frontface.setRotationPoint(0.0F, 0.0F, 8.0F);
		box.addChild(frontface);
		frontface.setTextureOffset(32, 88).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
		frontface.setModelRendererName("frontface");
		this.registerModelRenderer(frontface);

		leftface = new AnimatedModelRenderer(this);
		leftface.setRotationPoint(8.0F, 0.0F, 0.0F);
		box.addChild(leftface);
		leftface.setTextureOffset(40, 40).addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);
		leftface.setModelRendererName("leftface");
		this.registerModelRenderer(leftface);

		backface = new AnimatedModelRenderer(this);
		backface.setRotationPoint(0.0F, 0.0F, -8.0F);
		box.addChild(backface);
		backface.setTextureOffset(32, 88).addBox(-8.0F, -16.0F, 0.0F, 16.0F, 16.0F, 0.0F, 0.0F, false);
		backface.setModelRendererName("backface");
		this.registerModelRenderer(backface);

		rightface = new AnimatedModelRenderer(this);
		rightface.setRotationPoint(-8.0F, 0.0F, 0.0F);
		box.addChild(rightface);
		rightface.setTextureOffset(40, 40).addBox(0.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, 0.0F, false);
		rightface.setModelRendererName("rightface");
		this.registerModelRenderer(rightface);

		topface = new AnimatedModelRenderer(this);
		topface.setRotationPoint(0.0F, -16.0F, 0.0F);
		rightface.addChild(topface);
		topface.setTextureOffset(0, 0).addBox(0.0F, 0.0F, -8.0F, 16.0F, 0.0F, 16.0F, 0.0F, false);
		topface.setModelRendererName("topface");
		this.registerModelRenderer(topface);

		this.rootBones.add(machine);
		this.rootBones.add(box);
	}


	@Override
	public ResourceLocation getAnimationFileLocation()
	{
		return new ResourceLocation(TechariumBotanica.ModID, "animations/workbench_anim.json");
	}
}