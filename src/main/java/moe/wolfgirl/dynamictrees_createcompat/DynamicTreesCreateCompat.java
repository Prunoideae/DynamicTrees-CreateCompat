package moe.wolfgirl.dynamictrees_createcompat;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(DynamicTreesCreateCompat.MODID)
public class DynamicTreesCreateCompat {
	public static final String MODID = "dynamictrees_createcompat";
	public static final Logger LOGGER = LogUtils.getLogger();

	public DynamicTreesCreateCompat(ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}
}
