package www.icebd.com.suzukibangladesh.dealer_list;

/**
 * Created by Acer on 5/24/2016.
 */
public class Custom_dealer_list {
    public String shop_title;
    public String contact_person;
    public String address;
    public String mobile_number;
    public String shop_type;



    public String district;

    public Custom_dealer_list(){

    }

    public Custom_dealer_list(String shop_title, String contact_person, String address, String mobile_number, String shop_type,String district) {
        super();
        this.shop_title = shop_title;
        this.contact_person = contact_person;
        this.address = address;
        this.mobile_number = mobile_number;
        this.shop_type = shop_type;
        this.district = district;
    }


    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public void setShop_type(String shop_type) {
        this.shop_type = shop_type;
    }


    public String getContact_person() {
        return contact_person;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getShop_type() {
        return shop_type;
    }
    public String getDistrict()
    {
        return district;
    }

    public String getShop_title()
    {
        return shop_title;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public void setShop_title(String shop_title)
    {
        this.shop_title = shop_title;
    }
}
