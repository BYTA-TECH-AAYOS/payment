package com.bytatech.ayoos.payment.service.mapper;

import com.bytatech.ayoos.payment.domain.*;
import com.bytatech.ayoos.payment.service.dto.AmountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Amount and its DTO AmountDTO.
 */
@Mapper(componentModel = "spring", uses = {AmountDetailsMapper.class})
public interface AmountMapper extends EntityMapper<AmountDTO, Amount> {

    @Mapping(source = "details.id", target = "detailsId")
    AmountDTO toDto(Amount amount);

    @Mapping(source = "detailsId", target = "details")
    Amount toEntity(AmountDTO amountDTO);

    default Amount fromId(Long id) {
        if (id == null) {
            return null;
        }
        Amount amount = new Amount();
        amount.setId(id);
        return amount;
    }
}
