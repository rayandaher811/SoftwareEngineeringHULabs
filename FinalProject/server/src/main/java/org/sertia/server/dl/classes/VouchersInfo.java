package org.sertia.server.dl.classes;


import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="vouchers_info")
public class VouchersInfo {
    // The table is single lined table and this is it's single record Id
    public static final int singleRecordId = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="vouchers_info_id")
    private int id;

    private int voucherInitialBalance;

    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVoucherInitialBalance() {
        return voucherInitialBalance;
    }

    public void setVoucherInitialBalance(int voucherInitialBalance) {
        this.voucherInitialBalance = voucherInitialBalance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VouchersInfo that = (VouchersInfo) o;
        return id == that.id && voucherInitialBalance == that.voucherInitialBalance && Double.compare(that.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, voucherInitialBalance, price);
    }
}
