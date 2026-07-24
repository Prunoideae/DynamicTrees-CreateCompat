package moe.wolfgirl.dynamictrees_createcompat;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = DynamicTreesCreateCompat.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();


	private static final ModConfigSpec.DoubleValue DROP_MULTIPLIER = BUILDER
			.comment("Drop chance for the mechanical saw")
			.defineInRange("dropMultiplier", 1d, 0d, 1d);

	private static final ModConfigSpec.BooleanValue SIMULATE_AXE = BUILDER
			.comment("Let the tree fall like got chopped by player")
			.define("simulateFall", false);

	static final ModConfigSpec SPEC = BUILDER.build();

	public static double dropMultiplier;
	public static boolean simulateFall;

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent event) {
		dropMultiplier = DROP_MULTIPLIER.get();
		simulateFall = SIMULATE_AXE.get();
	}
}
