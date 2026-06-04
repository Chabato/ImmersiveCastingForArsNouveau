package net.chabato.immersive_casting.mixin;

import com.hollingsworth.arsnouveau.api.registry.GlyphRegistry;
import com.hollingsworth.arsnouveau.api.spell.AbstractSpellPart;
import net.chabato.immersive_casting.CustomSpells.AbstractCustomSpell;
import net.chabato.immersive_casting.Config;
import net.chabato.immersive_casting.ImmersiveCastingAddonforArsNouveau;
import net.minecraft.resources.ResourceLocation;
import org.modogthedev.speech.SpeechRecognizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vosk.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Mixin(value = SpeechRecognizer.class, remap = false)
public class SpeechRecognizerMixin {
    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(Model model, int sampleRate, CallbackInfo ci) {
        String grammar = chabsimmersivecasting$parseGrammar();
        ((SpeechRecognizer)(Object)this).recognizer.setGrammar(grammar);
    }

    @Unique
    private String chabsimmersivecasting$parseGrammar() {
        String grammar = "[";

        if (Config.ENABLE_GLYPH_CASTING.getAsBoolean()) {
            List<String> glyphNames = chabsimmersivecasting$getGlyphNames();
            for(String glyphName : glyphNames) {
                grammar += "\"" + glyphName + "\", ";
            }
        }
        List<AbstractCustomSpell> customSpells = ImmersiveCastingAddonforArsNouveau.customSpellLoader.getSpells();
        for(AbstractCustomSpell customSpell : customSpells) {
            String str = customSpell.name;
            str = str.replace("_"," ");
            grammar += "\"" + str + "\", ";
        }
        grammar += "\"[unk]\"]";
        return grammar;
    }

    @Unique
    private List<String> chabsimmersivecasting$getGlyphNames() {
        Map<ResourceLocation,AbstractSpellPart> map = GlyphRegistry.getSpellpartMap();
        List<AbstractSpellPart> list = new ArrayList<>(map.values());
        List<String> glyphNames = new ArrayList<>();
        for (AbstractSpellPart spellPart : list) {
            String name = spellPart.getRegistryName().getPath();
            String str;
            if(name.contains("glyph_")){
                str = name.substring(6);
            }else {str = name;}
            str = str.replace("_"," ");
            glyphNames.add(str);
        }
        return glyphNames;
    }
}
