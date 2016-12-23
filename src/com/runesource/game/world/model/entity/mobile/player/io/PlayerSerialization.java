package com.runesource.game.world.model.entity.mobile.player.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.runesource.game.world.Position;
import com.runesource.game.world.model.entity.mobile.player.Player;

public final class PlayerSerialization {

	private static final int EXISTING_ACCOUNT = 0;
	
	private static final int NEW_ACCOUNT = 1;
	
	private static final int INVALID_PASSWORD = 2;
	
	public static void serialize(Player player) {
		if (player == null) {
			return;
		}
		try {
			File file = new File("./data/characters/" + player.getUsername().toLowerCase() + ".dat");
			if (file.exists()) {
				file.createNewFile();
			}
			DataOutputStream writer = new DataOutputStream(new FileOutputStream(file));
			writer.writeUTF(player.getUsername());
			writer.writeUTF(player.getPassword());
			writer.writeInt(player.getPosition().getX());
			writer.writeInt(player.getPosition().getY());
			writer.writeInt(player.getPosition().getZ());
			for (int index = 0; index < player.getAppearance().toArray().length; index++) {
				writer.writeInt(player.getAppearance().toArray()[index]);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int unserialize(Player player) {
		try {
			File file = new File("./data/characters/" + player.getUsername().toLowerCase() + ".dat");
			if (!file.exists()) {
				return NEW_ACCOUNT;
			}
			DataInputStream reader = new DataInputStream(new FileInputStream(file));
			reader.readUTF();
			if (!reader.readUTF().toString().equals(player.getPassword())) {
				reader.close();
				return INVALID_PASSWORD;
			}
			player.getPosition().setAs(new Position(reader.readInt(), reader.readInt(), reader.readInt()));
			for (int index = 0; index < player.getAppearance().toArray().length; index++) {
				player.getAppearance().set(index, reader.readInt());
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EXISTING_ACCOUNT;
	}
}
