package org.sertia.contracts.user.login;

import java.io.Serializable;

public enum UserRole implements Serializable {
    None,
    CostumerSupport,
    CinemaManager,
    BranchManager,
    MediaManager,
}
