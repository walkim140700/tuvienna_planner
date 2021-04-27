import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * Lecture
 * Class for holding info to one lecture
 * Author: Kilian Waltl
 * Last-Change: 27.04.2021
 */
public class Lecture implements Comparable{
    private String name;
    private Date date;
    private String url;
    private String location;

    /**
     * Creates a new lecture object
     *
     * @param name  The name of the lecture
     * @param date  The date at which the lecture starts
     * @param url  The url where the lecture was found
     * @param location  The location where the lecture will take place
     */
    public Lecture(String name, Date date, String url, String location) {
        this.name = name;
        this.date = date;
        this.url = url;
        this.location = location;
    }

    /**
     * Will return a nicely formatted String containing all info about the lecture
     *
     * @return a nicely formatted string
     */
    @Override
    public String toString() {
        return "Lecture " +name + " on " + date + " with url: " + Planner.MAIN_URL + url + "\nAt location: " + location.toString();
    }

    /**
     * Returns the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     *
     * @param name the name you want to change it to
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date
     *
     * @param date the date you want to change it to
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns the url
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url
     *
     * @param url the url you want to change it to
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Returns the location
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location
     *
     * @param location the location you want to change it to
     */
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return date.compareTo(((Lecture)o).getDate());
    }

    @Override
    public int hashCode() {
        return this.date.hashCode()+this.location.hashCode()+this.url.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Lecture){
            Lecture temp = (Lecture) obj;
            if(this.url.equals(temp.url) && this.date.equals(temp.date) && this.location.equals(temp.location))
                return true;
        }
        return false;
    }
}
