pipeline {
    agent any    
    stages{
        stage('Build Maven'){
            steps{
                checkout([$class: 'GitSCM', branches: [[name: '*/feature']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/Bhavani1711/cicd-project.git']]])
                sh '.mvnw clean install'
            }
        }
  }
}