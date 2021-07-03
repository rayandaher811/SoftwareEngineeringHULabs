package org.sertia.server.bl;

import org.hibernate.Session;
import org.sertia.contracts.price.change.request.BasicPriceChangeRequest;
import org.sertia.server.SertiaException;
import org.sertia.server.dl.DbUtils;
import org.sertia.server.dl.HibernateSessionFactory;
import org.sertia.server.dl.classes.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.NotSupportedException;
import java.util.LinkedList;
import java.util.List;

public class PriceChangeController {

	public PriceChangeController() {
		List<VouchersInfo> vouchersInfoTable = DbUtils.getAll(VouchersInfo.class);

		// Initializing the vouchers info table if its not initialized
		if(vouchersInfoTable.size() == 0){
			try(Session session = HibernateSessionFactory.getInstance().openSession()){
				VouchersInfo vouchersInfo = new VouchersInfo();
				vouchersInfo.setId(VouchersInfo.singleRecordId);
				vouchersInfo.setVoucherInitialBalance(20);
				vouchersInfo.setPrice(0);

				session.beginTransaction();
				session.save(vouchersInfo);
				session.flush();
				session.clear();
				session.getTransaction().commit();
			} finally {

			}
		}
	}

	public void requestPriceChange(BasicPriceChangeRequest priceChangeRequest, String username) throws Exception {
		if(priceChangeRequest.newPrice < 0)
			throw new SertiaException("Price cannot be negative number.");

		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			PriceChangeRequest request = new PriceChangeRequest();
			request.setRequester(DbUtils.getUserByUsername(username));
			request.setAccepted(false);
			request.setMovie(MoviesCatalogController.getMovieById(priceChangeRequest.movieId, session));
			request.setTicketType(Utils.clientTicketTypeToDL(priceChangeRequest.clientTicketType));
			request.setNewPrice(priceChangeRequest.newPrice);

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
				if(!request.isAccepted() && request.getHandler() == null)
					clientRequests.add(new BasicPriceChangeRequest(request.getId(),
																	request.getMovie().getId(),
																	request.getRequester().getUsername(),
																	Utils.dlTicketTypeToClient(request.getTicketType()),
																	request.getNewPrice(),
																	false));
			}

			return clientRequests;
		} finally {
		}
	}

	public void approveRequest(int priceChangeRequestId, String handlingUsername) throws Exception{
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			PriceChangeRequest request = getPriceChangeRequest(priceChangeRequestId, session);

			// Updating
			request.setAccepted(true);
			request.setHandler(DbUtils.getUserByUsername(handlingUsername));

			session.beginTransaction();

			switch (request.getTicketType()) {
				case Streaming :
					// Updating the streaming properly
					Streaming streaming = session.get(Streaming.class, request.getMovie().getId());
					if(streaming == null)
						throw new SertiaException("There are no such streaming with the id " + request.getMovie().getId());

					streaming.setExtraDayPrice(request.getNewPrice());
					session.update(streaming);
					break;
				case Screening:
					// Updating the screenable movie properly
					ScreenableMovie screenableMovie = session.get(ScreenableMovie.class, request.getMovie().getId());
					if(screenableMovie == null)
						throw new SertiaException("There are no such screenable movie with the id " + request.getMovie().getId());

					screenableMovie.setTicketPrice(request.getNewPrice());
					session.update(screenableMovie);
					break;
				case Voucher:
					// Updating the voucher info
					VouchersInfo vouchersInfo = session.get(VouchersInfo.class, VouchersInfo.singleRecordId);
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

	public void disapprovePriceChangeRequest(int priceChangeRequestId, String handlingUsername) throws SertiaException {
		Session session = null;

		try {
			session = HibernateSessionFactory.getInstance().openSession();

			PriceChangeRequest request = getPriceChangeRequest(priceChangeRequestId, session);

			// Updating
			request.setAccepted(false);
			request.setHandler(DbUtils.getUserByUsername(handlingUsername));

			session.beginTransaction();
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

	private PriceChangeRequest getPriceChangeRequest(int priceChangeRequestId, Session session) throws SertiaException {
		PriceChangeRequest request = session.get(PriceChangeRequest.class, priceChangeRequestId);

		if(request == null)
			throw new SertiaException("THere are no such requests with the " + priceChangeRequestId + " Id");
		return request;
	}

	private List<PriceChangeRequest> getAllPriceChangeRequests(Session session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<PriceChangeRequest> query = builder.createQuery(PriceChangeRequest.class);
		query.from(PriceChangeRequest.class);
		return session.createQuery(query).getResultList();
	}
}