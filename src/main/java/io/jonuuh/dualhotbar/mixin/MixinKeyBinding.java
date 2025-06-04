package io.jonuuh.dualhotbar.mixin;

import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(KeyBinding.class)
public interface MixinKeyBinding
{
    @Accessor("keybindArray")
    static List<KeyBinding> dualhotbar$accessKeyBindingArray()
    {
        return java.util.Collections.emptyList();
    }
}
