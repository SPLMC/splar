
package jdd.util;

import java.io.*;

/** A bit stream, used by jdd.util.zip classes ??*/

public class BitStream {
	private InputStream rd;
	private int read, pos, bytes, ones, zeros;
	private boolean fail;
	public BitStream(InputStream rd) {
		this.rd = rd;
		this.pos = 0;
		this.fail = false;
		this.bytes = this.ones = this.zeros = 0;
	}

	/** how many bytes have we seen so far? */

	public int bytesRead() { return bytes; }
	public int onesRead() { return ones; }
	public int zerosRead() { return zeros; }

	public boolean next() {
		if(fail) return false;


		if(pos == 0) {
			pos = 128;
			try {
				// if(!rd.ready()) JDDConsole.out.println("NOT READY");
				bytes++;
				read = rd.read();
				if(read == -1) {
					JDDConsole.out.println("END of stream reached!");
					fail = true;
					return false;
				}
			} catch(IOException exx) {
				exx.printStackTrace();
				fail = true;
				return false;

			}
		}


		boolean ret = (read & pos) == 0 ? false: true;
		pos >>= 1;
		if(ret) ones++; else zeros++;
		return ret;
	}

	public void skipByte() {
		pos = 0;
	}
}
