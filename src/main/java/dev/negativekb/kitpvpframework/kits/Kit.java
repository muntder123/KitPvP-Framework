package dev.negativekb.kitpvpframework.kits;

import dev.negativekb.kitpvpframework.core.util.UtilPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public abstract class Kit {

    @Getter
    private final Kits type;
    @Getter
    private final long cost;
    @Getter
    @Setter
    private boolean automaticallyUnlocked;

    private Consumer<EntityDamageByEntityEvent> damagePlayerEventConsumer;
    private Consumer<PlayerDeathEvent> deathEventConsumer;
    private Consumer<PlayerMoveEvent> moveEventConsumer;
    private Consumer<PlayerInteractEvent> interactEventConsumer;
    private Consumer<PlayerInteractEvent> leftClickEventConsumer;
    private Consumer<PlayerInteractEvent> rightClickEventConsumer;
    private Consumer<PlayerInteractAtEntityEvent> rightClickEntityEventConsumer;

    @Getter
    @Setter
    private List<PotionEffect> permanentPotionEffects;

    public Kit(Kits type, long cost) {
        this.type = type;
        this.cost = cost;
    }


    /**
     * @return - Returns helmet itemstack
     */
    public abstract ItemStack getHelmet();

    /**
     * @return - Returns chestplate itemstack
     */
    public abstract ItemStack getChestplate();

    /**
     * @return - Returns leggings itemstack
     */
    public abstract ItemStack getLeggings();

    /**
     * @return - Returns boots itemstack
     */
    public abstract ItemStack getBoots();

    /**
     * @return - Returns hashmap of kit contents, Integer is the index of the soon-to-be inventory
     */
    public abstract HashMap<Integer, ItemStack> kitContents();

    /**
     * @return - Returns icon of the kit for the Kit Selector
     */
    public abstract ItemStack getIcon();

    public void applyKit(Player player) {
        UtilPlayer.reset(player);

        PlayerInventory inv = player.getInventory();

        if (getHelmet().getType() != Material.AIR || getHelmet() != null)
            inv.setHelmet(getHelmet());

        if (getChestplate().getType() != Material.AIR || getChestplate() != null)
            inv.setChestplate(getChestplate());

        if (getLeggings().getType() != Material.AIR || getLeggings() != null)
            inv.setLeggings(getLeggings());

        if (getBoots().getType() != Material.AIR || getBoots() != null)
            inv.setBoots(getBoots());

        if (!kitContents().isEmpty() || kitContents() != null)
            kitContents().forEach(inv::setItem);

        if (getPermanentPotionEffects() != null)
            getPermanentPotionEffects().forEach(player::addPotionEffect);

        player.playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
    }


    public void setRightClickEntityEvent(Consumer<PlayerInteractAtEntityEvent> function) {
        this.rightClickEntityEventConsumer = function;
    }

    public void onRightClickEntity(PlayerInteractAtEntityEvent event) {
        if (rightClickEntityEventConsumer == null)
            return;

        rightClickEntityEventConsumer.accept(event);
    }

    public void setLeftClickEvent(Consumer<PlayerInteractEvent> function) {
        this.leftClickEventConsumer = function;
    }

    public void onLeftClickEvent(PlayerInteractEvent event) {
        if (leftClickEventConsumer == null)
            return;

        leftClickEventConsumer.accept(event);
    }

    public void setDamagePlayerEvent(Consumer<EntityDamageByEntityEvent> function) {
        this.damagePlayerEventConsumer = function;
    }

    public void onRightClickEvent(PlayerInteractEvent event) {
        if (rightClickEventConsumer == null)
            return;

        rightClickEventConsumer.accept(event);
    }

    public void setInteractEvent(Consumer<PlayerInteractEvent> function) {
        this.interactEventConsumer = function;
    }

    public void onInteractEvent(PlayerInteractEvent event) {
        if (interactEventConsumer == null)
            return;

        interactEventConsumer.accept(event);
    }


    public void setDeathEvent(Consumer<PlayerDeathEvent> function) {
        this.deathEventConsumer = function;
    }

    public void onDeathEvent(PlayerDeathEvent event) {
        if (deathEventConsumer == null)
            return;

        deathEventConsumer.accept(event);
    }

    public void setMoveEvent(Consumer<PlayerMoveEvent> function) {
        this.moveEventConsumer = function;
    }

    public void onMoveEvent(PlayerMoveEvent event) {
        if (moveEventConsumer == null)
            return;

        moveEventConsumer.accept(event);
    }

    public void setRightClickEvent(Consumer<PlayerInteractEvent> function) {
        this.rightClickEventConsumer = function;
    }

    public void onDamagePlayer(EntityDamageByEntityEvent event) {
        if (damagePlayerEventConsumer == null)
            return;

        damagePlayerEventConsumer.accept(event);
    }

    public abstract void onDisable();
}
