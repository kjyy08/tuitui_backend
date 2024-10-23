pipeline{
    agent any
    environment {
        SCRIPT_PATH = '/var/jenkins_home/custom/tuitui_backend'
    }
    tools {
        gradle 'gradle 8.7'
    }
    stages{
        stage('Checkout') {
            steps {
                 checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/main']],  // main 브랜치를 명시적으로 설정
                            doGenerateSubmoduleConfigurations: false,
                            extensions: [[$class: 'CleanBeforeCheckout']],  // 이전 작업 클린업
                 ])
            }
        }

        stage('Prepare'){
            steps {
                sh 'gradle clean'
            }
        }

        stage('Replace Prod Properties And Environment') {
            steps {
                withCredentials([file(credentialsId: 'applicationMain', variable: 'applicationMain')]) {
                    script {
                        sh 'cp $applicationMain ./src/main/resources/application.yml'
                    }
                }

                withCredentials([file(credentialsId: 'applicationProd', variable: 'applicationProd')]) {
                    script {
                        sh 'cp $applicationProd ./src/main/resources/application-prod.yml'
                    }
                }

                withCredentials([file(credentialsId: 'applicationEnv', variable: 'applicationEnv')]) {
                    script {
                        sh 'cp $applicationEnv ./docker/.env'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sh 'gradle build -x test'
            }
        }

        stage('Test') {
            steps {
                sh 'gradle test'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    cp  ./docker/.env ${SCRIPT_PATH}
                    cp ./docker/docker-compose.blue.yml ${SCRIPT_PATH}
                    cp ./docker/docker-compose.green.yml ${SCRIPT_PATH}
                    cp ./docker/Dockerfile ${SCRIPT_PATH}
                    cp ./scripts/deploy.sh ${SCRIPT_PATH}
                    cp ./build/libs/*.jar ${SCRIPT_PATH}
                    chmod +x ${SCRIPT_PATH}/deploy.sh
                    ${SCRIPT_PATH}/deploy.sh
                '''
            }
        }
    }
}