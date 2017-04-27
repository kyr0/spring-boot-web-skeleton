package com.mad2man.sbweb.util.hash;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;

@Deprecated

public final class SHA224 extends BaseHash {
    //<editor-fold defaultstate="collapsed" desc="Algorithm specific look-up tables.">

    /**
     * Initialize variables.
     * <p>
     * First 32 bits of the fractional parts of the square roots of the first 8 primes 2..19.
     */
    private static final int[] H =
            {
                    0xc1059ed8, 0x367cd507, 0x3070dd17, 0xf70e5939, 0xffc00b31, 0x68581511, 0x64f98fa7, 0xbefa4fa4
            };
    /**
     * Initialize table of round constants
     * <p>
     * First 32 bits of the fractional parts of the cube roots of the first 64 primes 2..311.
     */
    private static final int[] K =
            {
                    0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5, 0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
                    0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3, 0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
                    0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc, 0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
                    0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7, 0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
                    0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13, 0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
                    0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3, 0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
                    0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5, 0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
                    0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208, 0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
            };
    //</editor-fold>
    /**
     * Target buffer to receive bytes which passed hashing.
     */
    private ByteBuffer dataBuf = ByteBuffer.allocate(64);
    /**
     * Number of bytes put to hash.
     */
    private int length = 0;
    /**
     * Instance copy of the H.
     */
    private int h[] = new int[H.length];

    /**
     * Default constructor.
     */
    public SHA224() {
        super();
        this.reset();
    }

    @Override
    protected void reset() {
        this.length = 0;
        this.dataBuf.clear();
        System.arraycopy(H, 0, h, 0, H.length);
    }

    @Override
    public final byte[] hash(byte[] input) throws InvalidParameterException {
        if (null == input || input.length == 0) {
            throw new InvalidParameterException("Attempted to hash empty input");
        }
        this.engineUpdate(input, 0, input.length);
        return this.getDigest();
    }

    @Override
    public final byte[] hash(String input) throws InvalidParameterException {
        if (null == input || 0 == input.length()) {
            throw new InvalidParameterException("Attempted to hash empty input");
        }
        return this.hash(input.getBytes());
    }

    /**
     * Finalize digest and return.
     *
     * @return
     */
    private byte[] getDigest() {
        long bitLength = length * 8L;
        engineUpdate((byte) 0x80);
        while ((length + 8) % 64 != 0) {
            engineUpdate((byte) 0);
        }
        dataBuf.putLong(bitLength);
        processBuffer();

        ByteBuffer result = ByteBuffer.allocate(28);
        for (int i = 0; i < 7; i++) {
            result.putInt(h[i]);
        }

        this.reset();
        return result.array();
    }

    /**
     * Chunk processing is triggered every time the next 64 bytes of input passed to hasher.
     */
    private void processBuffer() {
        dataBuf.rewind();
        int[] w = new int[64];
        for (int i = 0; i < 16; i++) {
            w[i] = dataBuf.getInt();
        }
        dataBuf.clear();

        for (int i = 16; i < 64; i++) {
            int s0 = w[i - 15];
            s0 = ((s0 >>> 7) | (s0 << 25)) ^ ((s0 >>> 18) | (s0 << 14)) ^ (s0 >>> 3);
            int s1 = w[i - 2];
            s1 = ((s1 >>> 17) | (s1 << 15)) ^ ((s1 >>> 19) | (s1 << 13)) ^ (s1 >>> 10);
            w[i] = w[i - 16] + s0 + w[i - 7] + s1;
        }

        int a = this.h[0],
                b = this.h[1],
                c = this.h[2],
                d = this.h[3],
                e = this.h[4],
                f = this.h[5],
                g = this.h[6],
                h = this.h[7];

        for (int i = 0; i < 64; i++) {
            int s0 = ((a >>> 2) | (a << 30)) ^ ((a >>> 13) | (a << 19)) ^ ((a >>> 22) | (a << 10));
            int maj = (a & b) ^ (a & c) ^ (b & c);
            int t2 = s0 + maj;
            int s1 = ((e >>> 6) | (e << 26)) ^ ((e >>> 11) | (e << 21)) ^ ((e >>> 25) | (e << 7));
            int ch = (e & f) ^ ((~e) & g);
            int t1 = h + s1 + ch + K[i] + w[i];

            h = g;
            g = f;
            f = e;
            e = d + t1;
            d = c;
            c = b;
            b = a;
            a = t1 + t2;
        }

        this.h[0] += a;
        this.h[1] += b;
        this.h[2] += c;
        this.h[3] += d;
        this.h[4] += e;
        this.h[5] += f;
        this.h[6] += g;
        this.h[7] += h;
    }

    /**
     * TODO: add comment
     *
     * @param src
     */
    private void engineUpdate(byte src) {
        this.dataBuf.put(src);
        if (0 == ++this.length % 64) {
            processBuffer();
        }
    }

    /**
     * TODO: add comment
     *
     * @param src
     * @param offset
     * @param count
     */
    private void engineUpdate(byte[] src, int offset, int count) {
        for (int i = offset; i < offset + count; i++) {
            engineUpdate(src[i]);
        }
    }

}
