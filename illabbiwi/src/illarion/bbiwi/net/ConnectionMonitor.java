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
package illarion.bbiwi.net;

import illarion.bbiwi.BBIWI;
import illarion.common.net.NetComm;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is used to keep track of the connection status.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public class ConnectionMonitor {
    @Nullable
    private NetComm netComm;

    private boolean loginPending;
    private boolean loginSuccessful;

    /**
     * This object is used as lock to wait until the login is done.
     */
    private final Object loginWaitLock = new Object();

    public void startLogin(@Nonnull final NetComm comm) {
        netComm = comm;
        loginPending = true;
    }

    public void reportLoginSucess() {
        loginPending = false;
        loginSuccessful = true;

        synchronized (loginWaitLock) {
            loginWaitLock.notify();
        }
    }

    public void reportDisconnect(final int reason) {
        if (loginPending) {
            loginPending = false;
            loginSuccessful = false;
            synchronized (loginWaitLock) {
                loginWaitLock.notify();
            }
            if (netComm != null) {
                netComm.disconnect();
            }
        } else {
            if (netComm != null) {
                netComm.disconnect();
            }
            BBIWI.disconnect(reason);
        }
    }

    public void waitUntilLoginDone(final long timeout) {
        if (!loginPending) {
            return;
        }

        synchronized (loginWaitLock) {
            final long startTime = System.currentTimeMillis();
            while (loginPending && ((startTime + timeout) > System.currentTimeMillis())) {
                try {
                    loginWaitLock.wait(Math.max(1, (startTime + timeout) - System.currentTimeMillis()));
                } catch (@Nonnull final InterruptedException ignored) {
                    // nothing to do
                }
            }
        }
    }

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }
}
