package nl.tabuu.tabuucore.inventory.ui;

import nl.tabuu.tabuucore.TabuuCore;
import nl.tabuu.tabuucore.inventory.InventorySize;
import nl.tabuu.tabuucore.inventory.ui.graphics.InventoryCanvas;
import nl.tabuu.tabuucore.util.vector.Vector2f;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class InventoryUI extends InventoryCanvas {

    private Inventory _inventory;
    private String _title;
    private List<InventoryAction> _blockedActions;
    private boolean _initial;

    public InventoryUI(String title, InventorySize size){
        _title = title;
        _blockedActions = new ArrayList<>();
        _initial = true;
        setSize(size);
    }

    public void open(HumanEntity player) {
        if(_initial) {
            createInventory();
            draw(); /* onDraw(); */ //TODO: Replace.
            _initial = false;
        }

        player.openInventory(getInventory());
    }

    public void close(HumanEntity player){
        InventoryView view = player.getOpenInventory();
        Inventory topInventory = view.getTopInventory();

        if(topInventory.equals(getInventory()))
            player.closeInventory();
    }

    public void closeAll(){
        new ArrayList<>(getInventory().getViewers()).forEach(this::close);
    }

    public void reload() {
        if(_initial) return;

        List<HumanEntity> viewers = new ArrayList<>(getInventory().getViewers());

        ItemStack[] contents = Arrays.copyOfRange(getInventory().getContents().clone(), 0, getSize().getSize());
        createInventory();
        getInventory().setContents(contents);

        viewers.forEach(this::open);
    }

    @Deprecated
    public boolean isReloading(){
        return false;
    }

    protected void createInventory() {

        switch (getSize()){

            case ONE_BY_FIVE:
                _inventory = Bukkit.createInventory(null, InventoryType.HOPPER, _title);
                break;

            case THREE_BY_THREE:
                _inventory = Bukkit.createInventory(null, InventoryType.DROPPER, _title);
                break;

            default:
                _inventory = Bukkit.createInventory(null, getSize().getSize(), _title);
                break;

        }
        
        TabuuCore.getInstance().getInventoryUIManager().register(this);
    }

    public void setTitle(String title){
        _title = title;
    }

    public List<InventoryAction> getBlockedActions(){
        return _blockedActions;
    }

    public boolean isBlockedAction(InventoryAction action){
        return _blockedActions.contains(action);
    }

    public void addBlockedAction(InventoryAction... actions){
        _blockedActions.addAll(Arrays.asList(actions));
    }

    public void setBlockedActions(InventoryAction... actions){
        _blockedActions.clear();
        _blockedActions.addAll(Arrays.asList(actions));
    }

    public void removeBlockedAction(InventoryAction... actions){
        _blockedActions.removeAll(Arrays.asList(actions));
    }

    @Override
    public Inventory getInventory(){
        return _inventory;
    }

    @Override
    public void setInventory(Inventory inventory){
        _inventory = inventory;
    }

    @Override
    public void onClick(InventoryClickEvent event) { }

    @Override
    public void onClickUI(InventoryClickEvent event) { }

    @Override
    public void onDrag(InventoryDragEvent event) { }

    @Override
    public void onDragUI(InventoryDragEvent event) { }

    @Override
    public void onOpen(Player player) { }

    @Override
    public void onClose(Player player){
        if(!player.getItemOnCursor().getType().equals(Material.AIR)){
            player.getWorld().dropItemNaturally(player.getLocation(), player.getItemOnCursor());
            player.setItemOnCursor(null);
        }
    }
}
