package com.jiuyuan.entity;

import java.io.Serializable;

public class TestProduct implements Serializable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4136832931906545329L;
		private long id;

		private String name;
		public TestProduct() {	
			
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		} 
	}