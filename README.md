# Spring Authentication - Güvenli Kimlik Doğrulama Sistemi

## 📋 Proje Hakkında

Spring Authentication, Java Spring Framework kullanılarak geliştirilmiş kapsamlı bir kimlik doğrulama ve yetkilendirme sistemidir. Bu proje, modern web uygulamaları için güvenli, ölçeklenebilir ve kullanıcı dostu bir kimlik doğrulama çözümü sunmayı amaçlamaktadır. Spring Security'nin güçlü özelliklerini kullanarak, JWT tabanlı token sistemi ve çoklu güvenlik katmanları ile korumalı bir authentication framework'ü sağlar.

## ✨ Özellikler

### 🔐 Kimlik Doğrulama (Authentication)
- **JWT Token Sistemi**: JSON Web Token tabanlı stateless kimlik doğrulama
- **Form-Based Login**: Geleneksel form tabanlı giriş sistemi
- **Remember Me**: Uzun süreli oturum hatırlama
- **Password Reset**: Güvenli şifre sıfırlama sistemi
- **Email Verification**: E-posta doğrulama sistemi
- **Two-Factor Authentication**: İki faktörlü kimlik doğrulama (2FA)

### 🛡️ Yetkilendirme (Authorization)
- **Role-Based Access Control (RBAC)**: Rol tabanlı erişim kontrolü
- **Permission-Based Security**: İzin tabanlı güvenlik sistemi
- **Method-Level Security**: Metot seviyesinde güvenlik
- **URL-Based Security**: URL tabanlı güvenlik kuralları
- **Custom Security Expressions**: Özel güvenlik ifadeleri

### 🔒 Güvenlik Özellikleri
- **Password Encoding**: BCrypt ile şifre hashleme
- **CSRF Protection**: Cross-Site Request Forgery koruması
- **Session Management**: Güvenli oturum yönetimi
- **Rate Limiting**: API istek sınırlama
- **Account Lockout**: Hesap kilitleme mekanizması
- **Audit Logging**: Güvenlik denetim kayıtları

### 📱 Kullanıcı Deneyimi
- **Responsive Design**: Mobil uyumlu tasarım
- **User Registration**: Kullanıcı kayıt sistemi
- **Profile Management**: Profil yönetimi
- **Password Change**: Şifre değiştirme
- **Account Settings**: Hesap ayarları
- **Login History**: Giriş geçmişi

### 🔧 Teknik Özellikler
- **RESTful API**: REST standartlarına uygun API
- **Database Integration**: JPA/Hibernate ile veritabanı entegrasyonu
- **Validation**: Bean Validation ile veri doğrulama
- **Exception Handling**: Kapsamlı hata yönetimi
- **Logging**: Detaylı loglama sistemi
- **Testing**: Kapsamlı test coverage

## 🛠️ Teknoloji Stack'i

### Backend Framework
- **Spring Boot 3.x**: Ana framework
- **Spring Security**: Güvenlik framework'ü
- **Spring Web**: REST API desteği
- **Spring Data JPA**: Veritabanı erişimi
- **Spring Validation**: Veri doğrulama

### Veritabanı ve ORM
- **Hibernate**: JPA implementasyonu
- **H2 Database**: Geliştirme veritabanı
- **PostgreSQL/MySQL**: Prodüksiyon veritabanı
- **HikariCP**: Bağlantı havuzu

### Güvenlik ve Kimlik Doğrulama
- **JWT**: JSON Web Token
- **BCrypt**: Şifre hashleme
- **Spring Security**: Güvenlik katmanı
- **CORS**: Cross-origin resource sharing
- **CSRF**: Cross-Site Request Forgery koruması

### Frontend ve Template
- **FreeMarker**: Template engine
- **Bootstrap**: CSS framework
- **Thymeleaf**: Server-side template engine
- **JavaScript**: Dinamik kullanıcı etkileşimi

### Geliştirme Araçları
- **Maven**: Bağımlılık yönetimi
- **Lombok**: Boilerplate kod azaltma
- **MapStruct**: Object mapping
- **Swagger/OpenAPI**: API dokümantasyonu

