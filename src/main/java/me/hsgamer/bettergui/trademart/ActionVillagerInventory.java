package me.hsgamer.bettergui.trademart;

import me.hsgamer.bettergui.action.ActionApplier;
import me.hsgamer.bettergui.util.ProcessApplierConstants;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.task.BatchRunnable;
import org.bukkit.entity.Player;
import teammt.villagerguiapi.classes.VillagerInventory;
import teammt.villagerguiapi.classes.VillagerTrade;

import java.util.List;

public class ActionVillagerInventory extends VillagerInventory {
    private final TradeMenu menu;

    public ActionVillagerInventory(TradeMenu menu) {
        this.menu = menu;
    }

    public ActionVillagerInventory(TradeMenu menu, List<VillagerTrade> trades, Player forWho) {
        super(trades, forWho);
        this.menu = menu;
    }

    public TradeMenu getMenu() {
        return menu;
    }

    private void executeActionApplier(ActionApplier applier) {
        BatchRunnable batchRunnable = new BatchRunnable();
        batchRunnable.getTaskPool(ProcessApplierConstants.ACTION_STAGE).addLast(process -> applier.accept(getForWho().getUniqueId(), process));
        Scheduler.current().async().runTask(batchRunnable);
    }

    public void callOpenAction() {
        executeActionApplier(menu.getOpenActionApplier());
    }

    public void callCloseAction() {
        executeActionApplier(menu.getCloseActionApplier());
    }
}
