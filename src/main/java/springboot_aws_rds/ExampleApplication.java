package springboot_aws_rds;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.SneakyThrows;

@SpringBootApplication
@RestController
@RequestMapping("/book")
public class ExampleApplication {
	
	@Autowired
	private BookRepository bookRepository;
	
	@PostMapping
	public Book saveBook(@RequestBody Book book) {
		return bookRepository.save(book);
	}
	
	@GetMapping
	public List<Book> findBooks(){
		return bookRepository.findAll();
	}
	
	@SneakyThrows
	@GetMapping("/{id}")
	public Book findBook(@PathVariable int id) {
		Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("Book"));
		return book;
	}

	public static void main(String[] args) {
		SpringApplication.run(ExampleApplication.class, args);
	}

}
