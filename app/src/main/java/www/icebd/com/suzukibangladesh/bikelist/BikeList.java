package www.icebd.com.suzukibangladesh.bikelist;

import java.util.List;

/**
 * Created by Momen Dewan on 4/26/2016.
 */
public class BikeList
{


    private String message;
    private boolean status;
    private int status_code;
    private List<BikeItem> bikeItemsList;

    public  BikeList(){}
    public BikeList(String message,boolean status,int status_code,List<BikeItem> bikeItemsList)
    {
        this.message = message;
        this.status = status;
        this.status_code = status_code;
        this.bikeItemsList = bikeItemsList;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setStatus(boolean status)
    {
        this.status = status;
    }

    public void setStatus_code(int status_code)
    {
        this.status_code = status_code;
    }

    public void setBikeItemsList(List<BikeItem> bikeItemsList)
    {
        this.bikeItemsList = bikeItemsList;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean isStatus()
    {
        return status;
    }

    public int getStatus_code()
    {
        return status_code;
    }

    public List<BikeItem> getBikeItemsList()
    {
        return bikeItemsList;
    }

    public class BikeItem
    {
        private String bike_code;
        private String bike_id;
        private String bike_name;
        private String bike_cc;
        private String bike_mileage;
        private String thumble_img;

        public BikeItem()
        {}
        public BikeItem(String bike_id,String bike_name,String bike_cc,String bike_mileage,String thumble_img)
        {
            this.bike_id = bike_id;
            this.bike_name = bike_name;
            this.bike_cc = bike_cc;
            this.bike_mileage = bike_mileage;
            this.thumble_img = thumble_img;
        }

        public void setBike_code(String bike_code)
        {
            this.bike_code = bike_code;
        }

        public void setBike_id(String bike_id)
        {
            this.bike_id = bike_id;
        }

        public void setBike_name(String bike_name)
        {
            this.bike_name = bike_name;
        }

        public void setBike_cc(String bike_cc)
        {
            this.bike_cc = bike_cc;
        }

        public void setBike_mileage(String bike_mileage)
        {
            this.bike_mileage = bike_mileage;
        }

        public void setThumble_img(String thumble_img)
        {
            this.thumble_img = thumble_img;
        }

        public String getBike_code()
        {
            return bike_code;
        }

        public String getBike_id()
        {
            return bike_id;
        }

        public String getBike_name()
        {
            return bike_name;
        }

        public String getBike_cc()
        {
            return bike_cc;
        }

        public String getBike_mileage()
        {
            return bike_mileage;
        }

        public String getThumble_img()
        {
            return thumble_img;
        }
    }









}
