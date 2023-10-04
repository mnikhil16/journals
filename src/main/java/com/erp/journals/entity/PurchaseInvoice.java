package com.erp.journals.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "purchases_invoice")
public class PurchaseInvoice implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "purchaseamount")
    private Integer purchaseAmount;

    @Column(name = "pur_invoice_date")
    private ZonedDateTime purchaseDate;

    @Column(name = "payment_details")
    private String paymentDetails;

    // Method to extract "method" from paymentDetails JSON
    public String extractPaymentMethod() {
        if (paymentDetails == null) {
            // Handle the case when paymentDetails is null, e.g., return a default value or throw an exception.
            return null; // Or return a default value or throw an exception
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Parse the paymentDetails JSON string
            JsonNode rootNode = objectMapper.readTree(paymentDetails);

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