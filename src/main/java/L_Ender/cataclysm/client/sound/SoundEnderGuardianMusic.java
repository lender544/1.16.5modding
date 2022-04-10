package L_Ender.cataclysm.client.sound;

import L_Ender.cataclysm.ClientProxy;
import L_Ender.cataclysm.entity.Ender_Guardian_Entity;
import L_Ender.cataclysm.init.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.TickableSound;
import net.minecraft.util.SoundCategory;

import java.util.Map;

public class SoundEnderGuardianMusic extends TickableSound {
    private final Ender_Guardian_Entity Guardian;
    private int ticksExisted = 0;
    public SoundEnderGuardianMusic(Ender_Guardian_Entity guardian) {
        super(ModSounds.ENDERGUARDIAN_MUSIC.get(), SoundCategory.RECORDS);
        this.Guardian = guardian;
        this.attenuationType = AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
        this.priority = true;
        this.x = this.Guardian.getPosX();
        this.y = this.Guardian.getPosY();
        this.z = this.Guardian.getPosZ();
    }

    public boolean shouldPlaySound() {
        return !this.Guardian.isSilent() && ClientProxy.GUARDIAN_SOUND_MAP.get(this.Guardian.getEntityId()) == this;
    }

    public boolean isNearest() {
        float dist = 400;
        for(Map.Entry<Integer, SoundEnderGuardianMusic> entry : ClientProxy.GUARDIAN_SOUND_MAP.entrySet()){
            SoundEnderGuardianMusic GuardianMusic = entry.getValue();
            if(GuardianMusic != this && distanceSq(GuardianMusic.x, GuardianMusic.y, GuardianMusic.z) < dist * dist && GuardianMusic.shouldPlaySound()){
                return false;
            }
        }
        return true;
    }


    public double distanceSq(double p_218140_1_, double p_218140_3_, double p_218140_5_) {
        double lvt_10_1_ = (double)this.getX() - p_218140_1_;
        double lvt_12_1_ = (double)this.getY() - p_218140_3_;
        double lvt_14_1_ = (double)this.getZ() - p_218140_5_;
        return lvt_10_1_ * lvt_10_1_ + lvt_12_1_ * lvt_12_1_ + lvt_14_1_ * lvt_14_1_;
    }

    public void tick() {
        if(ticksExisted % 100 == 0){
            Minecraft.getInstance().getMusicTicker().stop();

        }
        if (!this.Guardian.removed && this.Guardian.isAlive()) {
            this.volume = 1;
            this.pitch = 1;
            this.x = this.Guardian.getPosX();
            this.y = this.Guardian.getPosY();
            this.z = this.Guardian.getPosZ();
        } else {
            this.finishPlaying();
            ClientProxy.GUARDIAN_SOUND_MAP.remove(Guardian.getEntityId());
        }
        ticksExisted++;
    }

}
