package com.belajar.task_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class TaskApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApiApplication.class, args);
	}

}
/*
* Open your browser and go to: http://localhost:8080/swagger-ui/index.html
You will see both your /api/auth/register and /api/auth/login endpoints.
Click on the POST /api/auth/register endpoint, click "Try it out", enter the JSON body:
json
{
  "username": "testuser",
  "password": "password123"
}
Click Execute. You should get a 200 OK response saying "User berhasil didaftarkan!".
Next, do the exact same thing for POST /api/auth/login using the same credentials. When you execute it, you will receive your JWT Token.
Method 2: Using Postman (Standard Developer Tool)
If you have Postman installed:

1. Register a User:

Method: POST
URL: http://localhost:8080/api/auth/register
Go to the Body tab -> select raw -> select JSON.
Paste this body and click Send:
json
{
  "username": "testuser",
  "password": "password123"
}
2. Login:

Method: POST
URL: http://localhost:8080/api/auth/login
Use the same JSON body as above and click Send.
The response will be your generated JWT Token.
Method 3: Using Command Line (cURL)
If you prefer the terminal, you can run these commands directly.

Register:

bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\", \"password\":\"password123\"}"
Login:

bash
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"testuser\", \"password\":\"password123\"}"
* */