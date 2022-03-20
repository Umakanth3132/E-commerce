package com.dailycodebuffer.CloudGateway.Model;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
	
	private String userId;
	private String accessToken;
	private String refreshToken;
	private long expriesAt;
	private Collection<String> authorityList;

}