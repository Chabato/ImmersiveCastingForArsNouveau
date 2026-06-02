package net.chabato.immersive_casting.CustomSpells;

import com.google.gson.Gson;
import net.chabato.immersive_casting.ImmersiveCastingAddonforArsNouveau;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class CustomSpellLoader {
    private static final List<AbstractCustomSpell> spells = new ArrayList<>();

    private static final Gson GSON = new Gson();
    private static final Path DIR_PATH = FMLPaths.CONFIGDIR.get().resolve(ImmersiveCastingAddonforArsNouveau.MODID).resolve("custom_spells");
    private static final Path EX_PATH = FMLPaths.CONFIGDIR.get().resolve(ImmersiveCastingAddonforArsNouveau.MODID).resolve("custom_spells").resolve("example.json");

    public List<AbstractCustomSpell> getSpells() {
        return spells;
    }

    private AbstractCustomSpells exampleCustomSpells() {
        List<AbstractCustomSpell> list = new ArrayList<>();
        AbstractCustomSpell exSpell =  new AbstractCustomSpell("example", "projectile lightning");
        list.add(exSpell);
        return new AbstractCustomSpells(list);
    }

    public CustomSpellLoader() {
        try {
            if (Files.notExists(EX_PATH)) {
                Files.createDirectories(EX_PATH.getParent());
                try (Writer writer = Files.newBufferedWriter(EX_PATH)) {
                    GSON.toJson(exampleCustomSpells(), writer);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try (Stream<Path> stream = Files.list(DIR_PATH)) {
            stream.forEach(path -> {
                ImmersiveCastingAddonforArsNouveau.LOGGER.info("Loading Custom Spell File: " + path.getFileName().toString());
                try (Reader reader = Files.newBufferedReader(path)){
                    AbstractCustomSpells cspells = GSON.fromJson(reader, AbstractCustomSpells.class);
                    ImmersiveCastingAddonforArsNouveau.LOGGER.info(cspells.spells.getFirst().name + ": " + cspells.spells.getFirst().spell);
                    if(cspells.spells != null) {
                        spells.addAll(cspells.spells);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
