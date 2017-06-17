package szewek.fl.recipes;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Recipe builder class. It contains methods that make creating recipes much easier.
 */
public final class RecipeBuilder {
	R9[] shape;
	byte width, height, mirror;
	int meta = 0, size = 1;
	Item result;
	NBTTagCompound tags = null;
	final RecipeItem[] items = new RecipeItem[9];
	final String[] oreDicts = new String[9];

	/**
	 * CLeans all recipe input data (shape, items, ore dictionary names)
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder cleanInput() {
		shape = null;
		width = height = mirror = 0;
		for (int i = 0; i < 9; i++) {
			items[i] = null;
			oreDicts[i] = null;
		}
		return this;
	}

	/**
	 * Cleans all recipe output data (item, size, meta, tags)
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder cleanOutput() {
		meta = 0;
		size = 1;
		result = null;
		tags = null;
		return this;
	}

	/**
	 * Sets a recipe shape with specified width and height
	 * @param sh Recipe shape
	 * @param w Recipe width
	 * @param h Recipe height
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder shape(R9[] sh, int w, int h) {
		width = (byte) w;
		height = (byte) h;
		if (width * height != sh.length)
			throw new RuntimeException("Size of shape (" + sh.length + ") is not equal to specified width and height (" + width + " * " + height + ")");
		shape = sh;
		return this;
	}

	/**
	 * Clears all items and ore dictionary names by a specified {@link R9}
	 * @param r9s Recipe identifiers (from A to I)
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder clear(R9... r9s) {
		for (R9 id : r9s) {
			items[id.ord] = null;
			oreDicts[id.ord] = null;
		}
		return this;
	}

	/**
	 * Sets mirroring options
	 * @param x {@code true} for mirroring by the X axis
	 * @param y {@code true} for mirroring by the Y axis
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder mirror(boolean x, boolean y) {
		mirror = 0;
		if (x) mirror |= 2;
		if (y) mirror |= 1;
		return this;
	}

	/**
	 * Sets a result item used as a recipe output
	 * @param it An item
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder result(Item it) {
		result = it;
		return this;
	}

	/**
	 * Sets a result block used as a recipe output
	 * @param b A block
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder result(Block b) {
		result = Item.getItemFromBlock(b);
		return this;
	}

	/**
	 * Sets output stack size
	 * @param s Size
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder size(int s) {
		size = s;
		return this;
	}

	/**
	 * Sets output item metadata
	 * @param m Metadata
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder meta(int m) {
		meta = m;
		return this;
	}

	/**
	 * Sets output stack tags
	 * @param nbt NBT tag compund
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder tags(NBTTagCompound nbt) {
		tags = nbt;
		return this;
	}

	/**
	 * Sets a recipe item to a specified {@link R9}
	 * @param id Recipe identifier (from A to I)
	 * @param ri Recipe Item
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder with(R9 id, RecipeItem ri) {
		items[id.ord] = ri;
		return this;
	}

	/**
	 * Sets an ore dictionary name to a specified {@link R9}
	 * @param id Recipe identifier (from A to I)
	 * @param s Ore dictionary name
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder with(R9 id, String s) {
		oreDicts[id.ord] = s;
		return this;
	}

	/**
	 * Builds and registers a recipe (copies necessary values)
	 * @return This recipe builder (chainable method)
	 */
	public RecipeBuilder register() {
		if (result != null)
			net.minecraftforge.fml.common.registry.GameRegistry.addRecipe(new BuiltShapedRecipe(this));
		return this;
	}
}
