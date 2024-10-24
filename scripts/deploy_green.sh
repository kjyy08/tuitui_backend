#!/bin/bash

# 작업 디렉토리로 변경
cd /var/jenkins_home/custom/tuitui_backend || exit 1

# 환경변수
DOCKER_APP_NAME=tuitui-backend
LOG_FILE=./deploy.log

# 로그 기록 함수 포함
source ./log.sh

# 헬스 체크 함수 포함
source ./health_check.sh  # 헬스 체크 함수 로드

# 그린 환경 배포
deploy_green() {
    log "green 배포 시작 : $(date +'%Y-%m-%d %H:%M:%S')"
    docker-compose -p "${DOCKER_APP_NAME}-green" -f docker-compose.green.yml up -d --build

    if check_health 8444; then
        log "blue 중단 시작 : $(date +'%Y-%m-%d %H:%M:%S')"
        docker-compose -p "${DOCKER_APP_NAME}-blue" -f docker-compose.blue.yml down
    else
        log "그린 배포가 실패하여 롤백합니다."
        docker-compose -p "${DOCKER_APP_NAME}-green" -f docker-compose.green.yml down
        exit 1
    fi
}

# 스크립트 실행
deploy_green
