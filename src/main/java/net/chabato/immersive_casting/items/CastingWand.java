package net.chabato.immersive_casting.items;

import net.chabato.immersive_casting.ImmersiveCastingAddonforArsNouveauClient;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.VoiceLibClient;
import org.modogthedev.api.VoiceLibApi;

public class CastingWand  extends Item {
    public CastingWand(Properties properties) {
        super(properties);
    }

    //on use
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(net.minecraft.world.level.@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        player.startUsingItem(hand);

        return InteractionResultHolder.consume(stack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 72000;
    }


    //using animation
    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;    // BOW / EAT / DRINK / SPEAR / CROSSBOW / NONE
    }

    //on finish using
    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeCharged) {
        if (!(entity instanceof Player)) return;

        if(level.isClientSide){
            ImmersiveCastingAddonforArsNouveauClient.isCasting = false;
        }
    }

}
