package com.example.fintech.day6.webmvctest;

import java.math.BigDecimal;
import java.util.List;

/** Transfer service interface — mocked in @WebMvcTest. */
public interface TransferService {

    TransferResponse create(String fromUserId, String toUserId,
                            BigDecimal amount, String currency, String reference);

    TransferResponse findById(String transferId);

    List<TransferResponse> findByUserId(String userId);

    void cancel(String transferId);
}
