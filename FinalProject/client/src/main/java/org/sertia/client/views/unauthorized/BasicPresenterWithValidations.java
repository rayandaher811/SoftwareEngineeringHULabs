package org.sertia.client.views.unauthorized;

import org.sertia.client.views.unauthorized.didntuse.BasicPresenter;

public abstract class BasicPresenterWithValidations extends BasicPresenter {
    protected abstract boolean isDataValid();
}
