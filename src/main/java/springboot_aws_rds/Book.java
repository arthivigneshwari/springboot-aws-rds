package springboot_aws_rds;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
	
    @Id
    @GeneratedValue
	private int id;
    private String name;
    private double price;	

}
