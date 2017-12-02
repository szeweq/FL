package szewek.fl.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import szewek.fl.util.PreRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class FLProxyClient extends FLProxy {
	private final List<PreRegister> prl = new ArrayList<>();
	@Override
	public void addPreRegister(PreRegister pr) {
		prl.add(pr);
	}

	public void init() {
		final ItemModelMesher imm = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		for (PreRegister pr : prl) {
			Set<Item> iset = pr.getItems();
			for (Item i : iset) {
				ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
				imm.register(i, 0, mrl);
			}
		}
		prl.clear();
	}
}
