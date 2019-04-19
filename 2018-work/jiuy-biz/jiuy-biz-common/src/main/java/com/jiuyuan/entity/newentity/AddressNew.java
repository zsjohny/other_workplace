package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-07
 */
@TableName("store_Address")
public class AddressNew extends Model<AddressNew> {

    private static final long serialVersionUID = 1L;

    /**
     * 地址id
     */
	@TableId(value="AddrId", type= IdType.AUTO)
	private Integer AddrId;
    /**
     * 关联的用户id
     */
	private Long StoreId;
    /**
     * 收件人名称
     */
	private String ReceiverName;
    /**
     * 省编码
     */
	private String ProvinceName;
    /**
     * 市编码
     */
	private String CityName;
    /**
     * 区/县编码
     */
	private String DistrictName;
    /**
     * 详细地址
     */
	private String AddrDetail;
    /**
     * 邮政编码
     */
	private String MailCode;
    /**
     * 手机号码
     */
	private String Telephone;
    /**
     * 固话，以-横杠作为分隔符
     */
	private String FixPhone;
    /**
     * 完整地址
     */
	private String AddrFull;
    /**
     * 状态0-可用
     */
	private Integer Status;
    /**
     * 默认标识0-当前为默认，非0标识不是默认，并按照数字先后作为排序依据
     */
	private Integer IsDefault;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;


	public Integer getAddrId() {
		return AddrId;
	}

	public void setAddrId(Integer AddrId) {
		this.AddrId = AddrId;
	}

	public Long getStoreId() {
		return StoreId;
	}

	public void setStoreId(Long StoreId) {
		this.StoreId = StoreId;
	}

	public String getReceiverName() {
		return ReceiverName;
	}

	public void setReceiverName(String ReceiverName) {
		this.ReceiverName = ReceiverName;
	}

	public String getProvinceName() {
		return ProvinceName;
	}

	public void setProvinceName(String ProvinceName) {
		this.ProvinceName = ProvinceName;
	}

	public String getCityName() {
		return CityName;
	}

	public void setCityName(String CityName) {
		this.CityName = CityName;
	}

	public String getDistrictName() {
		return DistrictName;
	}

	public void setDistrictName(String DistrictName) {
		this.DistrictName = DistrictName;
	}

	public String getAddrDetail() {
		return AddrDetail;
	}

	public void setAddrDetail(String AddrDetail) {
		this.AddrDetail = AddrDetail;
	}

	public String getMailCode() {
		return MailCode;
	}

	public void setMailCode(String MailCode) {
		this.MailCode = MailCode;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String Telephone) {
		this.Telephone = Telephone;
	}

	public String getFixPhone() {
		return FixPhone;
	}

	public void setFixPhone(String FixPhone) {
		this.FixPhone = FixPhone;
	}

	
	 public String getAddrFull() {
	        if (StringUtils.isNotBlank(AddrFull)) {
	            return AddrFull;
	        }
	        StringBuilder builder = new StringBuilder();
	        builder.append(getProvinceName());
	        if (!StringUtils.equals(getProvinceName(), getCityName())) {
	            builder.append(getCityName());
	        }
	        builder.append(getDistrictName()).append(getAddrDetail());
	        return builder.toString();
	    }

	public void setAddrFull(String AddrFull) {
		this.AddrFull = AddrFull;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Integer getIsDefault() {
		return IsDefault;
	}

	public void setIsDefault(Integer IsDefault) {
		this.IsDefault = IsDefault;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.AddrId;
	}
		/**
		 * 获取组拼收货信息
		 * @return
		 */
	 public String getExpressInfo() {
	        StringBuilder builder = new StringBuilder();
	        builder.append(getReceiverName());
	        builder.append(", ");
	        builder.append(StringUtils.defaultString(getTelephone(), getFixPhone()));
	        builder.append(", ");
	        builder.append(getAddrFull());
	        if (StringUtils.isNotBlank(getMailCode())) {
	            builder.append(", ");
	            builder.append(getMailCode());
	        }
	        return builder.toString();
	    }

}
