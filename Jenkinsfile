pipeline {
    agent any

    environment {
        JAVA_HOME = tool 'JDK21'
        NODE_HOME = tool 'NodeJS20'
        PATH = "${JAVA_HOME}/bin:${NODE_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend Build & Test') {
            steps {
                echo 'Building and testing Spring Boot backend...'
                sh 'mvn -f backend/pom.xml clean test'
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Backend Package') {
            steps {
                echo 'Packaging executable JAR...'
                sh 'mvn -f backend/pom.xml package -DskipTests'
            }
        }

        stage('Frontend Build') {
            steps {
                echo 'Installing frontend packages and building Vite bundle...'
                dir('frontend') {
                    sh 'npm ci'
                    sh 'npm run build'
                }
            }
        }

        stage('Docker Image Build') {
            steps {
                echo 'Building Docker container images...'
                sh 'docker compose -f docker-compose.yml build'
            }
        }
    }

    post {
        success {
            echo 'FormGenius AI build and testing completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check build logs for details.'
        }
    }
}
