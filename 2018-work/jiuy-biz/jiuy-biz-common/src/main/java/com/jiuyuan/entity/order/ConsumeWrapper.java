package com.jiuyuan.entity.order;

public class ConsumeWrapper {

	//市场总价，老数据是包含 * 100 单位为分
    private int originalAmountInCents;

    //实付玖币
    private int unavalCoinUsed;
    
    //实付金额(包含邮费)
    private double cash;

    public int getOriginalAmountInCents() {
        return originalAmountInCents;
    }

    public void setOriginalAmountInCents(int originalAmountInCents) {
        this.originalAmountInCents = originalAmountInCents;
    }

    public int getUnavalCoinUsed() {
        return unavalCoinUsed;
    }

    public void setUnavalCoinUsed(int unavalCoinUsed) {
        this.unavalCoinUsed = unavalCoinUsed;
    }

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}
    
}
