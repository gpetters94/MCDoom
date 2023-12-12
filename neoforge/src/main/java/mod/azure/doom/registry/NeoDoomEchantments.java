package mod.azure.doom.registry;

import mod.azure.doom.MCDoom;
import mod.azure.doom.enchantments.StickyBombEnchantment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NeoDoomEchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(
            ForgeRegistries.ENCHANTMENTS, MCDoom.MOD_ID);
    public static final RegistryObject<Enchantment> STICKY_MOD = ENCHANTMENTS.register("stickyattachment",
            () -> new StickyBombEnchantment(EquipmentSlot.MAINHAND));
}
