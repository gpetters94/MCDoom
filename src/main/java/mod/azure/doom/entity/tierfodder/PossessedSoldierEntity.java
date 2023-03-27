package mod.azure.doom.entity.tierfodder;

import java.util.List;
import java.util.SplittableRandom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import mod.azure.doom.config.DoomConfig;
import mod.azure.doom.entity.DemonEntity;
import mod.azure.doom.entity.ai.goal.RandomFlyConvergeOnTargetGoal;
import mod.azure.doom.entity.attack.AbstractRangedAttack;
import mod.azure.doom.entity.attack.AttackSound;
import mod.azure.doom.entity.projectiles.entity.BarenBlastEntity;
import mod.azure.doom.entity.task.ProjectileAttack;
import mod.azure.doom.util.registry.DoomSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FloatToSurfaceOfFluid;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;

public class PossessedSoldierEntity extends DemonEntity implements SmartBrainOwner<PossessedSoldierEntity> {

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	public static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(PossessedSoldierEntity.class, EntityDataSerializers.INT);
	public int flameTimer;

	public PossessedSoldierEntity(EntityType<PossessedSoldierEntity> entityType, Level worldIn) {
		super(entityType, worldIn);
		moveControl = new SoldierMoveControl(this);
	}

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		var isDead = this.dead || this.getHealth() < 0.01 || this.isDeadOrDying();
		controllers.add(new AnimationController<>(this, "livingController", 0, event -> {
			if (event.isMoving() && !isDead && !this.swinging)
				return event.setAndContinue(RawAnimation.begin().thenLoop("walking"));
			if (this.swinging && !isDead)
				return event.setAndContinue(RawAnimation.begin().then("attacking", LoopType.PLAY_ONCE));
			if (!isOnGround() && !onGround && getVariant() == 2 && !isDead)
				return event.setAndContinue(RawAnimation.begin().thenLoop("flying"));
			return event.setAndContinue(isDead ? RawAnimation.begin().thenPlayAndHold("death") : RawAnimation.begin().thenLoop("idle"));
		}).setSoundKeyframeHandler(event -> {
			if (event.getKeyframeData().getSound().matches("walk"))
				if (level.isClientSide())
					getLevel().playLocalSound(this.getX(), this.getY(), this.getZ(), DoomSounds.PINKY_STEP, SoundSource.HOSTILE, 0.25F, 1.0F, false);
			if (event.getKeyframeData().getSound().matches("attack"))
				if (level.isClientSide())
					getLevel().playLocalSound(this.getX(), this.getY(), this.getZ(), DoomSounds.PISTOL_HIT, SoundSource.HOSTILE, 0.25F, 1.0F, false);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(VARIANT, 0);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setVariant(tag.getInt("Variant"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("Variant", getVariant());
	}

	public int getVariant() {
		return Mth.clamp(entityData.get(VARIANT), 1, 3);
	}

	public void setVariant(int variant) {
		entityData.set(VARIANT, variant);
	}

	public int getVariants() {
		return 3;
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, SpawnGroupData spawnDataIn, CompoundTag dataTag) {
		spawnDataIn = super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
		final SplittableRandom random = new SplittableRandom();
		final int var = random.nextInt(0, 4);
		setVariant(var);
		return spawnDataIn;
	}

	@Override
	protected void customServerAiStep() {
		tickBrain(this);
		super.customServerAiStep();
	}

	@Override
	protected Brain.Provider<?> brainProvider() {
		return new SmartBrainProvider<>(this);
	}

	@Override
	public List<ExtendedSensor<PossessedSoldierEntity>> getSensors() {
		return ObjectArrayList.of(new NearbyLivingEntitySensor<PossessedSoldierEntity>().setPredicate((target, entity) -> target.isAlive() && entity.hasLineOfSight(target) && !(target instanceof DemonEntity)), new HurtBySensor<>(), new UnreachableTargetSensor<PossessedSoldierEntity>());
	}

	@Override
	public BrainActivityGroup<PossessedSoldierEntity> getCoreTasks() {
		return BrainActivityGroup.coreTasks(new LookAtTarget<>(), new LookAtTargetSink(40, 300), new FloatToSurfaceOfFluid<>(), new StrafeTarget<>().speedMod(0.25F), new MoveToWalkTarget<>());
	}

	@Override
	public BrainActivityGroup<PossessedSoldierEntity> getIdleTasks() {
		return BrainActivityGroup.idleTasks(new FirstApplicableBehaviour<PossessedSoldierEntity>(new TargetOrRetaliate<>().alertAlliesWhen((mob, entity) -> this.isAggressive()), new SetPlayerLookTarget<>().stopIf(target -> !target.isAlive() || target instanceof Player && ((Player) target).isCreative()), new SetRandomLookTarget<>()), new OneRandomBehaviour<>(new SetRandomWalkTarget<>().setRadius(20).speedModifier(1.0f), new Idle<>().runFor(entity -> entity.getRandom().nextInt(300, 600))));
	}

	@Override
	public BrainActivityGroup<PossessedSoldierEntity> getFightTasks() {
		return BrainActivityGroup.fightTasks(new InvalidateAttackTarget<>().invalidateIf((target, entity) -> !target.isAlive() || !entity.hasLineOfSight(target)), new SetWalkTargetToAttackTarget<>().speedMod(1.05F), new ProjectileAttack<>(7).attackInterval(mob -> 80).attackDamage(DoomConfig.possessed_soldier_ranged_damage), new AnimatableMeleeAttack<>(20));
	}

	@Override
	protected void registerGoals() {
		if (getVariant() == 2)
			goalSelector.addGoal(5, new RandomFlyConvergeOnTargetGoal(this, 2, 15, 0.5));
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (isAggressive() && getVariant() == 2) {
			if (isInWater()) {
				moveRelative(0.02F, movementInput);
				move(MoverType.SELF, getDeltaMovement());
				this.setDeltaMovement(getDeltaMovement().scale(0.8F));
			} else if (isInLava()) {
				moveRelative(0.02F, movementInput);
				move(MoverType.SELF, getDeltaMovement());
				this.setDeltaMovement(getDeltaMovement().scale(0.5D));
			} else {
				final BlockPos ground = BlockPos.containing(this.getX(), this.getY() - 1.0D, this.getZ());
				float f = 0.91F;
				if (onGround) {
					f = level.getBlockState(ground).getBlock().getFriction() * 0.91F;
				}
				final float f1 = 0.16277137F / (f * f * f);
				f = 0.91F;
				if (onGround) {
					f = level.getBlockState(ground).getBlock().getFriction() * 0.91F;
				}
				moveRelative(onGround ? 0.1F * f1 : 0.02F, movementInput);
				move(MoverType.SELF, getDeltaMovement());
				this.setDeltaMovement(getDeltaMovement().scale(f));
			}
		} else {
			super.travel(movementInput);
		}
	}

	static class SoldierMoveControl extends MoveControl {
		protected final PossessedSoldierEntity entity;
		private int courseChangeCooldown;

		public SoldierMoveControl(PossessedSoldierEntity entity) {
			super(entity);
			this.entity = entity;
		}

		@Override
		public void tick() {
			if (entity.isAggressive() && entity.getVariant() == 2) {
				if (operation == MoveControl.Operation.MOVE_TO) {
					if (courseChangeCooldown-- <= 0) {
						courseChangeCooldown += entity.getRandom().nextInt(5) + 2;
						Vec3 vector3d = new Vec3(wantedX - entity.getX(), wantedY - entity.getY(), wantedZ - entity.getZ());
						final double d0 = vector3d.length();
						vector3d = vector3d.normalize();
						if (canReach(vector3d, Mth.ceil(d0))) {
							entity.setDeltaMovement(entity.getDeltaMovement().add(vector3d.scale(0.1D)));
						} else {
							operation = MoveControl.Operation.WAIT;
						}
					}
				} else {
					operation = MoveControl.Operation.WAIT;
					entity.setZza(0.0F);
				}
			} else if (operation == MoveControl.Operation.MOVE_TO) {
				operation = MoveControl.Operation.WAIT;
				final double d0 = wantedX - entity.getX();
				final double d1 = wantedZ - entity.getZ();
				final double d2 = wantedY - entity.getY();
				final double d3 = d0 * d0 + d2 * d2 + d1 * d1;
				if (d3 < 2.5000003E-7F) {
					entity.setZza(0.0F);
					return;
				}
				final float f9 = (float) (Mth.atan2(d1, d0) * (180F / (float) Math.PI)) - 90.0F;
				entity.setYRot(rotlerp(mob.getYRot(), f9, 90.0F));
				entity.setSpeed((float) 0.25D);
				final BlockPos blockpos = mob.blockPosition();
				final BlockState blockstate = mob.level.getBlockState(blockpos);
				final VoxelShape voxelshape = blockstate.getCollisionShape(mob.level, blockpos);
				if (d2 > mob.getEyeHeight() && d0 * d0 + d1 * d1 < Math.max(1.0F, mob.getBbWidth()) || !voxelshape.isEmpty() && mob.getY() < voxelshape.max(Direction.Axis.Y) + blockpos.getY() && !blockstate.is(BlockTags.DOORS) && !blockstate.is(BlockTags.FENCES)) {
					operation = MoveControl.Operation.JUMPING;
				}
			} else if (operation == MoveControl.Operation.JUMPING) {
				mob.setSpeed((float) 0.25D);
				if (mob.isOnGround()) {
					operation = MoveControl.Operation.WAIT;
				}
			} else {
				operation = MoveControl.Operation.WAIT;
				entity.setZza(0.0F);
			}
		}

		private boolean canReach(Vec3 direction, int steps) {
			AABB axisalignedbb = mob.getBoundingBox();
			for (int i = 1; i < steps; ++i) {
				axisalignedbb = axisalignedbb.move(direction);
				if (!mob.level.noCollision(entity, axisalignedbb)) {
					return false;
				}
			}
			return true;
		}
	}

	@Override
	protected PathNavigation createNavigation(Level worldIn) {
		final FlyingPathNavigation flyingpathnavigator = new FlyingPathNavigation(this, worldIn);
		flyingpathnavigator.setCanOpenDoors(false);
		flyingpathnavigator.setCanFloat(true);
		flyingpathnavigator.setCanPassDoors(true);
		return flyingpathnavigator;
	}

	public class FireballAttack extends AbstractRangedAttack {

		public FireballAttack(DemonEntity parentEntity, double xOffSetModifier, double entityHeightFraction, double zOffSetModifier, float damage) {
			super(parentEntity, xOffSetModifier, entityHeightFraction, zOffSetModifier, damage);
		}

		public FireballAttack(DemonEntity parentEntity) {
			super(parentEntity);
		}

		@Override
		public AttackSound getDefaultAttackSound() {
			return new AttackSound(DoomSounds.PLASMA_FIRING, 1, 1);
		}

		@Override
		public Projectile getProjectile(Level world, double d2, double d3, double d4) {
			return new BarenBlastEntity(world, parentEntity, d2, d3, d4, damage);
		}
	}

	public static AttributeSupplier.Builder createMobAttributes() {
		return LivingEntity.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 40.0D).add(Attributes.MAX_HEALTH, DoomConfig.possessed_soldier_health).add(Attributes.ATTACK_DAMAGE, 0.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_KNOCKBACK, 0.0D);
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
		return 1.74F;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return DoomSounds.PSOLDIER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return DoomSounds.PSOLDIER_DEATH;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 7;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		flameTimer = (flameTimer + 1) % 2;
	}

	public int getFlameTimer() {
		return flameTimer;
	}

}