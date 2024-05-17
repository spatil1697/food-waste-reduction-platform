pipeline {
    agent any

    tools {
        jdk 'jdk17'
        maven 'maven3'
    }


    stages {
        //Building and creating JAR
        stage('Build') {
            steps {
            dir('Backend') {
                    bat 'mvn -Dmaven.test.failure.ignore=true clean install'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Access the FILE credential and write its content to .env file
                    withCredentials([file(credentialsId: 'FILE', variable: 'envFile')]) {
                        bat "type %envFile% > .env"
                        bat 'docker-compose --env-file .env build'
                        bat 'docker tag foodwastereductionplatform-backend spatil1609/foodwastereductionplatform-app:latest'
                    }
                }
            }
        }

        stage('Docker Hub Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'sanketpatil-dockerhub', passwordVariable: 'dockerHubPassword', usernameVariable: 'dockerHubUser')]) {
                    bat "docker login -u ${env.dockerHubUser} -p ${env.dockerHubPassword}"
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                bat 'docker push spatil1609/foodwastereductionplatform-app:latest'
            }
        }
    }

    post {
        always {
            bat 'docker logout'
        }
    }
}
