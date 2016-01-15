package nl.mprog.renske.myapplication;

/**
 * Created by Renske on 11-1-2016.
 */
public class Achievement {

    public String name, imageviewpath, type;
    public int status, countercurrent, counternew;

    public Achievement(String the_name, String the_imageviewpath, int the_status, String the_type){
        this.name = the_name;
        this.imageviewpath = the_imageviewpath;
        this.status = the_status;
        this.type = the_type;
        this.countercurrent = 0;
        this.counternew = 0;
    }



    public void setStatus(int status_value){
        this.status = status_value;
    }

    public int getStatus(){
        return this.status;
    }



}
