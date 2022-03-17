package com.dailycodebuffer.OrderService.Service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dailycodebuffer.OrderService.Entity.Order;
import com.dailycodebuffer.OrderService.Exception.CustomException;
import com.dailycodebuffer.OrderService.Model.OrderRequest;
import com.dailycodebuffer.OrderService.Model.OrderResponse;
import com.dailycodebuffer.OrderService.Repository.OrderRepository;
import com.dailycodebuffer.OrderService.external.client.PaymentService;
import com.dailycodebuffer.OrderService.external.client.ProductService;
import com.dailycodebuffer.OrderService.external.request.PaymentRequest;
import com.dailycodebuffer.OrderService.external.response.PaymentResponse;
//import com.dailycodebuffer.ProductService.model.ProductResponse;
import com.dailycodebuffer.ProductService.model.ProductResponse;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public long placeOrder(OrderRequest orderRequest) {
		// Order Entity -> Save the data with Status Order Created
		// Product Service - Block Products (Reduce the Quantity)
		// Payment Service -> Payments->Success-> Complete,Else
		// CANCELLED
		log.info("Placeing Order Request: {}", orderRequest);

		productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

		log.info("Creating Order with Status CREATED");
		Order order = Order.builder().amount(orderRequest.getTotalAmount()).orderStatus("CREATED")
				.productId(orderRequest.getProductId()).orderDate(Instant.now()).quantity(orderRequest.getQuantity())
				.build();
		order = orderRepository.save(order);

		log.info("Calling Payment Service to complete the payment");

		PaymentRequest paymentRequest = PaymentRequest.builder().orderId(order.getId())
				.paymentMode(orderRequest.getPaymentMode()).amount(orderRequest.getTotalAmount()).build();

		String orderStatus = null;

		try {
			paymentService.doPayment(paymentRequest);

			log.info("Payment done Successfully. Changing the Order Status to Placed");
			orderStatus = "PLACED";

		} catch (Exception e) {
			// TODO: handle exception
			log.error("Error occured in payment.Changing order Status to PAYMENT_FAILED");

			orderStatus = "PAYMENT_FAILED";

		}

		order.setOrderStatus(orderStatus);
		orderRepository.save(order);

		log.info("Order Places successfully with Order Id:{}", order.getId());

		return order.getId();
	}

	@Override
	public OrderResponse getOrderDetails(long orderId) {
		log.info("Get order details for the order Id : {}", orderId);

		Order order = orderRepository.findById(orderId).orElseThrow(
				() -> new CustomException("Order not fount for the order Id:" + orderId, "NOT_FOUND", 404));

		log.info("Invoking Product service to fetch the product Details for Id : {}", order.getProductId());

		ProductResponse productResponse = restTemplate
				.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class);
		
		log.info("Getting payment information form the payment Service");
		 PaymentResponse paymentResponse
		 =restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(), PaymentResponse.class);

		OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
				.productName(productResponse.getProductName()).productId(productResponse.getProductId()).build();
		
		OrderResponse.PaymentDetails paymentDetails
		=OrderResponse.PaymentDetails
		.builder()
		.paymentId(paymentResponse.getPaymentId())
		.paymentStatus(paymentResponse.getStatus())
		.paymentDate(paymentResponse.getPaymentDate())
		.paymentMode(paymentResponse.getPaymentMode())
		.build();

		OrderResponse orderResponse = OrderResponse.builder().orderId(order.getId()).orderDate(order.getOrderDate())
				.amount(order.getAmount()).orderStatus(order.getOrderStatus()).productDetails(productDetails)
				.paymentDetails(paymentDetails)
				.build();
		return orderResponse;
	}

}
