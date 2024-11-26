package csds341.tln32aac.Tables;

public class SItem {
    public Integer id;
    public String name;
    public Integer currentPrice;
    public Integer supplier;
    public String unitType;
    public Integer discount;
    public Integer stock;

    public SItem(Integer id, String name, Integer currentPrice,
        Integer supplier, String unitType, Integer discount, Integer stock) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.supplier = supplier;
        this.unitType = unitType;
        this.discount = discount;
        this.stock = stock;
    }
}
