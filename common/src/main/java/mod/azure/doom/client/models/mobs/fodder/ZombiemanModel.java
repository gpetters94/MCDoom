package mod.azure.doom.client.models.mobs.fodder;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.doom.MCDoom;
import mod.azure.doom.entities.tierfodder.ZombiemanEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ZombiemanModel extends GeoModel<ZombiemanEntity> {

    @Override
    public ResourceLocation getModelResource(ZombiemanEntity object) {
        return MCDoom.modResource("geo/shotgunzombie.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZombiemanEntity object) {
        return new ResourceLocation(MCDoom.MOD_ID,
                "textures/entity/" + (object.getVariant() == 2 ? "zombieman64" : "eternalzombiemen") + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZombiemanEntity object) {
        return MCDoom.modResource("animations/shotgunzombie.animation.json");
    }

    @Override
    public void setCustomAnimations(ZombiemanEntity animatable, long instanceId, AnimationState<ZombiemanEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        var head = getAnimationProcessor().getBone("bipedHead");
        var entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }

    @Override
    public RenderType getRenderType(ZombiemanEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}