package net.chabato.immersive_casting;

import net.chabato.immersive_casting.items.CastingWand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import org.modogthedev.VoiceLibClient;

public class PlayerEvents {
    public static void onPlayerUseCastingWand(LivingEntityUseItemEvent.Start event) {
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Item item = event.getItem().getItem();
            if(item instanceof CastingWand) {
                if (player.level().isClientSide) {
                    ImmersiveCastingAddonforArsNouveauClient.isCasting = true;
                }
            }
        }
    }
}
