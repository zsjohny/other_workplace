package com.e_commerce.miscroservice.store.entity.vo;


import java.io.Serializable;


/**
 * 会员
 * @author Administrator
 *
 */
public class MemberDetail implements  Serializable {

    private static final long serialVersionUID = -1104663920836940976L;

    private ShopMember member;


    public ShopMember getMember() {
        return member;
    }

    public void setMember(ShopMember member) {
        this.member = member;
    }

    public long getId() {
        return getMember() == null ? 0 : getMember().getId();
    }

   

    @Override
    public String toString() {
        return "" + getId();
    }
}
