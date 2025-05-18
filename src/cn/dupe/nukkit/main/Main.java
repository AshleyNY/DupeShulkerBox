package cn.dupe.nukkit.main;

import cn.nukkit.plugin.PluginBase;


public class Main extends  PluginBase{
    private PlayerListener myListener;
    @Override
        public void onLoad() {
        this.getServer().getLogger().info("插件启动");
        this.getDataFolder().mkdir();
        this.saveDefaultConfig();
        }

        @Override
        public void onEnable() {
            this.getServer().getCommandMap().register("my_plugin",new EnableCommand(this));
            this.getLogger().info("Count命令已开启");
            this.getServer().getCommandMap().register("my_plugin",new CountCommand(this));
            this.getLogger().info("Dupe命令已开启");
            myListener = new PlayerListener(this);
            this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        }


public PlayerListener getMyListener() {
        return myListener;
}

}
