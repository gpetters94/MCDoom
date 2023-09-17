package mod.azure.doom.entity.task;

import java.util.function.Consumer;

import mod.azure.doom.entity.DemonEntity;
import mod.azure.doom.entity.tierboss.GladiatorEntity;
import mod.azure.doom.entity.tierheavy.MancubusEntity;
import mod.azure.doom.entity.tiersuperheavy.ArchvileEntity;
import mod.azure.doom.entity.tiersuperheavy.DoomHunterEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

public abstract class CustomDelayedRangedBehaviour<E extends DemonEntity> extends ExtendedBehaviour<E> {
	protected final int delayTime;
	protected long delayFinishedAt = 0;
	protected Consumer<E> delayedCallback = entity -> {
	};

	public CustomDelayedRangedBehaviour(int delayTicks) {
		this.delayTime = delayTicks;

		runFor(entity -> Math.max(delayTicks, 60));
	}

	/**
	 * A callback for when the delayed action is called.
	 * 
	 * @param callback The callback
	 * @return this
	 */
	public final CustomDelayedRangedBehaviour<E> whenActivating(Consumer<E> callback) {
		this.delayedCallback = callback;

		return this;
	}

	@Override
	protected final void start(ServerLevel level, E entity, long gameTime) {
		if (this.delayTime > 0) {
			this.delayFinishedAt = gameTime + this.delayTime;
			super.start(level, entity, gameTime);
		} else {
			super.start(level, entity, gameTime);
			doDelayedAction(entity);
		}
//		if (!(entity instanceof GladiatorEntity) || !(entity instanceof ArchvileEntity) || !(entity instanceof CarcassEntity))
//			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 100, false, false));
		if (entity instanceof ArchvileEntity)
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 100, false, false));
		if (entity instanceof MancubusEntity mancubusEntity)
			mancubusEntity.setAttackingState(2);
		else if (entity instanceof GladiatorEntity gladiatorEntity) {
			if (gladiatorEntity.getDeathState() == 0) {
				gladiatorEntity.setAttackingState(1);
				gladiatorEntity.setTextureState(2);
			} else
				gladiatorEntity.setAttackingState(4);
			gladiatorEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 100, false, false));
		} else
			entity.setAttackingState(1);
		entity.triggerAnim("livingController", "ranged");
		if (!(entity instanceof DoomHunterEntity))
			entity.triggerAnim("attackController", "ranged");
		if (entity instanceof DoomHunterEntity hunter) {
			if (hunter.getDeathState() == 1)
				hunter.triggerAnim("attackController", "flames");
			if (hunter.getDeathState() == 0)
				hunter.triggerAnim("attackController", "rocket");
		}
	}

	@Override
	protected final void stop(ServerLevel level, E entity, long gameTime) {
		super.stop(level, entity, gameTime);

		this.delayFinishedAt = 0;
		if (entity instanceof GladiatorEntity gladiatorEntity)
			gladiatorEntity.setTextureState(0);
	}

	@Override
	protected boolean shouldKeepRunning(E entity) {
		return this.delayFinishedAt >= entity.level().getGameTime();
	}

	@Override
	protected final void tick(ServerLevel level, E entity, long gameTime) {
		super.tick(level, entity, gameTime);

		if (this.delayFinishedAt <= gameTime) {
			doDelayedAction(entity);
			this.delayedCallback.accept(entity);
		}
		entity.getNavigation().stop();
	}

	/**
	 * The action to take once the delay period has elapsed.
	 *
	 * @param entity The owner of the brain
	 */
	protected void doDelayedAction(E entity) {
	}
}
