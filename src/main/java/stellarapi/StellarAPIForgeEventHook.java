package stellarapi;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.world.WorldEvent;
import stellarapi.api.IViewScope;
import stellarapi.api.PerWorldManager;
import stellarapi.api.StellarAPIReference;

public class StellarAPIForgeEventHook {
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event) {
		PerWorldManager.initiatePerWorldManager(event.world);
	}
	
	@SubscribeEvent
	public void playerUseItemEvent(PlayerUseItemEvent.Start event) {
		if(event.item.getItem() instanceof IViewScope)
			StellarAPIReference.updateScope(event.entity, event.item);
	}
	
	@SubscribeEvent
	public void playerUseItemEvent(PlayerUseItemEvent.Stop event) {
		if(event.item.getItem() instanceof IViewScope)
			StellarAPIReference.updateScope(event.entity, event.item);
	}
	
	@SubscribeEvent
	public void onSleepInBed(PlayerSleepInBedEvent event) {
		if(!StellarAPIReference.getSleepWakeManager().isEnabled() || event.entityPlayer.worldObj.isRemote) {
			return;
		}

		if(event.result == null || event.result == EnumStatus.OK || event.result == EnumStatus.NOT_POSSIBLE_NOW) {
			World worldObj = event.entityPlayer.worldObj;
			
			event.result = StellarAPIReference.getSleepWakeManager()
					.getSleepPossibility(worldObj, event.result);
		}
	}
}