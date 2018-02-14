# Spring Boot JWT

## About
** JWT (JSON Web Token) *** with *** Spring Security *** and *** Spring Boot *** with *** MySql ***.

```$xslt
Before starting the application you should edit resources/application.properties file and fill it with your datasource properties 
```

I've also included sample sql seed scripts:
```
 ROLE_ADMIN
 username: admin
 password: admin
 --------------
 ROLE_USER
 username: vladix
 password: vladix
```

## Registration 
![Screenshot from running application](etc/register-screen-shot.png?raw=true "Screenshot JWT Spring Security Demo")

```
If the passwords do not match or there is already a user with same username:
```

![Screenshot from running application](etc/passwords-do-not-match.png?raw=true "Screenshot JWT Spring Security Demo")

![Screenshot from running application](etc/email-exists.png?raw=true "Screenshot JWT Spring Security Demo")

-----------------------------------------------------------------------------------------------------------

## Login
```
After successful login, JWT token is received with 1 week expiration time.
```

![Screenshot from running application](etc/login.png?raw=true "Screenshot JWT Spring Security Demo")

# Roles
## User Role
	GET "/profile" returning personal information about the current user JSON[username, firstName, lastName]
	
## Admin Role
	GET "/users"   returning all users in the as List<User[username, firstName, lastName]>

