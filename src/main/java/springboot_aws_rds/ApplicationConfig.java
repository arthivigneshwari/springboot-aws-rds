package springboot_aws_rds;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.google.gson.Gson;

/*import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;*/

@Configuration
public class ApplicationConfig {
	
		@Value("${cloud.aws.credentials.access-key}")
		private String accessKey;
		
		@Value("${cloud.aws.credentials.secret-key}")
		private String secretKey;
	
		private Gson gson = new Gson(); 
		
		@Bean
		public DataSource dataSource() {
			AwsSecrets secrets = getSecret();
			return DataSourceBuilder
					.create()
					.driverClassName("org.postgresql.Driver")
					.url("jdbc:"+secrets.getEngine()+"ql"+"://"+secrets.getHost()+":"+secrets.getPort()+"/artvignedb")
					.username(secrets.getUsername())
					.password(secrets.getPassword())
					.build();			
		}
	
		private AwsSecrets getSecret() {

	    String secretName = "artvigne-db-credentials";
	    //Region region = Region.of("ap-south-1");
	    String region = "ap-south-1";
	    String secret;

	    // Create a Secrets Manager client
	    
	   /* SecretsManagerClient client = SecretsManagerClient.builder()
	            .region(region)	           
	            .build();*/
	    
	    AWSSecretsManager client = AWSSecretsManagerClientBuilder
	    		.standard()
	    		.withRegion(region)
	    		.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
	    		.build();

	    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
	            .withSecretId(secretName);	         

	    GetSecretValueResult getSecretValueResult = null;

	    try {
	        getSecretValueResult = client.getSecretValue(getSecretValueRequest);
	    } catch (Exception e) {
	        throw e;
	    }

	    if(getSecretValueResult.getSecretString() != null) {
	    	secret = getSecretValueResult.getSecretString();
	    	return gson.fromJson(secret, AwsSecrets.class);
	    }
	    return null;
	    
	}		

}
