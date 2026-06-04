package net.chabato.immersive_casting;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.*;
import com.hollingsworth.arsnouveau.common.capability.IPlayerCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import net.chabato.immersive_casting.CustomSpells.AbstractCustomSpell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpellCast {
    public static void castPB(Player player, String str) {
        IPlayerCap playerCap = CapabilityRegistry.getPlayerDataCap(player);

        List<AbstractSpellPart> sList = new ArrayList<>(List.of());

        List<String> list = toWordlist(str);
        String pending = "";
        boolean hasMethod = false;
        for (String word : list) {
            if(word.contains("[unk]")){
                continue;
            }
            word = pending + word;
            List<AbstractSpellPart> spellParts = SpellCast.getSpellPart(word, player);
            if(spellParts.getFirst()==null || spellParts.isEmpty()){
                pending += word + "_";
            }else if(hasMethod){
                pending = "";
                sList.addAll(spellParts);
            }else if(spellParts.getFirst() instanceof AbstractCastMethod){
                pending = "";
                sList.addAll(spellParts);
                hasMethod = true;
            }
        }

        Spell spell = new Spell();
        boolean containLockedGlyphs = false;
        boolean allowLockGlyphs = Config.ALLOW_LOCKED_GLYPHS.getAsBoolean();
        for (AbstractSpellPart spellPart : sList) {
            if(playerCap.knowsGlyph(spellPart) || spellPart.defaultedStarterGlyph() || allowLockGlyphs){
                spell = spell.add(spellPart);
            } else {
                containLockedGlyphs = true;
            }
        }

        if(containLockedGlyphs){
            player.sendSystemMessage(Component.literal("Your cast contains Locked(unlearned) glyphs").setStyle(Style.EMPTY.withColor(ChatFormatting.GOLD)));
        }

        SpellContext ctx = SpellContext.fromEntity(spell, player, ItemStack.EMPTY);
        SpellResolver resolver = new SpellResolver(ctx);
        resolver.onCast(ItemStack.EMPTY, player.level());
    }

    private static List<String> toWordlist(String str) {
        return List.of(str.split(" "));
    }

    private static List<AbstractSpellPart> getSpellPart(String str, Player entity) {
        List<AbstractSpellPart> list = new ArrayList<>();

        List<AbstractCustomSpell> spells = ImmersiveCastingAddonforArsNouveau.customSpellLoader.getSpells();
        for(AbstractCustomSpell customSpell : spells) {
            if(str.equals(customSpell.name)) {
                String spell =  customSpell.spell;
                castPB(entity, spell);
                list.add(null);
                return list;
            }
        }

        if(str.contains("'")){
            list.add(null);
            return list;
        }


        if(str.contains("projectile")) {
            ResourceLocation id = ResourceLocation.tryParse("ars_nouveau:glyph_projectile");
            ResourceLocation idd = ResourceLocation.tryParse("ars_nouveau:glyph_accelerate");
            list.add(GlyphRegistry.getSpellPartOrDefault(id));

            for(int i = 0; i < Config.PROJECTILE_ACCELERATION.getAsInt(); i++){
                list.add(GlyphRegistry.getSpellPartOrDefault(idd));
            }
            return list;
        }

        //ResourceLocation id = ResourceLocation.tryParse("ars_nouveau:glyph_" + str.toLowerCase());

        Map<ResourceLocation,AbstractSpellPart> map = GlyphRegistry.getSpellpartMap();
        List<AbstractSpellPart> spelllist = new ArrayList<>(map.values());
        for(AbstractSpellPart spellPart : spelllist){
            String name = spellPart.getRegistryName().getPath();
            name = name.replace("glyph_", "");
            if(str.equals(name)){
                list.add(spellPart);
                return list;
            }
        }

        return list;
    }
}
