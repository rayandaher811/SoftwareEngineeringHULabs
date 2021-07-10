package org.sertia.server.bl;

import com.mysql.cj.jdbc.JdbcConnection;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.sertia.contracts.complaints.ClientOpenComplaint;
import org.sertia.contracts.complaints.requests.CreateNewComplaintRequest;
import org.sertia.contracts.reports.ClientReport;
import org.sertia.server.SertiaException;
import org.sertia.server.bl.Services.CreditCardService;
import org.sertia.server.bl.Services.CustomerNotifier;
import org.sertia.server.bl.Services.ICreditCardService;
import org.sertia.server.bl.Services.Reportable;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import javax.naming.OperationNotSupportedException;
import javax.transaction.NotSupportedException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ComplaintsController extends Reportable {

	private CustomerNotifier notifier;
	private ICreditCardService creditCardService;

	public ComplaintsController(ICreditCardService creditCardService) {
		notifier = CustomerNotifier.getInstance();
		this.creditCardService = creditCardService;
	}

	public void createNewComplaint(CreateNewComplaintRequest createComplaintRequest) throws Exception {
		Session session = null;
		String ticketPayerId = "";
		ClientOpenComplaint clientComplaint = createComplaintRequest.complaint;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			CostumerComplaint complaint = new CostumerComplaint();
			complaint.setOpenedDate(LocalDateTime.now());
			complaint.setDescription(clientComplaint.description);
			complaint.setTicketType(Utils.clientTicketTypeToDL(clientComplaint.ticketType));



			// Extracting and adding the right ticket type in the right place
			switch (complaint.getTicketType()) {
				case Streaming:
					complaint.setStreamingLink(getTicketById(clientComplaint.ticketId, session, StreamingLink.class));
					ticketPayerId = complaint.getStreamingLink().getCustomerPaymentDetails().getPayerId();
					break;
				case Screening:
					complaint.setScreeningTicket(getTicketById(clientComplaint.ticketId, session, ScreeningTicket.class));
					ticketPayerId = complaint.getScreeningTicket().getPaymentInfo().getPayerId();
					break;
				case Voucher:
					complaint.setTicketsVoucher(getTicketById(clientComplaint.ticketId, session, TicketsVoucher.class));
					ticketPayerId = complaint.getTicketsVoucher().getCustomerPaymentDetails().getPayerId();
					break;
			}

			if(!ticketPayerId.equals(createComplaintRequest.clientIdNumber))
				throw new SertiaException("Wrong ticket info inserted.");

			// Saving the request
			session.beginTransaction();
			session.save(complaint);
			session.flush();
			session.getTransaction().commit();

			// Notifying our client
			notifier.notify(clientComplaint.customerEmail, "You had opened an complaint in sertia server, we will contact you within 24 hours.");
		} catch (JDBCConnectionException e){
			e.printStackTrace();
			throw new SertiaException("We couldn't add your complaint due internal technical issues.");
		}
		catch (Exception e){
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<ClientOpenComplaint> getAllUnhandledComplaints() throws Exception {
		try(Session session = HibernateSessionFactory.getInstance().openSession()) {
			List<ClientOpenComplaint> complaintsToReturn = new LinkedList<>();

			for (CostumerComplaint complaint : DbUtils.getAll(CostumerComplaint.class)) {
				if(complaint.getHandler() == null){
					complaintsToReturn.add(parseDlComplaintToClientComplaint(complaint));
				}
			}

			return complaintsToReturn;
		} finally {
		}
	}

	public void closeComplaint(int complaintId, String handlerUsername) throws Exception{
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			CostumerComplaint complaint = getCostumerComplaint(complaintId, session);

			// Closing the complaint
			if(Duration.between(LocalDateTime.now(), complaint.getOpenedDate()).toHours() <= 24){
				complaint.setClosedDate(LocalDateTime.now());
				complaint.setHandler(DbUtils.getUserByUsername(handlerUsername));

				// Notifying our client
				notifier.notify(extractCustomerPaymentDetails(complaint).getEmail(),
						"Your complaint in sertia cinema has been closed.");
			}
			else {
				throw new SertiaException("More than 24 hours passed since the complaint had been opened, we cannot close it.");
			}

			session.beginTransaction();
			session.update(complaint);
			session.flush();
			session.clear();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public void cancelPurchaseFromComplaint(int complaintId, String handlerUsername, double refundAmund) throws Exception{
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			CostumerComplaint complaint = getCostumerComplaint(complaintId, session);

			// Closing the complaint with a refund
			if(Duration.between(LocalDateTime.now(), complaint.getOpenedDate()).toHours() <= 24){
				complaint.setClosedDate(LocalDateTime.now());
				complaint.setHandler(DbUtils.getUserByUsername(handlerUsername));

				switch (complaint.getTicketType()) {
					case Voucher:
						creditCardService.refund(extractCustomerPaymentDetails(complaint), refundAmund, RefundReason.ScreeningVouchersService);
						break;
					case Screening:
						creditCardService.refund(extractCustomerPaymentDetails(complaint), refundAmund, RefundReason.ScreeningService);
						break;
					case Streaming:
						creditCardService.refund(extractCustomerPaymentDetails(complaint), refundAmund, RefundReason.StreamingService);
						break;
				}
			}
			else {
				throw new OperationNotSupportedException("More than 24 hours passed since the complaint had been opened, we cannot close it.");
			}

			session.beginTransaction();
			session.update(complaint);
			session.flush();
			session.clear();
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<ClientReport> createSertiaReports() {
		ClientReport report = new ClientReport();
		List<CostumerComplaint> complaints = getDataOfThisMonth(CostumerComplaint.class, "openedDate");
		int openedComplaints = 0;
		int closedComplaints = 0;
		for (CostumerComplaint customerComplaint : complaints) {
			if (customerComplaint.getClosedDate() != null) {
				closedComplaints++;
			} else {
				openedComplaints++;
			}
		}

		report.title = "מצב תלונות בחודש האחרון";
		report.addEntry("תלונות פתוחות", openedComplaints);
		report.addEntry("תלונות סגורות", closedComplaints);

		return Collections.singletonList(report);
	}

	@Override
	public List<ClientReport> createCinemaReports(int cinemaId) {
		return Collections.emptyList();
	}

	private ClientOpenComplaint parseDlComplaintToClientComplaint(CostumerComplaint complaint) throws SertiaException {
		CustomerPaymentDetails customerDetails = extractCustomerPaymentDetails(complaint);
		return new ClientOpenComplaint(complaint.getId(),
				customerDetails.getFullName(),
				customerDetails.getPhoneNumber(),
				customerDetails.getEmail(),
				complaint.getOpenedDate(),
				complaint.getDescription(),
				extractTicketId(complaint),
				Utils.dlTicketTypeToClient(complaint.getTicketType()));
	}

	private CustomerPaymentDetails extractCustomerPaymentDetails(CostumerComplaint complaint) throws SertiaException {
		switch (complaint.getTicketType()) {
			case Screening:
				return complaint.getScreeningTicket().getPaymentInfo();
			case Streaming:
				return complaint.getStreamingLink().getCustomerPaymentDetails();
			case Voucher:
				return complaint.getTicketsVoucher().getCustomerPaymentDetails();
			default:
				throw new SertiaException("There are no such ticket");
		}
	}

	private <T> T getTicketById(int ticketId, Session session, Class<T> ticketType) throws SertiaException {
		T ticket = session.get(ticketType, ticketId);

		if(ticket == null)
			throw new SertiaException("There are no such streaming/Ticket/Voucher with the id " + ticketId);

		return ticket;
	}

	private CostumerComplaint getCostumerComplaint(int complaintId, Session session) throws SertiaException {
		CostumerComplaint complaint = session.get(CostumerComplaint.class, complaintId);

		if(complaint == null)
			throw new SertiaException("There are no such complaint with the Id " + complaintId);

		return complaint;
	}

	private int extractTicketId(CostumerComplaint complaint) throws SertiaException {
		switch (complaint.getTicketType()) {
			case Screening:
				return complaint.getScreeningTicket().getId();
			case Streaming:
				return complaint.getStreamingLink().getId();
			case Voucher:
				return complaint.getTicketsVoucher().getId();
			default:
				throw new SertiaException("There are no such ticket");
		}
	}
}