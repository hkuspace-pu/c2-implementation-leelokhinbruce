import com.example.restaurant_reservation_lib.R;

public class MenuItem {
    private String name;
    private double price;
    private int imageRes;

    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
        this.imageRes = R.drawable.photo_icon;
    }

    // Getter
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageRes() {
        return imageRes;
    }
}