### Test Araçları
- **JUnit 5**: Test framework'ü
- **Mockito**: Mock kütüphanesi
- **TestContainers**: Container tabanlı testler
- **JaCoCo**: Kod kapsama analizi

## 🚀 Kurulum

### Gereksinimler
- Java 17 veya üzeri
- Maven 3.6+
- Git
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Adım 1: Projeyi İndirin
```bash
git clone https://github.com/orhanurullah/spring_authentication.git
cd spring_authentication
```

### Adım 2: Veritabanını Hazırlayın
```bash
# H2 Database (geliştirme için otomatik başlar)
# PostgreSQL için:
sudo -u postgres createdb spring_auth_db
```

### Adım 3: Uygulamayı Başlatın
```bash
# Maven ile
mvn spring-boot:run

# Veya JAR dosyası oluşturup çalıştırın
mvn clean package
java -jar target/spring-authentication-1.0.0.jar
```

Uygulama `http://localhost:8080` adresinde çalışmaya başlayacaktır.

## 📁 Proje Yapısı

```
spring_authentication/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/springauth/
│   │   │       ├── SpringAuthenticationApplication.java    # Ana uygulama sınıfı
│   │   │       ├── config/                                 # Konfigürasyon sınıfları
│   │   │       │   ├── SecurityConfig.java
│   │   │       │   ├── JwtConfig.java
│   │   │       │   ├── WebConfig.java
│   │   │       │   └── DatabaseConfig.java
│   │   │       ├── controller/                             # REST Controller'ları
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── UserController.java
│   │   │       │   ├── AdminController.java
│   │   │       │   └── PublicController.java
│   │   │       ├── service/                                # İş mantığı katmanı
│   │   │       │   ├── AuthService.java
│   │   │       │   ├── UserService.java
│   │   │       │   ├── JwtService.java
│   │   │       │   └── EmailService.java
│   │   │       ├── repository/                             # Veri erişim katmanı
│   │   │       │   ├── UserRepository.java
│   │   │       │   ├── RoleRepository.java
│   │   │       │   └── TokenRepository.java
│   │   │       ├── model/                                  # Entity sınıfları
│   │   │       │   ├── User.java
│   │   │       │   ├── Role.java
│   │   │       │   ├── Token.java
│   │   │       │   └── RefreshToken.java
│   │   │       ├── dto/                                    # Data Transfer Objects
│   │   │       │   ├── LoginRequest.java
│   │   │       │   ├── RegisterRequest.java
│   │   │       │   ├── AuthResponse.java
│   │   │       │   └── UserDto.java
│   │   │       ├── security/                               # Güvenlik sınıfları
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   ├── JwtAuthorizationFilter.java
│   │   │       │   ├── CustomUserDetailsService.java
│   │   │       │   └── CustomAuthenticationProvider.java
│   │   │       ├── exception/                              # Özel exception'lar
│   │   │       │   ├── AuthenticationException.java
│   │   │       │   ├── UserNotFoundException.java
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       └── util/                                   # Yardımcı sınıflar
│   │   │           ├── JwtUtil.java
│   │   │           ├── PasswordUtil.java
│   │   │           └── EmailUtil.java
│   │   └── resources/
│   │       ├── application.yml                             # Uygulama konfigürasyonu
│   │       ├── application-dev.yml                         # Geliştirme konfigürasyonu
│   │       ├── application-prod.yml                        # Prodüksiyon konfigürasyonu
│   │       ├── templates/                                  # Template dosyaları
│   │       │   ├── login.html
│   │       │   ├── register.html
│   │       │   ├── dashboard.html
│   │       │   └── profile.html
│   │       └── static/                                     # Statik dosyalar
│   │           ├── css/
│   │           ├── js/
│   │           └── images/
│   └── test/
│       └── java/
│           └── com/example/springauth/
│               ├── controller/                             # Controller testleri
│               ├── service/                                # Service testleri
│               ├── security/                               # Security testleri
│               └── integration/                            # Entegrasyon testleri
├── pom.xml                                                 # Maven bağımlılıkları
├── springdocDependency.xml                                 # Swagger bağımlılıkları
├── .gitignore                                             # Git ignore dosyası
└── README.md                                              # Proje dokümantasyonu
```

