pipeline {
    agent any

    environment {
        COMPOSE_FILE = 'compose.yaml'
    }

    stages {
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Build & Deploy') {
            steps {
                sh 'docker-compose -f compose.yaml build app'
                sh 'docker-compose -f compose.yaml up -d app'
            }
        }
    }
}