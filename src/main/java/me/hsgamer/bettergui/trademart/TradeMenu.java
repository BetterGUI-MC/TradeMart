package me.hsgamer.bettergui.trademart;

import me.hsgamer.bettergui.builder.ButtonBuilder;
import me.hsgamer.bettergui.menu.BaseMenu;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.gui.object.BukkitItem;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.CaseInsensitivePathString;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.minecraft.gui.button.Button;
import org.bukkit.entity.Player;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;

import java.util.*;

public class TradeMenu extends BaseMenu {
    private final List<ButtonTrade> buttonTrades;
    private final String title;

    public TradeMenu(Config config) {
        super(config);

        buttonTrades = new ArrayList<>();
        for (Map.Entry<CaseInsensitivePathString, Object> entry : configSettings.entrySet()) {
            String key = entry.getKey().getPathString().getLastPath();
            Map<String, Object> map = MapUtils.castOptionalStringObjectMap(entry.getValue())
                    .<Map<String, Object>>map(CaseInsensitiveStringMap::new)
                    .orElseGet(Collections::emptyMap);

            Button button = Optional.ofNullable(MapUtils.getIfFound(map, "button", "item"))
                    .flatMap(MapUtils::castOptionalStringObjectMap)
                    .<Button>flatMap(values -> ButtonBuilder.INSTANCE.build(new ButtonBuilder.Input(this, "trade_" + key + "_button", values)))
                    .orElse(Button.EMPTY);
            button.init();

            Button button2 = Optional.ofNullable(MapUtils.getIfFound(map, "button2", "item2"))
                    .flatMap(MapUtils::castOptionalStringObjectMap)
                    .<Button>flatMap(values -> ButtonBuilder.INSTANCE.build(new ButtonBuilder.Input(this, "trade_" + key + "_button2", values)))
                    .orElse(Button.EMPTY);
            button2.init();

            Button result = Optional.ofNullable(MapUtils.getIfFound(map, "result", "button3", "item3"))
                    .flatMap(MapUtils::castOptionalStringObjectMap)
                    .<Button>flatMap(values -> ButtonBuilder.INSTANCE.build(new ButtonBuilder.Input(this, "trade_" + key + "_result", values)))
                    .orElse(Button.EMPTY);
            result.init();

            int maxUses = Optional.ofNullable(MapUtils.getIfFound(map, "max-uses", "uses", "max"))
                    .map(Objects::toString)
                    .flatMap(Validate::getNumber)
                    .map(Number::intValue)
                    .orElse(1);

            buttonTrades.add(new ButtonTrade(button, button2, result, maxUses));
        }

        title = Optional.ofNullable(MapUtils.getIfFound(menuSettings, "title", "name"))
                .map(Objects::toString)
                .orElse("Trade");
    }

    private List<VillagerTrade> getTrades(Player player) {
        List<VillagerTrade> trades = new ArrayList<>();
        for (ButtonTrade buttonTrade : buttonTrades) {
            BukkitItem item = (BukkitItem) buttonTrade.button.getItem(player.getUniqueId());
            BukkitItem item2 = (BukkitItem) buttonTrade.button2.getItem(player.getUniqueId());
            BukkitItem result = (BukkitItem) buttonTrade.result.getItem(player.getUniqueId());

            if (item == null || result == null) {
                continue;
            }

            VillagerTrade trade = new VillagerTrade(
                    item.getItemStack(),
                    item2 == null ? null : item2.getItemStack(),
                    result.getItemStack(),
                    buttonTrade.maxUses
            );
            trades.add(trade);
        }
        return trades;
    }

    @Override
    protected boolean createChecked(Player player, String[] args, boolean bypass) {
        List<VillagerTrade> trades = getTrades(player);
        String parsedTitle = StringReplacerApplier.replace(title, player.getUniqueId(), this);

        VillagerInventory inventory = new VillagerInventory(trades, player);
        inventory.setName(parsedTitle);
        inventory.open();
        return true;
    }

    @Override
    public void update(Player player) {
        // EMPTY
    }

    @Override
    public void close(Player player) {
        // EMPTY
    }

    @Override
    public void closeAll() {
        buttonTrades.forEach(buttonTrade -> {
            buttonTrade.button.stop();
            buttonTrade.button2.stop();
            buttonTrade.result.stop();
        });
    }

    private static final class ButtonTrade {
        public final Button button;
        public final Button button2;
        public final Button result;
        public final int maxUses;

        private ButtonTrade(Button button, Button button2, Button result, int maxUses) {
            this.button = button;
            this.button2 = button2;
            this.result = result;
            this.maxUses = maxUses;
        }
    }
}
