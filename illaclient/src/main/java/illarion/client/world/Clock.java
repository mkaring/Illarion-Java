/*
 * This file is part of the Illarion project.
 *
 * Copyright © 2015 - Illarion e.V.
 *
 * Illarion is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Illarion is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package illarion.client.world;

import org.jetbrains.annotations.Contract;

import javax.annotation.concurrent.ThreadSafe;

/**
 * This class is the implementation of the time in Illarion.
 *
 * @author Martin Karing &lt;nitram@illarion.org&gt;
 */
@SuppressWarnings("ClassNamingConvention")
@ThreadSafe
public final class Clock {
    /**
     * The day that was set during the last synchronization.
     */
    private int day;

    /**
     * The month that was set during the last synchronization.
     */
    private int month;

    /**
     * The year that was set during the last synchronization.
     */
    private int year;

    /**
     * The hour that was set during the last synchronization.
     */
    private int hour;

    /**
     * The minute that was set during the last synchronization.
     */
    private int minute;

    /**
     * The time in milliseconds when the synchronization was applied last.
     */
    private long lastSync;

    /**
     * Set the current date and time.
     *
     * @param year the year of the new date
     * @param month the month of the new date
     * @param day the day of the new date
     * @param hour the hour of the new date
     * @param minute the minute of the new date
     */
    public void setDateTime(int year, int month, int day, int hour, int minute) {
        lastSync = System.currentTimeMillis();
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Get the current day.
     *
     * @return the current day
     */
    @Contract(pure = true)
    public int getDay() {
        return day;
    }

    /**
     * Get the current month.
     *
     * @return the current month
     */
    @Contract(pure = true)
    public int getMonth() {
        return month;
    }

    /**
     * Get the current year.
     *
     * @return the current year
     */
    @Contract(pure = true)
    public int getYear() {
        return year;
    }

    /**
     * Get the current hours.
     *
     * @return the current hour
     */
    @Contract(pure = true)
    public int getHour() {
        long illaHoursPass = getIllaSecondPass() / 60 / 60;
        return (int) ((hour + illaHoursPass) % 24);
    }

    /**
     * Get the current minute.
     *
     * @return the current minute
     */
    @Contract(pure = true)
    public int getMinute() {
        long illaMinutesPass = getIllaSecondPass() / 60;
        return (int) ((minute + illaMinutesPass) % 60);
    }

    /**
     * Get the current second.
     *
     * @return the current second
     */
    @Contract(pure = true)
    public int getSecond() {
        return (int) (getIllaSecondPass() % 60);
    }

    /**
     * Get the amount of seconds in Illarion Time that did pass since the last time the synchronization was applied.
     *
     * @return the elapsed time in Illarion seconds
     */
    @Contract(pure = true)
    private long getIllaSecondPass() {
        long secondsPass = (System.currentTimeMillis() - lastSync) / 1000;
        return secondsPass * 3L;
    }
}
