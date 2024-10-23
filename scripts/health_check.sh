#!/bin/bash

# 헬스 체크 함수
check_health() {
    local port=$1
    local retries=5
    local count=0

    while [ $count -lt $retries ]; do
        echo "Checking health at http://127.9.13.1:$port/actuator/health"

        http_code=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$port/actuator/health)

        if [ "$http_code" -eq 200 ]; then
            echo "서비스가 정상 작동 중입니다 (포트: $port)."
            return 0
        fi

        echo "서비스가 응답하지 않습니다. 재시도 중... (시도: $((count + 1)))"
        count=$((count + 1))
        sleep 5
    done

    echo "서비스가 정상 작동하지 않습니다. 배포를 중단합니다."
    return 1
}
