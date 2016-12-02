package adapters;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class StringWithTag {
    public String string;
    public String tag;

    public StringWithTag(String string, String tag) {
        this.string = string;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return string;
    }

    public String getString() {
        return string;
    }

    public String getTag() {
        return tag;
    }
}