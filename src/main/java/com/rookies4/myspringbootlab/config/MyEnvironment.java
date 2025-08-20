package com.rookies4.myspringbootlab.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class MyEnvironment {
   	String mode;  	
}