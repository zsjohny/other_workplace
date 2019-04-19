package com.jiuyuan.util;

public interface CommonConstant {
	/***********
	 * 编码
	 * @author zhuliming
	 */
	public static enum Encoding{
		UTF8("UTF-8"),
		GBK("GBK"),
		ISO88591("ISO-8859-1");
		public final String type;
		private Encoding(final String _encoding){
			type = _encoding;
		}

	}

    public static  enum SaleState {
        WaitSale(0),
        Saling(1),
        SaleFinish(2),
        Unknown(1000);

        public final int value;

        private SaleState(final int value) {
            this.value = value;
        }

        public static SaleState parse(int value) {
            for(SaleState state : SaleState.values()) {
                if(state.value == value)
                    return state;
            }
            return Unknown;
        }
    }
    public static  enum SaleCurrency {
        RMB(0),
        JiuBi(1),
        Unknown(1000);

        public final int value;

        private SaleCurrency(final int value) {
            this.value = value;
        }

        public static SaleCurrency parse(int value) {
            for(SaleCurrency state : SaleCurrency.values()) {
                if(state.value == value)
                    return state;
            }
            return Unknown;
        }
    }

    /**
     * 快递类型
     * */
    public static  enum ExpressType {
        SF(0),
        EMS(1),
        PinYou(2),
        Unknown(1000);

        public final int value;

        private ExpressType(final int value) {
            this.value = value;
        }

        public static ExpressType parse(int value) {
            for(ExpressType state : ExpressType.values()) {
                if(state.value == value)
                    return state;
            }
            return Unknown;
        }
    }


}
