//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.marlowcrystal.marlowcrystal.mixin;

import com.marlowcrystal.marlowcrystal.MarlowCrystal;
import net.minecraft.util.Hand;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult.Type;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientConnection.class})
public class ClientConnectionMixin {
    public ClientConnectionMixin() {
    }

    @Inject(
            method = {"send(Lnet/minecraft/network/Packet;)V"},
            at = {@At("HEAD")}
    )
    private void onPacketSend(Packet<?> packet, CallbackInfo info) {
        final MinecraftClient mc = MinecraftClient.getInstance();
        if (!MarlowCrystal.isEnabled) return;
        if (packet instanceof PlayerInteractEntityC2SPacket) {
            PlayerInteractEntityC2SPacket interactPacket = (PlayerInteractEntityC2SPacket) packet;
             if (interactPacket.getType() == PlayerInteractEntityC2SPacket.InteractionType.ATTACK) {
                HitResult hitResult = mc.crosshairTarget;
                if (hitResult != null) {
                    if (hitResult.getType() == Type.ENTITY) {
                        EntityHitResult entityHitResult = (EntityHitResult)hitResult;
                        Entity entity = entityHitResult.getEntity();
                        if (entity instanceof EndCrystalEntity) {
                            assert mc.player != null;
                            StatusEffectInstance weakness = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
                            StatusEffectInstance strength = mc.player.getStatusEffect(StatusEffects.STRENGTH);
                            if (weakness != null && (strength == null || strength.getAmplifier() <= weakness.getAmplifier()) && !ClientConnectionMixin.this.isTool(mc.player.getMainHandStack())) {
                                return;
                            }

                            entity.kill();
                        }
                    }
                }
            }
        }

    }

    private boolean isTool(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ToolItem && !(itemStack.getItem() instanceof HoeItem)) {
            ToolMaterial material = ((ToolItem)itemStack.getItem()).getMaterial();
            return material == ToolMaterials.DIAMOND || material == ToolMaterials.NETHERITE;
        } else {
            return false;
        }
    }
}
