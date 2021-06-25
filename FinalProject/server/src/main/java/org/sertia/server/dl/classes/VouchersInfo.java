package org.sertia.server.dl.classes;


import javax.persistence.*;

@Entity
@Table(name="vouchers_info")
public class VouchersInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="vouchers_info_id")
    private int id;

    private int voucherInitialBalance;

    private double price;

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
}
