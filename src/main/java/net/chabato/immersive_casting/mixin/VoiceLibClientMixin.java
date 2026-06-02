package net.chabato.immersive_casting.mixin;


import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.chabato.immersive_casting.ImmersiveCastingAddonforArsNouveauClient;
import net.minecraft.client.KeyMapping;
import org.modogthedev.VoiceLibClient;
import org.modogthedev.client.event.EventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = VoiceLibClient.class, remap = false)
public class VoiceLibClientMixin {
    @Shadow public static KeyMapping vKeyMapping;
    @Shadow public static boolean recordingSpeech;

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private static void init(CallbackInfo ci) {
        ci.cancel();
        KeyMappingRegistry.register(vKeyMapping);
        ClientTickEvent.CLIENT_POST.register(minecraft ->
                recordingSpeech = vKeyMapping.isDown() || ImmersiveCastingAddonforArsNouveauClient.isCasting
        );

        EventHandler.register();
    }

}
