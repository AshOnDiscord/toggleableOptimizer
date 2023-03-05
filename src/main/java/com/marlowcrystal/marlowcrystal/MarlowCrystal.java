//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.marlowcrystal.marlowcrystal;

import java.util.HashMap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import org.lwjgl.glfw.GLFW;
import net.minecraft.text.LiteralText;

@Environment(EnvType.CLIENT)
public class MarlowCrystal implements ClientModInitializer {
	public static final int delay = 0;
	public static final HashMap<Entity, Integer> toKill = new HashMap<>();
	private static KeyBinding keyBinding;
	public static boolean isEnabled = false;

	public MarlowCrystal() {
	}

	public void onInitializeClient() {
		toKill.clear();
		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.marlowcrystal.toggle", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_B, // The keycode of the key
				"category.marlowcrystal.optimizer" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (keyBinding.wasPressed()) {
				assert client.player != null;
				isEnabled = !isEnabled;
				String stateStr = isEnabled ? "enabled" : "disabled";
				client.player.sendMessage(new LiteralText("Key was pressed! Optimizer is now " + stateStr), false);
			}
		});
	}

}
