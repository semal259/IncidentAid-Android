package com.cmusv.ias;

public class Directory {

		private String name = null;
		private String contact = null;

		public Directory() {

		}

		public Directory(String name, String contact) {
			this.name = name;
			this.contact = contact;
		}
		
		public void setContact(String contact) {
			this.contact = contact;
		}

		public String getContact() {
			return contact;
		}
		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
}
