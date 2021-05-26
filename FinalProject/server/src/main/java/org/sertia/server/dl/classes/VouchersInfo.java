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

    private int price;

}
