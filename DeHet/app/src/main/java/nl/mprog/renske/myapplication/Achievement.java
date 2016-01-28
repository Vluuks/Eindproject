// Renske Talsma, UvA 10896503, vluuks@gmail.com

package nl.mprog.renske.myapplication;

/**
 * Object that contains information about every achievement, such as state of completion, amount
 * of times repeated, name and difficulty.
 */
public class Achievement {

    public String name, imageviewpath, type;
    public int status, counter;

    /**
     * Constructor.
     */
    public Achievement(String theName, String theType){
        this.name = theName;
        this.status = 0;
        this.type = theType;
        this.counter = 0;
    }

    /**
     * Get/set methods.
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status_value){
        this.status = status_value;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounterPlusOne() {
        this.counter++;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
