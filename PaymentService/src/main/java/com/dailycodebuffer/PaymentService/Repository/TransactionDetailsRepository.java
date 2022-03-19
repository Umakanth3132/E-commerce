package com.dailycodebuffer.PaymentService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailycodebuffer.PaymentService.Entity.TransactionDetails;
@Repository
public interface TransactionDetailsRepository extends JpaRepository<TransactionDetails, Long>{

	TransactionDetails findByOrderId(long orderId);
}
