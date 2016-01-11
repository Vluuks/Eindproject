package nl.mprog.renske.myapplication;

/**
 * Created by Renske on 11-1-2016.
 */
public class Achievement {

    public String name, imageviewpath, type;
    public int status;

    public Achievement(String the_name, String the_imageviewpath, int the_status, String the_type){
        this.name = the_name;
        this.imageviewpath = the_imageviewpath;
        this.status = the_status;
        this.type = the_type;
    }

    public void setStatus(int status_value){
        this.status = status_value;
    }

    public int getStatus(){
        return this.status;
    }



}
