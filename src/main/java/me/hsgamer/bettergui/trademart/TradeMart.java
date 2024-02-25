package me.hsgamer.bettergui.trademart;

import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.expansion.common.Expansion;

public final class TradeMart implements Expansion {
    @Override
    public void onEnable() {
        MenuBuilder.INSTANCE.register(TradeMenu::new, "trade");
    }
}
