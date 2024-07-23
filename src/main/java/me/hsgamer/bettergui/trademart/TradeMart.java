package me.hsgamer.bettergui.trademart;

import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.expansion.common.Expansion;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import teammt.villagerguiapi.api.AdapterLoader;

public final class TradeMart implements Expansion, GetPlugin {
    private final TradeInventoryListener tradeInventoryListener = new TradeInventoryListener();

    @Override
    public void onEnable() {
        AdapterLoader.init(getPlugin());

        MenuBuilder.INSTANCE.register(TradeMenu::new, "trade", "villager");

        Bukkit.getPluginManager().registerEvents(tradeInventoryListener, getPlugin());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(tradeInventoryListener);
    }
}
