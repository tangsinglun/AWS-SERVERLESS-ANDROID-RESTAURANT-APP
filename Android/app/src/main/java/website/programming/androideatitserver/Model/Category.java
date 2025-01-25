package website.programming.androideatitserver.Model;

/**
 * Created by cokel on 3/7/2018.
 */

public class Category {

    private String Categoryid;
    private String Name;
    private String Image;
    private String description = "";
    private int price;
    private int discount;
    private String menuid = "";

    public Category() {
    }

    public Category(String categoryid) {
        Categoryid = categoryid;
    }

    public String getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.Categoryid = categoryid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
}
