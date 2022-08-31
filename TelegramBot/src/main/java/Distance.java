import javax.print.DocFlavor;

public class Distance {
    private double myLatitude1;
    private double myLongitude1;
    private double latitude2;
    private double longitude2;
    private double radius = 6731;

    public Distance(double myLatitude1, double myLongitude1, double latitude2, double longitude2){
        this.myLatitude1 = myLatitude1;
        this.myLongitude1 = myLongitude1;
        this.latitude2 = latitude2;
        this.longitude2 = longitude2;
    }

    public boolean onDistance(){
        double lat1_in_radians = myLatitude1 * Math.PI/180;
        double lat2_in_radians = latitude2 * Math.PI/180;
        double deltaLat = (latitude2 - myLatitude1) * Math.PI/180;
        double deltaLng = (longitude2 - myLongitude1) * Math.PI/180;
        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                   Math.cos(lat1_in_radians) * Math.cos(lat2_in_radians) * Math.sin(deltaLng/2) * Math.sin(deltaLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = radius * c;
        System.out.println("дуга  " + d);
        if(d <= 1){
            return true;
        }
        else{
            return false;
        }
    }
}
