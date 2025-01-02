package com.gachtaxi.domain.members.entity;

import com.gachtaxi.global.common.entity.BaseEntity;
import jakarta.persistence.Entity;

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
