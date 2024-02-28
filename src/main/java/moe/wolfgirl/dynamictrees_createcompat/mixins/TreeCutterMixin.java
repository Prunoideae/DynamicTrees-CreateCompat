package moe.wolfgirl.dynamictrees_createcompat.mixins;

import com.simibubi.create.compat.Mods;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import com.simibubi.create.foundation.utility.TreeCutter;
import moe.wolfgirl.dynamictrees_createcompat.DynamicTree;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(TreeCutter.class)
public abstract class TreeCutterMixin {

	@Shadow(remap = false)
	public static boolean canDynamicTreeCutFrom(Block startBlock) {
		return false;
	}

	@Inject(method = "canDynamicTreeCutFrom", cancellable = true, remap = false, at = @At("HEAD"))
	private static void dtcc$canDynamicTreeCutFrom(Block startBlock, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(
				Mods.DYNAMICTREES.runIfInstalled(() -> () -> DynamicTree.isDynamicBranch(startBlock))
						.orElse(false)
		);
	}


	@Inject(method = "findDynamicTree", cancellable = true, remap = false, at = @At("HEAD"))
	private static void dtcc$findDynamicTree(Block startBlock, BlockPos pos, CallbackInfoReturnable<Optional<AbstractBlockBreakQueue>> cir) {
		if (canDynamicTreeCutFrom(startBlock)) {
			cir.setReturnValue(
					Mods.DYNAMICTREES.runIfInstalled(() -> () -> new DynamicTree(pos))
			);
		} else {
			cir.setReturnValue(Optional.empty());
		}
	}
}
