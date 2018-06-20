package coursework;

public class CarSales {
    final private String Region, Vehicle, Year;
    final private Integer Quantity, QTR;
    //final private Byte Qtr;

    public CarSales(String region, String vehicle, Integer quantity, String year, Integer qtr){
    this.Region = region;
    this.Vehicle = vehicle;
    this.Quantity = quantity;
    this.Year = year;
    this.QTR = qtr;
}

@Override
public String toString(){
    return String.format("%s%s%s%s%s",
            ("Year:" + Year + " "), ("Qtr:" + QTR + " "), ("Region:" + Region + " "), ("Vehicle:" + Vehicle + " "), ("Quantity:" + Quantity + " "));
}

    public String getRegion() {
        return Region;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public String getYear() {
        return Year;
    }

    public Integer getQTR() {
        return QTR;
    }
}