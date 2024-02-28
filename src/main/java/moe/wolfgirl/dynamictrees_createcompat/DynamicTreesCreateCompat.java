package moe.wolfgirl.dynamictrees_createcompat;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(DynamicTreesCreateCompat.MODID)
public class DynamicTreesCreateCompat {
	public static final String MODID = "dynamictrees_createcompat";
	public static final Logger LOGGER = LogUtils.getLogger();

	public DynamicTreesCreateCompat() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}
}
