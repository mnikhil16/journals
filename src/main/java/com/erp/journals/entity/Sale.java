package com.erp.journals.entity;

//import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
//import com.vladmihalcea.hibernate.type.json.JsonStringType;
//import org.hibernate.annotations.Type;
//import org.hibernate.annotations.TypeDef;
//import org.hibernate.annotations.TypeDefs;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

/** The persistent class for the sales database table. */
@Entity
@Table(name = "sales")
//@NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s")
//@TypeDefs({
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
public class Sale  {
//    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private int id;

//    @Column(name = "active")
//    private Boolean active;
//
//    private BigDecimal cess;
//
//    private BigDecimal cessValue;
//
//    @Column(name = "cumilative_cgst")
//    private BigDecimal cumilativeCgst;
//
//    @Column(name = "cumilative_igst")
//    private BigDecimal cumilativeIgst;
//
//    @Column(name = "cumilative_sgst")
//    private BigDecimal cumilativeSgst;
//
//    @Column(name = "cumilative_tax")
//    private BigDecimal totalTax;
//
//    private BigDecimal discount;
//
//    @Column(name = "discount_value")
//    private BigDecimal discountValue;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "due_date")
//    private Date dueDate;
//
//    @Column(name = "ecommerce_gstin")
//    private String ecommerceGstin;
//
//    @Column(name = "file_id")
//    private String fileId;

    @Column(name = "invoice_date")
    private Date invoiceDate;

//    @Column(name = "invoice_message")
//    private String invoiceMessage;
//
//    @Column(name = "invoice_number")
//    private String invoiceNumber;
//
//    @Column(name = "invoice_type")
//    private String invoiceType;
//
//    @Column(name = "is_ecommerce")
//    private Boolean isEcommerce;
//
//    private String isBillingAddress;
//
//    private String isCgst;
//
//    private String isIgst;
//
//    private String isProduct;
//
//    private String isService;
//
//    @Lob private String memo;
//
//    private int paid;
//
//    @Column(name = "payment_mode")
//    private String paymentMode;
//
//    private Byte print;
//
//    @Column(name = "purchase_order")
//    private String purchaseOrder;
//
//    @Column(name = "sale_status")
//    private String saleStatus;
//
//    @Column(name = "save_type")
//    private String saveType;
//
//    private BigDecimal subtotal;
//
//    private BigDecimal total;
//
//    @Column(name = "total_before_tax")
//    private BigDecimal totalBeforeTax;

//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Staff user;

//    @Column(name = "overdue_receivables")
//    private BigDecimal overdueReceivables;
//
//    @Column(name = "overdue_time")
//    private String overdueTime;

    // bi-directional many-to-one association to Company
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    private Company company;

    // bi-directional many-to-one association to Customer
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Customer customer;

