package moe.wolfgirl.dynamictrees_createcompat;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.block.branch.BranchBlock;
import com.ferreusveritas.dynamictrees.block.branch.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.util.BranchDestructionData;
import com.simibubi.create.foundation.utility.AbstractBlockBreakQueue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Modified from {@link com.simibubi.create.compat.dynamictrees.DynamicTree}
 *
 * @author Prunoideae
 */
public class DynamicTree extends AbstractBlockBreakQueue {


	private BlockPos startCutPos;

	public DynamicTree(BlockPos startCutPos) {
		this.startCutPos = startCutPos;
	}

	public static boolean isDynamicBranch(Block block) {
		return TreeHelper.isBranch(block) || block instanceof TrunkShellBlock;
	}

	@Override
	public void destroyBlocks(Level level, ItemStack toDamage, @Nullable Player playerEntity, BiConsumer<BlockPos, ItemStack> drop) {
		BranchBlock start = TreeHelper.getBranch(level.getBlockState(startCutPos));
		if (start == null) //if start is null, it was not a branch
			start = setBranchToShellMuse(level, level.getBlockState(startCutPos)); //we check for a trunk shell instead
		if (start == null) //if it is null again, it was neither a branch nor a trunk shell and thus we return
			return;

		// Play and render block break sound and particles
		level.levelEvent(null, 2001, startCutPos, Block.getId(level.getBlockState(startCutPos)));
		// Actually breaks the tree
		BranchDestructionData data = start.destroyBranchFromNode(level, startCutPos, Direction.DOWN, false, playerEntity);

		// Feed all the tree drops to drop bi-consumer
		data.leavesDrops.forEach(stackPos -> drop.accept(stackPos.pos.offset(startCutPos), stackPos.stack));
		List<ItemStack> woodItems = data.species.getBranchesDrops(level, data.woodVolume, ItemStack.EMPTY);
		woodItems.forEach(itemStack -> drop.accept(startCutPos, itemStack));
	}

	private BranchBlock setBranchToShellMuse(Level world, BlockState state) {
		Block block = state.getBlock();
		if (block instanceof TrunkShellBlock) {
			TrunkShellBlock.ShellMuse muse = ((TrunkShellBlock) block).getMuse(world, startCutPos);
			if (muse != null) {
				startCutPos = muse.pos; //the cut pos is moved to the center of the trunk
				return TreeHelper.getBranch(muse.state);
			}
		}
		return null;
	}
}
