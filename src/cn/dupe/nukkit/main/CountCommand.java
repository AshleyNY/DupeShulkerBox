package cn.dupe.nukkit.main;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class CountCommand extends Command {
    private final Main plugin;

    public CountCommand(Main plugin) {
        super("DCount", "设置挖掘盒子次数", "/DCount default|VIP [count]");
        this.plugin = plugin;
        this.setPermission("dupe.command.countcmd");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(args.length == 0) return false;
        if(args[0].equalsIgnoreCase("default")) {
            plugin.getConfig().set("DestroyCount", args[1]);
            plugin.saveConfig();
            plugin.reloadConfig();
            return true;
        }
        else if (args[0].equalsIgnoreCase("VIP")) {
            plugin.getConfig().set("VIPDCount", args[1]);
            plugin.saveConfig();
            plugin.reloadConfig();
            return true;
        }
        sender.sendMessage(TextFormat.RED + "未知参数: " + args[0]);
        sender.sendMessage(TextFormat.RED + "正确的使用方法: " + usageMessage);
        return false;

    }
}
