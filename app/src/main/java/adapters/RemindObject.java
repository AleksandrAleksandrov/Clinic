package adapters;

/**
 * Created by Aleksandr on 9/5/2016.
 */
public class RemindObject {

    public String doctor;
    public long time;
    public int id;

    public RemindObject(String doctor, long time, int id) {
        this.doctor = doctor;
        this.time = time;
        this.id = id;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setTime(int id) {
        this.id = id;
    }
}
