package com.runesource.core.network.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.DefaultByteBufHolder;
import io.netty.buffer.PooledByteBufAllocator;

public class ProtocolBuffer extends DefaultByteBufHolder {

	public static final ByteBufAllocator ALLOC = PooledByteBufAllocator.DEFAULT;

	private static final int[] BIT_MASK = new int[32];

	private final ByteBuf buf;

	private final int opcode;

	private final PacketHeader type;

	private int bitIndex = -1;

	static {
		for (int i = 0; i < BIT_MASK.length; i++) {
			BIT_MASK[i] = (1 << i) - 1;
		}
	}
	
	public ProtocolBuffer() {
		this(ALLOC.buffer(128), -1, PacketHeader.RAW);
	}

	public ProtocolBuffer(int opcode) {
		this(opcode, PacketHeader.FIXED);
	}

	public ProtocolBuffer(ByteBuf buf) {
		this(buf, -1, PacketHeader.RAW);
	}

	public ProtocolBuffer(int opcode, PacketHeader type) {
		this(ALLOC.buffer(128), opcode, type);
	}
	
	private ProtocolBuffer(ByteBuf buf, int opcode, PacketHeader type) {
		super(buf);
		this.buf = buf;
		this.opcode = opcode;
		this.type = type;
	}

	public void startBitAccess() {
		// checkState(bitIndex == -1, "this ByteMessage instance is already in
		// bit access mode");

		bitIndex = buf.writerIndex() << 3;
	}

	public void endBitAccess() {
		// checkState(bitIndex != -1, "this ByteMessage instance is not in bit
		// access mode");

		buf.writerIndex((bitIndex + 7) >> 3);
		bitIndex = -1;
	}

	public ProtocolBuffer writeBytes(ByteBuf from) {
		for (int i = 0; i < from.writerIndex(); i++) {
			writeByte(from.getByte(i));
		}
		return this;
	}

	public ProtocolBuffer writeBytes(ProtocolBuffer from) {
		return writeBytes(from.getInternal());
	}

	public ProtocolBuffer writeBytes(byte[] from) {
		buf.writeBytes(from, 0, from.length);
		return this;
	}

	public ProtocolBuffer writeBytesReverse(byte[] data) {
		for (int i = data.length - 1; i >= 0; i--) {
			writeByte(data[i]);
		}
		return this;
	}

	public ProtocolBuffer writeBits(int amount, int value) {
		// checkState(amount >= 1 || amount <= 32, "Number of bits must be
		// between 1 and 32 inclusive.");

		int bytePos = bitIndex >> 3;
		int bitOffset = 8 - (bitIndex & 7);
		bitIndex = bitIndex + amount;
		int requiredSpace = bytePos - buf.writerIndex() + 1;
		requiredSpace += (amount + 7) / 8;
		if (buf.writableBytes() < requiredSpace) {
			buf.capacity(buf.capacity() + requiredSpace);
		}
		for (; amount > bitOffset; bitOffset = 8) {
			byte tmp = buf.getByte(bytePos);
			tmp &= ~BIT_MASK[bitOffset];
			tmp |= (value >> (amount - bitOffset)) & BIT_MASK[bitOffset];
			buf.setByte(bytePos++, tmp);
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			byte tmp = buf.getByte(bytePos);
			tmp &= ~BIT_MASK[bitOffset];
			tmp |= value & BIT_MASK[bitOffset];
			buf.setByte(bytePos, tmp);
		} else {
			byte tmp = buf.getByte(bytePos);
			tmp &= ~(BIT_MASK[amount] << (bitOffset - amount));
			tmp |= (value & BIT_MASK[amount]) << (bitOffset - amount);
			buf.setByte(bytePos, tmp);
		}
		return this;
	}

	public ProtocolBuffer writeBit(boolean flag) {
		writeBits(1, flag ? 1 : 0);
		return this;
	}

	public ProtocolBuffer writeByte(int value, ByteModification modification) {
		switch (modification) {
		case ADDITION:
			value += 128;
			break;
		case NEGATED:
			value = -value;
			break;
		case S:
			value = 128 - value;
			break;
		case NORMAL:
			break;
		}
		buf.writeByte((byte) value);
		return this;
	}

	public ProtocolBuffer writeByte(int value) {
		writeByte(value, ByteModification.NORMAL);
		return this;
	}

	public ProtocolBuffer writeShort(int value, ByteModification modification, ByteOrder order) {
		switch (order) {
		case BIG:
			writeByte(value >> 8);
			writeByte(value, modification);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("Middle-endian short is impossible.");
		case INVERSE_MIDDLE_ENDIAN:
			throw new UnsupportedOperationException("Inversed-middle-endian short is impossible.");
		case LITTLE_ENDIAN:
			writeByte(value, modification);
			writeByte(value >> 8);
			break;
		}
		return this;
	}

