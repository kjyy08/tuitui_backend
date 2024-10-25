# ✨목차

> 1. [프로젝트 간략 소개](#프로젝트-간략-소개)
> 2. [기술 스택](#기술-스택)
> 3. [ERD 구조](#erd-구조)
> 4. [백엔드 아키텍처](#백엔드-아키텍처)
> 5. [JWT](#JWT)
> 6. [배포 과정](#배포-과정)
> 7. [API 명세](#API-명세)

# 🏠프로젝트 간략 소개
> 안드로이드 플랫폼을 대상으로 한 장소 AR 기술을 활용한 SNS 애플리케이션입니다.\
> 앱을 활용하여 사용자들이 사진과 글을 원하는 장소에 타임캡슐로 기록하고 이를 공유할 수 있습니다.
> 
> ### Git 주소
> * Main\
>    [TuiTui Repository](https://github.com/kjyy08/tuitui.git)
> * Frontend\
>    [Client Repository](https://github.com/swuProject/main)
> * Backend\
>    [API Repository](https://github.com/kjyy08/tuitui_backend.git)

# 💻기술 스택
### Backend
![Java](https://img.shields.io/badge/JDK_17-000000.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![SpringBoot](https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![JPA](https://img.shields.io/badge/Spring_JPA-green.svg?style=for-the-badge&logo=springboot&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-purple?style=for-the-badge&logo=JSON%20web%20tokens)
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-FF4438.svg?style=for-the-badge&logo=redis&logoColor=white)

### Server
![AWS EC2](https://img.shields.io/badge/AWS_EC2-FF9900.svg?style=for-the-badge&logo=amazonec2&logoColor=white)
![AWS RDS](https://img.shields.io/badge/AWS_RDS-527FFF.svg?style=for-the-badge&logo=amazonrds&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS_S3-569A31.svg?style=for-the-badge&logo=amazons3&logoColor=white)
![AWS Route53](https://img.shields.io/badge/AWS_Route53-8C4FFF.svg?style=for-the-badge&logo=amazonroute53&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED.svg?style=for-the-badge&logo=docker&logoColor=white)

### API Documentation & Test
![Postman](https://img.shields.io/badge/Postman-FF6C37.svg?style=for-the-badge&logo=postman&logoColor=white)

# 🔎ERD 구조
![ERD](https://github.com/user-attachments/assets/a96130a4-87fa-47b9-8b81-069e260a4d40)

# ⚙️백엔드 아키텍처
![아키텍처](https://github.com/user-attachments/assets/0eba517e-9458-4f7f-90df-8d44ce05d412)

# 🐋배포 과정
* ### 도커를 통한 배포
![image](https://github.com/user-attachments/assets/7753a3aa-7d3d-4248-9301-c49471f8a17c)

| 순서 |                설명                 |             비고              |
|:--:|:---------------------------------:|:---------------------------:|
| 1  |   도커 이미지를 빌드하여 도커 허브로 Push 합니다.   |                             |   
| 2  | AWS EC2 인스턴스에서 필요한 이미지를 Pull 합니다. |                             |
| 3  |   도커 컴포즈를 이용하여 필요한 컨테이너를 실행합니다.   | docker-compose.yml 파일 작성 필요 |

* ### docker-compose.yml 예시

```yaml
version: '3.8'

services:
  tuitui-backend:
    container_name: tuitui-backend
    image: kjyy08/tuitui-backend
    ports:
      - "8443:8443" # Https 연결을 위해 8443 포트 설정, 로컬 환경에서는 8080:8080으로 변경
    volumes:
      - /{ssl_directory}:/app/ssl/{file_name} # Https 연결에 필요한 인증서 루트 경로, 로컬 환경에서는 지우기
    environment:
      # 환경 변수 설정, .env 파일 작성 필요
      - SPRING_PROFILES_ACTIVE
      - SPRING_SQL_URL
      - SPRING_SQL_USERNAME
      - SPRING_SQL_PASSWORD
      - SSL_PASSWORD
      - ADMIN_SECRET_KEY
      - MANAGER_SECRET_KEY
      - JWT_SECRET_KEY
      - CLOUD_FRONT_URL
      - S3_BUCKET_NAME
      - S3_ACCESS_KEY
      - S3_SECRET_ACCESS_KEY
      - MULTIPART_TEMP_ROOT
      - S3_BUCKET_URL
    networks:
      - tuitui-network

networks:
  tuitui-network:
    driver: bridge
```

# 🔑JWT
* ### 토큰 발급 및 생성 과정
![image](https://github.com/user-attachments/assets/e7a33cc0-ad6f-44ea-af49-a8925966aa88)

| 순서 |                           설명                            |                     비고                     |
|:--:|:-------------------------------------------------------:|:------------------------------------------:|
| 1  |      유저가 소셜 로그인에 성공하여 필요한 정보를 담아<br> 로그인 요청을 보냅니다.      | grant_type, Access/Refresh 토큰,<br> 계정 정보 등 |   
| 2  |               컨트롤러에서 서비스로 토큰 검증 요청을 보냅니다.               |                                            |
| 3  | 서비스에서 실제 SNS 이용자인지 검증하기 위해<br> SNS 서버로 유저 검증 요청을 보냅니다.  |          SDK에서 발급받은 Access 토큰 이용           |
| 4  |                 유저 검증에 성공하면 토큰을 생성합니다.                  |                                            |   
| 5  |           토큰 재발급에 사용할 Refresh 토큰을 DB에 저장합니다.            |     Refresh 토큰, 계정 정보,<br> 만료일을 DB에 저장     |   
| 6  |           Access, Refresh 토큰 정보를 컨트롤러에 반환합니다.           |                                            |   
| 7  |               유저에게 토큰 정보을 반환하여 발급을 마칩니다.                |                                            |

# 📋API 명세
➡️[TuiTui API Document](https://documenter.getpostman.com/view/34178237/2sA3s3GB1N#50c1d557-1a40-468d-8c30-838589dd38f3)

| Domain  |                   URL                   | Http Method |    Description     |
|:-------:|:---------------------------------------:|:-----------:|:------------------:|
|  User   |                 /token                  |     GET     |   유저 토큰 발급 및 재발급   |
|         |                 /users                  |   DELETE    |       유저 삭제        |
|         |                 /users                  |     GET     |      전체 유저 조회      |
|         |             /users/{userId}             |     GET     |      특정 유저 조회      |
|         |        /users/{userId}/profiles         |     GET     | 유저 ID에 해당하는 프로필 조회 |
|         |                 /logout                 |    POST     |        로그아웃        |
| Profile |         /profiles/without-image         |    POST     | 프로필 이미지 미포함 프로필 생성 |
|         |          /profiles/with-image           |    POST     | 프로필 이미지 포함 프로필 생성  |
|         |            /profiles/images             |    POST     |     프로필 이미지 수정     |
|         |          /profiles/{profileId}          |   DELETE    |       프로필 삭제       |
|         |                /profiles                |     PUT     |     프로필 정보 수정      |
|         |                /profiles                |     GET     |     전체 프로필 조회      |
|         |          /profiles/{profileId}          |     GET     |     특정 프로필 조회      |
|         |     /profiles/nicknames/{nickname}      |     GET     |  닉네임에 해당하는 프로필 조회  |
|         | /profiles/nicknames/{nickname}/capsules |     GET     |  닉네임에 해당하는 캡슐 조회   |
|         |     /profiles/{profileId}/capsules      |     GET     | 프로필 ID에 해당하는 캡슐 조회 |
| Capsule |         /capsules/without-image         |    POST     |  이미지 미포함 타임 캡슐 생성  |
|         |          /capsules/with-image           |    POST     |  이미지 포함 타임 캡슐 생성   |
|         |                /capsules                |     PUT     |      타임 캡슐 수정      |
|         |                /capsules                |     GET     |    전체 타임 캡슐 조회     |
|         |          /capsules/{capsuleId}          |     GET     |      특정 캡슐 조회      |
|         |          /capsules/{capsuleId}          |   DELETE    |      타임 캡슐 삭제      |
|         |            /capsules/nearby             |     GET     | 위도, 경도 기반 인근 캡슐 조회 |
|         |       /capsules/{capsuleId}/likes       |     GET     |   좋아요를 누른 유저 조회    |
|         |             /capsules/likes             |    POST     |     캡슐 좋아요 저장      |
|         |     /capsules/likes/{capsuleLikeId}     |   DELETE    |     캡슐 좋아요 삭제      |
|         |      /capsules/{capsuleId}/visits       |     GET     |     캡슐 조회수 조회      |
|         |      /capsules/{capsuleId}/visits       |    POST     |     캡슐 조회수 증가      |
| Comment |           /capsules/comments            |    POST     |       댓글 생성        |
|         |           /capsules/comments            |     PUT     |       댓글 수정        |
|         |           /capsules/comments            |     GET     |      전체 댓글 조회      |
|         |     /capsules/{capsuleId}/comments      |     GET     |    특정 캡슐 댓글 조회     |
|         |     /capsules/comments/{commentId}      |   DELETE    |       댓글 삭제        |
|         |             /comments/likes             |    POST     |     댓글 좋아요 저장      |
|         |             /comments/likes             |   DELETE    |     댓글 좋아요 삭제      |
|         |       /comments/likes/{commentId}       |     GET     |     댓글 좋아요 조회      |
| Follow  |            /profiles/follows            |    POST     |     특정 유저 팔로우      |
|         |            /profiles/follows            |   DELETE    |       팔로우 취소       |
|         |      /profiles/follows/{profileId}      |     GET     |    팔로워 및 팔로잉 조회    |






