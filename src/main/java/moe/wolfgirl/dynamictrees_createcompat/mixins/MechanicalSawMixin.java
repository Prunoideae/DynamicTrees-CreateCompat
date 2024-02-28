package moe.wolfgirl.dynamictrees_createcompat.mixins;

import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.TrunkShellBlock;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.content.kinetics.saw.SawBlockEntity;
import com.simibubi.create.foundation.utility.TreeCutter;
import moe.wolfgirl.dynamictrees_createcompat.Config;
import moe.wolfgirl.dynamictrees_createcompat.DynamicTreesCreateCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Instead of restoring compat, I also want to add a behavior that makes
 * the tree fall exactly like a player chopped it down.
 */
@Mixin(SawBlockEntity.class)
public abstract class MechanicalSawMixin extends BlockBreakingKineticBlockEntity {

	public MechanicalSawMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}


	@Inject(method = "onBlockBroken", cancellable = true, remap = false, at = @At("HEAD"))
	void onBlockBroken(BlockState stateToBreak, CallbackInfo ci) {
		if (Config.simulateFall &&
				TreeCutter.findDynamicTree(stateToBreak.getBlock(), breakingPos).isPresent()
		) {
			if (level instanceof ServerLevel serverLevel) {
				DeployerFakePlayer fakePlayer = new DeployerFakePlayer(serverLevel, null);
				fakePlayer.setPos(worldPosition.getCenter().add(0, -0.5, 0));
				float rotation = switch (getBlockState().getValue(DirectionalKineticBlock.FACING)) {
					case DOWN, SOUTH, UP -> 0.0F;
					case WEST -> 90.0F;
					case NORTH -> 180.0F;
					case EAST -> 270.0F;
				};
				DynamicTreesCreateCompat.LOGGER.info(String.valueOf(rotation));
				fakePlayer.setYHeadRot(rotation);
				if (stateToBreak.getBlock() instanceof BranchBlock branchBlock) {
					branchBlock.removedByEntity(stateToBreak, serverLevel, breakingPos, fakePlayer);
				} else if (stateToBreak.getBlock() instanceof TrunkShellBlock trunkShellBlock) {
					trunkShellBlock.onDestroyedByPlayer(stateToBreak, level, breakingPos, fakePlayer, true, level.getFluidState(breakingPos));
				}
				ci.cancel();
			}
		}
	}
}
