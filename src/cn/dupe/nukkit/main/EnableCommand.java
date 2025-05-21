package cn.dupe.nukkit.main;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;

public class EnableCommand extends Command {
    private final Main plugin;

    public EnableCommand(Main plugin) {
        super("Dupe", "控制dupe是否开启", "/dupe enable||disable");
        this.plugin = plugin;
        this.setPermission("sad.admin");
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if(!sender.hasPermission("sad.admin")) {
            return false;
        }
        if(args.length == 0) {
            sender.sendMessage(usageMessage);
            return false;
        }

        if(args[0].equalsIgnoreCase("enable")) {
            sender.sendMessage("Dupe已启动");
            plugin.getConfig().set("status", true);
            plugin.saveConfig();
            plugin.reloadConfig();
            return true;
        }
        else if(args[0].equalsIgnoreCase("disable")) {
            sender.sendMessage("Dupe已关闭");
            plugin.getConfig().set("status", false);
            plugin.saveConfig();
            plugin.reloadConfig();
            return true;
        }
        sender.sendMessage(TextFormat.RED + "未知参数: " + args[0]);
        sender.sendMessage(TextFormat.RED + "正确的使用方法: " + usageMessage);
        return false;

    }


}
