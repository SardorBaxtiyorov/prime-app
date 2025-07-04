package org.exp.primeapp.models.dto.request;

import lombok.Builder;

@Builder
public record CategoryReq(
        String name,
        Long spotlightId,
        //Long imageId,
        Boolean active
) {
}
