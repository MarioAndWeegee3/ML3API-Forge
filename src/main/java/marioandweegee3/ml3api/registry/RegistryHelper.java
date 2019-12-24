package marioandweegee3.ml3api.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class RegistryHelper {
    private final String modid;

    public final Logger logger;

    private Map<ResourceLocation, Block> blocks;
    private Map<ResourceLocation, Item> items;
    private Map<ResourceLocation, TileEntityType<?>> blockEntities;

    /**
     * Initialize your RegistryHelper.
     * Only call this from your Mod's constructor.
     * 
     * The RegistryHelper automatically registers all of the necessary methods with Forge's EventBus.
     * @param modid The id of your mod.
     */
    public RegistryHelper(String modid) {
        this.modid = modid;
        this.logger = LogManager.getLogger(modid);
        this.blocks = new HashMap<>();
        this.items = new HashMap<>();
        this.blockEntities = new HashMap<>();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addGenericListener(Item.class, this::onItemRegistry);
        bus.addGenericListener(Block.class, this::onBlockRegistry);
        bus.addGenericListener(TileEntityType.class, this::onBlockEntityRegistry);
    }

    public ResourceLocation makeID(String name) {
        return new ResourceLocation(modid, name);
    }

    @SubscribeEvent
    protected void onBlockRegistry(RegistryEvent.Register<Block> event) {
        for(Map.Entry<ResourceLocation, Block> entry : blocks.entrySet()) {
            event.getRegistry().register(entry.getValue().setRegistryName(entry.getKey()));
        }
    }

    @SubscribeEvent
    protected void onItemRegistry(RegistryEvent.Register<Item> event) {
        for(Map.Entry<ResourceLocation, Item> entry : items.entrySet()) {
            event.getRegistry().register(entry.getValue().setRegistryName(entry.getKey()));
        }
    }

    @SubscribeEvent
    protected void onBlockEntityRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
        for(Map.Entry<ResourceLocation, TileEntityType<?>> entry : blockEntities.entrySet()) {
            event.getRegistry().register(entry.getValue().setRegistryName(entry.getKey()));
        }
    }

    public void registerBlock(String name, Block block, ItemGroup group) {
        blocks.put(makeID(name), block);
        registerItem(name, new BlockItem(block, new Item.Properties().group(group)));
    }

    public void registerAllBlocks(Map<String, Block> map, ItemGroup group) {
        for(Map.Entry<String, Block> entry : map.entrySet()){
            registerBlock(entry.getKey(), entry.getValue(), group);
        }
    }

    public void registerItem(String name, Item item) {
        items.put(makeID(name), item);
    }

    public void registerAllItems(Map<String, Item> map) {
        for(Map.Entry<String, Item> entry : map.entrySet()){
            registerItem(entry.getKey(), entry.getValue());
        }
    }

    public void registerBlockEntity(String name, TileEntityType<?> type) {
        blockEntities.put(makeID(name), type);
    }

    public void registerTileEntity(String name, TileEntityType<?> type) {
        registerBlockEntity(name, type);
    }

    public <T extends TileEntity> TileEntityType<T> registerAndCreateBlockEntity(String name, Supplier<T> blockEntity, Block... blocks){
        TileEntityType<T> type = TileEntityType.Builder.create(blockEntity, blocks).build(null);
        registerBlockEntity(name, type);
        return type;
    }

    public <T extends TileEntity> TileEntityType<T> registerAndCreateTileEntity(String name, Supplier<T> tileEntity, Block... blocks){
        TileEntityType<T> type = TileEntityType.Builder.create(tileEntity, blocks).build(null);
        registerBlockEntity(name, type);
        return type;
    }

    public void log(String message){
        logger.info("["+modid+"] "+message);
    }
}