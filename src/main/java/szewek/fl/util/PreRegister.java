package szewek.fl.util;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import szewek.fl.FL;
import szewek.fl.annotations.NamedResource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class PreRegister {
	private final String domain;
	private final CreativeTabs tab;
	private final Set<Item> items = new HashSet<>();
	private final Set<Block> blocks = new HashSet<>();

	public PreRegister(String dn, CreativeTabs t) {
		domain = dn;
		tab = t;
		FL.PROXY.addPreRegister(this);
	}

	public PreRegister item(String name, Item i) {
		final String fn = domain + ':' + name;
		i.setUnlocalizedName(fn).setCreativeTab(tab).setRegistryName(fn);
		items.add(i);
		return this;
	}
	public PreRegister block(String name, Block b) {
		final String fn = domain + ':' + name;
		b.setUnlocalizedName(fn).setCreativeTab(tab).setRegistryName(fn);
		blocks.add(b);
		return this;
	}
	public void tileEntityClasses(Class<? extends TileEntity>[] tecs) {
		for (Class<? extends TileEntity> tec : tecs) {
			final NamedResource nr = tec.getAnnotation(NamedResource.class);
			if (nr == null)
				continue;
			GameRegistry.registerTileEntity(tec, nr.value());
		}
	}
	public void registerItems(IForgeRegistry<Item> ifr) {
		for (Item i : items) {
			ifr.register(i);
		}
	}
	public void registerBlocks(IForgeRegistry<Block> ifr) {
		for (Block b : blocks) {
			ifr.register(b);
		}
	}
	public Set<Item> getItems() {
		return Collections.unmodifiableSet(items);
	}

	public Set<Block> getBlocks() {
		return Collections.unmodifiableSet(blocks);
	}
}
