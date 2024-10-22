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
                checkout scm
            }
        }

        stage('Prepare'){
            steps {
                sh 'gradle clean'
            }
        }

        stage('Replace Prod Properties') {
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
                    cp ./docker/docker-compose-blue.yml ${SCRIPT_PATH}
                    cp ./docker/docker-compose-green.yml ${SCRIPT_PATH}
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