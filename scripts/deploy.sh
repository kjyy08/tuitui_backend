#!/bin/bash

# 작업 디렉토리로 변경
cd /var/jenkins_home/custom/tuitui_backend || exit 1

# 환경변수
DOCKER_APP_NAME=tuitui-backend
LOG_FILE=./deploy.log

# 로그 기록 함수 포함
source ./log.sh

# 헬스 체크 함수 포함
source ./health_check.sh

# 메인 함수
main() {
    log "배포 시작일자 : $(date +'%Y-%m-%d %H:%M:%S')"

    EXIST_BLUE=$(docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker-compose.blue.yml ps | grep -E "Up|running")

    if [ -z "$EXIST_BLUE" ]; then
        ./deploy_green.sh  # 그린 배포
    else
        ./deploy_blue.sh  # 블루 배포
    fi

    # 사용하지 않는 이미지 삭제
    docker image prune -af

    log "배포 종료  : $(date +'%Y-%m-%d %H:%M:%S')"
    log "===================== 배포 완료 ====================="
}

# 스크립트 실행
main
