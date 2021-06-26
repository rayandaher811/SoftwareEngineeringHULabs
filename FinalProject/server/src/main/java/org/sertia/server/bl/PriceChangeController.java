package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.server.bl.Services.ControllerUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.NotSupportedException;
import java.util.LinkedList;
import java.util.List;

public class PriceChangeController {

	public void requestPriceChange(BasicPriceChangeRequest priceChangeRequest, String username) throws Exception {
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			PriceChangeRequest request = new PriceChangeRequest();
			request.setRequester(session.get(User.class, username));
			request.setAccepted(false);
			request.setMovie(session.get(Movie.class, priceChangeRequest.movieId));
			request.setTicketType(ControllerUtils.clientTicketTypeToDL(priceChangeRequest.clientTicketType));

			// Saving the request
			session.beginTransaction();
			session.save(request);
			session.flush();
			session.getTransaction().commit();
		} catch (Exception e){
			session.getTransaction().rollback();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<BasicPriceChangeRequest> getUnapprovedRequests() throws Exception {
		try (Session session = HibernateSessionFactory.getInstance().openSession()){
			List<BasicPriceChangeRequest> clientRequests = new LinkedList<BasicPriceChangeRequest>();

			// Collecting the unapproved requests
			for (PriceChangeRequest request : getAllPriceChangeRequests(session)) {
				if(!request.isAccepted())
					clientRequests.add(new BasicPriceChangeRequest(request.getId(),
																	request.getMovie().getId(),
																	request.getRequester().getUsername(),
																	ControllerUtils.dlTicketTypeToClient(request.getTicketType()),
																	request.getNewPrice(),
																	false));
			}

			return clientRequests;
		} finally {
		}
	}

	/**
	 * 
	 * @param priceChangeRequestId
	 */
	public void approveRequest(int priceChangeRequestId, String handlingUsername) throws Exception{
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			PriceChangeRequest request = session.get(PriceChangeRequest.class, priceChangeRequestId);

			// Updating
			request.setAccepted(true);
			request.setHandler(ControllerUtils.getUser(handlingUsername, session));


			switch (request.getTicketType()) {
				case Streaming :
					// Updating the streaming properly
					Streaming streaming = session.get(Streaming.class, request.getMovie().getId());
					streaming.setPricePerStream(request.getNewPrice());
					session.update(streaming);
					break;
				case Screening:
					// Updating the screenable movie properly
					ScreenableMovie screenableMovie = session.get(ScreenableMovie.class, request.getMovie().getId());
					screenableMovie.setTicketPrice(request.getNewPrice());
					session.update(screenableMovie);
					break;
				case Voucher:
					// Updating the voucher info
					VouchersInfo vouchersInfo = session.get(VouchersInfo.class, 0);
					vouchersInfo.setPrice(request.getNewPrice());
					session.update(vouchersInfo);
					break;
				default:
					throw new NotSupportedException("There are such not ticket type to update it's price");
			}

			session.update(request);
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

	public void disapprovePriceChangeRequest(int priceChangeRequestId, String handlingUsername) {
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			PriceChangeRequest request = session.get(PriceChangeRequest.class, priceChangeRequestId);

			// Updating
			request.setAccepted(false);
			request.setHandler(ControllerUtils.getUser(handlingUsername, session));

			session.update(request);
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

	private List<PriceChangeRequest> getAllPriceChangeRequests(Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<PriceChangeRequest> query = builder.createQuery(PriceChangeRequest.class);
		query.from(PriceChangeRequest.class);
		return session.createQuery(query).getResultList();
	}
}