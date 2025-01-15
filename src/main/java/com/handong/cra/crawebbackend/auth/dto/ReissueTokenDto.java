package com.handong.cra.crawebbackend.auth.dto;

import com.handong.cra.crawebbackend.auth.dto.request.ReqReissueTokenDto;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReissueTokenDto {
    private Long userId;
    private String refreshToken;

    public ReissueTokenDto(ReqReissueTokenDto reqReissueTokenDto) {
        this.userId = reqReissueTokenDto.getUserId();
        this.refreshToken = reqReissueTokenDto.getRefreshToken();
    }

    public static ReissueTokenDto from(ReqReissueTokenDto reqReissueTokenDto) {
        return new ReissueTokenDto(reqReissueTokenDto);
    }
}
