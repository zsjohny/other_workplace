package com.reliable.domain

/**
 * Created by nessary on 16-5-7.
 */
class User {

    private String phone;
    private String pass;
    private String sex;
    private Integer age;
    private String isSupport;

    String getPhone() {
        return phone
    }

    void setPhone(String phone) {
        this.phone = phone
    }

    String getPass() {
        return pass
    }

    void setPass(String pass) {
        this.pass = pass
    }

    String getSex() {
        return sex
    }

    void setSex(String sex) {
        this.sex = sex
    }

    Integer getAge() {
        return age
    }

    void setAge(Integer age) {
        this.age = age
    }

    String getIsSupport() {
        return isSupport
    }

    void setIsSupport(String isSupport) {
        this.isSupport = isSupport
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", pass='" + pass + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", isSupport='" + isSupport + '\'' +
                '}';
    }
}
