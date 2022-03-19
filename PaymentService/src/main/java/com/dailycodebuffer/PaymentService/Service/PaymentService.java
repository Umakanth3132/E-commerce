package com.dailycodebuffer.PaymentService.Service;

import com.dailycodebuffer.PaymentService.Model.PaymentRequest;
import com.dailycodebuffer.PaymentService.Model.PaymentResponse;

public interface PaymentService {

	long doPayment(PaymentRequest paymentRequest);

	PaymentResponse getPaymentDetailsByOrderId(String orderId);

}
