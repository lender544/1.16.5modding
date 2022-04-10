package L_Ender.cataclysm.config;

import L_Ender.cataclysm.cataclysm;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

public class CMConfig {

    public static int Voidrunedamage = 7;
    public static int Lavabombradius = 2;

    public static boolean ScreenShake = true;
    public static boolean BossMusic = true;

    public static int EnderguardianDamageCap = 22;
    public static int MonstrosityDamageCap = 22;
    public static int IgnisDamageCap = 20;

    public static int Lavabombmagazine = 3;
    public static int Lavabombamount = 3;

    public static int EnderguardianBlockBreakingX = 15;
    public static int EnderguardianBlockBreakingY = 2;
    public static int EnderguardianBlockBreakingZ = 15;

    public static boolean EnderguardianBlockBreaking = true;
    public static boolean EndergolemBlockBreaking = false;

    public static boolean SoulblacksmithSpawn = true;
    public static boolean RuinedcitadelSpawn = true;

    public static double MonstrosityHealth = 360D;
    public static double MonstrosityDamage = 22D;
    public static boolean NetheritemonstrosityBodyBloking = true;

    public static double EnderguardianHealth = 300D;
    public static double EnderguardianDamage = 16D;

    public static double IgnisHealth = 333D;
    public static double IgnisDamage = 14D;

    public static double EnderGolemHealth = 150D;
    public static double EnderGolemDamage = 10D;

    public static double MonstrosityLongRangelimit = 18D;
    public static double EnderguardianLongRangelimit = 12D;
    public static double EndergolemLongRangelimit = 6D;
    public static double IgnisLongRangelimit = 20D;


    public static void bake(ModConfig config) {
        try {
            Voidrunedamage = ConfigHolder.COMMON.Voidrunedamage.get();
            Lavabombradius = ConfigHolder.COMMON.Lavabombradius.get();
            ScreenShake = ConfigHolder.COMMON.ScreenShake.get();
            BossMusic = ConfigHolder.COMMON.BossMusic.get();
            EnderguardianDamageCap = ConfigHolder.COMMON.EnderguardianDamageCap.get();
            MonstrosityDamageCap = ConfigHolder.COMMON.MonstrosityDamageCap.get();
            IgnisDamageCap = ConfigHolder.COMMON.MonstrosityDamageCap.get();
            Lavabombmagazine = ConfigHolder.COMMON.Lavabombmagazine.get();
            Lavabombamount = ConfigHolder.COMMON.Lavabombamount.get();
            EnderguardianBlockBreakingX = ConfigHolder.COMMON.EnderguardianBlockBreakingX.get();
            EnderguardianBlockBreakingY = ConfigHolder.COMMON.EnderguardianBlockBreakingY.get();
            EnderguardianBlockBreakingZ = ConfigHolder.COMMON.EnderguardianBlockBreakingZ.get();
            NetheritemonstrosityBodyBloking = ConfigHolder.COMMON.NetheritemonstrosityBodyBloking.get();
            EnderguardianBlockBreaking = ConfigHolder.COMMON.EnderguardianBlockBreaking.get();
            EndergolemBlockBreaking = ConfigHolder.COMMON.EndergolemBlockBreaking.get();
            SoulblacksmithSpawn = ConfigHolder.COMMON.SoulblacksmithSpawn.get();
            RuinedcitadelSpawn = ConfigHolder.COMMON.RuinedcitadelSpawn.get();
            MonstrosityHealth = ConfigHolder.COMMON.MonstrosityHealth.get();
            MonstrosityDamage = ConfigHolder.COMMON.MonstrosityDamage.get();
            EnderguardianHealth = ConfigHolder.COMMON.EnderguardianHealth.get();
            EnderguardianDamage = ConfigHolder.COMMON.EnderguardianDamage.get();

            EnderGolemHealth = ConfigHolder.COMMON.EndergolemHealth.get();
            EnderGolemDamage = ConfigHolder.COMMON.EndergolemDamage.get();

            IgnisHealth = ConfigHolder.COMMON.IgnisHealth.get();
            IgnisDamage = ConfigHolder.COMMON.IgnisDamage.get();
            MonstrosityLongRangelimit = ConfigHolder.COMMON.MonstrosityLongRangelimit.get();
            EnderguardianLongRangelimit = ConfigHolder.COMMON.EnderguardianLongRangelimit.get();
            EndergolemLongRangelimit = ConfigHolder.COMMON.EndergolemLongRangelimit.get();
            IgnisLongRangelimit = ConfigHolder.COMMON.IgnisLongRangelimit.get();

        } catch (Exception e) {
            cataclysm.LOGGER.warn("An exception was caused trying to load the config for CM");
            e.printStackTrace();
        }
    }

}