    // bi-directional many-to-one association to SalesItem
//    @OneToMany(mappedBy = "sale")
//    private List<SalesItem> salesItems;
//
//    // bi-directional many-to-one association to SalesPayment
//    @OneToMany(mappedBy = "sale")
//    private List<SalesPayment> salesPayments;
//
//    // bi-directional many-to-one association to SalesTaxe
//    @OneToMany(mappedBy = "sale")
//    private List<SalesTaxe> salesTaxes;
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id")
//    private Store store;
//
//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "billingaddress_id")
//    private Address billingAddress;
//
//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "shippingaddress_id")
//    private Address shippingAddress;

//    @Column(name = "last_due_amount")
//    private BigDecimal lastDueAmount;
//
//    @Column(name = "transaction_id")
//    private String transactionId;
//
//    @Column(name = "ext_data", columnDefinition = "json")
////    @Type(type = "jsonb")
//    private String extData;
//
//    private String uuid;
//
//    @Column(name = "is_ordered")
//    private Boolean isOrdered;
//
//    @Column(name = "mobile_number")
//    private String mobileNumber;


//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "salesman_id")
//    private Staff salesmanId;
//
//    public Staff getSalesmanId() {
//        return salesmanId;
//    }
//
//    public void setSalesmanId(Staff salesmanId) {
//        this.salesmanId = salesmanId;
//    }

//    public String getMobileNumber() {
//        return mobileNumber;
//    }
//
//    public void setMobileNumber(String mobileNumber) {
//        this.mobileNumber = mobileNumber;
//    }
//
//    public Boolean getIsOrdered() {
//        return isOrdered;
//    }
//
//    public void setIsOrdered(Boolean isOrdered) {
//        this.isOrdered = isOrdered;
//    }
//
//    public String getUuid() {
//        return uuid;
//    }
//
//    public void setUuid(String uuid) {
//        this.uuid = uuid;
//    }
//
//    public BigDecimal getLastDueAmount() {
//        return lastDueAmount;
//    }
//
//    public void setLastDueAmount(BigDecimal lastDueAmount) {
//        this.lastDueAmount = lastDueAmount;
//    }
//
//    public String getTransactionId() {
//        return transactionId;
//    }

//    public Address getBillingAddress() {
//        return billingAddress;
//    }
//
//    public void setBillingAddress(Address billingAddress) {
//        this.billingAddress = billingAddress;
//    }
//
//    public Address getShippingAddress() {
//        return shippingAddress;
//    }
//
//    public void setShippingAddress(Address shippingAddress) {
//        this.shippingAddress = shippingAddress;
//    }

//    public void setTransactionId(String transactionId) {
//        this.transactionId = transactionId;
//    }

//    public Store getStore() {
//        return store;
//    }
//
//    public void setStore(Store store) {
//        this.store = store;
//    }

//    @Column(name = "due_amt")
//    private BigDecimal dueAmount;

//    public BigDecimal getOverdueReceivables() {
//        return overdueReceivables;
//    }
//
//    public void setOverdueReceivables(BigDecimal overdueReceivables) {
//        this.overdueReceivables = overdueReceivables;
//    }
//
//    public String getOverdueTime() {
//        return overdueTime;
//    }
//
//    public void setOverdueTime(String overdueTime) {
//        this.overdueTime = overdueTime;
//    }
//
//    public BigDecimal getDueAmount() {
//        return dueAmount;
//    }
//
//    public void setDueAmount(BigDecimal dueAmount) {
//        this.dueAmount = dueAmount;
//    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    @Column(name = "paid_amt")
    private BigDecimal paidAmount;

//    @Column(name = "checkin_time")
//    private String checkinTime;
//
//    @Column(name = "checkout_time")
//    private String checkoutTime;
//
//    @Column(name = "nxt_service_date")
//    private Date nxtServiceDate;
//
//    @Column(name = "technician_name")
//    private String technicianName;

//    @ManyToOne(optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "workorder_id")
//    private WorkOrder workOrder;