	public ProtocolBuffer writeShort(int value) {
		writeShort(value, ByteModification.NORMAL, ByteOrder.BIG);
		return this;
	}

	public ProtocolBuffer writeShort(int value, ByteModification modification) {
		writeShort(value, modification, ByteOrder.BIG);
		return this;
	}

	public ProtocolBuffer writeShort(int value, ByteOrder order) {
		writeShort(value, ByteModification.NORMAL, order);
		return this;
	}

	public ProtocolBuffer writeInt(int value, ByteModification modification, ByteOrder order) {
		switch (order) {
		case BIG:
			writeByte(value >> 24);
			writeByte(value >> 16);
			writeByte(value >> 8);
			writeByte(value, modification);
			break;
		case MIDDLE:
			writeByte(value >> 8);
			writeByte(value, modification);
			writeByte(value >> 24);
			writeByte(value >> 16);
			break;
		case INVERSE_MIDDLE_ENDIAN:
			writeByte(value >> 16);
			writeByte(value >> 24);
			writeByte(value, modification);
			writeByte(value >> 8);
			break;
		case LITTLE_ENDIAN:
			writeByte(value, modification);
			writeByte(value >> 8);
			writeByte(value >> 16);
			writeByte(value >> 24);
			break;
		}
		return this;
	}

	public ProtocolBuffer writeInt(int value) {
		writeInt(value, ByteModification.NORMAL, ByteOrder.BIG);
		return this;
	}

	public ProtocolBuffer writeInt(int value, ByteModification modification) {
		writeInt(value, modification, ByteOrder.BIG);
		return this;
	}

	public ProtocolBuffer writeInt(int value, ByteOrder order) {
		writeInt(value, ByteModification.NORMAL, order);
		return this;
	}

	public ProtocolBuffer writeLong(long value, ByteModification modification, ByteOrder order) {
		switch (order) {
		case BIG:
			writeByte((int) (value >> 56));
			writeByte((int) (value >> 48));
			writeByte((int) (value >> 40));
			writeByte((int) (value >> 32));
			writeByte((int) (value >> 24));
			writeByte((int) (value >> 16));
			writeByte((int) (value >> 8));
			writeByte((int) value, modification);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("Middle-endian long is not implemented!");
		case INVERSE_MIDDLE_ENDIAN:
			throw new UnsupportedOperationException("Inverse-middle-endian long is not implemented!");
		case LITTLE_ENDIAN:
			writeByte((int) value, modification);
			writeByte((int) (value >> 8));
			writeByte((int) (value >> 16));
			writeByte((int) (value >> 24));
			writeByte((int) (value >> 32));
			writeByte((int) (value >> 40));
			writeByte((int) (value >> 48));
			writeByte((int) (value >> 56));
			break;
		}
		return this;
	}

	public ProtocolBuffer writeLong(long value) {
		writeLong(value, ByteModification.NORMAL, ByteOrder.BIG);
		return this;
	}

	public ProtocolBuffer writeLong(long value, ByteModification modification) {
		writeLong(value, modification, ByteOrder.BIG);
		return this;
	}

	public ProtocolBuffer writeLong(long value, ByteOrder order) {
		writeLong(value, ByteModification.NORMAL, order);
		return this;
	}

	public ProtocolBuffer writeString(String string) {
		for (byte value : string.getBytes()) {
			writeByte(value);
		}
		writeByte(10);
		return this;
	}

	public int readByte(boolean signed, ByteModification modification) {
		int value = buf.readByte();
		switch (modification) {
		case ADDITION:
			value = value - 128;
			break;
		case NEGATED:
			value = -value;
			break;
		case S:
			value = 128 - value;
			break;
		case NORMAL:
			break;
		}
		return signed ? value : value & 0xff;
	}

	public int readByte() {
		return readByte(true, ByteModification.NORMAL);
	}

	public int readByte(boolean signed) {
		return readByte(signed, ByteModification.NORMAL);
	}

	public int readByte(ByteModification modification) {
		return readByte(true, modification);
	}

	public int readShort(boolean signed, ByteModification modification, ByteOrder order) {
		int value = 0;
		switch (order) {
		case BIG:
			value |= readByte(false) << 8;
			value |= readByte(false, modification);
			break;
		case MIDDLE:
			throw new UnsupportedOperationException("Middle-endian short is impossible!");
		case INVERSE_MIDDLE_ENDIAN:
			throw new UnsupportedOperationException("Inverse-middle-endian short is impossible!");
		case LITTLE_ENDIAN:
			value |= readByte(false, modification);
			value |= readByte(false) << 8;
			break;
		}
		return signed ? value : value & 0xffff;
	}

	public int readShort() {
		return readShort(true, ByteModification.NORMAL, ByteOrder.BIG);
	}

