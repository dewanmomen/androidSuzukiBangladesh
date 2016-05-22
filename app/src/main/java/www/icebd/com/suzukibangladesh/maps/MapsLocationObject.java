package www.icebd.com.suzukibangladesh.maps;

import java.util.List;

/**
 * Created by Acer on 5/17/2016.
 */
public class MapsLocationObject
{
    private String message;
    private boolean status;
    private int status_code;
    private List<Locations> listLocation;

    public MapsLocationObject(){}
    public MapsLocationObject(String message,boolean status,int status_code,List<Locations> listLocation)
    {
        this.message = message;
        this.status = status;
        this.status_code = status_code;
        this.listLocation = listLocation;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public void setListLocation(List<Locations> listLocation) {
        this.listLocation = listLocation;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public List<Locations> getListLocation() {
        return listLocation;
    }




    public class Locations
    {

        private String location_id;
        private String location_type;
        private String location_name;
        private String location_address;
        private String location_contact_person_name;
        private String location_contact_person_email;
        private String location_contact_person_phone;
        private double lat;
        private double lng;
        private String district;




        public Locations(){}



        public Locations(String location_id, String location_type, String location_name, String location_address,
                         String location_contact_person_name, String location_contact_person_email, String location_contact_person_phone, double lat, double lng, String district)
        {
            this.location_id = location_id;
            this.location_type = location_type;
            this.location_name = location_name;
            this.location_address = location_address;
            this.location_contact_person_name = location_contact_person_name;
            this.location_contact_person_email = location_contact_person_email;
            this.location_contact_person_phone = location_contact_person_phone;
            this.lat = lat;
            this.lng = lng;
            this.district = district;
        }

        public String getLocation_id() {
            return location_id;
        }

        public String getLocation_type() {
            return location_type;
        }

        public String getLocation_name() {
            return location_name;
        }

        public String getLocation_address() {
            return location_address;
        }

        public String getLocation_contact_person_name() {
            return location_contact_person_name;
        }

        public String getLocation_contact_person_email() {
            return location_contact_person_email;
        }

        public String getLocation_contact_person_phone() {
            return location_contact_person_phone;
        }

        public void setLocation_id(String location_id) {
            this.location_id = location_id;
        }

        public void setLocation_type(String location_type) {
            this.location_type = location_type;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
        }

        public void setLocation_address(String location_address) {
            this.location_address = location_address;
        }

        public void setLocation_contact_person_name(String location_contact_person_name) {
            this.location_contact_person_name = location_contact_person_name;
        }

        public void setLocation_contact_person_email(String location_contact_person_email) {
            this.location_contact_person_email = location_contact_person_email;
        }

        public void setLocation_contact_person_phone(String location_contact_person_phone) {
            this.location_contact_person_phone = location_contact_person_phone;
        }
        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
        public String getDistrict()
        {
            return district;
        }

        public void setDistrict(String district)
        {
            this.district = district;
        }

    }
}