    public Sale() {}

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public Boolean getActive() {
//        return this.active;
//    }
//
//    public void setActive(Boolean active) {
//        this.active = active;
//    }
//
//    public BigDecimal getCess() {
//        return this.cess;
//    }
//
//    public void setCess(BigDecimal cess) {
//        this.cess = cess;
//    }
//
//    public BigDecimal getCessValue() {
//        return this.cessValue;
//    }
//
//    public void setCessValue(BigDecimal cessValue) {
//        this.cessValue = cessValue;
//    }
//
//    public BigDecimal getCumilativeCgst() {
//        return this.cumilativeCgst;
//    }
//
//    public void setCumilativeCgst(BigDecimal cumilativeCgst) {
//        this.cumilativeCgst = cumilativeCgst;
//    }
//
//    public BigDecimal getCumilativeIgst() {
//        return this.cumilativeIgst;
//    }
//
//    public void setCumilativeIgst(BigDecimal cumilativeIgst) {
//        this.cumilativeIgst = cumilativeIgst;
//    }
//
//    public BigDecimal getCumilativeSgst() {
//        return this.cumilativeSgst;
//    }
//
//    public void setCumilativeSgst(BigDecimal cumilativeSgst) {
//        this.cumilativeSgst = cumilativeSgst;
//    }
//
//    public BigDecimal getTotalTax() {
//        return this.totalTax;
//    }
//
//    public void setTotalTax(BigDecimal totalTax) {
//        this.totalTax = totalTax;
//    }
//
//    public BigDecimal getDiscount() {
//        return this.discount;
//    }
//
//    public void setDiscount(BigDecimal discount) {
//        this.discount = discount;
//    }
//
//    public BigDecimal getDiscountValue() {
//        return this.discountValue;
//    }
//
//    public void setDiscountValue(BigDecimal discountValue) {
//        this.discountValue = discountValue;
//    }
//
//    public Date getDueDate() {
//        return this.dueDate;
//    }
//
//    public void setDueDate(Date dueDate) {
//        this.dueDate = dueDate;
//    }
//
//    public String getEcommerceGstin() {
//        return this.ecommerceGstin;
//    }
//
//    public void setEcommerceGstin(String ecommerceGstin) {
//        this.ecommerceGstin = ecommerceGstin;
//    }
//
//    public String getFileId() {
//        return this.fileId;
//    }
//
//    public void setFileId(String fileId) {
//        this.fileId = fileId;
//    }
//
//    public String getInvoiceMessage() {
//        return this.invoiceMessage;
//    }
//
//    public void setInvoiceMessage(String invoiceMessage) {
//        this.invoiceMessage = invoiceMessage;
//    }
//
//    public String getInvoiceNumber() {
//        return this.invoiceNumber;
//    }
//
//    public void setInvoiceNumber(String invoiceNumber) {
//        this.invoiceNumber = invoiceNumber;
//    }
//
//    public String getInvoiceType() {
//        return this.invoiceType;
//    }
//
//    public void setInvoiceType(String invoiceType) {
//        this.invoiceType = invoiceType;
//    }
//
//    public Boolean getIsEcommerce() {
//        return this.isEcommerce;
//    }
//
//    public void setIsEcommerce(Boolean isEcommerce) {
//        this.isEcommerce = isEcommerce;
//    }
//
//    public String getIsBillingAddress() {
//        return this.isBillingAddress;
//    }
//
//    public void setIsBillingAddress(String isBillingAddress) {
//        this.isBillingAddress = isBillingAddress;
//    }
//
//    public String getIsCgst() {
//        return this.isCgst;
//    }
//
//    public void setIsCgst(String isCgst) {
//        this.isCgst = isCgst;
//    }
//
//    public String getIsIgst() {
//        return this.isIgst;
//    }
//
//    public void setIsIgst(String isIgst) {
//        this.isIgst = isIgst;
//    }
//
//    public String getIsProduct() {
//        return this.isProduct;
//    }
//
//    public void setIsProduct(String isProduct) {
//        this.isProduct = isProduct;
//    }
//
//    public String getIsService() {
//        return this.isService;
//    }
//
//    public void setIsService(String isService) {
//        this.isService = isService;
//    }
//
//    public String getMemo() {
//        return this.memo;
//    }
//
//    public void setMemo(String memo) {
//        this.memo = memo;
//    }
//
//    public int getPaid() {
//        return this.paid;
//    }
//
//    public void setPaid(int paid) {
//        this.paid = paid;
//    }
//
//    public String getPaymentMode() {
//        return this.paymentMode;
//    }
//
//    public void setPaymentMode(String paymentMode) {
//        this.paymentMode = paymentMode;
//    }
//
//    public Byte getPrint() {
//        return this.print;
//    }
//
//    public void setPrint(byte print) {
//        this.print = print;
//    }
//
//    public String getPurchaseOrder() {
//        return this.purchaseOrder;
//    }
//
//    public void setPurchaseOrder(String purchaseOrder) {
//        this.purchaseOrder = purchaseOrder;
//    }
//
//    public String getSaleStatus() {
//        return this.saleStatus;
//    }
//
//    public void setSaleStatus(String saleStatus) {
//        this.saleStatus = saleStatus;
//    }
//
//    public String getSaveType() {
//        return this.saveType;
//    }
//
//    public void setSaveType(String saveType) {
//        this.saveType = saveType;
//    }
//
//    public BigDecimal getSubtotal() {
//        return this.subtotal;
//    }
//
//    public void setSubtotal(BigDecimal subtotal) {
//        this.subtotal = subtotal;
//    }
//
//    public BigDecimal getTotal() {
//        return this.total;
//    }
//
//    public void setTotal(BigDecimal total) {
//        this.total = total;
//    }
//
//    public BigDecimal getTotalBeforeTax() {
//        return this.totalBeforeTax;
//    }
//
//    public void setTotalBeforeTax(BigDecimal totalBeforeTax) {
//        this.totalBeforeTax = totalBeforeTax;
//    }

//    public Staff getUser() {
//        return user;
//    }
//
//    public void setUser(Staff user) {
//        this.user = user;
//    }
//
//    public Company getCompany() {
//        return this.company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

//    public List<SalesItem> getSalesItems() {
//        return this.salesItems;
//    }
//
//    public void setSalesItems(List<SalesItem> salesItems) {
//        this.salesItems = salesItems;
//    }
//
//    public SalesItem addSalesItem(SalesItem salesItem) {
//        getSalesItems().add(salesItem);
//        salesItem.setSale(this);
//
//        return salesItem;
//    }
//
//    public SalesItem removeSalesItem(SalesItem salesItem) {
//        getSalesItems().remove(salesItem);
//        salesItem.setSale(null);
//
//        return salesItem;
//    }
//
//    public List<SalesPayment> getSalesPayments() {
//        return this.salesPayments;
//    }
//
//    public void setSalesPayments(List<SalesPayment> salesPayments) {
//        this.salesPayments = salesPayments;
//    }
//
//    public SalesPayment addSalesPayment(SalesPayment salesPayment) {
//        getSalesPayments().add(salesPayment);
//        salesPayment.setSale(this);
//
//        return salesPayment;
//    }
//
//    public SalesPayment removeSalesPayment(SalesPayment salesPayment) {
//        getSalesPayments().remove(salesPayment);
//        salesPayment.setSale(null);
//
//        return salesPayment;
//    }
//
//    public List<SalesTaxe> getSalesTaxes() {
//        return this.salesTaxes;
//    }
//
//    public void setSalesTaxes(List<SalesTaxe> salesTaxes) {
//        this.salesTaxes = salesTaxes;
//    }
//
//    public SalesTaxe addSalesTaxe(SalesTaxe salesTaxe) {
//        getSalesTaxes().add(salesTaxe);
//        salesTaxe.setSale(this);
//
//        return salesTaxe;
//    }
//
//    public SalesTaxe removeSalesTaxe(SalesTaxe salesTaxe) {
//        getSalesTaxes().remove(salesTaxe);
//        salesTaxe.setSale(null);
//
//        return salesTaxe;
//    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

//    public String getCheckinTime() {
//        return checkinTime;
//    }
//
//    public void setCheckinTime(String checkinTime) {
//        this.checkinTime = checkinTime;
//    }
//
//    public String getCheckoutTime() {
//        return checkoutTime;
//    }
//
//    public void setCheckoutTime(String checkoutTime) {
//        this.checkoutTime = checkoutTime;
//    }
//
//    public Date getNxtServiceDate() {
//        return nxtServiceDate;
//    }
//
//    public void setNxtServiceDate(Date nxtServiceDate) {
//        this.nxtServiceDate = nxtServiceDate;
//    }
//
//    public String getTechnicianName() {
//        return technicianName;
//    }
//
//    public void setTechnicianName(String technicianName) {
//        this.technicianName = technicianName;
//    }

//    public WorkOrder getWorkOrder() {
//        return workOrder;
//    }
//
//    public void setWorkOrder(WorkOrder workOrder) {
//        this.workOrder = workOrder;
//    }

//    public String getExtData() {
//        return extData;
//    }
//
//    public void setExtData(String extData) {
//        this.extData = extData;
//    }
}

