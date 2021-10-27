package io.github.pseudodistant.doublejump.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow protected abstract void jump();
    boolean hasDoubleJump = true;
    public LivingEntityMixin(World world) {
        super(world);
    }
    @Redirect(method = "tickMovement", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;onGround:Z"))
    public boolean doubleJumpRedir(LivingEntity thing) {
        System.out.println(thing.onGround || hasDoubleJump);
        if (thing.onGround) hasDoubleJump = true;
        return thing.onGround || hasDoubleJump;
    }
    @Inject(method = "tickMovement", at=@At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;jump()V"))
    public void jumpInject(CallbackInfo ci) {
        if (!this.onGround) {
            jump();
            hasDoubleJump = false;
        }
    }
}