## 🗄️ Veritabanı Şeması

### Kullanıcılar (Users)
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    enabled BOOLEAN DEFAULT TRUE,
    account_non_expired BOOLEAN DEFAULT TRUE,
    account_non_locked BOOLEAN DEFAULT TRUE,
    credentials_non_expired BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255),
    reset_token_expiry TIMESTAMP,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Roller (Roles)
```sql
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_roles (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);
```

### Token'lar (Tokens)
```sql
CREATE TABLE refresh_tokens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 🔌 API Endpoint'leri

### Kimlik Doğrulama
```
POST /api/auth/login              # Kullanıcı girişi
POST /api/auth/register           # Kullanıcı kaydı
POST /api/auth/refresh            # Token yenileme
POST /api/auth/logout             # Çıkış yapma
POST /api/auth/verify-email       # E-posta doğrulama
POST /api/auth/forgot-password    # Şifre sıfırlama isteği
POST /api/auth/reset-password     # Şifre sıfırlama
POST /api/auth/change-password    # Şifre değiştirme
```

### Kullanıcı Yönetimi
```
GET    /api/users                 # Tüm kullanıcıları listele (Admin)
GET    /api/users/{id}            # Kullanıcı detayı
GET    /api/users/profile         # Kendi profil bilgileri
PUT    /api/users/profile         # Profil güncelleme
DELETE /api/users/{id}            # Kullanıcı sil (Admin)
```

### Admin İşlemleri
```
GET    /api/admin/users           # Tüm kullanıcıları listele
PUT    /api/admin/users/{id}/enable    # Kullanıcı aktifleştir
PUT    /api/admin/users/{id}/disable   # Kullanıcı deaktifleştir
POST   /api/admin/users/{id}/roles     # Kullanıcı rolü ata
DELETE /api/admin/users/{id}/roles     # Kullanıcı rolünü kaldır
```

### Örnek API Kullanımı

#### Kullanıcı Kaydı
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

#### Kullanıcı Girişi
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "password123"
  }'
```

#### Token ile Korumalı Endpoint
```bash
curl -X GET http://localhost:8080/api/users/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 🔧 Konfigürasyon

### application.yml
```yaml
spring:
  application:
    name: spring-authentication
  
  datasource:
    url: jdbc:h2:mem:authdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  security:
    user:
      name: admin
      password: admin123

server:
  port: 8080

jwt:
  secret: your-super-secret-jwt-key-here-make-it-long-and-secure
  expiration: 86400000 # 24 saat
  refresh-expiration: 604800000 # 7 gün

app:
  auth:
    jwtSecret: your-super-secret-jwt-key-here-make-it-long-and-secure
    jwtExpirationMs: 86400000
    jwtRefreshExpirationMs: 604800000

logging:
  level:
    com.example.springauth: DEBUG
    org.springframework.security: DEBUG
```

### Güvenlik Konfigürasyonu
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler)
            );
        
        return http.build();
    }
}
```

## 🧪 Test

### Birim Testleri
```bash
# Tüm testleri çalıştır
mvn test

# Belirli bir test sınıfını çalıştır
mvn test -Dtest=AuthServiceTest

# Test coverage raporu
mvn jacoco:report
```

### Entegrasyon Testleri
```bash
# Entegrasyon testlerini çalıştır
mvn test -Dtest=*IntegrationTest

# Security testleri
mvn test -Dtest=*SecurityTest
```

### API Testleri
```bash
# Postman Collection'ı import edin
# veya curl ile test edin
curl -X GET http://localhost:8080/api/health
```

## 📦 Kullanılan Bağımlılıklar

### Spring Boot Starters
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### Güvenlik Bağımlılıkları
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

