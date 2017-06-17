package szewek.fl.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import szewek.fl.FL;

import java.util.Arrays;

import static net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE;

/**
 * Built shaped recipe generated by {@link RecipeBuilder}
 */
public class BuiltShapedRecipe implements IRecipe {
	public final byte width, height;
	private final R9[] shape;
	private final RecipeItem[] items;
	private final String[] oreDicts;
	private final byte mirror;
	private final ItemStack result;

	BuiltShapedRecipe(RecipeBuilder rb) {
		width = rb.width;
		height = rb.height;
		shape = rb.shape.clone();
		items = rb.items.clone();
		oreDicts = rb.oreDicts.clone();
		mirror = rb.mirror;
		result = new ItemStack(rb.result, rb.size, rb.meta, rb.tags.copy());
	}

	public Object[] getCached() {
		final Object[] o = new Object[width * height];
		final ItemStack[] stacks = new ItemStack[items.length];
		int i;
		for (i = 0; i < items.length; i++) {
			stacks[i] = items[i] == null ? null : items[i].makeItemStack();
		}
		for (i = 0; i < shape.length; i++) {
			if (shape[i] == null) {
				o[i] = null;
				continue;
			}
			final byte id = shape[i].ord;
			Object r = null;
			String od = oreDicts[id];
			ItemStack is = stacks[id];
			if (is != null && od != null) {
				r = Arrays.asList(is, od);
			} else if (is != null) {
				r = is;
			} else if (od != null) {
				r = od;
			}
			o[i] = r;
		}
		return o;
	}

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		for (byte x = 0; x < 4 - width; x++)
			for (byte y = 0; y < 4 - height; y++) {
				if (offsetMatch(inv, x, y, (byte) 0))
					return true;
				if (mirror == 0)
					continue;
				if ((mirror & 2) != 0 && offsetMatch(inv, x, y, (byte) 2))
					return true;
				if ((mirror & 1) != 0 && offsetMatch(inv, x, y, (byte) 1))
					return true;
				if ((mirror & 3) != 0 && offsetMatch(inv, x, y, (byte) 3))
					return true;
			}
		return false;
	}

	private boolean offsetMatch(InventoryCrafting inv, byte ox, byte oy, byte m) {
		byte x, y = 0, mx;
		LOOP_XY:
		for (x = 0; x < width; x++) {
			mx = (m & 2) != 0 ? (byte) (width - 1 - x) : x;
			for (y = 0; y < height; y++) {
				final ItemStack slot = inv.getStackInRowAndColumn(x + ox, y + oy);
				final boolean eSlot = FL.isItemEmpty(slot);
				byte id = (byte) (((m & 1) != 0 ? height - 1 - y : y) * height + mx);
				if (shape[id] == null) {
					if (eSlot)
						continue;
					else
						break LOOP_XY;
				}
				id = shape[id].ord;
				final boolean eStack = items[id] == null;
				final NonNullList<ItemStack> odi = oreDicts[id] != null ? OreDictionary.getOres(oreDicts[id]) : null;
				final boolean eOredict = odi == null || odi.size() == 0;
				if ((eStack && eOredict) == eSlot) {
					if (!eSlot) {
						if ((eStack || !items[id].matchesStack(slot, false)) && (eOredict || !allMatch(slot, odi, false)))
							break LOOP_XY;
					}
				} else
					break LOOP_XY;
			}
		}
		return x == width && y == height;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		return result.copy();
	}

	@Override
	public int getRecipeSize() {
		return width * height;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}

	private static boolean allMatch(ItemStack target, NonNullList<ItemStack> inputs, boolean strict) {
		final boolean empty = target.isEmpty();
		Item it = target.getItem();
		int m = target.getItemDamage();
		for (ItemStack is : inputs)
			if (empty == is.isEmpty() && it == is.getItem() && ((is.getItemDamage() == WILDCARD_VALUE && !strict) || is.getItemDamage() == m))
				return true;
		return false;
	}
}
