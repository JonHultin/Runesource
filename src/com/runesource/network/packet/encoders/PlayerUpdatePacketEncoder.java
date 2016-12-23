package com.runesource.network.packet.encoders;

import java.util.Iterator;

import com.runesource.game.world.Position;
import com.runesource.game.world.World;
import com.runesource.game.world.model.entity.mobile.player.Player;
import com.runesource.game.world.model.entity.mobile.player.PlayerAppearance;
import com.runesource.network.buffer.ByteModification;
import com.runesource.network.buffer.ByteOrder;
import com.runesource.network.buffer.PacketHeader;
import com.runesource.network.buffer.ProtocolBuffer;
import com.runesource.network.packet.PacketEncoder;
import com.runesource.util.Misc;

public final class PlayerUpdatePacketEncoder implements PacketEncoder {

	@Override
	public ProtocolBuffer dispatch(Player player) {
		ProtocolBuffer buffer = new ProtocolBuffer(81, PacketHeader.VARIABLE_SHORT);
		ProtocolBuffer block = new ProtocolBuffer();
		try {
			buffer.startBitAccess();
			updateMovement(player, buffer);
			if (player.isUpdateRequired()) {
				updateState(player, block, false, true);
			}
			updateLocal(player, buffer, block);
			if (block.getInternal().writerIndex() > 0) {
				buffer.writeBits(11, 2047);
				buffer.endBitAccess();
				buffer.writeBytes(block.getInternal());
			} else {
				buffer.endBitAccess();
			}
		} catch (Exception e) {
			buffer.release();
			e.printStackTrace();
		} finally {
			block.release();
		}
		return buffer;
	}

