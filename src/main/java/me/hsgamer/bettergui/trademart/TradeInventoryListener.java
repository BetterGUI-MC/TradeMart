package me.hsgamer.bettergui.trademart;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.events.VillagerInventoryCloseEvent;
import teammt.villagerguiapi.events.VillagerInventoryOpenEvent;

public class TradeInventoryListener implements Listener {
    @EventHandler
    public void onOpen(VillagerInventoryOpenEvent event) {
        VillagerInventory inventory = event.getInventory();
        if (inventory instanceof ActionVillagerInventory) {
            ((ActionVillagerInventory) inventory).callOpenAction();
        }
    }

    @EventHandler
    public void onClose(VillagerInventoryCloseEvent event) {
        VillagerInventory inventory = event.getInventory();
        if (inventory instanceof ActionVillagerInventory) {
            ((ActionVillagerInventory) inventory).callCloseAction();
        }
    }
}
