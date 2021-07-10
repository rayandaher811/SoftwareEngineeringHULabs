package org.sertia.client.views.unauthorized.purchase;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.sertia.client.App;
import org.sertia.client.controllers.ClientCovidRegulationsControl;
import org.sertia.client.controllers.ClientPurchaseControl;
import org.sertia.client.global.MovieHolder;
import org.sertia.client.global.NumberOfTicketsHolder;
import org.sertia.client.global.ScreeningHolder;
import org.sertia.client.global.SeatsHolder;
import org.sertia.client.views.unauthorized.BasicPresenterWithValidations;
import org.sertia.contracts.screening.ticket.VoucherDetails;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithCovidRequest;
import org.sertia.contracts.screening.ticket.request.ScreeningTicketWithSeatsRequest;
import org.sertia.contracts.screening.ticket.response.ScreeningPaymentResponse;
import org.sertia.contracts.screening.ticket.response.VoucherBalanceResponse;

import java.io.IOException;
import java.util.List;

public class ByPrepaidCardFormPresenter extends BasicPresenterWithValidations {
    public TextField voucherIdTxt;
    public TextField voucherClientId;

    public void payWithVoucher() {
        if (isInputValid()) {
            int voucherId = Integer.parseInt(voucherIdTxt.getText());
            VoucherBalanceResponse response =
                    ClientPurchaseControl.getInstance().requestVoucherBalance(voucherId, voucherClientId.getText());
            if (response.isSuccessful) {
                int currentBalance = response.balance;
                if (currentBalance < NumberOfTicketsHolder.getInstance().getNumberOfTickets()) {
                    Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
                    missingKeyFieldsAlert.setTitle("אין מספיק יתרה בכרטיסיה");
                    missingKeyFieldsAlert.setContentText("אנו מתנצלים, איך היתרה בכרטיסיה הוא כרגע " + currentBalance);
                    missingKeyFieldsAlert.show();
                } else {
                    VoucherDetails voucherDetails = new VoucherDetails(voucherClientId.getText(), voucherId);
                    if (ClientCovidRegulationsControl.getInstance().getCovidRegulationsStatus().isActive) {
                        purchaseWithCovid(voucherDetails);
                    } else {
                        purchaseInNormalTime(voucherDetails);
                    }
                }
            } else {
                Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
                missingKeyFieldsAlert.setTitle("שגיאה בעת שימוש בכרטיסיה");
                missingKeyFieldsAlert.setContentText("אנו מתנצלים, לא הצלחנו לקבל מידע על הכרטיסיה:\n " + response.failReason);
                missingKeyFieldsAlert.show();
            }
        } else {
            notifyClient();
        }
    }

    private void purchaseWithCovid(VoucherDetails voucherDetails) {
        ScreeningTicketWithCovidRequest request = new ScreeningTicketWithCovidRequest(ScreeningHolder.getInstance().getScreening().screeningId, voucherDetails, NumberOfTicketsHolder.getInstance().getNumberOfTickets());
        ScreeningPaymentResponse response =
                ClientPurchaseControl.getInstance().purchaseScreeningTicketsWithCovid(request);
        handlePaymentResponse(response);
    }

    private void purchaseInNormalTime(VoucherDetails voucherDetails) {
        List<Integer> selectedSeats = SeatsHolder.getInstance().getUserSelection();
        ScreeningTicketWithSeatsRequest request = new ScreeningTicketWithSeatsRequest(ScreeningHolder.getInstance().getScreening().screeningId, voucherDetails, selectedSeats);
        ScreeningPaymentResponse response =
                ClientPurchaseControl.getInstance().purchaseScreeningTicketsWithSeats(request);
        handlePaymentResponse(response);
    }

    private void handlePaymentResponse(ScreeningPaymentResponse response) {
        Alert.AlertType type;
        String msg = "";
        if (response.isSuccessful) {
            type = Alert.AlertType.INFORMATION;
            if(response.isVoucher) {
                msg = "הרכישה הסתיימה, פרטי הכרטיסים ויתרת הכרטיסיה נשלחו לבעל הכרטיסיה";
            } else {
                msg = "הרכישה הסתיימה, פרטי הכרטיסים נשלחו לבעל הכרטיס";
            }
            Alert errorAlert = new Alert(type);
            errorAlert.setTitle("Buying from sertia system");
            errorAlert.setContentText(msg);
            errorAlert.showAndWait();
            try {
                App.setRoot("unauthorized/primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            type = Alert.AlertType.ERROR;
            msg = response.failReason;
            Alert errorAlert = new Alert(type);
            errorAlert.setTitle("Buying from sertia system");
            errorAlert.setContentText(msg);
            errorAlert.showAndWait();
        }
    }

    @Override
    protected boolean isDataValid() {
        return isIdCorrect(voucherClientId.getText()) &&
                isPurchaseIdValid(voucherIdTxt.getText());
    }

    public void notifyClient() {
        Alert missingKeyFieldsAlert = new Alert(Alert.AlertType.ERROR);
        missingKeyFieldsAlert.setTitle("Payment with voucher");
        missingKeyFieldsAlert.setContentText("Please insert valid voucher id");
        missingKeyFieldsAlert.show();
    }

    public void toMain(ActionEvent actionEvent) {
        try {
            App.setRoot("unauthorized/payment/selectionMethodForm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
        @FXML
    private void checkBalance(){
        if (isInputValid()) {

            if (!response.isSuccessful) {
                Alert.AlertType type;
                String msg = "";
                if (response.isSuccessful){
                    type = Alert.AlertType.INFORMATION;
                    msg = "Prepaid tickets bought successfully!";
                } else {
                    type = Alert.AlertType.ERROR;
                    msg = response.failReason;
                }
                Alert errorAlert = new Alert(type);
                errorAlert.setTitle("Buying prepaid tickets from sertia system");
                errorAlert.setContentText(msg);
                errorAlert.showAndWait();
            } else {
                balanceTxt.setText(String.valueOf(response.balance));
            }
        }
    }
     */
}
