package com.example.demo.model;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyVO {
	int id;
	String name;
	String reg_num;
	String phone;
	String mobile;
	String fax;
	int isCorporate;
	String corporate_num;
	String president_name;
	String president_reg;
	String address;
	String postcode;
	String address_detail;
	String email;
	String charger_name;
	String charger_phone;
	String charger_email;
	String charger_fax;
	int reg_photo;
	int corporate_reg_photo;
	Date wdate;
	Date udate;
	
	@Builder
	public CompanyVO(int id, String name, String reg_num, String phone,
			String mobile, String fax, int isCorporate, String corporate_num,
			String president_name, String president_reg, String address, String postcode,
			String address_detail, String email, String charger_name, String charger_phone,
			String charger_email, String charger_fax) {
		
	}
}
