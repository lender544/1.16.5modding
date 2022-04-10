package L_Ender.cataclysm.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class CommonConfig {


    public final ForgeConfigSpec.IntValue Voidrunedamage;
    public final ForgeConfigSpec.IntValue Lavabombradius;

    public final ForgeConfigSpec.BooleanValue ScreenShake;
    public final ForgeConfigSpec.BooleanValue BossMusic;

    public final ForgeConfigSpec.BooleanValue SoulblacksmithSpawn;
    public final ForgeConfigSpec.BooleanValue RuinedcitadelSpawn;

    public final ForgeConfigSpec.DoubleValue EnderguardianHealth;
    public final ForgeConfigSpec.DoubleValue EnderguardianDamage;
    public final ForgeConfigSpec.IntValue EnderguardianDamageCap;
    public final ForgeConfigSpec.BooleanValue EnderguardianBlockBreaking;
    public final ForgeConfigSpec.DoubleValue EnderguardianLongRangelimit;
    public final ForgeConfigSpec.IntValue EnderguardianBlockBreakingX;
    public final ForgeConfigSpec.IntValue EnderguardianBlockBreakingY;
    public final ForgeConfigSpec.IntValue EnderguardianBlockBreakingZ;

    public final ForgeConfigSpec.IntValue Lavabombmagazine;
    public final ForgeConfigSpec.IntValue Lavabombamount;
    public final ForgeConfigSpec.DoubleValue MonstrosityHealth;
    public final ForgeConfigSpec.DoubleValue MonstrosityDamage;
    public final ForgeConfigSpec.IntValue MonstrosityDamageCap;
    public final ForgeConfigSpec.DoubleValue MonstrosityLongRangelimit;
    public final ForgeConfigSpec.BooleanValue NetheritemonstrosityBodyBloking;

    public final ForgeConfigSpec.BooleanValue EndergolemBlockBreaking;
    public final ForgeConfigSpec.DoubleValue EndergolemLongRangelimit;
    public final ForgeConfigSpec.DoubleValue EndergolemHealth;
    public final ForgeConfigSpec.DoubleValue EndergolemDamage;

    public final ForgeConfigSpec.DoubleValue IgnisHealth;
    public final ForgeConfigSpec.DoubleValue IgnisDamage;

    public final ForgeConfigSpec.IntValue IgnisDamageCap;
    public final ForgeConfigSpec.DoubleValue IgnisLongRangelimit;

    public CommonConfig(final ForgeConfigSpec.Builder builder) {
        builder.push("Etc");
        Voidrunedamage = buildInt(builder, "Voidrunedamage", "all", 7, 0, 1000000, "Void Rune's Damage");
        Lavabombradius = buildInt(builder, "Lavabombradius", "all", 2, 1, 7, "Lava bomb's Radius");
        ScreenShake = buildBoolean(builder, "ScreenShake(on/off)", "all", true, "ScreenShake(on/off)");
        BossMusic = buildBoolean(builder, "BossMusic(on/off)", "all", true, "BossMusic(on/off)");
        builder.pop();

        builder.push("Ender Guardian");
        EnderguardianHealth = buildDouble(builder, "EnderGuardianHealth", "all", 300.0D, 0D, 1000000D, "EnderGuardian's Health");
        EnderguardianDamage = buildDouble(builder, "EnderGuardianDamage", "all", 16.0D, 0D, 1000000D, "EnderGuardian's Damage.");
        EnderguardianDamageCap = buildInt(builder, "EnderGuardianDamageCap", "all", 22, 0, 1000000, "EnderGuardian's DamageCap");
        EnderguardianBlockBreaking = buildBoolean(builder, "EnderguardianBlockBreaking", "all", true, "Ender guardian's block breaking ignore the MobGriefing");
        RuinedcitadelSpawn = buildBoolean(builder, "RuinedcitadelSpawn", "all", true, "ruined citadel spawn");
        EnderguardianLongRangelimit = buildDouble(builder, "Guardian's prevent attacks from far away Range", "all", 12.0D, 1D, 1000000D, "Guardian's Immune to Long distance attack range.");
        EnderguardianBlockBreakingX = buildInt(builder, "EnderGuardianBlockBreaking X", "all", 15, 0, 20, "EnderGuardianBlockBreaking radius");
        EnderguardianBlockBreakingY = buildInt(builder, "EnderGuardianBlockBreaking Y", "all", 2, 0, 10, "EnderGuardianBlockBreaking radius");
        EnderguardianBlockBreakingZ = buildInt(builder, "EnderGuardianBlockBreaking Z", "all", 15, 0, 20, "EnderGuardianBlockBreaking radius");
        builder.pop();

        builder.push("Netherite Monstrosity");
        Lavabombmagazine = buildInt(builder, "LavabombMagazine", "all", 3, 1, 1000000, "Monstrosity's Lavabomb magazine.");
        Lavabombamount = buildInt(builder, "Lavabombamount", "all", 3, 1, 1000000, "Monstrosity's Lavabomb amount" );
        MonstrosityHealth = buildDouble(builder, "MonstrosityHealth", "all", 360.0D, 0D, 1000000D, "Monstrosity's Health");
        MonstrosityDamage = buildDouble(builder, "MonstrosityDamage", "all", 22.0D, 0D, 1000000D, "Monstrosity's Damage");
        MonstrosityDamageCap = buildInt(builder, "MonstrosityDamageCap", "all", 22, 0, 1000000, "Monstrosity's DamageCap");
        SoulblacksmithSpawn = buildBoolean(builder, "SoulblacksmithSpawn", "all", true, "soul black smith spawn");
        NetheritemonstrosityBodyBloking = buildBoolean(builder, "NetheritemonstrosityBodyBloking", "all", true, "Monstrosity's bodyBlocking verdict");
        MonstrosityLongRangelimit = buildDouble(builder, "Monstrosity's prevent attacks from far away Range", "all", 18.0D, 1D, 1000000D, "Monstrosity's Immune to Long distance attack range.");
        builder.pop();

        builder.push("Ender Golem");
        EndergolemBlockBreaking = buildBoolean(builder, "EndergolemBlockBreaking", "all", false, "Ender Golem's block breaking ignore the MobGriefing");
        EndergolemLongRangelimit = buildDouble(builder, "Endergolem's prevent attacks from far away Range", "all", 6.0D, 1D, 1000000D, "Endergolem's Immune to Long distance attack range.");
        EndergolemHealth = buildDouble(builder, "GolemHealth", "all", 150.0D, 0D, 1000000D, "Golem's Health");
        EndergolemDamage = buildDouble(builder, "GolemDamage", "all", 10.0D, 0D, 1000000D, "Golem's Damage.");
        builder.pop();

        builder.push("Ignis");
        IgnisHealth = buildDouble(builder, "IgnisHealth", "all", 333.0D, 0D, 1000000D, "Ignis's Health");
        IgnisDamage = buildDouble(builder, "IgnisDamage", "all", 14.0D, 0D, 1000000D, "Ignis's Damage.");
        IgnisLongRangelimit = buildDouble(builder, "Ignis's prevent attacks from far away Range", "all", 20.0D, 1D, 1000000D, "Ignis's Immune to Long distance attack range.");
        IgnisDamageCap = buildInt(builder, "IgnisDamageCap", "all", 20, 0, 1000000, "Ignis's DamageCap");
        builder.pop();
    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, String catagory, boolean defaultValue, String comment) {
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, String catagory, int defaultValue, int min, int max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, String catagory, double defaultValue, double min, double max, String comment) {
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }
}
