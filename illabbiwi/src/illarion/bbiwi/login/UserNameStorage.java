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
import org.jdesktop.swingx.auth.UserNameStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class is the username storage for the BBIWI tool. It will take care to store the last used user names properly.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
public final class UserNameStorage extends UserNameStore {
    /**
     * The pattern used to split the user names in the storage.
     */
    private static final Pattern NAME_SPLIT_PATTERN = Pattern.compile("\\|\\|");

    /**
     * The configuration system that acts as persistent storage unit.
     */
    private final Config config;

    /**
     * The buffered user names.
     */
    private final List<String> userNames;

    /**
     * Create a new instance of this storage and specify the configuration system used to store the last user names.
     *
     * @param config the configuration system
     */
    public UserNameStorage(final Config config) {
        this.config = config;
        userNames = new ArrayList<String>();
        loadUserNames();
    }

    @Override
    public void loadUserNames() {
        final String names = config.getString("userNames");
        if ((names == null) || names.isEmpty()) {
            return;
        }
        final String[] splitNames = NAME_SPLIT_PATTERN.split(names);
        Collections.addAll(userNames, splitNames);
    }

    @Override
    public void addUserName(final String userName) {
        userNames.add(0, userName);
    }

    @Override
    public boolean containsUserName(final String name) {
        return userNames.contains(name);
    }

    @Override
    public String[] getUserNames() {
        return userNames.toArray(new String[userNames.size()]);
    }

    @Override
    public void removeUserName(final String userName) {
        userNames.remove(userName);
    }

    @Override
    public void saveUserNames() {
        final StringBuilder builder = new StringBuilder();
        for (final String name : userNames) {
            builder.append(name);
            builder.append("||");
        }
        builder.setLength(builder.length() - 2);
        config.set("userNames", builder.toString());
        config.save();
    }

    @Override
    public void setUserNames(final String[] names) {
        userNames.clear();
        Collections.addAll(userNames, names);
    }
}
