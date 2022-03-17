package com.dailycodebuffer.OrderService.Service;

import com.dailycodebuffer.OrderService.Model.OrderRequest;
import com.dailycodebuffer.OrderService.Model.OrderResponse;

public interface OrderService {

	long placeOrder(OrderRequest orderRequest);

	OrderResponse getOrderDetails(long orderId);
	
	
	

}
