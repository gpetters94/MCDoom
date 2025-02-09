package mod.azure.doom.entities.task;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.doom.entities.DemonEntity;
import mod.azure.doom.entities.tierambient.CueBallEntity;
import mod.azure.doom.entities.tierboss.GladiatorEntity;
import mod.azure.doom.entities.tierboss.IconofsinEntity;
import mod.azure.doom.entities.tierheavy.Hellknight2016Entity;
import mod.azure.doom.entities.tierheavy.MancubusEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class DemonMeleeAttack<E extends DemonEntity> extends CustomDelayedMeleeBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));

    protected ToIntFunction<E> attackIntervalSupplier = entity -> 20;

    @Nullable
    protected LivingEntity target = null;

    public DemonMeleeAttack(int delayTicks) {
        super(delayTicks);
    }

    /**
     * Set the time between attacks.
     *
     * @param supplier The tick value provider
     * @return this
     */
    public DemonMeleeAttack<E> attackInterval(ToIntFunction<E> supplier) {
        this.attackIntervalSupplier = supplier;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);

        assert this.target != null;
        return entity.getSensing().hasLineOfSight(this.target) && entity.isWithinMeleeAttackRange(this.target);
    }

    @Override
    protected void start(E entity) {
        entity.swing(InteractionHand.MAIN_HAND);
        assert this.target != null;
        BehaviorUtils.lookAtEntity(entity, this.target);
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
        if (entity instanceof Hellknight2016Entity hellknight)
            hellknight.addEffect(
                    new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, this.delayTime, 100, false, false));
    }

    @Override
    protected void doDelayedAction(E entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true,
                this.attackIntervalSupplier.applyAsInt(entity));

        if (this.target == null) return;

        if (!entity.getSensing().hasLineOfSight(this.target) || !entity.isWithinMeleeAttackRange(this.target)) return;

        BehaviorUtils.lookAtEntity(entity, this.target);
        if (entity instanceof IconofsinEntity iconEntity) { // damage and explode the world
            iconEntity.doHurtTarget(this.target);
            iconEntity.level().explode(entity, this.target.getX(), this.target.getY(), this.target.getZ(), 3.0F, false,
                    Level.ExplosionInteraction.BLOCK);
            this.target.invulnerableTime = 0;
        } else if (entity instanceof MancubusEntity mancubusEntity) { // summon flames around self
            for (var j = 0; j < 5; ++j) {
                float h2 = (float) Mth.atan2(target.getZ() - entity.getZ(),
                        target.getX() - entity.getX()) + j * (float) Math.PI * 0.4F;
                mancubusEntity.spawnFlames(entity.getX() + Mth.cos(h2) * 1.5D, entity.getZ() + Mth.sin(h2) * 1.5D,
                        Math.min(target.getY(), entity.getY()), Math.max(target.getY(), entity.getY()) + 1.0D);
            }
        } else if (entity instanceof CueBallEntity cueballEntity) {
            if (cueballEntity.getVariant() != 3) cueballEntity.setFuseSpeed(1);
            else cueballEntity.doHurtTarget(this.target);
        } else if (entity instanceof GladiatorEntity gladiatorEntity) {
            var areaeffectcloudentity = new AreaEffectCloud(entity.level(), entity.getX(), entity.getY(),
                    entity.getZ());
            areaeffectcloudentity.setParticle(ParticleTypes.SMOKE);
            areaeffectcloudentity.setRadius(3.0F);
            areaeffectcloudentity.setDuration(55);
            areaeffectcloudentity.setPos(entity.getX(), entity.getY(), entity.getZ());
            gladiatorEntity.level().addFreshEntity(areaeffectcloudentity);
            gladiatorEntity.doHurtTarget(this.target);
            gladiatorEntity.tryAttack1(this.target);
        } else if (entity instanceof Hellknight2016Entity marauderEntity) {
            final var aabb = marauderEntity.getBoundingBox().inflate(16);
            final var checkBlocking = TargetingConditions.forCombat().range(3.0D).selector(
                    target2 -> !target2.getUseItem().is(Items.SHIELD));
            marauderEntity.level().getNearbyEntities(LivingEntity.class, checkBlocking, marauderEntity, aabb).forEach(
                    target1 -> target1.hurt(marauderEntity.damageSources().mobAttack(marauderEntity),
                            (float) marauderEntity.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        } else entity.doHurtTarget(this.target);
    }
}