package com.xmdevelopments.rest.security;

import org.apache.log4j.Logger;
import com.google.common.base.Optional;

public class WsseCredentials {

	final static Logger logger = Logger.getLogger(SecurityConfig.class);
	
	private Optional<String> passwordDigest;
	
	private Optional<String> nonce;
	
	private Optional<String> created;

	public WsseCredentials(Optional<String> passwordDigest, Optional<String> nonce, Optional<String> created) {
				
		//logger.info("Password digest: " + passwordDigest.toString());
		//logger.info("Nonce: " + nonce.toString());
		//logger.info("Created: " + created.toString());
		
		this.passwordDigest = passwordDigest;		
		this.nonce = nonce;		
		this.created = created;
	}
	

	public Optional<String> getPasswordDigest() {
		return passwordDigest;
	}


	public void setPasswordDigest(Optional<String> passwordDigest) {
		this.passwordDigest = passwordDigest;
	}


	public Optional<String> getNonce() {
		return nonce;
	}

	public void setNonce(Optional<String> nonce) {
		this.nonce = nonce;
	}

	public Optional<String> getCreated() {
		return created;
	}

	public void setCreated(Optional<String> created) {
		this.created = created;
	}



	
	
	
}