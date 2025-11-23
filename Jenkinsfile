pipeline {
    agent any
    tools{
        maven 'maven_3_5_0'
    }
    stages{
        stage('Build Maven'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/feature']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Bhavani1711/cicd-project.git']]])
                sh 'mvnw clean install'
            }
        }
  }