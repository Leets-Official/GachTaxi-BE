package com.gachtaxi.domain.members.entity;

import com.gachtaxi.domain.members.dto.request.UserRequestDto;
import com.gachtaxi.domain.members.entity.enums.Gender;
import com.gachtaxi.domain.members.entity.enums.Role;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@SuperBuilder
@Table(name="members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Members extends BaseEntity {

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "real_name", nullable = false)
    private String realName;

    @Column(name = "student_number", nullable = false, unique = true)
    private String studentNumber;

    @Column(name = "phone_number", unique = true) // 피그마 참고, 일단 null 허용
    private String phoneNumber;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "google_id", unique = true)
    private Long googleId;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // 이용 약관 동의
    @Column(name = "terms_agreement", nullable = false)
    @ColumnDefault("true")
    private Boolean termsAgreement;

    // 개인정보 수집 동의
    @Column(name = "privacy_agreement", nullable = false)
    @ColumnDefault("true")
    private Boolean privacyAgreement;

    // 광고성 정보 수신 동의
    @Column(name = "marketing_agreement", nullable = false)
    @ColumnDefault("false")
    private Boolean marketingAgreement;

    // 2차 인증 (전화번호)
    @Column(name = "two_factor_authentication", nullable = false)
    @ColumnDefault("false")
    private Boolean twoFactorAuthentication;

    /*
    * 추가할 사항
    * blackList
    * notification
    * friend_info
    * */

    public static Members of(UserRequestDto.SignUpDto dto){
        return Members.builder()
                //.profilePicture(dto.profilePicture())
                .email(dto.email())
                .nickname(dto.nickName())
                .realName(dto.realName())
                .studentNumber(dto.studentNumber())
                //.phoneNumber(dto.phoneNumber())
                .kakaoId(dto.kakaoId())
                .googleId(dto.googleId())
                .role(Role.MEMBER)
                .gender(dto.gender())
                .termsAgreement(dto.termsAgreement())
                .privacyAgreement(dto.privacyAgreement())
                .marketingAgreement(dto.marketingAgreement())
                .twoFactorAuthentication(dto.twoFactorAuthentication())
                .build();
    }
}
