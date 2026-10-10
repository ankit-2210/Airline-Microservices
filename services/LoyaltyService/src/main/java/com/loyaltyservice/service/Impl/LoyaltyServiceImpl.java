package com.loyaltyservice.service.Impl;

import com.airlineportal.payload.request.Loyalty.AdjustPointsRequest;
import com.airlineportal.payload.request.Loyalty.EarnPointsRequest;
import com.airlineportal.payload.request.Loyalty.RedeemPointsRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyAccountResponse;
import com.airlineportal.payload.response.Loyalty.LoyaltyTransactionResponse;
import com.airlineportal.utils.Loyalty.LoyaltyTransactionType;
import com.loyaltyservice.external.ExternalService;
import com.loyaltyservice.helper.LoyaltyHelper;
import com.loyaltyservice.mapper.LoyaltyMapper;
import com.loyaltyservice.model.LoyaltyAccount;
import com.loyaltyservice.model.LoyaltyTransaction;
import com.loyaltyservice.repository.LoyaltyAccountRepository;
import com.loyaltyservice.repository.LoyaltyTransactionRepository;
import com.loyaltyservice.service.LoyaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoyaltyServiceImpl implements LoyaltyService {
    private final LoyaltyAccountRepository loyaltyAccountRepository;
    private final LoyaltyTransactionRepository loyaltyTransactionRepository;
    private final LoyaltyHelper loyaltyHelper;
    private final ExternalService externalService;

    @Override
    @Transactional
    public LoyaltyAccountResponse createAccount(Long userId) {
        if(userId == null){
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if(loyaltyAccountRepository.existsByUserId(userId)){
            throw new IllegalStateException("Loyalty account already exists " + "for user ID: " + userId);
        }

        LoyaltyAccount account = LoyaltyMapper.toEntity(userId);
        LoyaltyAccount saved = loyaltyAccountRepository.save(account);
        return LoyaltyMapper.toAccountResponse(saved);
    }

    @Override
    public LoyaltyAccountResponse getAccountByUserId(Long userId) {
        return LoyaltyMapper.toAccountResponse(loyaltyHelper.findAccountByUserId(userId));
    }

    @Override
    @Transactional
    public LoyaltyAccountResponse earnPoints(EarnPointsRequest request) {
        // 3. Get booking
        ApiResponse<BookingResponse> bookingResponse = externalService.getBookingById(request.getBookingId());
        if(bookingResponse == null || bookingResponse.getData() == null){
            throw new IllegalArgumentException("Unable to retrieve booking");
        }
        BookingResponse booking = bookingResponse.getData();

        loyaltyHelper.validateBookingForPoints(booking, request.getBookingId());
        if(loyaltyHelper.pointsAlreadyEarned(request.getBookingId())){
            throw new IllegalStateException("Loyalty points have already been " + "earned for booking ID: " + request.getBookingId());
        }

        LoyaltyAccount account = loyaltyHelper.findAccountByUserId(request.getUserId());
        long points = loyaltyHelper.calculateEarnedPoints(request.getBookingAmount());
        if(points <= 0){
            throw new IllegalStateException("Booking amount is too low to earn points");
        }

        account.setAvailablePoints(account.getAvailablePoints() + points);
        account.setTotalPoints(account.getTotalPoints() + points);
        account.setLifetimePoints(account.getLifetimePoints() + points);
        account.setLoyaltyTier(loyaltyHelper.calculateTier(account.getLifetimePoints()));
        account.setUpdatedAt(Instant.now());

        LoyaltyAccount savedAccount = loyaltyAccountRepository.save(account);

        LoyaltyTransaction transaction = LoyaltyMapper.toEntityTransaction(savedAccount, request, points);
        loyaltyTransactionRepository.save(transaction);
        return LoyaltyMapper.toAccountResponse(savedAccount);

    }

    @Override
    @Transactional
    public LoyaltyAccountResponse redeemPoints(RedeemPointsRequest request) {
        LoyaltyAccount account = loyaltyHelper.findAccountByUserId(request.getUserId());

        loyaltyHelper.validateRedeemPoints(account, request.getPoints());

        account.setAvailablePoints(account.getAvailablePoints()-request.getPoints());
        account.setUpdatedAt(Instant.now());

        LoyaltyAccount savedAccount = loyaltyAccountRepository.save(account);

        LoyaltyTransaction loyaltyTransaction =  LoyaltyTransaction.builder()
                .loyaltyAccountId(savedAccount.getId())
                .userId(request.getUserId())

                .points(request.getPoints())
                .transactionType(LoyaltyTransactionType.REDEEMED)
                .description("Loyalty points redeemed")

                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        loyaltyTransactionRepository.save(loyaltyTransaction);
        return LoyaltyMapper.toAccountResponse(savedAccount);

    }

    @Override
    @Transactional
    public LoyaltyAccountResponse adjustPoints(AdjustPointsRequest request) {
        LoyaltyAccount account = loyaltyHelper.findAccountByUserId(request.getUserId());

        loyaltyHelper.validateAccountUser(account, request.getUserId());
        long points = request.getPoints();
        if(request.getAdd()){
            account.setAvailablePoints(account.getAvailablePoints()+points);
            account.setTotalPoints(account.getTotalPoints()+points);
            account.setLifetimePoints(account.getLifetimePoints()+points);
        }
        else{
            if(account.getAvailablePoints() < points){
                throw new IllegalStateException("Insufficient points for adjustment");
            }

            account.setAvailablePoints(account.getAvailablePoints() - points);
        }

        account.setLoyaltyTier(loyaltyHelper.calculateTier(account.getLifetimePoints()));
        account.setUpdatedAt(Instant.now());

        LoyaltyAccount savedAccount = loyaltyAccountRepository.save(account);
        LoyaltyTransaction transaction = LoyaltyTransaction.builder()
                        .loyaltyAccountId(savedAccount.getId())
                        .userId(request.getUserId())

                        .points(points)
                        .transactionType(LoyaltyTransactionType.ADJUSTED)
                        .description(request.getDescription())

                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();

        loyaltyTransactionRepository.save(transaction);
        return LoyaltyMapper.toAccountResponse(savedAccount);

    }

    @Override
    public Page<LoyaltyTransactionResponse> getTransactionsByUserId(Long userId, Pageable pageable) {
        return loyaltyTransactionRepository.findByUserId(userId, pageable)
                .map(LoyaltyMapper::toTransactionResponse);
    }

    @Override
    public Page<LoyaltyTransactionResponse> getTransactionsByAccountId(Long accountId, Pageable pageable) {
        return loyaltyTransactionRepository.findByLoyaltyAccountId(accountId, pageable)
                .map(LoyaltyMapper::toTransactionResponse);
    }
}
