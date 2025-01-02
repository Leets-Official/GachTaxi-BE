package com.gachtaxi.domain.members.entity;

import com.gachtaxi.domain.members.entity.enums.Role;
import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Getter
@Entity
public class Members extends BaseEntity {

    private String email;

    private String real_name;

    private String nickName;

    private String gender;

    private String phoneNumber;

    private String profile_picture;

    private Long kakaoId;

    private Long googleId;

    @Enumerated(EnumType.STRING)
    private Role role;

    boolean marketing_agreement;

    boolean privacy_agreement;

    boolean terms_agreement;

    boolean two_factor_authentication;

    /*
    * 추가할 사항
    * blackList
    * notification
    * friend_info
    * */

}
