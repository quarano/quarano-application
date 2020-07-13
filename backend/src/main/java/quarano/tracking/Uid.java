package quarano.tracking;

import java.nio.ByteBuffer;

public class Uid {

	public static void main(String[] args) {
		java.util.UUID randomUuid = java.util.UUID.randomUUID();
		long leastSignificantBits = randomUuid.getLeastSignificantBits();
		long mostSignificantBits = randomUuid.getMostSignificantBits();
		byte[] bytes = longToBytes(leastSignificantBits, mostSignificantBits);
		String uuid = Base32.encode(bytes, 6);
		System.out.println(uuid);
	}

	public static byte[] longToBytes(long x, long y) {

		ByteBuffer buffer = ByteBuffer.allocate(2 * Long.SIZE / 8);
		buffer.putLong(x);
		buffer.putLong(y);
		return buffer.array();
	}
}
