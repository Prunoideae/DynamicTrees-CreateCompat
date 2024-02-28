package moe.wolfgirl.dynamictrees_createcompat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = DynamicTreesCreateCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


	private static final ForgeConfigSpec.DoubleValue DROP_MULTIPLIER = BUILDER
			.comment("Drop chance for the mechanical saw")
			.defineInRange("dropMultiplier", 1d, 0d, 1d);

	private static final ForgeConfigSpec.BooleanValue SIMULATE_AXE = BUILDER
			.comment("Let the tree fall like got chopped by player")
			.define("simulateFall", false);

	static final ForgeConfigSpec SPEC = BUILDER.build();

	public static double dropMultiplier;
	public static boolean simulateFall;

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent event) {
		dropMultiplier = DROP_MULTIPLIER.get();
		simulateFall = SIMULATE_AXE.get();
	}
}
