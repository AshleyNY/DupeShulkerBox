package cn.dupe.nukkit.main;
import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {
    private final Main plugin;
    private Map<String,Integer> breakCounts = new HashMap<>();
    private int requiredBreaks = 10;


    public PlayerListener(Main plugin) {
        this.plugin = plugin;

    }

@EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        requiredBreaks = plugin.getConfig().getInt("DestroyCount");

        if(block.getId() == BlockID.SHULKER_BOX){
            String playerName = player.getName();

            int count = breakCounts.getOrDefault(playerName,0) + 1;
            breakCounts.put(playerName,count);

            if(count >= requiredBreaks){
                breakCounts.put(playerName,0);

                BlockEntity blockEntity = player.getLevel().getBlockEntity(block);
                Item duplicateItem = null;

                if(blockEntity instanceof BlockEntityShulkerBox){
                    BlockEntityShulkerBox shulkerBox = (BlockEntityShulkerBox) blockEntity;
                    Inventory inventory = shulkerBox.getInventory();

                    duplicateItem = new ItemBlock(block,0);
                    duplicateItem.setCompoundTag(shulkerBox.namedTag);
                }else{
                    duplicateItem = new ItemBlock(block,0);
                }
                player.getInventory().addItem(duplicateItem);
                player.sendMessage("成功复制！");
            }
        }
    }
}
