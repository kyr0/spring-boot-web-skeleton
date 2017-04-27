package com.mad2man.sbweb.util.hash;

public abstract class BaseHash {
    /**
     * characters of the hex-decimal number notation
     */
    private static final char hex[] = new char[]
            {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };

    protected BaseHash() {
    }

    /**
     * convert array containing hashed data into hexadecimal string.
     *
     * @param digest array containing hash
     * @return hex-decimal hash representation
     */
    public static String convertDigestToString(final byte digest[]) {
        char[] val = new char[2 * digest.length];
        for (int i = 0; i < digest.length; i++) {
            int b = digest[i] & 0xff;
            val[2 * i] = hex[b >>> 4];
            val[2 * i + 1] = hex[b & 15];
        }
        return String.valueOf(val);
    }

    /**
     * ensure all internal data structures are prepared for hashing task remove potential interim hashing data
     */
    protected abstract void reset();

    /**
     * perform hashing on incoming byte array.
     * Hashing should be only performed on non-empty inputs,
     * exception should be thrown otherwise. Background:
     * we must ensure that error-prone input is bounced.
     *
     * @param input bytes to be hashed
     * @return hashed data
     * @throws IllegalArgumentException MUST BE THROWN
     *                                  if input data cannot be used to generate distinct hash
     */
    public abstract byte[] hash(final byte input[]) throws IllegalArgumentException;

    /**
     * perform hashing on incoming string.
     * Hashing should be only performed on non-empty inputs,
     * exception should be thrown otherwise. Background:
     * we must ensure that error-prone input is bounced.
     *
     * @param input text to be hashed
     * @return hashed data
     * @throws IllegalArgumentException MUST BE THROWN
     *                                  if input data cannot be used to generate distinct hash
     */
    public abstract byte[] hash(final String input) throws IllegalArgumentException;

    /**
     * performs hashing and returns the hash as String instead of byte array.
     * Hashing should be only performed on non-empty inputs,
     * exception should be thrown otherwise. Background:
     * we must ensure that error-prone input is bounced.
     *
     * @param input text to be hashed
     * @return hexadecimal presentation of the hash
     * @throws IllegalArgumentException MUST BE THROWN
     *                                  if input data cannot be used to generate distinct hash
     */
    public final String hashAsString(final String input) throws IllegalArgumentException {
        return BaseHash.convertDigestToString(this.hash(input));
    }
}
