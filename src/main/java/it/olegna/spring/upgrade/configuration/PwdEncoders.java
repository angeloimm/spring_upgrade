package it.olegna.spring.upgrade.configuration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
public class PwdEncoders {
	private static final String USR_PWD_ENC_ID = "usrPwdEnc";
	private static final String OAUTH_PWD_ENC_ID = "oauthPwdEnc";
	public static final String USR_PWD_ENC_PREFIX;
	public static final String OAUTH_PWD_ENC_PREFIX;
	static {
		USR_PWD_ENC_PREFIX = "{"+USR_PWD_ENC_ID+"}";
		OAUTH_PWD_ENC_PREFIX = "{"+OAUTH_PWD_ENC_ID+"}";
	}
	@Bean(name="delPwdEnc")
	public PasswordEncoder delegatingPasswordEncoder()
	{
		String encodingId = "bcrypt";
		PasswordEncoder defaultPwdEnc = new BCryptPasswordEncoder();
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(encodingId, defaultPwdEnc);
		encoders.put("ldap", new LdapShaPasswordEncoder());
		encoders.put("MD4", new Md4PasswordEncoder());
		encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
		encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
		encoders.put("sha256", new StandardPasswordEncoder());
		encoders.put(USR_PWD_ENC_ID, new BCryptPasswordEncoder(8));
		encoders.put(OAUTH_PWD_ENC_ID, new BCryptPasswordEncoder(4));
		DelegatingPasswordEncoder result = new DelegatingPasswordEncoder(encodingId, encoders);
		result.setDefaultPasswordEncoderForMatches(defaultPwdEnc);
		return result;
	}
}
