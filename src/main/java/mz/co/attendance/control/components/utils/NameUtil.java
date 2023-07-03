package mz.co.attendance.control.components.utils;

public class NameUtil {

    public static String fromUsername(String username){
        if(username.contains(".")){
            String[] exploded = username.split("\\.");
            for(int i = 0; i < exploded.length; i++){
                exploded[i] =  exploded[i].substring(0, 1).toUpperCase() + exploded[i].substring(1);
            }
            username = String.join(" ", exploded);
        } else {
            username = username.substring(0, 1).toUpperCase() + username.substring(1);
        }
        return username;
    }
}
