package online.kingdomkeys.kingdomkeys.entity.organization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class ArrowRainCoreEntity extends ThrowableEntity {

	int maxTicks = 240;
	float dmg;

	double dmgMult;
	float radius;
	float space;

	public ArrowRainCoreEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public ArrowRainCoreEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_ARROW_RAIN.get(), world);
	}

	public ArrowRainCoreEntity(World world) {
		super(ModEntities.TYPE_ARROW_RAIN.get(), world);
		this.preventEntitySpawning = true;
	}

	public ArrowRainCoreEntity(World world, PlayerEntity player, LivingEntity target, float dmg) {
		super(ModEntities.TYPE_ARROW_RAIN.get(), player, world);
		setCaster(player.getUniqueID());
		setTarget(target.getUniqueID());
		this.dmg = dmg;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks || getCaster() == null) {
			this.remove();
		}
		
		this.setMotion(0, 0, 0);
		this.velocityChanged = true;

		this.dmgMult = ModConfigs.limitArrowRainMult;

		// world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(),
		// getPosZ(), 1, 1, 0);
		world.addParticle(ParticleTypes.BUBBLE, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		double X = getPosX();
		double Y = getPosY();
		double Z = getPosZ();

		if (getCaster() != null) {
			if (ticksExisted == 1) {
				LaserDomeShotEntity bullet = new LaserDomeShotEntity(world, getCaster(), dmg * dmgMult);
				bullet.setPosition(X, Y, Z);
				bullet.setMaxTicks(30);
				bullet.shoot(0, 255, 0, 1f, 0);
				world.addEntity(bullet);
				world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 0.6F);

			} else if (ticksExisted > 40 && ticksExisted % 2 == 0) { // Get all targets right before starting to shoot
				radius = Math.min((ticksExisted-34) / 10F, 20);
				space = 20 + 6 - radius;
				for (int s = 1; s < 360; s += space) {
					double x = X + (radius * Math.cos(Math.toRadians(s)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)));
					LaserDomeShotEntity bullet = new LaserDomeShotEntity(world, getCaster(), dmg * dmgMult);
					bullet.setPosition(X, Y + 27, Z);
					bullet.setMaxTicks(maxTicks);
					bullet.shoot(x - bullet.getPosX(), this.getPosY() - bullet.getPosY()+1, z - bullet.getPosZ(), 2.5f, 0);
					//list.add(bullet);
					world.addEntity(bullet);
				}
				world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 1F);

				
			}/* else if (ticksExisted > 40 && !targetList.isEmpty()) {
				if (ticksExisted == 80) {
					updateList();
				}

				for (int i = 0; i < shotsPerTick; i++) {
					int num;
					do {
						num = rand.nextInt(list.size());
					} while (usedIndexes.contains(num) && usedIndexes.size() != list.size());
					usedIndexes.add(num);

					Entity target = this;
					int targetIndex = rand.nextInt(targetList.size());
					target = targetList.get(targetIndex);

					if (target != null && target.isAlive() && getCaster() != null) {
						LaserDomeShotEntity bullet = list.get(num);
						bullet.shoot(target.getPosX() - bullet.getPosX(), target.getPosY() - bullet.getPosY(), target.getPosZ() - bullet.getPosZ(), 2f, 0);
						world.playSound(getCaster(), getCaster().getPosition(), ModSounds.laser.get(), SoundCategory.PLAYERS, 1F, 1F);
					}

				}
			}*/
		}
		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		remove();
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		if (this.dataManager.get(OWNER) != null) {
			compound.putString("OwnerUUID", this.dataManager.get(OWNER).get().toString());
			compound.putString("TargetUUID", this.dataManager.get(TARGET).get().toString());
		}
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.dataManager.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
		this.dataManager.set(TARGET, Optional.of(UUID.fromString(compound.getString("TargetUUID"))));
	}

	private static final DataParameter<Optional<UUID>> OWNER = EntityDataManager.createKey(ArrowRainCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	private static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(ArrowRainCoreEntity.class, DataSerializers.OPTIONAL_UNIQUE_ID);

	public PlayerEntity getCaster() {
		return this.getDataManager().get(OWNER).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(OWNER).get()) : null;
	}

	public void setCaster(UUID uuid) {
		this.dataManager.set(OWNER, Optional.of(uuid));
	}

	public PlayerEntity getTarget() {
		return this.getDataManager().get(TARGET).isPresent() ? this.world.getPlayerByUuid(this.getDataManager().get(TARGET).get()) : null;
	}

	public void setTarget(UUID uuid) {
		this.dataManager.set(TARGET, Optional.of(uuid));
	}

	@Override
	protected void registerData() {
		this.dataManager.register(OWNER, Optional.of(new UUID(0L, 0L)));
		this.dataManager.register(TARGET, Optional.of(new UUID(0L, 0L)));
	}
}
