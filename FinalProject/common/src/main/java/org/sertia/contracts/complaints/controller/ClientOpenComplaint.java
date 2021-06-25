package org.sertia.contracts.complaints.controller;

import org.joda.time.DateTime;
import org.sertia.contracts.price.change.ClientTicketType;

import java.time.LocalDateTime;

public class ClientOpenComplaint {

	public int complaintId;
	public String customerName;
	public String customerPhoneNumber;
	public String customerEmail;
	public LocalDateTime creationDate;
	public String description;
	public int ticketId;
	public ClientTicketType ticketType;

	public ClientOpenComplaint(int complaintId, String customerName, String customerPhoneNumber, String customerEmail, LocalDateTime creationDate, String description, int ticketId, ClientTicketType ticketType) {
		this.complaintId = complaintId;
		this.customerName = customerName;
		this.customerPhoneNumber = customerPhoneNumber;
		this.customerEmail = customerEmail;
		this.creationDate = creationDate;
		this.description = description;
		this.ticketId = ticketId;
		this.ticketType = ticketType;
	}
}