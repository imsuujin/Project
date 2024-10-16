package com.shop.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotEmpty(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 4, max = 8, message = "비밀번호는 4자 이상, 8자 이하로 입력해주세요")
    private String password;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String phone;

    private String streetAddress;

    @NotEmpty(message = "주소를 입력해주세요.")
    private String detailAddress;
}
