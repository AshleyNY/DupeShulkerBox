package cn.dupe.nukkit.main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityShulkerBox;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEntityEvent;  // 保留原有导入
import cn.nukkit.entity.Entity;  // 新增导入
import cn.nukkit.entity.passive.EntityChicken;  // 保留原有导入
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.scheduler.Task;  // 保留原有导入
import cn.nukkit.nbt.tag.CompoundTag;  // 保留原有导入

import java.io.File;
import java.util.*;

public class PlayerListener implements Listener {
    private final Main plugin;
    private Map<String,Integer> breakCounts = new HashMap<>();
    private int requiredBreaks = 10;
    private Set<Entity> hatchingChickens = new HashSet<>();  // 新增：记录孵化中的鸡

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

            if(count >= requiredBreaks) {
                breakCounts.put(playerName, 0);

                BlockEntity blockEntity = player.getLevel().getBlockEntity(block);
                Item duplicateItem = null;

                if (blockEntity instanceof BlockEntityShulkerBox) {
                    BlockEntityShulkerBox shulkerBox = (BlockEntityShulkerBox) blockEntity;
                    Inventory inventory = shulkerBox.getInventory();

                    duplicateItem = new ItemBlock(block, 0);
                    duplicateItem.setCompoundTag(shulkerBox.namedTag);
                } else {
                    duplicateItem = new ItemBlock(block, 0);
                }
                player.getInventory().addItem(duplicateItem);
                player.sendMessage("§b§2成功复制！");

                // 新增：鸡交互事件处理
            }
        }
    }
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Item handItem = player.getInventory().getItemInHand();
        Entity target = event.getEntity();

        // 仅处理对鸡的交互
        if (!(target instanceof EntityChicken)) return;

        // 新增：检查是否已在孵化中
        if (hatchingChickens.contains(target)) {
            player.sendMessage("§b§4这只kunkun正在孵化潜影盒，请勿重复点击！");
            return;
        }

        // 检查手持物品是否为潜影盒
        if (!(handItem instanceof ItemBlock) ||
            ((ItemBlock) handItem).getBlock().getId() != BlockID.SHULKER_BOX) {
            return;
        }

        // 从配置获取孵化时间（秒），默认20秒
        // 修改第91行的配置获取方式：


        // 修改为更安全的获取方式：
        int hatchSeconds = plugin.getConfig().get("ChickenHatchTime", 20) instanceof Number ?
                         plugin.getConfig().getInt("ChickenHatchTime", 20) :
                         Integer.parseInt(plugin.getConfig().getString("ChickenHatchTime", "20"));
        int delayTicks = hatchSeconds * 20;  // 转换为游戏刻（20刻=1秒）

        // 获取潜影盒NBT数据（包含内部物品）
        byte[] shulkerNbt = handItem.getCompoundTag();

        // 新增：标记为孵化中并固定位置
        hatchingChickens.add(target);
        target.setImmobile(true);  // 固定鸡的位置（Nukkit实体通用方法）
        target.setNameTag("§b§4kunkun正在下蛋");

        plugin.getServer().getScheduler().scheduleDelayedTask(
                plugin,
                new Task() {
                    @Override
                    public void onRun(int taskId) {
                        // 新增：无论是否存活都移除孵化标记
                        hatchingChickens.remove(target);

                        if (target.isAlive()) {
                            target.setImmobile(false);  // 恢复移动能力
                            target.setNameTag("§b§2kunkun还未孵化");
                            // 创建复制的潜影盒
                            Item duplicated = new ItemBlock(Block.get(BlockID.SHULKER_BOX), 0);
                            duplicated.setCompoundTag(shulkerNbt);

                            // 在鸡的位置生成物品
                            target.getLevel().dropItem(target.getPosition(), duplicated);
                            player.sendMessage("§b§2KunKun下了一个潜影盒！");
                        }
                    }
                },
                delayTicks
        );
    }
}

