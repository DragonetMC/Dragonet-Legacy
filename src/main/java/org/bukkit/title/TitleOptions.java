package org.bukkit.title;

import org.apache.commons.lang.Validate;

/**
 * Represents the options for the title on a player
 */
public class TitleOptions implements Cloneable {

    private int fadeIn;
    private int fadeOut;
    private int visible;

    /**
     * Creates new title options.
     *
     * @param fadeIn  the fade in time, in ticks
     * @param visible the amount of time to display the title, in ticks
     * @param fadeOut the fade out time, in ticks
     */
    public TitleOptions(int fadeIn, int visible, int fadeOut) {
        setFadeInTime(fadeIn);
        setFadeOutTime(fadeOut);
        setVisibleTime(visible);
    }

    /**
     * Creates new title options with the default values.
     */
    public TitleOptions() {
        reset();
    }

    /**
     * Resets the title options to their defaults.
     */
    public void reset() {
        this.fadeIn = 20;
        this.fadeOut = 20;
        this.visible = 60;
    }

    /**
     * Gets the fade in time.
     *
     * @return the current fade in time, in ticks
     */
    public int getFadeInTime() {
        return fadeIn;
    }

    /**
     * Gets the fade out time.
     *
     * @return the current fade out time, in ticks
     */
    public int getFadeOutTime() {
        return fadeOut;
    }

    /**
     * Gets the amount of time the title is visible for.
     *
     * @return the visible time, in ticks
     */
    public int getVisibleTime() {
        return visible;
    }

    /**
     * Sets the fade in time.
     *
     * @param time the new time, in ticks
     */
    public void setFadeInTime(int time) {
        Validate.isTrue(time >= 0, "Fade in time must be at least 0 ticks");
        this.fadeIn = time;
    }

    /**
     * Sets the fade out time.
     *
     * @param time the new time, in ticks
     */
    public void setFadeOutTime(int time) {
        Validate.isTrue(time >= 0, "Fade out time must be at least 0 ticks");
        this.fadeOut = time;
    }

    /**
     * Sets the amount of time the title is visible for.
     *
     * @param time the new time, in ticks
     */
    public void setVisibleTime(int time) {
        Validate.isTrue(time >= 0, "Visible time must be at least 0 ticks");
        this.visible = time;
    }

    @Override
    public TitleOptions clone() {
        try {
            return (TitleOptions) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}
