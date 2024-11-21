# spring-boot-google-oauth-nextjs-example
An example Spring Boot project that sets up Google OAuth login with a Next.JS 15 app.

Many Spring Boot OAuth examples stop after getting the app running on localhost and without a working logout.
This example aims to avoid making the same mistake and provide a production-ready Spring Boot + Google OAuth app.

Important files:

1. [backend/src/main/java/com/example/springboot/config/SecurityConfig.java](backend/src/main/java/com/example/springboot/config/SecurityConfig.java)
   - This is the main Spring Security Config file
2. [backend/src/main/resources/application-postgres.properties](backend/src/main/resources/application-postgres.properties)
   - Configures Spring and the Database connection
3. [backend/src/main/java/com/example/springboot/auth/Oauth2LoginSuccessHandler.java](backend/src/main/java/com/example/springboot/auth/Oauth2LoginSuccessHandler.java)
   - Overwrites the built-in login handler
4. [backend/src/main/java/com/example/springboot/auth/Oauth2LogoutSuccessHandler.java](backend/src/main/java/com/example/springboot/auth/Oauth2LogoutSuccessHandler.java)
   - Overwrites the built-in logout handler

## 1. Setup a Google Cloud Console App

https://console.cloud.google.com

1. Create new "Login Data" - OAuth-Client-ID
2. Add `http://localhost:8080/login/oauth2/code/google` to "Authorized Redirect URIs"
   - This is the spring-created backend route handler for the google oauth login redirect
3. Retrieve the Google Client ID and Client Secret
   - Add both as ENV vars to your spring boot application

## 2. Setup your Spring Boot configuration

1. Setup a local postgres database
   - You can use the provided `backend/docker-compose.yml` file
2. Add the other required ENV vars according to `backend/.env.example`
3. Spring Profile `postgres` will load the available application properties `backend/src/main/resources/application-postgres.properties`
4. There are some important settings here
   - `server.servlet.session.cookie.domain=${COOKIE_DOMAIN:example.com}`
     - Crucial, if your backend and frontend do not share the same domain
       - e.g `backend.example.com` and `frontend.example.com`
     - Assuming you have a wildcard ssl cert for `example.com`, add `example.com` as `COOKIE_DOMAIN` here
   - `server.servlet.session.cookie.same-site=lax`
     - The Google login redirect will not work with `same-site=strict` and a wildcard domain
   - The following settings are required when running the Spring Boot application behind a reverse proxy such as nginx. If these are not set, Spring Boot's internal `getProtocol()` will return `http` instead of `https` during the login flow, which will fail the Google OAuth redirect requirement for a redirect uri that starts with `https`.
     - `server.forward-headers-strategy=framework` 
     - `server.tomcat.redirect-context-root=false`
     - `server.tomcat.remoteip.host-header=X-Forwarded-Host`
     - `server.tomcat.remoteip.internal-proxies=`
       - this is intentionally empty
     - `server.tomcat.remoteip.protocol-header-https-value=https`

## 3. Run the App

1. `cd frontend`
2. `npm run dev`
3. Start your Spring Boot app
   - via your IDE or build and run the jar `java -jar backend/build/libs/spring-boot-google-oauth-nextjs-example-0.0.1-SNAPSHOT.jar`
4. Visit `http://localhost:3000`
5. Click Login
   - follow the Google Login prompts
6. Observe the intermediate redirect to your backend and immediate redirect to your frontend
7. A JSESSIONID cookie will be available now
   - these can be 'anonymous' token, if the login fails, dont get confused ;-)
8. Pass this token along with any backend fetch to authorize your request
9. Click logout
   - Spring Boot will clear your backend session
   - JSESSIONID cookie will have been deleted
   - You should be redirected to `/`

The backend provides a `me` GET endpoint (`backend/src/main/java/com/example/springboot/controller/AuthController.java`), that returns `yourself` - the authenticated user from the current request.

## Open Topics

I cannot get CSRF / XSRF tokens to work. So it is disabled via the Spring security config (`.csrf(AbstractHttpConfigurer::disable)`), not ideal but not a complete roadblock. If you know how to get this to work with Spring Boot 3.x and e.g. Next.JS, please contact me :)


