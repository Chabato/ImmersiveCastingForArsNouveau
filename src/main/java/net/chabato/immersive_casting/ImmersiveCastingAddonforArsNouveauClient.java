package net.chabato.immersive_casting;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.modogthedev.VoiceLibClient;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ImmersiveCastingAddonforArsNouveau.MODID, dist = Dist.CLIENT)

@EventBusSubscriber(modid = ImmersiveCastingAddonforArsNouveau.MODID, value = Dist.CLIENT)
public class ImmersiveCastingAddonforArsNouveauClient {
    public static boolean isCasting = false;
    public ImmersiveCastingAddonforArsNouveauClient(ModContainer container) {
        VoiceLibClient.alwaysOnRecording = false;
        NeoForge.EVENT_BUS.addListener(PlayerEvents::onPlayerUseCastingWand);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ImmersiveCastingAddonforArsNouveau.LOGGER.info("Immersive Casting Addon for Ars Nouveau - Client Set Up");
    }
}
