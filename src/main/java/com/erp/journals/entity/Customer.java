package com.erp.journals.entity;

//import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
//import com.vladmihalcea.hibernate.type.json.JsonStringType;
//import org.hibernate.annotations.Type;
//import org.hibernate.annotations.TypeDef;
//import org.hibernate.annotations.TypeDefs;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

/** The persistent class for the customers database table. */

@Entity
@Table(name = "customers")
//@NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
//@TypeDefs({
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
public class Customer {
//    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

//    @Column(name = "uuid")
//    private String customerUuid;
//
//    private String active;
//
//    @Column(name = "business_name")
//    private String businessName;
//
//    @Column(name = "customer_type")
//    private String customerType;

    @Column(name = "display_name")
    private String displayName;

//    private String email;
//
//    private String fax;
//
//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "gst_reg_number")
//    private String gstRegNumber;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    private String mobile;
//
//    @Column(name = "mobile_company_id")
//    private String mobileCompanyId;
//
//    private String website;

    // bi-directional many-to-one association to Address
//    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "billing_address_id")
//    private Address billingAddress;

//    @Column(name = "pricing_discount")
//    private Double pricingDiscount;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id")
//    private Store store;

//    @Column(name = "alternative_mobilenum")
//    private String altMobileNum;
//
//    @Column(name = "discount_fromDt")
//    private Date discountFromDt;
//
//    @Column(name = "discount_toDt")
//    private Date discountToDt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private Staff staff;

//    @Column(name = "due_amount")
//    private BigDecimal previousDueAmount = new BigDecimal(0);
//
//    @Column(name = "date_of_birth")
//    private Date dateOfBirth;
//
//    @Column(name = "gst_type")
//    private Integer gstType;
//
//    @Column(name = "ext_data", columnDefinition = "json")
//    @Type(type = "jsonb")
//    private String extData;
//
//    public String getExtData() {
//        return extData;
//    }
//
//    public void setExtData(String extData) {
//        this.extData = extData;
//    }
//
//    public String getCustomerUuid() {
//        return customerUuid;
//    }
//
//    public void setCustomerUuid(String customerUuid) {
//        this.customerUuid = customerUuid;
//    }
//
//    public Integer getGstType() {
//        return gstType;
//    }
//
//    public void setGstType(Integer gstType) {
//        this.gstType = gstType;
//    }
//
//    public Date getDateOfBirth() {
//        return dateOfBirth;
//    }
//
//    public void setDateOfBirth(Date dateOfBirth) {
//        this.dateOfBirth = dateOfBirth;
//    }
//
//    public BigDecimal getPreviousDueAmount() {
//        return previousDueAmount;
//    }
//
//    public void setPreviousDueAmount(BigDecimal previousDueAmount) {
//        this.previousDueAmount = previousDueAmount;
//    }

//    public Staff getStaff() {
//        return staff;
//    }
//
//    public void setStaff(Staff staff) {
//        this.staff = staff;
//    }

//    public String getAltMobileNum() {
//        return altMobileNum;
//    }
//
//    public void setAltMobileNum(String altMobileNum) {
//        this.altMobileNum = altMobileNum;
//    }
//
//    public Date getDiscountFromDt() {
//        return discountFromDt;
//    }
//
//    public void setDiscountFromDt(Date discountFromDt) {
//        this.discountFromDt = discountFromDt;
//    }
//
//    public Date getDiscountToDt() {
//        return discountToDt;
//    }
//
//    public void setDiscountToDt(Date discountToDt) {
//        this.discountToDt = discountToDt;
//    }

//    public Store getStore() {
//        return store;
//    }
//
//    public void setStore(Store store) {
//        this.store = store;
//    }

//    public Double getPricingDiscount() {
//        return pricingDiscount;
//    }
//
//    public void setPricingDiscount(Double pricingDiscount) {
//        this.pricingDiscount = pricingDiscount;
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

    // bi-directional many-to-one association to Address
//    @OneToOne(cascade = CascadeType.ALL, optional = true, fetch = FetchType.LAZY)
//    @JoinColumn(name = "shipping_address_id")
//    private Address shippingAddress;
//
//    // bi-directional many-to-one association to Company
//    @OneToOne(optional = false, fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_id")
//    private Company company;

//    @Column(name = "payment_method")
//    private String paymentMethod;
//
//    @Column(name = "gst_num")
//    private String gstNo;

    // bi-directional many-to-one association to Payment
//    @OneToMany(mappedBy = "customer")
//    private List<Payment> payments;
//
//    // bi-directional many-to-one association to Quotation
//    @OneToMany(mappedBy = "customer")
//    private List<Quotation> quotations;

    public Customer() {
    }

//    public Integer getId() {
//        return this.id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getActive() {
//        return this.active;
//    }
//
//    public void setActive(String active) {
//        this.active = active;
//    }
//
//    public String getBusinessName() {
//        return this.businessName;
//    }
//
//    public void setBusinessName(String businessName) {
//        this.businessName = businessName;
//    }
//
//    public String getCustomerType() {
//        return this.customerType;
//    }
//
//    public void setCustomerType(String customerType) {
//        this.customerType = customerType;
//    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

//    public String getEmail() {
//        return this.email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getFax() {
//        return this.fax;
//    }
//
//    public void setFax(String fax) {
//        this.fax = fax;
//    }
//
//    public String getFirstName() {
//        return this.firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getGstRegNumber() {
//        return this.gstRegNumber;
//    }
//
//    public void setGstRegNumber(String gstRegNumber) {
//        this.gstRegNumber = gstRegNumber;
//    }
//
//    public String getLastName() {
//        return this.lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getMobile() {
//        return this.mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getMobileCompanyId() {
//        return this.mobileCompanyId;
//    }
//
//    public void setMobileCompanyId(String mobileCompanyId) {
//        this.mobileCompanyId = mobileCompanyId;
//    }
//
//    public String getWebsite() {
//        return this.website;
//    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }

//    public Company getCompany() {
//        return this.company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }
//
//    public List<Payment> getPayments() {
//        return this.payments;
//    }
//
//    public void setPayments(List<Payment> payments) {
//        this.payments = payments;
//    }

//    public Payment addPayment(Payment payment) {
//        getPayments().add(payment);
//        payment.setCustomer(this);
//
//        return payment;
//    }
//
//    public Payment removePayment(Payment payment) {
//        getPayments().remove(payment);
//        payment.setCustomer(null);
//
//        return payment;
//    }
//
//    public List<Quotation> getQuotations() {
//        return this.quotations;
//    }

//    public void setQuotations(List<Quotation> quotations) {
//        this.quotations = quotations;
//    }
//
//    public Quotation addQuotation(Quotation quotation) {
//        getQuotations().add(quotation);
//        quotation.setCustomer(this);
//
//        return quotation;
//    }
//
//    public Quotation removeQuotation(Quotation quotation) {
//        getQuotations().remove(quotation);
//        quotation.setCustomer(null);
//
//        return quotation;
//    }

//    public String getGstNo() {
//        return gstNo;
//    }
//
//    public void setGstNo(String gstNo) {
//        this.gstNo = gstNo;
//    }
//
//    public String getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public void setPaymentMethod(String paymentMethod) {
//        this.paymentMethod = paymentMethod;
//    }
}