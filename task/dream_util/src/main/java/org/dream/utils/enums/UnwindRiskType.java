package org.dream.utils.enums;

public enum UnwindRiskType {
	UNWIND_RISK_START(1,"风控开始"),
	UNWIND_RISK_END(0,"风控终止"),
	UNWIND_RISK_UPDATE(2,"风控更新");
	public Integer value;
	public String  show;
	private UnwindRiskType(Integer value,String show){
		this.value=value;
		this.show=show;
	}
}
