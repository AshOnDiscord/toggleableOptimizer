//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.marlowcrystal.marlowcrystal.mixin;

import com.marlowcrystal.marlowcrystal.MarlowCrystal;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity.RemovalReason;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftClient.class})
public class MinecraftClientMixin {
	public MinecraftClientMixin() {
	}

	@Inject(
			method = {"tick"},
			at = {@At("HEAD")}
	)
	private void onPreTick(CallbackInfo info) {
		Iterator<Map.Entry<Entity, Integer>> iterator = MarlowCrystal.toKill.entrySet().iterator();

		while(iterator.hasNext()) {
			Map.Entry<Entity, Integer> entry = iterator.next();
			Entity entity = (Entity)entry.getKey();
			int delay = (Integer)entry.getValue() - 1;
			if (delay == 0) {
				iterator.remove();
				if (!entity.isAlive()) {
					entity.kill();
					entity.setRemoved(RemovalReason.KILLED);
					entity.onRemoved();
				}
			} else {
				entry.setValue(delay);
			}
		}

	}
}
