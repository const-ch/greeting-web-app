# greeting-web-app
Small server app, created for educational purposes. 
Build with spring boot, maven, docker

Specification:
1. Given the following input values 
account=personal and id=123 

and the allowable values for an account are personal and business
and the allowable values for id are all positive integers

then return "Hi, userId 123".




2. Given the following input values account=business and type=small and 

and the allowable values for an account are personal and business
and the allowable values for type are small and big

then return an error that the path is not yet implemented.




3. Given the following input values account=business and type=big and 

and the allowable values for an account are personal and business
and the allowable values for type are small and big

then return "Welcome, business user!".



mvn spring-boot:run    			            build and run app in maven 

docker build -t greeting-app .  	      build image

docker run -p 5000:5000 greeting-app 	  run image  
