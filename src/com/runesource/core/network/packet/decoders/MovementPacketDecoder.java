package com.runesource.core.network.packet.decoders;

import com.runesource.core.network.packet.Packet;
import com.runesource.core.network.buffer.ByteModification;
import com.runesource.core.network.buffer.ByteOrder;
import com.runesource.core.network.buffer.ProtocolBuffer;
import com.runesource.core.network.packet.PacketDecoder;
import com.runesource.core.world.Position;
import com.runesource.core.world.model.entity.mobile.player.Player;

public final class MovementPacketDecoder implements PacketDecoder {

	@Override
	public void execute(Player player, Packet packet) {
		ProtocolBuffer buffer = new ProtocolBuffer(packet.getPayload());
		switch (packet.getOpcode()) {
		case 248: // Movement.
		case 164: // ^
		case 98: // ^
			int length = packet.getPayload().readableBytes();
			if (packet.getOpcode() == 248) {
				length -= 14;
			}
			int steps = (length - 5) / 2;
			int[][] path = new int[steps][2];
			int firstStepX = buffer.readShort(ByteModification.ADDITION, ByteOrder.LITTLE_ENDIAN);
			for (int i = 0; i < steps; i++) {
				path[i][0] = buffer.readByte();
				path[i][1] = buffer.readByte();
			}
			int firstStepY = buffer.readShort(ByteOrder.LITTLE_ENDIAN);

			player.getMovementHandler().reset();
			player.getMovementHandler().setRunPath(buffer.readByte(ByteModification.NEGATED) == 1);
			player.getMovementHandler().addToPath(new Position(firstStepX, firstStepY));
			for (int i = 0; i < steps; i++) {
				path[i][0] += firstStepX;
				path[i][1] += firstStepY;
				player.getMovementHandler().addToPath(new Position(path[i][0], path[i][1]));
			}
			player.getMovementHandler().finish();
			break;
		}
	}

}
