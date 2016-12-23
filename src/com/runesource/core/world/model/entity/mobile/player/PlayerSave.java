package com.runesource.core.world.model.entity.mobile.player;
/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.StringTokenizer;

import com.runesource.core.world.Position;

/**
 * Static utility methods for saving and loading players.
 * 
 * @author blakeman8192
 */
public class PlayerSave {

	/** The directory where players are saved. */
	public static final String directory = "./data/characters/";

	/**
	 * Saves the player.
	 * 
	 * @param player
	 *            the player to save
	 * @return
	 */
	public static void save(Player player) throws Exception {
		File file = new File(directory + player.getUsername() + ".txt");
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		// Player base attributes.
		writer.write("[username]: " + player.getUsername());
		writer.newLine();
		writer.write("[password]: " + player.getPassword());
		writer.newLine();
		writer.write("[staffRights]: " + player.getStaffRights());
		writer.newLine();
		writer.write("[position]: " + player.getPosition().getX() + " " + player.getPosition().getY() + " " + player.getPosition().getZ());
		writer.newLine();
		writer.write("[gender]: " + player.getAppearance().toArray()[PlayerAppearance.GENDER]);
		writer.newLine();

		// Player appearance.
		writer.write("[appearance]:");
		writer.newLine();
		for (int i = 0; i < player.getAppearance().toArray().length; i++) {
			writer.write("" + player.getAppearance().toArray()[i]);
			writer.newLine();
		}
		writer.newLine();
		writer.flush();
		writer.close();
	}

	/**
	 * Loads the player (and sets the loaded attributes).
	 * 
	 * @param player
	 *            the player to load.
	 * @return 0 for success, 1 if the player does not have a saved game, 2 for
	 *         invalid username/password
	 */
	public static int load(Player player) throws Exception {
		File file = new File(directory + player.getUsername() + ".txt");
		if (!file.exists()) {
			return 1;
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		reader.readLine().substring(10); // Username.

		// Check the password.
		String password = reader.readLine().substring(12);
		if (!player.getPassword().equals(password)) {
			reader.close();
			return 2;
		}

		// Load the staff rights.
		int staffRights = Integer.parseInt(reader.readLine().substring(15));
		player.setStaffRights(staffRights);

		// Load the position.
		StringTokenizer t = new StringTokenizer(reader.readLine());
		t.nextToken(); // Skip "position: "
		int x = Integer.parseInt(t.nextToken());
		int y = Integer.parseInt(t.nextToken());
		int z = Integer.parseInt(t.nextToken());
		player.getPosition().setAs(new Position(x, y, z));

		// Load the gender.
		int gender = Integer.parseInt(reader.readLine().substring(10));
		reader.readLine(); // Break line.
		player.getAppearance().set(PlayerAppearance.GENDER, gender);		
		reader.readLine(); // Break line.
		for (int i = 0; i < player.getAppearance().toArray().length; i++) {
			int id = Integer.parseInt(reader.readLine());
			player.getAppearance().set(i, id);
		}
		reader.readLine(); // Break line.
		reader.close();
		return 0;
	}

}
