package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.TransactionInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TransactionInfo and its DTO TransactionInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {AmountMapper.class, PayeeMapper.class})
public interface TransactionInfoMapper extends EntityMapper<TransactionInfoDTO, TransactionInfo> {

    @Mapping(source = "amount.id", target = "amountId")
    @Mapping(source = "payee.id", target = "payeeId")
    TransactionInfoDTO toDto(TransactionInfo transactionInfo);

    @Mapping(source = "amountId", target = "amount")
    @Mapping(source = "payeeId", target = "payee")
    @Mapping(target = "transactionDetails", ignore = true)
    TransactionInfo toEntity(TransactionInfoDTO transactionInfoDTO);

    default TransactionInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setId(id);
        return transactionInfo;
    }
}
