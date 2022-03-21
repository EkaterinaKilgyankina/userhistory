<h3>Данный сервис регистрирует пользователя в базе, создает ему токен для дальнейшей аутентификации и позволяет сохранять/получать переданные сообщения.</h3>

### описание сервиса
<li> Первая точка входа в сервис - это метод, который принимает данные о клиенте, обрабатывает их. При успешном сохранении в базу - возвращаются данные о сохранненном объекте. </li>

<li> Вторая точка входа в сервис - это метод создания токена для клиента по его имени. При успешном выполнении запроса контроллер возвращает строку токена.</li>

<li> Третья точка входа в сервис - это метод, принимает на вход имя клиента и сообщение. При успешном выполнении контроллер либо сохраняет сообщения клиента в базу, либо при сообщении вида {"history 10"} - возвращение 10 последних сообщений сохранненных данным клиентом.</li>

Основной стек технологий:
    
    Java 8
    Docker
    Spring Boot 2
    PostgreSQL 11
    Flyway
    Hibernate
    
    JUnit
    TestRestTemplate

### запуск приложения
Скачать файл docker-compose.yaml
Выполнить команду для запуска образов

    docker-compose up
   
Приложение по дефолту поднимается на 8080 порт
        
### примеры валидных запросов:
Примеры валидных запросов:

    -//регистрируем пользователя
     curl -X POST http://localhost:8080/registrations 
        -H "Content-Type: application/json"
        -d '{"name": "TestClient", "password": "Test1!"}' 
        
     //получаем токен->используем в {Bearer token}
     curl -X POST http://localhost:8080/authentications 
        -H "Content-Type: application/json"
        -d '{"name": "TestClient", "password": "Test1!"}'
        
     //сохраняем сообщение 1
     curl -X POST http://localhost:8080/authentications 
        -H "Content-Type: application/json"
        -H "Authorization: Bearer {token}"
        -d '{""userName": "TestClient", "text": "firstmessage"}'
        
     //сохраняем сообщение 2
     curl -X POST http://localhost:8080/authentications 
        -H "Content-Type: application/json"
        -H "Authorization: Bearer {token}"
        -d '{""userName": "TestClient", "text": "secondMessage"}'
        
     //возвращаем сохранные 2 сообщения пользователя
     curl -X POST http://localhost:8080/authentications 
        -H "Content-Type: application/json"
        -H "Authorization: Bearer {token}"
        -d '{""userName": "TestClient", "text": "history 2"}'

 
