package www.icebd.com.suzukibangladesh.spare_parts;

import java.util.List;

/**
 * Created by Momen Dewan on 4/26/2016.
 */
public class SparePartsListObject
{


    private String message;
    private boolean status;
    private int status_code;
    private List<SparePartsItem> sparePartsItemsList;

    public SparePartsListObject(){}
    public SparePartsListObject(String message, boolean status, int status_code, List<SparePartsItem> sparePartsItemsList)
    {
        this.message = message;
        this.status = status;
        this.status_code = status_code;
        this.sparePartsItemsList = sparePartsItemsList;
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

    public void setSparePartsItemsList(List<SparePartsItem> sparePartsItemsList)
    {
        this.sparePartsItemsList = sparePartsItemsList;
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

    public List<SparePartsItem> getSparePartsItemsList()
    {
        return sparePartsItemsList;
    }

    public class SparePartsItem
    {


        private String spare_parts_id;
        private String spare_parts_name;
        private String spare_parts_price;
        private String spare_parts_code;
        private String parts_type;
        private String bike_name;
        private String thumble_img;

        private int aInteger;

        public SparePartsItem()
        {}
        public SparePartsItem(String spare_parts_id, String spare_parts_name, String spare_parts_price, String spare_parts_code, String parts_type, String bike_name, String thumble_img)
        {
            this.spare_parts_id = spare_parts_id;
            this.spare_parts_name = spare_parts_name;
            this.spare_parts_price = spare_parts_price;
            this.spare_parts_code = spare_parts_code;
            this.parts_type = parts_type;
            this.bike_name = bike_name;
        }
        public void setSpare_parts_id(String spare_parts_id)
        {
            this.spare_parts_id = spare_parts_id;
        }

        public void setSpare_parts_name(String spare_parts_name)
        {
            this.spare_parts_name = spare_parts_name;
        }

        public void setSpare_parts_price(String spare_parts_price)
        {
            this.spare_parts_price = spare_parts_price;
        }

        public void setSpare_parts_code(String spare_parts_code)
        {
            this.spare_parts_code = spare_parts_code;
        }

        public void setParts_type(String parts_type)
        {
            this.parts_type = parts_type;
        }

        public void setBike_name(String bike_name)
        {
            this.bike_name = bike_name;
        }

        public void setThumble_img(String thumble_img)
        {
            this.thumble_img = thumble_img;
        }

        public String getSpare_parts_id()
        {
            return spare_parts_id;
        }

        public String getSpare_parts_name()
        {
            return spare_parts_name;
        }

        public String getSpare_parts_price()
        {
            return spare_parts_price;
        }

        public String getSpare_parts_code()
        {
            return spare_parts_code;
        }

        public String getParts_type()
        {
            return parts_type;
        }

        public String getBike_name()
        {
            return bike_name;
        }

        public String getThumble_img()
        {
            return thumble_img;
        }
        public int getaInteger()
        {
            return aInteger;
        }

        public void setaInteger(int aInteger)
        {
            this.aInteger = aInteger;
        }

    }









}
