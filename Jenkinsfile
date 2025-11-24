pipeline {
    agent any
    environment {
        AWS_ACCOUNT_ID="904233105350"
        AWS_DEFAULT_REGION="ap-south-1"
        IMAGE_REPO_NAME="asb/dockerized-my-app"		
        IMAGE_TAG="latest"		        
        ECR_REPO_URL = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
		IMAGE_LATEST = "${ECR_REPO_URL}:${IMAGE_TAG}"
		CONTAINER_NAME = 'my-web-app'
        HOST_PORT = '9090'
        CONTAINER_PORT = '9090'
		EC2_HOST = '15.206.224.9' 
        EC2_USER = 'ubuntu'  
		SSH_CREDENTIALS_ID = 'ec2-ssh-key'
	}
   
    stages {        
         stage('1.Logging into AWS ECR') {
            steps {
                script {
                sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                }
                 
            }
        }
        
        stage('2.Cloning Git') {
            steps {
                checkout scmGit(branches: [[name: '*/feature']], extensions: [], userRemoteConfigs: [[credentialsId: 'Gitcreds', url: 'https://github.com/Bhavani1711/cicd-project.git']])     
            }
        }  
    
    stage('3.Building image') {
      steps{
        script {
          dockerImage = docker.build "${IMAGE_REPO_NAME}:${IMAGE_TAG}"
        }
      }
    }  
    
    stage('4.Pushing to ECR') {
     steps{  
         script {
                sh "docker tag ${IMAGE_REPO_NAME}:${IMAGE_TAG} ${ECR_REPO_URL}:$IMAGE_TAG"
                sh "docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
         }
        }
      }
	  
	stage('5. Pull ECR image and Deploy to EC2 ') {
      steps {
                script {
                    // Use SSH credentials from Jenkins
                    sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                        sh """
                            ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} << 'ENDSSH'
                                set -e
                                
                                echo "========================================="
                                echo "Step 1: AWS ECR Login"
                                echo "========================================="
                                
                                # Use full path to aws CLI or install if not exists
                                AWS_CLI=\$(command -v aws || echo /usr/local/bin/aws)
                                
                                if [ ! -x "\$AWS_CLI" ]; then
                                    echo "AWS CLI not found, attempting to install..."
                                    
                                    # Install unzip if not available
                                    if ! command -v unzip &> /dev/null; then
                                        echo "Installing unzip..."
                                        sudo apt-get update -qq
                                        sudo apt-get install -y unzip
                                    fi
                                    
                                    cd /tmp
                                    curl -s "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
                                    unzip -q awscliv2.zip
                                    sudo ./aws/install --update
                                    AWS_CLI=/usr/local/bin/aws
                                fi
                                
                                # ECR Login
                                \$AWS_CLI ecr get-login-password --region ${AWS_DEFAULT_REGION} | \
                                    docker login --username AWS --password-stdin ${ECR_REPO_URL}
                                
                                echo "========================================="
                                echo "Step 2: Stop and Remove Old Container"
                                echo "========================================="
                                docker stop ${CONTAINER_NAME} 2>/dev/null || true
                                docker rm ${CONTAINER_NAME} 2>/dev/null || true
                                
                                echo "========================================="
                                echo "Step 3: Pull Latest Image from ECR"
                                echo "========================================="
                                docker pull ${IMAGE_LATEST}
                                
                                echo "========================================="
                                echo "Step 4: Run New Container"
                                echo "========================================="
                                docker run -d \
                                    --name ${CONTAINER_NAME} \
                                    -p ${HOST_PORT}:${CONTAINER_PORT} \
                                    --restart unless-stopped \
                                    ${IMAGE_LATEST}
                                
                                echo "========================================="
                                echo "Step 5: Verify Deployment"
                                echo "========================================="
                                docker ps | grep ${CONTAINER_NAME}
                                
                                echo "========================================="
                                echo "Step 6: Cleanup Old Images"
                                echo "========================================="
                                docker image prune -f
                                
                                echo "Deployment completed successfully!"
ENDSSH
                        """
                    }
                }
            }
        }
    }
}