	private void updateState(Player player, ProtocolBuffer block, boolean forceAppearance, boolean noChat) {
		int mask = 0x0;
		if (player.isChatUpdateRequired() && !noChat) {
			mask |= 0x80;
		}
		if (player.isAppearanceUpdateRequired() || forceAppearance) {
			mask |= 0x10;
		}
		if (player.isForcedMovementRequired()) {
			mask |= 0x400;
		}
		if (mask >= 0x100) {
			mask |= 0x40;
			block.writeShort(mask, ByteOrder.LITTLE_ENDIAN);
		} else {
			block.writeByte(mask);
		}
		if (player.isChatUpdateRequired() && !noChat) {
			updateChat(player, block);
		}
		if (player.isForcedMovementRequired()) {
			Position current = player.getPosition();
			Position to = new Position(3222, 3222);
			block.writeByte(current.getX(), ByteModification.S);
			block.writeByte(current.getY(), ByteModification.S);
			block.writeByte(to.getX(), ByteModification.S);
			block.writeByte(to.getY(), ByteModification.S);
			block.writeShort(1, ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
			block.writeShort(1, ByteModification.ADDITION);
			block.writeByte(0, ByteModification.S);
			System.out.println("moved");
		}
		if (player.isAppearanceUpdateRequired() || forceAppearance) {
			updateAppearance(player, block);
		}
	}
	
	private void updateMovement(Player player, ProtocolBuffer out) {
		boolean updateRequired = player.isUpdateRequired();
		if (player.needsPlacement()) {
			out.writeBit(true);
			out.writeBits(2, 3);
			out.writeBits(2, player.getPosition().getZ());
			out.writeBit(player.isResetMovementQueue());
			out.writeBit(player.isUpdateRequired());
			out.writeBits(7, player.getPosition().getLocalY(player.getRegionManager().getPosition()));
			out.writeBits(7, player.getPosition().getLocalX(player.getRegionManager().getPosition()));
		} else {
			if (player.getWalkingDirection() != -1) { 
				out.writeBit(true); 
				if (player.getRunningDirection() != -1) { 
					out.writeBits(2, 2);
					out.writeBits(3, player.getWalkingDirection());
					out.writeBits(3, player.getRunningDirection());
					out.writeBit(player.isUpdateRequired());
				} else { 
					out.writeBits(2, 1);
					out.writeBits(3, player.getWalkingDirection());
					out.writeBit(player.isUpdateRequired());
				}
			} else { 
				if (updateRequired) { 
					out.writeBit(true); 
					out.writeBits(2, 0);
				} else { 
					out.writeBit(false);
				}
			}
		}
	}
	
	private void updateLocal(Player player, ProtocolBuffer out, ProtocolBuffer block) {
		out.writeBits(8, player.getRegionManager().getPlayers().size());
		for (Iterator<Player> i = player.getRegionManager().getPlayers().iterator(); i.hasNext();) {
			Player other = i.next();
			if (other.getPosition().isViewableFrom(player.getPosition()) && !other.needsPlacement() && other.getChannel().isActive()) {
				updateLocalMovement(other, out);
				if (other.isUpdateRequired()) {
					updateState(other, block, false, false);
				}
			} else {
				out.writeBit(true);
				out.writeBits(2, 3);
				i.remove();
			}
		}
		for (Player other : World.getSingleton().getPlayers()) {
			if (player.getRegionManager().getPlayers().size() >= 255) {
				break;
			}
			if (other == null || other == player) {
				continue;
			}
			if (!player.getRegionManager().getPlayers().contains(other) && other.getPosition().isViewableFrom(player.getPosition())) {
				player.getRegionManager().add(other);
				out.writeBits(11, other.getIndex());
				out.writeBit(true); 
				out.writeBit(true); 
				Position delta = Misc.delta(player.getPosition(), other.getPosition());
				out.writeBits(5, delta.getY());
				out.writeBits(5, delta.getX());
				updateState(other, block, true, false);
			}
		}
	}
	
	private void updateLocalMovement(Player player, ProtocolBuffer out) {
		boolean updateRequired = player.isUpdateRequired();
		if (player.getWalkingDirection() != -1) {
			out.writeBit(true); 
			if (player.getRunningDirection() != -1) {
				out.writeBits(2, 2);
				out.writeBits(3, player.getWalkingDirection());
				out.writeBits(3, player.getRunningDirection());
				out.writeBit(player.isUpdateRequired());
			} else { 
				out.writeBits(2, 1);
				out.writeBits(3, player.getWalkingDirection());
				out.writeBit(player.isUpdateRequired());
			}
		} else {
			if (updateRequired) { 
				out.writeBit(true);
				out.writeBits(2, 0);
			} else {
				out.writeBit(false);
			}
		}
	}

	private void updateChat(Player player, ProtocolBuffer block) {
		block.writeShort(((player.getChatColor() & 0xff) << 8) + (player.getChatEffects() & 0xff), ByteOrder.LITTLE_ENDIAN);
		block.writeByte(player.getStaffRights());
		block.writeByte(player.getChatText().length, ByteModification.NEGATED);
		block.writeBytesReverse(player.getChatText());
	}
	
	private void updateAppearance(Player player, ProtocolBuffer block) {
		ProtocolBuffer properties = new ProtocolBuffer();
		properties.writeByte(player.getAppearance().toArray()[PlayerAppearance.GENDER]); // Gender
		properties.writeByte(0); // Skull icon
		// Hat.
		//if (e[Misc.EQUIPMENT_SLOT_HEAD] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_HEAD]);
		//} else {
			properties.writeByte(0);
		//}

		// Cape.
		//if (e[Misc.EQUIPMENT_SLOT_CAPE] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_CAPE]);
		//} else {
			properties.writeByte(0);
		//}

		// Amulet.
		//if (e[Misc.EQUIPMENT_SLOT_AMULET] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_AMULET]);
		//} else {
			properties.writeByte(0);
		//}

		// Weapon.
		//if (e[Misc.EQUIPMENT_SLOT_WEAPON] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_WEAPON]);
		//} else {
			properties.writeByte(0);
		//}

		// Chest.
		//if (e[Misc.EQUIPMENT_SLOT_CHEST] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_CHEST]);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.TORSO]);
		//}

		// Shield.
		//if (e[Misc.EQUIPMENT_SLOT_SHIELD] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_SHIELD]);
		//} else {
			properties.writeByte(0);
		//}

		// Arms TODO: Check platebody/non-platebody.
		//if (e[Misc.EQUIPMENT_SLOT_CHEST] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_CHEST]);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.ARMS]);
		//}

		// Legs.
		//if (e[Misc.EQUIPMENT_SLOT_LEGS] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_LEGS]);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.LEGS]);
		//}

		// Head (with a hat already on).
		//if (Misc.isFullHelm(e[Misc.EQUIPMENT_SLOT_HEAD]) || Misc.isFullMask(Misc.EQUIPMENT_SLOT_HEAD)) {
			//block.writeByte(0);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.HEAD]);
		//}

		// Hands.
		//if (e[Misc.EQUIPMENT_SLOT_HANDS] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_HANDS]);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.HANDS]);
		//}

		// Feet.
		//if (e[Misc.EQUIPMENT_SLOT_FEET] > 1) {
			//block.writeShort(0x200 + e[Misc.EQUIPMENT_SLOT_FEET]);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.FEET]);
		//}

		// Beard.
		//if (Misc.isFullHelm(e[Misc.EQUIPMENT_SLOT_HEAD]) || Misc.isFullMask(Misc.EQUIPMENT_SLOT_HEAD)) {
			//block.writeByte(0);
		//} else {
			properties.writeShort(0x100 + player.getAppearance().toArray()[PlayerAppearance.BEARD]);
		//}

		// Player colors
		properties.writeByte(player.getAppearance().toArray()[PlayerAppearance.HAIR_COLOR]);
		properties.writeByte(player.getAppearance().toArray()[PlayerAppearance.TORSO_COLOR]);
		properties.writeByte(player.getAppearance().toArray()[PlayerAppearance.LEG_COLOR]);
		properties.writeByte(player.getAppearance().toArray()[PlayerAppearance.FEET_COLOR]);
		properties.writeByte(player.getAppearance().toArray()[PlayerAppearance.SKIN_COLOR]);

		// Movement animations
		properties.writeShort(0x328); // stand
		properties.writeShort(0x337); // stand turn
		properties.writeShort(0x333); // walk
		properties.writeShort(0x334); // turn 180
		properties.writeShort(0x335); // turn 90 cw
		properties.writeShort(0x336); // turn 90 ccw
		properties.writeShort(0x338); // run

		properties.writeLong(Misc.nameToLong(player.getUsername()));
		properties.writeByte(3); // Combat level.
		properties.writeShort(0); // Total level.

		// Append the block length and the block to the packet.
		block.writeByte(properties.getInternal().writerIndex(), ByteModification.NEGATED);
		block.writeBytes(properties.getInternal());
	}
}
