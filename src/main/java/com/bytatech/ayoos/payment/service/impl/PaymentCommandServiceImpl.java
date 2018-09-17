package com.bytatech.ayoos.payment.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bytatech.ayoos.payment.client.paypal.api.PaymentGatewayApi;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentInitiateRequest;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentInitiateResponse;
import com.bytatech.ayoos.payment.client.paypal.model.Refund;
import com.bytatech.ayoos.payment.client.paypal.model.RefundSaleRequest;
import com.bytatech.ayoos.payment.client.paypal.model.RelatedResources;
import com.bytatech.ayoos.payment.domain.Amount;
import com.bytatech.ayoos.payment.domain.AmountDetails;
import com.bytatech.ayoos.payment.domain.CreditCard;
import com.bytatech.ayoos.payment.domain.FundingInstrument;
import com.bytatech.ayoos.payment.domain.Payee;
import com.bytatech.ayoos.payment.domain.Payer;
import com.bytatech.ayoos.payment.domain.Payment;
import com.bytatech.ayoos.payment.domain.RelatedTransactions;
import com.bytatech.ayoos.payment.domain.TransactionInfo;
import com.bytatech.ayoos.payment.domain.enumeration.PaymentMethod;
import com.bytatech.ayoos.payment.domain.enumeration.TransactionType;
import com.bytatech.ayoos.payment.repository.PayerRepository;
import com.bytatech.ayoos.payment.repository.PaymentRepository;
import com.bytatech.ayoos.payment.repository.TransactionInfoRepository;
import com.bytatech.ayoos.payment.repository.search.PaymentSearchRepository;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentDetails;
import com.bytatech.ayoos.payment.client.paypal.model.PaymentExecutionRequest;
import com.bytatech.ayoos.payment.service.PaymentCommandService;
import com.bytatech.ayoos.payment.service.mapper.PaymentMapper;
import com.paypal.api.payments.Patch;

