package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.RelatedTransactionsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity RelatedTransactions and its DTO RelatedTransactionsDTO.
 */
@Mapper(componentModel = "spring", uses = {TransactionInfoMapper.class})
public interface RelatedTransactionsMapper extends EntityMapper<RelatedTransactionsDTO, RelatedTransactions> {

    @Mapping(source = "transactionInfo.id", target = "transactionInfoId")
    RelatedTransactionsDTO toDto(RelatedTransactions relatedTransactions);

    @Mapping(source = "transactionInfoId", target = "transactionInfo")
    RelatedTransactions toEntity(RelatedTransactionsDTO relatedTransactionsDTO);

    default RelatedTransactions fromId(Long id) {
        if (id == null) {
            return null;
        }
        RelatedTransactions relatedTransactions = new RelatedTransactions();
        relatedTransactions.setId(id);
        return relatedTransactions;
    }
}
