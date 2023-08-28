package net.ashindigo.prismatic.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.searchtree.SearchRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Minecraft.class)
public interface MixinMinecraft { // TODO Remove?
    @Accessor(value="searchRegistry", remap=false)
    SearchRegistry getSearchRegistry();
}
