package net.chabato.immersive_casting;

import net.chabato.immersive_casting.CustomSpells.CustomSpellLoader;
import net.chabato.immersive_casting.items.CastingWand;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ImmersiveCastingAddonforArsNouveau.MODID)
public class ImmersiveCastingAddonforArsNouveau {
    public static final String MODID = "chabs_immersivecasting";// Define mod id in a common place for everything to reference
    public static final Logger LOGGER = LogUtils.getLogger();// Directly reference a slf4j logger

    // Create a Deferred Register to hold Items which will all be registered under the "chabs_immersivecasting" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "chabs_immersivecasting" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    // Creates a new food item with the id "chabs_immersivecasting:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> CASTING_WAND = ITEMS.registerItem("casting_wand", props -> new CastingWand(props.stacksTo(1)));
    public static final DeferredItem<Item> CASTING_WAND_RED = ITEMS.registerItem("casting_wand_red", props -> new CastingWand(props.stacksTo(1)));
    public static final DeferredItem<Item> CASTING_WAND_BLUE = ITEMS.registerItem("casting_wand_blue", props -> new CastingWand(props.stacksTo(1)));
    public static final DeferredItem<Item> CASTING_WAND_PURPLE = ITEMS.registerItem("casting_wand_purple", props -> new CastingWand(props.stacksTo(1)));
    public static final DeferredItem<Item> CASTING_WAND_YELLOW = ITEMS.registerItem("casting_wand_yellow", props -> new CastingWand(props.stacksTo(1)));

    // Creates a creative tab with the id "chabs_immersivecasting:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("casting_wand", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.chabs_immersivecasting")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> CASTING_WAND_PURPLE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(CASTING_WAND.get());
                output.accept(CASTING_WAND_RED.get());
                output.accept(CASTING_WAND_BLUE.get());
                output.accept(CASTING_WAND_PURPLE.get());
                output.accept(CASTING_WAND_YELLOW.get());// Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());

    public static CustomSpellLoader customSpellLoader;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ImmersiveCastingAddonforArsNouveau(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        customSpellLoader = new CustomSpellLoader();

        VoiceHelper.initializer();
        VoiceHelper.register();

        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ImmersiveCastingAddonforArsNouveau) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("Immersive Casting Addon for Ars Nouveau - Client Set Up");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
