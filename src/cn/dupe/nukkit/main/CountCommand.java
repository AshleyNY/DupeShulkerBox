package cn.dupe.nukkit.main;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class CountCommand extends Command {
    private final Main plugin;

    public CountCommand(Main plugin) {
        super("DupeTime", "设置挖掘盒子次数/鸡下蛋秒数", "/DupeTime Break|Chicken [times/time]");
        this.plugin = plugin;
        this.setPermission("sad.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!sender.hasPermission("sad.admin")) {
            return false;
        }
        if(args.length == 0) {
            sender.sendMessage(TextFormat.RED + "请输入参数");
            return false;
        }
        if(args[0].equalsIgnoreCase("Break")) {
            plugin.getConfig().set("DestroyCount", args[1]);
            plugin.saveConfig();
            plugin.reloadConfig();
            sender.sendMessage(TextFormat.GREEN + "已设置挖掘盒子次数为: " + args[1]);
            return true;
        }
        // 修改第28行配置设置方式：
        else if (args[0].equalsIgnoreCase("Chicken")) {
            // 将字符串参数转换为整数
            plugin.getConfig().set("ChickenHatchTime", Integer.parseInt(args[1]));
            plugin.saveConfig();
            plugin.reloadConfig();
            sender.sendMessage(TextFormat.GREEN + "已设置鸡下蛋秒数为: " + args[1]);
            return true;
        }
        return false;

    }
}
