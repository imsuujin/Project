package com.shop.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "올바른 이메일 주소를 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요.")
    private String password;

    @NotEmpty(message = "전화번호는 필수 입력 값입니다.")
    private String phone;

    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String address;

    private String streetAddress;

    private String detailAddress;


}
