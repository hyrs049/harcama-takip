pipeline {
    agent any

    stages {
       
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker-compose build'
            }
        }
        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }
         stage('Cleanup') {
            steps {
                cleanWs()   // Workspace'i temizler
            }
        }
    }
}
