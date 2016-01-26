package nl.mprog.renske.myapplication;

/**
 * Created by Renske on 11-1-2016.
 */
public class Achievement {

    public String name, imageviewpath, type;
    public int status, counter;

    public Achievement(String the_name, String the_type){
        this.name = the_name;
        this.imageviewpath = "@mipmap/icontest"; // todo in string resource zetten
        this.status = 0;
        this.type = the_type;
        this.counter = 0;
    }

    public void setStatus(int status_value){
        this.status = status_value;
    }
}