### Veritabanı
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Geliştirme Araçları
```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.3.Final</version>
</dependency>
```

## 🚀 Deployment

### Docker ile Deployment
```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/spring-authentication-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# Docker image oluştur
docker build -t spring-authentication .

# Container çalıştır
docker run -p 8080:8080 spring-authentication
```

### Kubernetes ile Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: spring-authentication
spec:
  replicas: 3
  selector:
    matchLabels:
      app: spring-authentication
  template:
    metadata:
      labels:
        app: spring-authentication
    spec:
      containers:
      - name: spring-authentication
        image: spring-authentication:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: JWT_SECRET
          valueFrom:
            secretKeyRef:
              name: jwt-secret
              key: jwt-secret
```

### Cloud Deployment
```bash
# Heroku
heroku create spring-authentication
git push heroku main

# AWS Elastic Beanstalk
eb init spring-authentication
eb create production
eb deploy
```

## 📊 Monitoring ve Health Checks

### Actuator Endpoint'leri
```
GET /actuator/health          # Uygulama sağlığı
GET /actuator/info           # Uygulama bilgileri
GET /actuator/metrics        # Metrikler
GET /actuator/env            # Ortam değişkenleri
GET /actuator/loggers        # Logger seviyeleri
```

### Güvenlik Metrikleri
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
  endpoint:
    health:
      show-details: always
```

## 🔐 Güvenlik Özellikleri

### JWT Token Yapısı
```java
// Token payload örneği
{
  "sub": "john_doe",
  "iat": 1640995200,
  "exp": 1641081600,
  "roles": ["USER"],
  "email": "john@example.com"
}
```

### Şifre Güvenliği
```java
// BCrypt ile şifre hashleme
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
}
```

### Rate Limiting
```java
// Login endpoint için rate limiting
@Bean
public RateLimiter loginRateLimiter() {
    return RateLimiter.create(5.0); // 5 istek/dakika
}
```

## 🤝 Katkıda Bulunma

1. Bu repository'yi fork edin
2. Yeni bir branch oluşturun (`git checkout -b feature/yeni-ozellik`)
3. Değişikliklerinizi commit edin (`git commit -am 'Yeni özellik eklendi'`)
4. Branch'inizi push edin (`git push origin feature/yeni-ozellik`)
5. Pull Request oluşturun

### Katkı Kuralları
- Java kod standartlarına uyun
- Unit test yazın
- Güvenlik testlerini ekleyin
- API dokümantasyonunu güncelleyin
- Commit mesajlarını açıklayıcı yazın

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır. Detaylar için `LICENSE` dosyasına bakın.

## 🆘 Destek

Herhangi bir sorun yaşarsanız:
1. [Issues](https://github.com/orhanurullah/spring_authentication/issues) bölümünde arama yapın
2. Yeni bir issue oluşturun
3. Detaylı hata açıklaması ve adımları ekleyin

## 👨‍💻 Geliştirici

Bu proje [Orhan Nurullah](https://github.com/orhanurullah) tarafından geliştirilmiştir.

## 🔄 Güncellemeler

### v1.0.0
- İlk sürüm yayınlandı
- JWT tabanlı kimlik doğrulama
- Spring Security entegrasyonu
- Kullanıcı kayıt ve giriş sistemi
- Rol tabanlı yetkilendirme
- Şifre sıfırlama sistemi

### Gelecek Özellikler
- [ ] OAuth2 entegrasyonu
- [ ] Social login (Google, Facebook, GitHub)
- [ ] Biometric authentication
- [ ] Multi-tenant support
- [ ] Advanced audit logging
- [ ] Real-time security monitoring

---

⭐ Bu projeyi beğendiyseniz yıldız vermeyi unutmayın!

## 📞 İletişim

- **GitHub**: [@orhanurullah](https://github.com/orhanurullah)
- **E-posta**: orhanurullah@example.com
- **LinkedIn**: [Orhan Urullah](https://linkedin.com/in/orhanurullah)

---

**Spring Authentication** - Güvenli kimlik doğrulama çözümü 🔐🚀 
