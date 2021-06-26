package org.sertia.server.communication;

import org.sertia.contracts.complaints.requests.CloseComplaintRequest;
import org.sertia.contracts.complaints.requests.GetAllUnhandledComplaintsRequest;
import org.sertia.contracts.complaints.requests.PurchaseCancellationFromComplaintRequest;
import org.sertia.contracts.movies.catalog.request.*;
import org.sertia.contracts.price.change.request.ApprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.ClientPriceChangeRequest;
import org.sertia.contracts.price.change.request.DissapprovePriceChangeRequest;
import org.sertia.contracts.price.change.request.GetUnapprovedPriceChangeRequests;
import org.sertia.contracts.user.login.UserRole;

public class RoleValidator {

    public boolean isClientAllowed(UserRole role, Class<?> requestType) {
        if (isMediaManagerOperation(requestType)) {
            return role == UserRole.MediaManager;
        }

        if (isCustomerSupportOperation(requestType)) {
            return role == UserRole.CostumerSupport;
        }

        if (isCinemaManagerOperation(requestType)) {
            return role == UserRole.CinemaManager;
        }

        if (isBranchManagerOperation(requestType)) {
            return role == UserRole.BranchManager;
        }

        return true;
    }

    public boolean isMediaManagerOperation(Class<?> requestType) {
        return requestType == ClientPriceChangeRequest.class ||
                requestType == StreamingAdditionRequest.class ||
                requestType == StreamingRemovalRequest.class ||
                requestType == AddScreeningRequest.class ||
                requestType == RemoveScreeningRequest.class ||
                requestType == AddMovieRequest.class ||
                requestType == RemoveMovieRequest.class ||
                requestType == ScreeningUpdateRequest.class;
    }

    public boolean isCustomerSupportOperation(Class<?> requestType) {
        return requestType == CloseComplaintRequest.class ||
                requestType == PurchaseCancellationFromComplaintRequest.class ||
                requestType == GetAllUnhandledComplaintsRequest.class;
    }

    public boolean isCinemaManagerOperation(Class<?> requestType) {
        return false;
    }

    public boolean isBranchManagerOperation(Class<?> requestType) {
        return requestType == GetUnapprovedPriceChangeRequests.class ||
                requestType == ApprovePriceChangeRequest.class ||
                requestType == DissapprovePriceChangeRequest.class;
    }
}