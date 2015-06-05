package org.bukkit.title;

/**
 * Represents a title to be sent to a player. A valid title is one which has a heading.
 */
public class Title implements Cloneable {

    private String title;
    private String subtitle;

    /**
     * Creates a new empty title.
     */
    public Title() {
        this(null, null);
    }

    /**
     * Creates a new title.
     *
     * @param heading the title's heading
     */
    public Title(String heading) {
        this(heading, null);
    }

    /**
     * Creates a new title.
     *
     * @param heading  the title's heading
     * @param subtitle the title's subtitle
     */
    public Title(String heading, String subtitle) {
        setHeading(heading);
        setSubtitle(subtitle);
    }

    /**
     * Gets the current heading text. Will be null if no heading set.
     *
     * @return the current heading, or null if none set
     */
    public String getHeading() {
        return title;
    }

    /**
     * Gets the current subtitle text. Will be null if no subtitle set.
     *
     * @return the current subtitle, or null if none set
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Sets the new heading for this title. If set to null or an empty string, the
     * heading will become invalid and may not be displayed to the player.
     *
     * @param heading the new heading
     */
    public void setHeading(String heading) {
        if (heading == null || heading.length() == 0) {
            title = null;
        } else {
            title = heading;
        }
    }

    /**
     * Sets the new subtitle for this title. If set to null or an empty string, the
     * subtitle will not be displayed.
     *
     * @param subtitle the new subtitle
     */
    public void setSubtitle(String subtitle) {
        if (subtitle == null || subtitle.length() == 0) {
            this.subtitle = null;
        } else {
            this.subtitle = subtitle;
        }
    }

    @Override
    public Title clone() {
        try {
            return (Title) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

}
