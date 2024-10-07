### 목차

> 1. [프로젝트 소개](#프로젝트-소개)
> 2. [ERD 구조](#erd-구조)
> 3. [백엔드 아키텍처](#백엔드-아키텍처)
> 4. [주요 기능](#주요-기능)
> 5. [API 명세](#API-명세)


# 프로젝트 소개
> 안드로이드 플랫폼을 대상으로 한 장소 AR 기술을 활용한 SNS 애플리케이션입니다.\
> 앱을 활용하여 사용자들이 사진과 글을 원하는 장소에 타임캡슐로 기록하고 이를 공유할 수 있습니다.
> 
> 이렇게 작성된 타임캡슐은 AR 기술을 통해 실제 공간에 나타나 다른 사용자들과 공유할 수 있게 됩니다.\
> 타임캡슐은 단순히 정보를 저장할 뿐 아니라 사용자가 설정한 시간이 지난 뒤에 알림을 통해 다시 회상할 수 있습니다.
> 
> 이러한 기능을 통해 사용자들은 장소에 대한 경험을 더 생생하게 기록하고,\
> 공유함으로써 다양한 추억을 만들어가고 건강한 장소 기반의 네트워크를 형성할 것입니다.

> ### 개발 인원
> FrontEnd 2명, BackEnd 1명, DB & AI 1명

> ### 아직 개발 중인 기능
> 1. 채팅
> 2. 태그
> 3. AR
> 4. 유저 차단

## 팀원
<div align="center">
    <table align="center"> <!-- 팀원 표 -->
        <tr>
            <th>Frontend 남도현</th>
            <th>Frontend 남기연</th>
            <th>Backend 김주엽</th>
            <th>DB & AI 박승교</th>
        </tr>
        <tr>
            <td align="center" class="도현">
                <a href="https://github.com/dohyeon0709">
                    <img alt="github-link" height="25" src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>
                </a>
                <br/>
            </td>
            <td align="center" class="기연">
                <a href="">
                    <img alt="github-link" height="25" src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>
                </a>
            </td>
            <td align="center" class="주엽">
                <a href="https://github.com/thsu1084">
                    <img alt="github-link" height="25" src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>
                </a>
                <br/>
                <a href="mailto:kjyy08@naver.com">
                    <img alt="gmail-link" height="25" src="https://img.shields.io/badge/Email-d14836?style=flat-square&logo=Gmail&&logoColor=white"/>
                </a>
            </td>
            <td align="center" class="승교">
                <a href="https://github.com/ps200093">
                    <img alt="github-link" height="25" src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/>
                </a>
            </td>
        </tr>
    </table>
</div>

# ERD 구조
![ERD](https://github.com/user-attachments/assets/a96130a4-87fa-47b9-8b81-069e260a4d40)

# 백엔드 아키텍처
![백엔드 아키텍처](https://github.com/user-attachments/assets/06b8e525-8e9d-4cae-b0ca-056b2e6b1e80)
# 주요 기능
|   기능   | 설명                                                                                                                                  |
|:------:|-------------------------------------------------------------------------------------------------------------------------------------|
|  글 작성  | ㆍ사용자의 감상, 현재 생각 등을 타임캡슐에 저장할 수 있고, 저장된 데이터는 다른 사용자들과 공유할 수 있습니다. <br>ㆍAR 기술을 활용하여 실제 공간에 기록하고 확인할 수 있습니다.                           |
|  타임캡슐  | ㆍ사용자가 작성한 글이 타임캡슐로 기록되는 정보로 날씨, 사진, 내용, 위치, 해시태그, 다음 열람 일 등을 담고 있습니다. <br> ㆍ지정한 날짜가 되면 다음 열람 일에 알림을 통해 다시 한번 열람할 수 있고 글의 공유도 가능합니다. |
| 여행지 정보 | ㆍTourAPI와 연동된 지도를 통해 사용자가 원하는 여행지 정보를 조회하고 확인할 수 있으며 추천 경로도 제공합니다. <br> ㆍ누적된 리뷰를 통해 다른 사용자들에게 여행의 즐거움을 알릴 수 있습니다.                   |
|   채팅   | ㆍ사용자들 간 메시지를 주고받을 수 있는 기능을 제공합니다. <br> ㆍ채팅을 통해 실시간으로 여행자들의 위치 정보도 확인할 수 있습니다.                                                       |
| 데이터 관리 | ㆍ사용자는 자신이 기록한 타임캡슐을 직접적으로 확인하고 검색할 수 있으며, <br> ㆍ다른 사용자의 타임캡슐 또한 공유 기능을 통해 위치 정보를 통해 확인할 수 있습니다.                                     |
|   AR   | ㆍ실제 공간에서 사용자가 남긴 타임캡슐을 확인할 수 있습니다. <br> ㆍAR 기술을 통해 2차원에서 벗어나 더욱 풍부한 경험을 제공합니다.                                                      |

# API 명세
➡️[TuiTui API Document](https://documenter.getpostman.com/view/34178237/2sA3s3GB1N#50c1d557-1a40-468d-8c30-838589dd38f3)
 - ### API 명세 예시
| Domain  |               URL               | Http Method |     Description     | Role |
|:-------:|:-------------------------------:|:-----------:|:-------------------:|:----:|
|  User   |             /token              |     GET     |   유저 토큰 발급 및 재발급    |  -   |
|         |             /users              |   DELETE    |        유저 삭제        |  -   |
|         |             /users              |     GET     |      전체 유저 조회       |  -   |
|         |         /users/{userId}         |     GET     |      특정 유저 조회       |  -   |
|         |             /logout             |    POST     |        로그아웃         |  -   |
| Profile |     /profiles/without-image     |    POST     | 프로필 이미지 미포함 프로필 생성  |  -   |
|         |      /profiles/with-image       |    POST     |  프로필 이미지 포함 프로필 생성  |  -   |
|         |        /profiles/images         |    POST     |     프로필 이미지 수정      |  -   |
|         |      /profiles/{profileId}      |   DELETE    |       프로필 삭제        |  -   |
|         |            /profiles            |     PUT     |      프로필 정보 수정      |  -   |
|         |            /profiles            |     GET     |      전체 프로필 조회      |  -   |
|         |      /profiles/{profileId}      |     GET     |      특정 프로필 조회      |  -   |
|         | /profiles/nicknames/{nickname}  |     GET     |  닉네임에 해당하는 프로필 조회   |  -   |
|         |    /users/{userId}/profiles     |     GET     | 유저 ID에 해당하는 프로필 조회  |  -   |
| Capsule |     /capsules/without-image     |    POST     |  이미지 미포함 타임 캡슐 생성   |  -   |
|         |      /capsules/with-image       |    POST     |   이미지 포함 타임 캡슐 생성   |  -   |
|         |      /capsules/{capsuleId}      |   DELETE    |      타임 캡슐 삭제       |  -   |
|         |            /capsules            |     GET     |     전체 타임 캡슐 조회     |  -   |
|         |      /capsules/{capsuleId}      |     GET     |      특정 캡슐 조회       |  -   |
|         |                .                |      .      |          .          |  .   |
|         |                .                |      .      |          .          |  .   |
|         |                .                |      .      |          .          |  .   |