@Service
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

	
	@Autowired
	private PaymentGatewayApi paymentGatewayApi;

	private final PaymentRepository paymentRepository;

	private final PaymentMapper paymentMapper;

	private final PaymentSearchRepository paymentSearchRepository;
	
	private final PayerRepository payerRepository;
	
	private final TransactionInfoRepository transactionInfoRepository;

	
	public PaymentCommandServiceImpl(PaymentRepository paymentRepository, PaymentMapper paymentMapper,
			PaymentSearchRepository paymentSearchRepository,PayerRepository payerRepository,TransactionInfoRepository transactionInfoRepository) {
		this.paymentRepository = paymentRepository;
		this.paymentMapper = paymentMapper;
		this.paymentSearchRepository = paymentSearchRepository;
		this.payerRepository=payerRepository;
		this.transactionInfoRepository=transactionInfoRepository;
	}

	@Override
	public PaymentInitiateResponse initiatePayment(PaymentInitiateRequest paymentInitiateRequest) {
		log.info("pay creates the payment to be execute " + paymentInitiateRequest);
		return paymentGatewayApi.initiatePayment(paymentInitiateRequest);

	}

	@Override
	public void updatePayment(String payment_id, ArrayList<Patch> patchRequest) {
		log.info("update the payment using the payment id " + payment_id + " and the " + patchRequest);
		paymentGatewayApi.updatePayment(payment_id, patchRequest);
	}

	@Override
	public void executePayment(String payment_id, PaymentExecutionRequest paymentExecution) {
		log.info("payment execution request");
		PaymentDetails paymentDetails = paymentGatewayApi.executePayment(payment_id, paymentExecution);
		Payment payment = new Payment();
		payment.setPaymentId(paymentDetails.getId());
		payment.setCreateTime(Instant.parse(paymentDetails.getCreate_time()));
		if (paymentDetails.getUpdate_time() != null)
			payment.setUpdateTime(Instant.parse(paymentDetails.getUpdate_time()));
		payment.setIntent(paymentDetails.getIntent());
		payment.setPaymentGatewayProvider("paypal");
		payment.setState(paymentDetails.getState());
		Payer payer = new Payer();
		payer.setPayerId(paymentDetails.getPayer().getPayer_info().getPayer_id());
		payer.setPaymentMethod(PaymentMethod.valueOf(paymentDetails.getPayer().getPayment_method().toUpperCase()));
		payer.setStatus(paymentDetails.getPayer().getStatus());
		payer.setUserId("abilashs");
		Set<FundingInstrument> fundingInstruments = new HashSet<FundingInstrument>();

		if (paymentDetails.getPayer().getFunding_instruments() != null) {
			FundingInstrument fundingInstrument = new FundingInstrument();
			CreditCard creditCard = new CreditCard();
			creditCard
					.setNumber(paymentDetails.getPayer().getFunding_instruments().get(0).getCredit_card().getNumber());
			creditCard.setCvv2(paymentDetails.getPayer().getFunding_instruments().get(0).getCredit_card().getCvv2());
			creditCard.setExpireMonth(
					paymentDetails.getPayer().getFunding_instruments().get(0).getCredit_card().getExpireMonth());
			creditCard.setExpireYear(
					paymentDetails.getPayer().getFunding_instruments().get(0).getCredit_card().getExpireYear());
			creditCard.setType(paymentDetails.getPayer().getFunding_instruments().get(0).getCredit_card().getType());
			fundingInstrument.setCreditCard(creditCard);
			fundingInstruments.add(fundingInstrument);
			payer.setFundingInstruments(fundingInstruments);
		}

		payment.setPayer(payer);
		TransactionInfo transactionInfo = new TransactionInfo();
		Amount amount = new Amount();
		amount.setCurrency(paymentDetails.getTransactions().get(0).getAmount().getCurrency());
		amount.setTotal(Double.parseDouble(paymentDetails.getTransactions().get(0).getAmount().getTotal()));

		AmountDetails amountDetails = new AmountDetails();
		if (paymentDetails.getTransactions().get(0).getAmount().getDetails().getSubtotal() != null)
			amountDetails.setSubtotal(
					Double.parseDouble(paymentDetails.getTransactions().get(0).getAmount().getDetails().getSubtotal()));
		if (paymentDetails.getTransactions().get(0).getAmount().getDetails().getHandling_fee() != null)
			amountDetails.setHandlingFee(Double
					.parseDouble(paymentDetails.getTransactions().get(0).getAmount().getDetails().getHandling_fee()));
		if (paymentDetails.getTransactions().get(0).getAmount().getDetails().getInsurance() != null)
			amountDetails.setInsurance(Double
					.parseDouble(paymentDetails.getTransactions().get(0).getAmount().getDetails().getInsurance()));
		amountDetails.setOtherFee(12.7);
		if (paymentDetails.getTransactions().get(0).getAmount().getDetails().getTax() != null)
			amountDetails.setTax(
					Double.parseDouble(paymentDetails.getTransactions().get(0).getAmount().getDetails().getTax()));
		if (paymentDetails.getTransactions().get(0).getAmount().getDetails().getShipping_discount() != null)
			amountDetails.setShippingDiscount(Double.parseDouble(
					paymentDetails.getTransactions().get(0).getAmount().getDetails().getShipping_discount()));
		if (paymentDetails.getTransactions().get(0).getAmount().getDetails().getShipping() != null)
			amountDetails.setShipping(
					Double.parseDouble(paymentDetails.getTransactions().get(0).getAmount().getDetails().getShipping()));
		amount.setDetails(amountDetails);

		transactionInfo.setAmount(amount);
		transactionInfo.setDescription(paymentDetails.getTransactions().get(0).getDescription());
		transactionInfo.setInvoiceNumber(paymentDetails.getTransactions().get(0).getInvoice_number());
		transactionInfo.setNoteToPayee(paymentDetails.getTransactions().get(0).getNote_to_payee());
		Payee payee = new Payee();
		payee.setEmail(paymentDetails.getTransactions().get(0).getPayee().getEmail());
		payee.setMerchandId(paymentDetails.getTransactions().get(0).getPayee().getMerchant_id());
		payee.setUserId("lxi");
		transactionInfo.setPayee(payee);
		Set<RelatedTransactions> relatedTransactions = new HashSet<RelatedTransactions>();
		for (RelatedResources resource : paymentDetails.getTransactions().get(0).getRelated_resources()) {
			if (resource.getSale() != null) {
				RelatedTransactions relatedTransaction = new RelatedTransactions();
				if (resource.getSale().getCreate_time() != null)
					relatedTransaction.setCreateTime(Instant.parse(resource.getSale().getCreate_time()));
				if (resource.getSale().getUpdate_time() != null)
					relatedTransaction.setUpdateTime(Instant.parse(resource.getSale().getUpdate_time()));
				relatedTransaction.setIntentId(resource.getSale().getId());
				relatedTransaction.setTransactionType(TransactionType.valueOf("SALE"));
				relatedTransaction.setPaymentId(resource.getSale().getParent_payment());
				relatedTransaction.setPaymentMode(resource.getSale().getPayment_mode());
				relatedTransaction.setReceiptId(resource.getSale().getReceiptId());
				relatedTransaction.setSaleReasonCode(resource.getSale().getReasonCode());
				relatedTransaction.setState(resource.getSale().getState());
				relatedTransactions.add(relatedTransaction);
			}
		}
		transactionInfo.setTransactionDetails(relatedTransactions);
		payment.setTransaction(transactionInfo);
		save(payment);
	}

	@Override
	public void refundSale(String sale_id, RefundSaleRequest refundSaleRequest) {

		Refund refundResponse=paymentGatewayApi.refundSale(sale_id, refundSaleRequest);
		String paymentId=refundResponse.getParent_payment();
		Payment payment=findByPaymentId(paymentId);
		RelatedTransactions refundTransaction=new RelatedTransactions();
		refundTransaction.setCreateTime(Instant.parse(refundResponse.getCreate_time()));
		refundTransaction.setIntentId(refundResponse.getId());
		refundTransaction.setRefundSaleId(refundResponse.getSale_id());
		refundTransaction.setPaymentId(refundResponse.getParent_payment());
		refundTransaction.setDescription(refundResponse.getDescription());
		refundTransaction.setRefundReason(refundResponse.getReason());
		refundTransaction.setTransactionType(TransactionType.valueOf("REFUND"));
		refundTransaction.setState(refundResponse.getState());
		if(refundResponse.getUpdate_time()!=null)
		refundTransaction.setUpdateTime(Instant.parse(refundResponse.getUpdate_time()));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+payment.getTransaction().getTransactionDetails());
		payment.getTransaction().getTransactionDetails().add(refundTransaction);
		save(payment);
		
	}
	
	public Payment findByPaymentId(String  paymentId) {
		return paymentRepository.findByPaymentId(paymentId);
	}

	/* RDBMS Spec Functions */

	/**
	 * Save a payment.
	 *
	 * @param paymentDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public void save(Payment payment) {
		log.debug("Request to save Payment : {}", payment);
		paymentRepository.save(payment);
		paymentSearchRepository.save(payment);

	}

	/**
	 * Delete the payment by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Payment : {}", id);
		paymentRepository.deleteById(id);
		paymentSearchRepository.deleteById(id);
	}

}
