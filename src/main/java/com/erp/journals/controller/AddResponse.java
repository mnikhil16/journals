package com.erp.journals.controller;

/**
 * This class represents an AddResponse object returned by the API endpoints.
 * It contains information about the response status and an optional message.
 * Usage:
 * The AddResponse class is used to return the outcome of operations that add or create entities.
 * For example, after adding a new customer to the system, an instance of this class can be returned
 * to indicate the success or failure of the operation and provide an optional descriptive message.
 */
public class AddResponse {
    int Id;
    String msg;

    /**
     * Get the ID of the response.
     *
     * @return The response ID as an integer.
     */
    public int getId() {
        return Id;
    }

    /**
     * Set the ID of the response.
     *
     * @param id The response ID to set.
     */
    public void setId(int id) {
        Id = id;
    }

    /**
     * Get the message associated with the response.
     *
     * @return The response message as a String.
     */
    public String getMsg() {
        return msg;
    }

    /**
     * Set the message for the response.
     *
     * @param msg The response message to set.
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
