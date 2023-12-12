package mod.azure.doom.registry;

import mod.azure.doom.MCDoom;
import mod.azure.doom.blocks.*;
import mod.azure.doom.items.blockitems.DoomBlockItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public record FabricDoomBlocks() {
    public static final Block JUMP_PAD = new JumppadBlock();
    public static final Block E1M1_1 = new ArgentBlock();
    public static final Block E1M1_2 = new ArgentBlock();
    public static final Block E1M1_3 = new ArgentBlock();
    public static final Block E1M1_4 = new ArgentBlock();
    public static final Block E1M1_5 = new ArgentBlock();
    public static final Block E1M1_6 = new ArgentBlock();
    public static final Block E1M1_7 = new E1M1StairsBlock(E1M1_5.defaultBlockState(),
            BlockBehaviour.Properties.copy(E1M1_5));
    public static final Block E1M1_8 = new E1M1StairsBlock(E1M1_4.defaultBlockState(),
            BlockBehaviour.Properties.copy(E1M1_4));
    public static final Block E1M1_9 = new E1M1TurnableBlock();
    public static final Block E1M1_10 = new E1M1TurnableBlock();
    public static final Block E1M1_11 = new E1M1TurnableHurtBlock();
    public static final Block E1M1_12 = new E1M1TurnableBlock();
    public static final Block E1M1_13 = new E1M1TurnableBlock();
    public static final Block E1M1_14 = new E1M1TurnableBlock();
    public static final Block E1M1_15 = new E1M1TurnableBlock();
    public static final Block E1M1_16 = new E1M1TurnableBlock();
    public static final Block E1M1_17 = new E1M1TurnableBlock();
    public static final Block E1M1_18 = new E1M1TurnableBlock();
    public static final Block E1M1_19 = new E1M1TurnableBlock();
    public static final Block E1M1_20 = new E1M1TurnableBlock();
    public static final Block E1M1_21 = new E1M1TurnableBlock();
    public static final Block E1M1_22 = new E1M1TurnableBlock();
    public static final Block E1M1_23 = new E1M1TurnableBlock();
    public static final Block E1M1_24 = new E1M1TurnableBlock();
    public static final Block E1M1_25 = new E1M1TurnableBlock();
    public static final Block E1M1_26 = new E1M1TurnableBlock();
    public static final Block E1M1_27 = new E1M1StairsBlock(E1M1_5.defaultBlockState(),
            BlockBehaviour.Properties.copy(E1M1_5));
    public static final Block E1M1_28 = new E1M1StairsBlock(E1M1_5.defaultBlockState(),
            BlockBehaviour.Properties.copy(E1M1_5));
    public static final Block E1M1_29 = new E1M1StairsBlock(E1M1_5.defaultBlockState(),
            BlockBehaviour.Properties.copy(E1M1_5));


    public static final Block DOOM_SAND = new DoomSandBlock();
    public static final Block ARGENT_BLOCK = new ArgentBlock();
    public static final Block BARREL_BLOCK = new BarrelBlock();
    public static final Block ICON_WALL1 = new DoomWallBlock();
    public static final Block ICON_WALL2 = new DoomWallBlock();
    public static final Block ICON_WALL3 = new DoomWallBlock();
    public static final Block ICON_WALL4 = new DoomWallBlock();
    public static final Block ICON_WALL5 = new DoomWallBlock();
    public static final Block ICON_WALL6 = new DoomWallBlock();
    public static final Block ICON_WALL7 = new DoomWallBlock();
    public static final Block ICON_WALL8 = new DoomWallBlock();
    public static final Block ICON_WALL9 = new DoomWallBlock();
    public static final Block ICON_WALL10 = new DoomWallBlock();
    public static final Block ICON_WALL11 = new DoomWallBlock();
    public static final Block ICON_WALL12 = new DoomWallBlock();
    public static final Block ICON_WALL13 = new DoomWallBlock();
    public static final Block ICON_WALL14 = new DoomWallBlock();
    public static final Block ICON_WALL15 = new DoomWallBlock();
    public static final Block ICON_WALL16 = new DoomWallBlock();
    public static final Block ARGENT_LAMP_BLOCK = new ArgentLampBlock();

    public static final Block TOTEM = new TotemBlock();
    public static final GunTableBlock GUN_TABLE = new GunTableBlock();

    public static void initialize() {
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("gun_table"), GUN_TABLE);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("gun_table"),
                new DoomBlockItem(GUN_TABLE, new Item.Properties(), "gun_table") {
                });
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("totem"), TOTEM);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("totem"),
                new DoomBlockItem(TOTEM, new Item.Properties(), "totem") {
                });
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("argent_block"), ARGENT_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("argent_block"),
                new BlockItem(ARGENT_BLOCK, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("jump_pad"), JUMP_PAD);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("jump_pad"),
                new BlockItem(JUMP_PAD, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("doom_sand"), DOOM_SAND);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("doom_sand"),
                new BlockItem(DOOM_SAND, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("argent_lamp_block"), ARGENT_LAMP_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("argent_lamp_block"),
                new BlockItem(ARGENT_LAMP_BLOCK, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("barrel"), BARREL_BLOCK);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("barrel"),
                new BlockItem(BARREL_BLOCK, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall1"), ICON_WALL1);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall2"), ICON_WALL2);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall3"), ICON_WALL3);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall4"), ICON_WALL4);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall5"), ICON_WALL5);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall6"), ICON_WALL6);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall7"), ICON_WALL7);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall8"), ICON_WALL8);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall9"), ICON_WALL9);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall10"), ICON_WALL10);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall11"), ICON_WALL11);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall12"), ICON_WALL12);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall13"), ICON_WALL13);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall14"), ICON_WALL14);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall15"), ICON_WALL15);
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("icon_wall16"), ICON_WALL16);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall1"),
                new BlockItem(ICON_WALL1, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall2"),
                new BlockItem(ICON_WALL2, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall3"),
                new BlockItem(ICON_WALL3, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall4"),
                new BlockItem(ICON_WALL4, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall5"),
                new BlockItem(ICON_WALL5, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall6"),
                new BlockItem(ICON_WALL6, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall7"),
                new BlockItem(ICON_WALL7, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall8"),
                new BlockItem(ICON_WALL8, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall9"),
                new BlockItem(ICON_WALL9, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall10"),
                new BlockItem(ICON_WALL10, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall11"),
                new BlockItem(ICON_WALL11, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall12"),
                new BlockItem(ICON_WALL12, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall13"),
                new BlockItem(ICON_WALL13, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall14"),
                new BlockItem(ICON_WALL14, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall15"),
                new BlockItem(ICON_WALL15, new Item.Properties()));
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("icon_wall16"),
                new BlockItem(ICON_WALL16, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block1"), E1M1_1);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block1"),
                new BlockItem(E1M1_1, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block2"), E1M1_2);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block2"),
                new BlockItem(E1M1_2, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block3"), E1M1_3);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block3"),
                new BlockItem(E1M1_3, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block4"), E1M1_4);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block4"),
                new BlockItem(E1M1_4, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block5"), E1M1_5);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block5"),
                new BlockItem(E1M1_5, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block6"), E1M1_6);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block6"),
                new BlockItem(E1M1_6, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block7"), E1M1_7);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block7"),
                new BlockItem(E1M1_7, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block8"), E1M1_8);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block8"),
                new BlockItem(E1M1_8, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block9"), E1M1_9);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block9"),
                new BlockItem(E1M1_9, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block10"), E1M1_10);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block10"),
                new BlockItem(E1M1_10, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block11"), E1M1_11);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block11"),
                new BlockItem(E1M1_11, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block12"), E1M1_12);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block12"),
                new BlockItem(E1M1_12, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block13"), E1M1_13);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block13"),
                new BlockItem(E1M1_13, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block14"), E1M1_14);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block14"),
                new BlockItem(E1M1_14, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block15"), E1M1_15);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block15"),
                new BlockItem(E1M1_15, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block16"), E1M1_16);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block16"),
                new BlockItem(E1M1_16, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block17"), E1M1_17);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block17"),
                new BlockItem(E1M1_17, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block18"), E1M1_18);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block18"),
                new BlockItem(E1M1_18, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block19"), E1M1_19);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block19"),
                new BlockItem(E1M1_19, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block20"), E1M1_20);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block20"),
                new BlockItem(E1M1_20, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block21"), E1M1_21);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block21"),
                new BlockItem(E1M1_21, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block22"), E1M1_22);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block22"),
                new BlockItem(E1M1_22, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block23"), E1M1_23);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block23"),
                new BlockItem(E1M1_23, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block24"), E1M1_24);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block24"),
                new BlockItem(E1M1_24, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block25"), E1M1_25);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block25"),
                new BlockItem(E1M1_25, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block26"), E1M1_26);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block26"),
                new BlockItem(E1M1_26, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block27"), E1M1_27);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block27"),
                new BlockItem(E1M1_27, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block28"), E1M1_28);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block28"),
                new BlockItem(E1M1_28, new Item.Properties()));
        Registry.register(BuiltInRegistries.BLOCK, MCDoom.modResource("e1m1_block29"), E1M1_29);
        Registry.register(BuiltInRegistries.ITEM, MCDoom.modResource("e1m1_block29"),
                new BlockItem(E1M1_29, new Item.Properties()));
    }

}
