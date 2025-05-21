package cn.dupe.nukkit.main;

import cn.nukkit.plugin.PluginBase;


public class Main extends  PluginBase{
    private PlayerListener myListener;
    @Override
        public void onLoad() {
        this.getServer().getLogger().info("复制插件启动");
        this.getDataFolder().mkdir();
        this.saveDefaultConfig();
        }

        @Override
        public void onEnable() {
            this.getServer().getCommandMap().register("EnableCommand",new EnableCommand(this));
            this.getServer().getCommandMap().register("CountCommand",new CountCommand(this));
            this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        }




}
