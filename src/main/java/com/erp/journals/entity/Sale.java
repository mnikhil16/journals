package com.erp.journals.entity;

import javax.persistence.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.time.ZonedDateTime;

/** The persistent class for the sales database table. */
@Data
@Entity
@Table(name = "sales")
//@NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s")
public class Sale  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "invoice_date")
    private ZonedDateTime invoiceDate;

    @Column(name = "paid_amt")
    private Integer paidAmount;

    @Column(name = "ext_data")
    private String extData;

    // bidirectional many-to-one association to Customer
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;

    // Method to extract "method" from extData JSON
    public String extractPaymentMethod() {
        if (extData == null) {
            // Handle the case when extData is null, e.g., return a default value or throw an exception.
            return null; // Or return a default value or throw an exception
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the extData JSON string
            JsonNode rootNode = objectMapper.readTree(extData);

            // Navigate to the "paymentData" object
            JsonNode paymentDataNode = rootNode.path("paymentData");

            // Check if "paymentList" exists and is an array with at least one element
            if (paymentDataNode.has("paymentList") && paymentDataNode.get("paymentList").isArray() && !paymentDataNode.get("paymentList").isEmpty()) {
                // Navigate to the first element in "paymentList" array
                JsonNode firstPaymentNode = paymentDataNode.get("paymentList").get(0);

                // Check if "method" exists within the firstPaymentNode
                if (firstPaymentNode.has("method")) {
                    // Extract the "method" value
                    return firstPaymentNode.get("method").asText();
                }
            }

            // Handle the case when the required fields are not found in the JSON
            return null; // Or return a default value or throw an exception
        } catch (Exception e) {
            // Handle parsing errors
            e.printStackTrace();
            return null; // Or return a default value or throw an exception
        }
    }
}