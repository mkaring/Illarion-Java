/*
 * This file is part of the Illarion BBIWI.
 *
 * Copyright © 2013 - Illarion e.V.
 *
 * The Illarion BBIWI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The Illarion BBIWI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the Illarion BBIWI.  If not, see <http://www.gnu.org/licenses/>.
 */
package illarion.bbiwi.login;

import illarion.common.config.Config;
import illarion.common.util.Base64;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.auth.PasswordStore;

import javax.annotation.Nonnull;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;

/**
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class PasswordStorage extends PasswordStore {
    /**
     * The logging instance of this class.
     */
    private static final Logger LOGGER = Logger.getLogger(PasswordStorage.class);

    /**
     * The configuration system that acts as persistent storage unit.
     */
    private final Config config;

    /**
     * Create a new instance of this password storage.
     *
     * @param config the configurations system used to store the password
     */
    public PasswordStorage(final Config config) {
        this.config = config;
    }

    @Override
    public char[] get(final String username, final String server) {
        final String password = config.getString("passwd-" + username);
        if ((password == null) || password.isEmpty()) {
            return new char[0];
        }
        return shufflePassword(username, password, true).toCharArray();
    }

    @Override
    public void removeUserPassword(final String username) {
        config.set("passwd-" + username, "");
    }

    @Override
    public boolean set(final String username, final String server, final char[] password) {
        config.set("passwd-" + username, shufflePassword(username, String.valueOf(password), false));
        return true;
    }

    /**
     * Encrypt or decrypt the password.
     *
     * @param username the username used to encrypted or decrypted
     * @param pw       the password to encrypted or decrypted
     * @param decode   {@code true} in case the password should be decrypted, else its encrypted
     * @return the encrypted or decrypted password
     */
    private static String shufflePassword(@Nonnull final String username, @Nonnull final String pw,
                                          final boolean decode) {
        try {
            final Charset usedCharset = Charset.forName("UTF-8");
            // creating the key
            final StringBuilder stringKeyBuilder = new StringBuilder(username);
            while (stringKeyBuilder.length() < 9) {
                stringKeyBuilder.append(username);
            }
            final KeySpec keySpec = new DESKeySpec(stringKeyBuilder.toString().getBytes(usedCharset));
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey key = keyFactory.generateSecret(keySpec);

            final Cipher cipher = Cipher.getInstance("DES");
            if (decode) {
                byte[] encrypedPwdBytes = Base64.decode(pw.getBytes(usedCharset));
                cipher.init(Cipher.DECRYPT_MODE, key);
                encrypedPwdBytes = cipher.doFinal(encrypedPwdBytes);
                return new String(encrypedPwdBytes, usedCharset);
            }

            final byte[] cleartext = pw.getBytes(usedCharset);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(Base64.encode(cipher.doFinal(cleartext)),
                    usedCharset);
        } catch (@Nonnull final GeneralSecurityException e) {
            if (decode) {
                LOGGER.warn("Decoding the password failed");
            } else {
                LOGGER.warn("Encoding the password failed");
            }
            return "";
        } catch (@Nonnull final IllegalArgumentException e) {
            if (decode) {
                LOGGER.warn("Decoding the password failed");
            } else {
                LOGGER.warn("Encoding the password failed");
            }
            return "";
        }
    }
}
