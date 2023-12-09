package hakasenz.ut;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class UT extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // 注册事件监听器
        Bukkit.getServer().getPluginManager().registerEvents(this, this);

        // 确保配置文件存在
        saveDefaultConfig();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();  //获取玩家对象
            double originalDamage = event.getDamage();  //获取原始伤害
            LivingEntity damagedEntity = (LivingEntity) event.getEntity();  //获取受伤对象
            // 从配置文件中读取玩家的attack值
            double attackModifier = getAttackModifier(player.getName()); //获取额外攻击

            double critchance = getAttackercritdamage(player.getName());  //获取暴击概率
            double critdamage = getAttackercritchance(player.getName());  //获取暴击倍率

            double ignorearmorchance = getAttackerignorearmorchance(player.getName());  //获取穿甲概率
            double ignorearmortoughnesschance = getAttackerignorearmortoughnesschance(player.getName());  //获取穿甲韧性概率
            double ignorearmor=0.01;  //获取穿甲效率
            double ignorearmortoughness=0.01;  //获取穿甲韧性效率

            double modifiedDamage = originalDamage + attackModifier;  //伤害=原始+额外
            // 设置爆伤
            if (Math.random() <= critchance) {
                double additionalDamage = modifiedDamage * critdamage;  //爆伤=伤害*爆伤倍率
                attackModifier = additionalDamage;      //赋值
                player.sendMessage("暴击了");
            }
            // 设置破甲
            if (Math.random() <= ignorearmorchance) {
                ignorearmor = getAttackerignorearmor(player.getName());  //获取穿甲效率
            }
            // 设置破甲韧性
            if (Math.random() <= ignorearmortoughnesschance) {
                ignorearmortoughness = getAttackerignorearmortoughness(player.getName());  //破甲韧性效率
            }
            modifiedDamage = (modifiedDamage + attackModifier) * (1 - damagedEntity.getAttribute(Attribute.GENERIC_ARMOR).getValue() * ignorearmor)*(1 + damagedEntity.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue() * ignorearmortoughness);  //最终伤害=(爆伤+基伤)*(1-破甲值)*(1-韧性值)
            event.setDamage(modifiedDamage);    //造成伤害
        }
    }
    // 从配置文件中读取玩家的attack值
    private double getAttackModifier(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0
        return config.getDouble(playerName + ".attack", 0.0);
    }
    // 从配置文件中读取玩家的critdamage值  爆伤
    private double getAttackercritdamage(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0.5
        return config.getDouble(playerName + ".critdamage", 0.2);
    }
    // 从配置文件中读取玩家的critchance值  爆率
    private double getAttackercritchance(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0.5
        return config.getDouble(playerName + ".critchance", 0.1);
    }
    // 从配置文件中读取玩家的ignorearmorchance值  穿甲概率
    private double getAttackerignorearmorchance(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0.5
        return config.getDouble(playerName + ".ignorearmorchance", 0.1);
    }
    // 从配置文件中读取玩家的ignorearmortoughnesschance值  穿甲韧性概率
    private double getAttackerignorearmortoughnesschance(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0.5
        return config.getDouble(playerName + ".ignorearmortoughnesschance", 0.1);
    }
    // 从配置文件中读取玩家的ignorearmor值  穿甲效率
    private double getAttackerignorearmor(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0.01
        return config.getDouble(playerName + ".ignorearmor", 0.01);
    }
    // 从配置文件中读取玩家的ignorearmortoughness值  穿甲韧性效率
    private double getAttackerignorearmortoughness(String playerName) {
        // 获取插件数据文件夹
        File dataFolder = getDataFolder();
        // 构建playerdata.yml文件路径
        File configFile = new File(dataFolder, "../DamageEvent/playerdata.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        // 默认值为0.01
        return config.getDouble(playerName + ".ignorearmortoughness", 0.01);
    }
}
