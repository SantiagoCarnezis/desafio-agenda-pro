pipeline {
    agent any
    tools {
        maven "mvn-3.8.4"
    }

    environment {
        SPRING_PROFILES_ACTIVE = 'jenkins'
    }

    stages {
        stage('compile') {

            steps {
                sh "mvn clean compile"
            }
        }
        stage('test') {
            steps {
                sh "mvn test"
            }
        }
        stage('package') {

            steps {
                sh "mvn package"
            }
        }
        stage('build docker image'){

            steps {
                sh 'docker build --no-cache -t santiagocarnezis/desafio-agenda-pro:${BUILD_NUMBER} .'
            }
        }
        stage('docker login'){

            steps {
                withCredentials([string(credentialsId: 'dockerhub', variable: 'dockerpass')]) {
                    sh "docker login -u santiagocarnezis -p ${dockerpass}"
                }
            }
        }
        stage('docker push'){

            steps {
                sh 'docker push santiagocarnezis/desafio-agenda-pro:${BUILD_NUMBER}'
            }
        }
        stage('stop container') { //si ya hay un container corriendo, lo frena
            steps {
                script {
                    sh '''
                    docker ps -q --filter "name=desafio-agenda-pro" | grep -q . && docker stop desafio-agenda-pro || echo "No running container"
                    '''
                }
            }
        }
        stage('docker deploy'){
            steps {

                sh 'docker run -itd --name desafio-agenda-pro -p 8090:8090 santiagocarnezis/desafio-agenda-pro:${BUILD_NUMBER}'
            }
        }
    }
}
