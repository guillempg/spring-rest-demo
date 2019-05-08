package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
class EmployeeController {

	private final EmployeeRepository repository;

	EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	// Aggregate root

	@GetMapping("/employees")
	List<Employee> all() {
		return repository.findAll();
	}

	@RequestMapping(value = "/employees", method = RequestMethod.POST, consumes = {"application/json"})
	String newEmployee(Employee newEmployee) {
		return repository.save(newEmployee).toString();
	}

	// Single item

	@GetMapping("/employees/{id}")
	Employee one(@PathVariable Long id) {

		return repository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@RequestMapping(value = "/employees/{id}", method = RequestMethod.PUT, consumes = {"application/json"})
	String replaceEmployee(Employee newEmployee, @PathVariable Long id) {

		return repository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return repository.save(employee);
				})
				.orElseGet(() -> {
					newEmployee.setId(id);
					return repository.save(newEmployee);
				}).toString();
	}

	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		repository.deleteById(id);
	}
}