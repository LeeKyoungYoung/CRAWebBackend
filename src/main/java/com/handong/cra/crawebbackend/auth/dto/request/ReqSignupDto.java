package com.handong.cra.crawebbackend.auth.dto.request;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReqSignupDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String githubId;
    private Long studentNumber;
    private String term;
}