	public int readShort(boolean signed) {
		return readShort(signed, ByteModification.NORMAL, ByteOrder.BIG);
	}

	public int readShort(ByteModification modification) {
		return readShort(true, modification, ByteOrder.BIG);
	}

	public int readShort(boolean signed, ByteModification modification) {
		return readShort(signed, modification, ByteOrder.BIG);
	}

	public int readShort(ByteOrder order) {
		return readShort(true, ByteModification.NORMAL, order);
	}

	public int readShort(boolean signed, ByteOrder order) {
		return readShort(signed, ByteModification.NORMAL, order);
	}

	public int readShort(ByteModification modification, ByteOrder order) {
		return readShort(true, modification, order);
	}

	public int readInt(boolean signed, ByteModification modification, ByteOrder order) {
		long value = 0;
		switch (order) {
		case BIG:
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			value |= readByte(false) << 8;
			value |= readByte(false, modification);
			break;
		case MIDDLE:
			value |= readByte(false) << 8;
			value |= readByte(false, modification);
			value |= readByte(false) << 24;
			value |= readByte(false) << 16;
			break;
		case INVERSE_MIDDLE_ENDIAN:
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			value |= readByte(false, modification);
			value |= readByte(false) << 8;
			break;
		case LITTLE_ENDIAN:
			value |= readByte(false, modification);
			value |= readByte(false) << 8;
			value |= readByte(false) << 16;
			value |= readByte(false) << 24;
			break;
		}
		return (int) (signed ? value : value & 0xffffffffL);
	}

	public int readInt() {
		return readInt(true, ByteModification.NORMAL, ByteOrder.BIG);
	}

	public int readInt(boolean signed) {
		return readInt(signed, ByteModification.NORMAL, ByteOrder.BIG);
	}

	public int readInt(ByteModification modification) {
		return readInt(true, modification, ByteOrder.BIG);
	}

	public int readInt(boolean signed, ByteModification modification) {
		return readInt(signed, modification, ByteOrder.BIG);
	}

	public int readInt(ByteOrder order) {
		return readInt(true, ByteModification.NORMAL, order);
	}

	public int readInt(boolean signed, ByteOrder order) {
		return readInt(signed, ByteModification.NORMAL, order);
	}

	public int readInt(ByteModification modification, ByteOrder order) {
		return readInt(true, modification, order);
	}

	public long readLong(ByteModification modification, ByteOrder order) {
		long value = 0;
		switch (order) {
		case BIG:
			value |= (long) readByte(false) << 56L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 8L;
			value |= readByte(false, modification);
			break;
		case INVERSE_MIDDLE_ENDIAN:
		case MIDDLE:
			throw new UnsupportedOperationException("Middle and inverse-middle value types not supported!");
		case LITTLE_ENDIAN:
			value |= readByte(false, modification);
			value |= (long) readByte(false) << 8L;
			value |= (long) readByte(false) << 16L;
			value |= (long) readByte(false) << 24L;
			value |= (long) readByte(false) << 32L;
			value |= (long) readByte(false) << 40L;
			value |= (long) readByte(false) << 48L;
			value |= (long) readByte(false) << 56L;
			break;
		}
		return value;
	}

	public long readLong() {
		return readLong(ByteModification.NORMAL, ByteOrder.BIG);
	}

	public long readLong(ByteModification modification) {
		return readLong(modification, ByteOrder.BIG);
	}

	public long readLong(ByteOrder order) {
		return readLong(ByteModification.NORMAL, order);
	}

	public String readString() {
		byte temp;
		StringBuilder b = new StringBuilder();
		while ((temp = (byte) readByte()) != 10) {
			b.append((char) temp);
		}
		return b.toString();
	}

	public byte[] readBytes(int amount) {
		return readBytes(amount, ByteModification.NORMAL);
	}

	public byte[] readBytes(int amount, ByteModification modification) {
		byte[] data = new byte[amount];
		for (int i = 0; i < amount; i++) {
			data[i] = (byte) readByte(modification);
		}
		return data;
	}

	public byte[] readBytesReverse(int amount, ByteModification modification) {
		byte[] data = new byte[amount];
		int dataPosition = 0;
		for (int i = buf.readerIndex() + amount - 1; i >= buf.readerIndex(); i--) {
			int value = buf.getByte(i);
			switch (modification) {
			case ADDITION:
				value -= 128;
				break;
			case NEGATED:
				value = -value;
				break;
			case S:
				value = 128 - value;
				break;
			case NORMAL:
				break;
			}
			data[dataPosition++] = (byte) value;
		}
		return data;
	}

	public ByteBuf getInternal() {
		return buf;
	}

	public int getOpcode() {
		return opcode;
	}

	public PacketHeader getPacketHeader() {
		return type;
	}

}
