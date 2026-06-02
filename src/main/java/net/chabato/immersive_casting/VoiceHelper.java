package net.chabato.immersive_casting;

import net.chabato.immersive_casting.items.CastingWand;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.util.Cast;
import org.modogthedev.api.VoiceLibApi;

public class VoiceHelper {
    public static void initializer () {
    }
    public static void register() {
        VoiceLibApi.registerServerPlayerSpeechListener((serverPlayerTalkEvent -> {
            String text = serverPlayerTalkEvent.getText();
            ServerPlayer player = serverPlayerTalkEvent.getPlayer();
            if(player.isUsingItem()) {
                if(player.getUseItem().getItem() instanceof CastingWand) {
                    player.sendSystemMessage(Component.literal(text));
                    SpellCast.castPB(player, text);
                }
            }
        }));

    }
}
