package com.gachtaxi.domain.members.entity;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.members.dto.request.MemberAgreementRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberInfoRequestDto;
import com.gachtaxi.domain.members.dto.request.MemberSupplmentRequestDto;
import com.gachtaxi.domain.members.entity.enums.Gender;
import com.gachtaxi.domain.members.entity.enums.Role;
import com.gachtaxi.domain.members.entity.enums.UserStatus;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Objects;

@Getter
@Entity
@SuperBuilder
@Table(name="members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Members extends BaseEntity {

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "student_number", unique = true)
    private Long studentNumber;

    @Column(name = "phone_number", unique = true) // 피그마 참고, 일단 null 허용
    private String phoneNumber;

    @Column(name = "account_number", unique = true)
    @Setter
    private String accountNumber;

    @Column(name = "kakao_id", unique = true)
    private Long kakaoId;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.MEMBER;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus status = UserStatus.INACTIVE;

    // 이용 약관 동의
    @Column(name = "terms_agreement")
    @ColumnDefault("false")
    @Builder.Default
    private Boolean termsAgreement = false;

    // 개인정보 수집 동의
    @Column(name = "privacy_agreement")
    @ColumnDefault("false")
    @Builder.Default
    private Boolean privacyAgreement = false;

    // 광고성 정보 수신 동의
    @Column(name = "marketing_agreement")
    @ColumnDefault("false")
    @Builder.Default
    private Boolean marketingAgreement = false;

    // 2차 인증 (전화번호)
    @Column(name = "two_factor_authentication")
    @ColumnDefault("false")
    @Builder.Default
    private Boolean twoFactorAuthentication = false;

    @Column(name = "fcm_token")
    private String fcmToken;

    /*
    * 추가할 사항
    * blackList
    * notification
    * friend_info
    * */

    public boolean hasKakaoId(){
        return kakaoId != null;
    }

    public boolean hasGoogleId(){
        return googleId != null;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateKakaoId(Long kakaoId) {
        this.kakaoId = kakaoId;
    }

    public void updateGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void updateMemberInfo(MemberInfoRequestDto dto) {
        this.profilePicture = dto.profilePicture();
        this.nickname = dto.nickName();
    }

    public void updateAgreement(MemberAgreementRequestDto dto) {
        this.termsAgreement = dto.termsAgreement();
        this.privacyAgreement = dto.privacyAgreement();
        this.marketingAgreement = dto.marketingAgreement();
    }

    public void updateToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateSupplment(MemberSupplmentRequestDto dto) {
        this.profilePicture = dto.profilePicture();
        this.nickname = dto.nickname();
        this.realName = dto.realName();
        this.studentNumber = dto.studentNumber();
        this.gender = dto.gender();
        this.status = UserStatus.ACTIVE;
    }

    public static Members ofKakaoId(Long kakaoId){
        return Members.builder()
                .kakaoId(kakaoId)
                .build();
    }

    public boolean isRoomMaster(MatchingRoom matchingRoom){
        return this.equals(matchingRoom.getRoomMaster());
    }

    public void delete() {
        this.status = UserStatus.DELETED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Members members = (Members) o;
        return Objects.equals(studentNumber, members.studentNumber) && Objects.equals(
            kakaoId, members.kakaoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber, kakaoId);
    }

    public static Members ofGoogleId(String googleId){
        return Members.builder()
                .googleId(googleId)
                .build();
    }
}
